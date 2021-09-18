package com.filepreview.conventer;

import com.filepreview.model.FileModel;
import com.filepreview.util.FileUtil;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class PdfFileConverter extends AbstractConverter {

    /**
     * pdf文件转换后格式不变，改变存储目录为 hash码目录/resource目录 + 源文件 + meta文件
     * meta文件存储文件基本信息
     * resource目录存放转换之后的文件，此处依然为源文件
     *
     * @param fileModel fileModel
     */
    @Override
    public void convert(FileModel fileModel) {
        File resourceDir = super.createConvertResource(fileModel);
        if (resourceDir.exists() && resourceDir.isDirectory()) {
            FileUtil.copyFile(fileModel.getFilePath(), resourceDir.getPath());
            fileModel.setState(FileModel.STATE_YZH);
            fileModel.setConvertedFileName(fileModel.getOriginalFile());
            fileModel.setConvertedFileDir(resourceDir.getPath());
            fileModel.setConvertedFilePath(resourceDir + File.separator + fileModel.getConvertedFileName());
        }
        super.createMetaFile(fileModel);
    }
}
