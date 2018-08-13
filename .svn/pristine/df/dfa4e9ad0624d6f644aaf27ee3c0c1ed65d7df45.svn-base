package com.jjshome.conventer;

import com.jjshome.model.FileModel;
import com.jjshome.service.FileService;
import com.jjshome.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

/**
 * Created by chicheng on 2017/12/27.
 * @author chicheng
 */
@Component
public class PdfFileConventer {

    @Value("${tmp.root}")
    private String root;

    /**
     * pdf文件转换后格式不变，改变存储目录为 hash码目录/resource目录 + 源文件 + meta文件
     * meta文件存储文件基本信息
     * resource目录存放转换之后的文件，此处依然为源文件
     * @param fileModel
     */
    public  void conventer(FileModel fileModel) {
        // 创建hash目录
        String hashDirPath = root + "/" + fileModel.getPathId();
        File hashDir = FileUtil.createDir(hashDirPath);
        if (hashDir.exists() && hashDir.isDirectory()) {
            // 复制源文件到hash目录
            String filePath = fileModel.getTempDir() + "\\" + fileModel.getOriginalFile();
            FileUtil.copyFile(filePath, hashDirPath);
            // 计算文件大小
            fileModel.setFileSize(FileUtil.getFileSize(filePath));
            // 创建resource目录，存放源文件
            String resourceDirPath = hashDirPath + "\\" + "resource";
            File resourceDir = FileUtil.createDir(resourceDirPath);
            if (resourceDir.exists() && resourceDir.isDirectory()) {
                FileUtil.copyFile(filePath, resourceDirPath);
            }
            // 创建meta文件，存放文件基本信息
            String metaPath = hashDirPath + "\\" + "meta";
            File metaFile = FileUtil.createFile(metaPath);
            FileUtil.writeContent(metaFile, fileModel, "GBK");
        }
    }
}
