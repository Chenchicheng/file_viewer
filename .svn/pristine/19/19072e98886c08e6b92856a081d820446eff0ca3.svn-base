package com.jjshome.conventer;

import com.jjshome.model.FileModel;
import com.jjshome.util.FileUtil;
import com.jjshome.util.ZipUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chicheng on 2017/12/27.
 * @author chicheng
 */
@Component
public class CompressedFileConventer {

    @Value("${tmp.root}")
    private String root;

    public void conventer(FileModel fileModel) {

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
            String resourceDirPath = hashDirPath + "/" + "resource";
            File resourceDir = FileUtil.createDir(resourceDirPath);
            if (resourceDir.exists() && resourceDir.isDirectory()) {
                // 压缩文件解压到resource目录下
                List<String> fileTree = new ArrayList<>();
                try {
                    // 解压文件，返回文件列表
                    fileTree = ZipUtil.unCompress(filePath, resourceDirPath);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                fileModel.setFileTree(fileTree);
            }
            // 创建meta文件，存放文件基本信息
            String metaPath = hashDirPath + "\\" + "meta";
            File metaFile = FileUtil.createFile(metaPath);
            FileUtil.writeContent(metaFile, fileModel, "GBK");
        }

    }
}
