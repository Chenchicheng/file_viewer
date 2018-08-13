package com.filepreview.conventer;

import com.filepreview.model.FileModel;
import com.filepreview.util.FileUtil;
import com.filepreview.util.ZipUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class CompressedFileConventer {

    @Value("${tmp.root}")
    private String root;

    public void conventer(FileModel fileModel) {

        // 创建hash目录
        String hashDirPath = root + File.separator + fileModel.getPathId();
        File hashDir = FileUtil.createDir(hashDirPath);
        if (hashDir.exists() && hashDir.isDirectory()) {
            // 复制源文件到hash目录
            String filePath = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
            FileUtil.copyFile(filePath, hashDirPath);
            // 计算文件大小
            fileModel.setFileSize(FileUtil.getFileSize(filePath));
            // 创建resource目录，存放源文件
            String resourceDirPath = hashDirPath + File.separator + "resource";
            File resourceDir = FileUtil.createDir(resourceDirPath);
            if (resourceDir.exists() && resourceDir.isDirectory()) {
                // 压缩文件解压到resource目录下
                String fileTree = "";
                try {
                    // 解压文件并获取文件列表
                    fileTree = ZipUtil.unCompress(filePath, resourceDirPath);
                    fileTree = fileTree.replace(root, "");
                }catch (Exception e) {
                    e.printStackTrace();
                }
                fileModel.setFileTree(fileTree);
                fileModel.setState(FileModel.STATE_YZH);
                int splitIndex = fileModel.getOriginalFile().lastIndexOf(".");
                fileModel.setConventedFileName(fileModel.getOriginalFile().substring(0, splitIndex));
            }
            // 创建meta文件，存放文件基本信息
            String metaPath = hashDirPath + File.separator + "meta";
            File metaFile = FileUtil.createFile(metaPath);
            FileUtil.writeContent(metaFile, fileModel, "GBK");
        }

    }
}
