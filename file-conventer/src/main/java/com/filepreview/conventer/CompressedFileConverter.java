package com.filepreview.conventer;

import com.filepreview.model.FileModel;
import com.filepreview.util.ZipUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class CompressedFileConverter extends AbstractConverter {

    @Value("${tmp.root}")
    private String root;

    @Override
    public void convert(FileModel fileModel) {
        File resourceDir = super.createConvertResource(fileModel);
        if (resourceDir.exists() && resourceDir.isDirectory()) {
            // 压缩文件解压到resource目录下
            String fileTree = "";
            try {
                // 解压文件并获取文件列表
                fileTree = ZipUtil.unCompress(fileModel.getFilePath(), resourceDir.getPath());
                fileTree = fileTree.replace(root, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileModel.setFileTree(fileTree);
            fileModel.setState(FileModel.STATE_YZH);
            int splitIndex = fileModel.getOriginalFile().lastIndexOf(".");
            fileModel.setConvertedFileName(fileModel.getOriginalFile().substring(0, splitIndex));
            fileModel.setConvertedFilePath(resourceDir.getPath() + File.separator + fileModel.getConvertedFileName());
        }
        super.createMetaFile(fileModel);
    }
}
