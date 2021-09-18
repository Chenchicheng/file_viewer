package com.filepreview.service;

import com.filepreview.model.FileModel;

/**
 * Created by chicheng on 2017/12/28.
 */
public interface FileConverterService {
    /**
     * 文件转换并存储
     * @param fileModel
     */
    void convert(FileModel fileModel);

}
