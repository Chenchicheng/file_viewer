package com.filepreview.conventer;

import com.filepreview.model.FileModel;
import com.filepreview.util.FileUtil;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class ImageFileConverter extends AbstractConverter {

    /**
     * 图片类型文件不转换,只更换存储目录 存储目录为 root/resource目录 + meta文件 + 源文件
     * resource目录存放转换后的文件，此处依然为源文件
     * meta文件存放文件基本信息
     *  @param fileModel fileModel
     */
    @Override
    public void convert(FileModel fileModel) {
        File resourceDir = super.createConvertResource(fileModel);
        if (resourceDir.exists() && resourceDir.isDirectory()) {
            FileUtil.copyFile(fileModel.getFilePath(), resourceDir.getPath());
            fileModel.setState(FileModel.STATE_YZH);
            fileModel.setConvertedFileDir(resourceDir.getPath());
            fileModel.setConvertedFileName(fileModel.getOriginalFile());
            fileModel.setConvertedFilePath(resourceDir.getPath() + File.separator + fileModel.getConvertedFileName());
        }
        super.createMetaFile(fileModel);
    }
}
