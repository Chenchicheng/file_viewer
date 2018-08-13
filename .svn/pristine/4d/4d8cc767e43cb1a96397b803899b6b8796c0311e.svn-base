package com.jjshome.controller;

import com.jjshome.model.FileModel;
import com.jjshome.service.DownloadNetFileService;
import com.jjshome.service.FileConventerService;
import com.jjshome.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

/**
 * Created by chicheng on 2017/12/28.
 * @author chicheng
 */
@Controller
public class ConventerController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DownloadNetFileService downloadNetFileService;

    @Autowired
    private FileConventerService fileConventerService;

    /**
     * 文件转换：1、从url地址下载文件 2、转换文件
     * @param filePath
     */
    @RequestMapping("/fileConventer")
    public void fileConventer(String filePath) {
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
            return;
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
        }
    }

}
