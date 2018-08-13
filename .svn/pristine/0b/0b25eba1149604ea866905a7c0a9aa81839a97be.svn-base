package com.jjshome.service.impl;

import com.jjshome.dao.FileDao;
import com.jjshome.model.FileModel;
import com.jjshome.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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


}
