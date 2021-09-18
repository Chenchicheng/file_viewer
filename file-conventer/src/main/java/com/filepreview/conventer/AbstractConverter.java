package com.filepreview.conventer;

import com.filepreview.model.FileModel;
import com.filepreview.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author chicheng
 * @date 2021/9/16
 */
@Component
public abstract class AbstractConverter {

    @Value("${tmp.root}")
    private String root;
    private static final String resource = "resource";
    private static final String meta = "meta";
    private static final String separator = "/";

    public abstract void convert(FileModel fileModel);

    /**
     * 创建文件转换后的存储目录
     *
     * @param fileModel fileModel
     * @return File
     */
    protected File createConvertResource(FileModel fileModel) {
        // 创建hash目录
        String hashDirPath = root + File.separator + fileModel.getPathId();
        File hashDir = FileUtil.createDir(hashDirPath);
        if (hashDir.exists() && hashDir.isDirectory()) {
            // 复制源文件到hash目录
            FileUtil.copyFile(fileModel.getFilePath(), hashDirPath);
            // 计算文件大小
            fileModel.setFileSize(FileUtil.getFileSize(fileModel.getFilePath()));
            // 创建resource目录, 存放源文件
            String resourceDirPath = hashDirPath + File.separator + resource;
            return FileUtil.createDir(resourceDirPath);
        }
        return null;
    }

    /**
     * 创建meta文件，存放文件基本信息
     * @param fileModel fileModel
     */
    protected void createMetaFile(FileModel fileModel) {
        String hashDirPath = root + File.separator + fileModel.getPathId();
        String metaPath = hashDirPath + File.separator + meta;
        File metaFile = FileUtil.createFile(metaPath);
        fileModel.setRelativeFileDir(fileModel.getPathId() + separator + resource);
        FileUtil.writeContent(metaFile, fileModel, "GBK");
    }
}
