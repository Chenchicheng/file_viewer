package com.filepreview.service;

import com.filepreview.model.FileModel;

import java.util.List;

/**
 * Created by chicheng on 2017/12/27.
 */
public interface FileService {

    /**
     * @param hashCode hashCode
     * @return FileModel
     */
    FileModel findFileModelByHashCode(String hashCode);

    /**
     * @Description: 查询所有key
     * @return List<String>
     */
    List<String> findAllKeys();


    /**
     * 获取转换后的所有图片文件路径
     * @param fileModel fileModel
     * @return List<String>
     */
    List<String> getImageFilesOfPPT(FileModel fileModel);
}
