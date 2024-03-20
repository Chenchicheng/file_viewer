package com.filepreview.controller;

import com.filepreview.conventer.FileType;
import com.filepreview.model.FileModel;
import com.filepreview.service.DownloadNetFileService;
import com.filepreview.service.FileConverterService;
import com.filepreview.service.FileService;
import com.filepreview.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by chicheng on 2017/12/28.
 */
@Controller
public class ConvertController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DownloadNetFileService downloadNetFileService;

    @Autowired
    private FileConverterService fileConverterService;

    @Value("${tmp.root}")
    private String rootPath;

    /**
     * 文件转换：1、从url地址下载文件 2、转换文件
     *
     * @param model    model
     * @param filePath filePath
     * @return String
     */
    @RequestMapping("/fileConvert")
    public String fileConverter(String filePath, Model model) {
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
        }
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
        this.fileConverterService.convert(fileModel);
        // 文件展现到前端
        if (fileModel.getState() != FileModel.STATE_YZH) {
            throw new RuntimeException("convert fail.");
        }
        return previewUrl(fileModel, model);
    }

    /**
     * 获取重定向路径
     *
     * @param fileModel fileModel
     * @param model     model
     * @return String
     */
    private String previewUrl(FileModel fileModel, Model model) {
        // 判断转换后的文件是否存在,不存在则跳到error页面
        File file = new File(fileModel.getConvertedFilePath());
        String subFix = FileUtil.getFileSufix(fileModel.getOriginalFile());
        model.addAttribute("pathId", fileModel.getPathId());
        model.addAttribute("fileType", subFix);
        if (!file.exists() || !FileType.contains(subFix.toLowerCase())) {
            return "forward:/fileNotSupported";
        }
        // 判断文件类型，不同的文件做不同的展示
        if (FileType.pdfType.contains(subFix.toLowerCase())) {
            return "office";
        } else if (FileType.textType.contains(subFix.toLowerCase())) {
            return "txt";
        } else if (FileType.imgType.contains(subFix.toLowerCase())) {
            return "picture";
        } else if (FileType.compressType.contains(subFix.toLowerCase())) {
            model.addAttribute("fileTree", fileModel.getFileTree());
            return "compress";
        } else if (FileType.officeType.contains(subFix.toLowerCase())) {
            if (!"pptx".equalsIgnoreCase(subFix) && !"ppt".equalsIgnoreCase(subFix)) {
                return "office";
            }
            model.addAttribute("imgPaths", getImgPaths(fileModel));
            return "ppt";
        }
        return null;
    }

    /**
     * 获取预览文件
     *
     * @param pathId       pathId
     * @param response     response
     * @param fileFullPath 此参数主要针对压缩文件,利用该参数获取解压后的文件
     * @return
     */
    @RequestMapping(value = "/viewer/document/{pathId}", method = RequestMethod.GET)
    @ResponseBody
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
        String fileUrl = fileModel.getConvertedFilePath();
        if (fileFullPath != null) {
            fileUrl = rootPath + File.separator + fileFullPath;
        }

        File file = new File(fileUrl);
        // 设置内容长度
        response.setContentLength((int) file.length());
        // 内容配置中要转码,inline 浏览器支持的格式会在浏览器中打开,否则下载
        String fullFileName = fileModel.getConvertedFileName();
        response.setHeader("Content-Disposition", "inline;fileName=\"" + fullFileName + "\"");
        // 设置content-type
        response.setContentType(fileModel.getOriginalMIMEType());
        FileInputStream is = new FileInputStream(fileUrl);
        OutputStream os = response.getOutputStream();
        printFile(is, os);
    }

    private String getImgPaths(FileModel fileModel) {
        List<String> imgFiles = fileService.getImageFilesOfPPT(fileModel);
        StringBuilder imgPaths = new StringBuilder();
        for (String s : imgFiles) {
            imgPaths.append(fileModel.getRelativeFileDir()).append("/").append(s).append(",");
        }
        return imgPaths.toString();
    }

    private void printFile(FileInputStream is, OutputStream os) throws IOException {
        try {
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
}

