package com.filepreview.service;

import com.filepreview.model.FileModel;

/**
 * Created by chicheng on 2017/12/28.
 */
public interface FileConventerService {
    /**
     * 文件转换并存储
     * @param fileModel
     */
    void conventer(FileModel fileModel);

}
