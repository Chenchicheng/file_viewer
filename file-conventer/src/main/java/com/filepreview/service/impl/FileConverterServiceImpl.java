package com.filepreview.service.impl;

import com.filepreview.conventer.*;
import com.filepreview.model.FileModel;
import com.filepreview.service.FileConverterService;
import com.filepreview.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by chicheng on 2017/12/28.
 */
@Component
public class FileConverterServiceImpl implements FileConverterService {

    private ConvertContext convertContext;

    public FileConverterServiceImpl(ConvertContext convertContext) {
        this.convertContext = convertContext;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void convert(FileModel fileModel) {
        if (fileModel.getState() != FileModel.STATE_YXZ) {
            throw new RuntimeException("the file state:" + fileModel.getState() + " is not 2.");
        }
        try {
            String subFix = FileUtil.getFileSufix(fileModel.getOriginalFile());
            convertContext.getConverter(subFix).convert(fileModel);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("不支持该类型文件的转换");
            throw new RuntimeException(e);
        }
    }
}
