package com.filepreview.controller;

import com.filepreview.model.FileModel;
import com.filepreview.service.DownloadNetFileService;
import com.filepreview.service.FileConventerService;
import com.filepreview.service.FileService;
import com.filepreview.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by chicheng on 2017/12/28.
 */
@Controller
public class ConventerController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DownloadNetFileService downloadNetFileService;

    @Autowired
    private FileConventerService fileConventerService;

    @Value("${tmp.root}")
    private String rootPath;

    @Value("${text.type}")
    private String textType;

    @Value("${img.type}")
    private String imgType;

    @Value("${office.type}")
    private String officeType;

    @Value("${compress.type}")
    private String compressType;

    @Value("${pdf.type}")
    private String pdfType;


    /**
     * 文件转换：1、从url地址下载文件 2、转换文件
     * @param model
     * @param filePath
     * @throws UnsupportedEncodingException
     * @return String
     */
    @RequestMapping("/fileConventer")
    public String fileConventer(String filePath, Model model) throws UnsupportedEncodingException {
        // 先去查询,如果存在,不需要转化文档,为找到有效安全的url编码,所以这里使用循环来判断当前文件是否存在
        FileModel oldFileModel = null;
        List<String> keys = this.fileService.findAllKeys();
        for (String key : keys) {
            FileModel tmp = this.fileService.findFileModelByHashCode(key);
            if (tmp != null && tmp.getOriginal().equals(filePath)) {
                oldFileModel = tmp;
                break;
            }
        }
        // 文件已下载，不需要转换
        if (oldFileModel != null) {
            return previewUrl(oldFileModel, model);
        } else {
            FileModel fileModel = new FileModel();
            // 文件来源url
            fileModel.setOriginal(filePath);
            // 创建时间,使用毫秒数
            fileModel.setCreateMs(System.currentTimeMillis());
            // 文件有效时间 10分钟
            fileModel.setLimitMs(10 * 60 * 1000);
            // 文件新建 未下载状态
            fileModel.setState(FileModel.STATE_WXZ);
            // 下载文件
            this.downloadNetFileService.download(fileModel);
            // 转换文件
            this.fileConventerService.conventer(fileModel);
            // 文件展现到前端
            if (fileModel.getState() != FileModel.STATE_YZH) {
                throw new RuntimeException("convert fail.");
            }
            return previewUrl(fileModel, model);
        }
    }

    /**
     * 获取重定向路径
     * @param fileModel
     * @param model
     * @throws UnsupportedEncodingException
     * @return String
     */
    private String previewUrl(FileModel fileModel, Model model) throws UnsupportedEncodingException {
        StringBuffer previewUrl = new StringBuffer();
        previewUrl.append("/viewer/document/");
        // pathId确定预览文件
        previewUrl.append(fileModel.getPathId());
        previewUrl.append(File.separator);

        // 判断转换后的文件是否存在,不存在则跳到error页面
        File file = new File(rootPath + File.separator + fileModel.getPathId()
                + File.separator + "resource" + File.separator + fileModel.getConventedFileName());
        String subfix = FileUtil.getFileSufix(fileModel.getOriginalFile());
        model.addAttribute("pathId", fileModel.getPathId());
        model.addAttribute("fileType", subfix);
        if (file.exists()) {
            // 判断文件类型，不同的文件做不同的展示
            if (this.pdfType.contains(subfix.toLowerCase())) {
                return "html";
            } else if (this.textType.contains(subfix.toLowerCase())) {
                return "txt";
            } else if (this.imgType.contains(subfix.toLowerCase())) {
                return "picture";
            } else if (this.compressType.contains(subfix.toLowerCase())) {
                model.addAttribute("fileTree", fileModel.getFileTree());
                return "compress";
            } else if (this.officeType.contains(subfix.toLowerCase())) {
                if ("xlsx".equalsIgnoreCase(subfix.toLowerCase()) ||
                        "xls".equalsIgnoreCase(subfix.toLowerCase())) {
                    return "office";
                }
                if ("pptx".equalsIgnoreCase(subfix.toLowerCase()) ||
                        "ppt".equalsIgnoreCase(subfix.toLowerCase())) {
                    List<String> imgFiles = fileService.getImageFilesOfPPT(fileModel.getPathId());
                    model.addAttribute("data", imgFiles.toArray());
                    return "html";
                } else {
                    return "html";
                }
            }
        } else {
            return "forward:/fileNotSupported";
        }
        return null;
    }

    /**
     * 获取预览文件
     * @param pathId
     * @param response
     * @param fileFullPath 此参数主要针对压缩文件,利用该参数获取解压后的文件
     * @return
     */
    @RequestMapping(value = "/viewer/document/{pathId}", method = RequestMethod.GET)
    public void onlinePreview(@PathVariable String pathId, String fileFullPath, HttpServletResponse response) throws IOException {

        // 根据pathId获取fileModel
        FileModel fileModel = this.fileService.findFileModelByHashCode(pathId);
        if (fileModel == null) {
            throw new RuntimeException("fileModel 不能为空");
        }
        if (fileModel.getState() != FileModel.STATE_YZH) {
            throw new RuntimeException("convert fail.");
        }

        // 得到转换后的文件地址
        String fileUrl = "";

        if (fileFullPath != null) {
            fileUrl = rootPath + File.separator + fileFullPath;
        } else {
            fileUrl = rootPath + File.separator + fileModel.getPathId()
                    + File.separator + "resource" + File.separator + fileModel.getConventedFileName();
        }
        File file = new File(fileUrl);
        // 设置内容长度
        response.setContentLength((int) file.length());

        // 内容配置中要转码,inline 浏览器支持的格式会在浏览器中打开,否则下载
        String fullFileName = new String(fileModel.getConventedFileName());
        response.setHeader("Content-Disposition", "inline;fileName=\""
                + fullFileName + "\"");

        // 设置content-type
        response.setContentType(fileModel.getOriginalMIMEType());
        FileInputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(new File(fileUrl));
            os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int tmp = 0;
            while ((tmp = is.read(bytes)) != -1) {
                os.write(bytes, 0, tmp);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    @RequestMapping("aaa")
    public String test() {
        return "ppt";
    }


}

