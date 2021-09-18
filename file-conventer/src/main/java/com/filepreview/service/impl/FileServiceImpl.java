package com.filepreview.service.impl;

import com.filepreview.dao.FileDao;
import com.filepreview.model.FileModel;
import com.filepreview.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class FileServiceImpl implements FileService {


    @Autowired
    private FileDao fileDao;

    @Override
    public FileModel findFileModelByHashCode(String hashCode) {
        return fileDao.findByHashCode(hashCode);
    }

    @Override
    public List<String> findAllKeys() {
        return this.fileDao.findAllKeys();
    }

    @Override
    public List<String> getImageFilesOfPPT(FileModel fileModel) {
        return this.fileDao.getImageFilesOfPPT(fileModel);
    }


}
