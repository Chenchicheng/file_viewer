package com.filepreview.dao;

import com.filepreview.model.FileModel;

import java.io.InputStream;
import java.util.List;

/**
 * Created by chicheng on 2017/12/27.
 */
public interface FileDao {
    /**
     * 查询所有的key
     * @return List
     */
    List<String> findAllKeys();

    /**
     * 查询key对应的FileModel
     * @param  hashCode
     * @return FileModel
     */
    FileModel findByHashCode(String hashCode);

    /**
     * 保存文件
     * @param in
     * @param fileModel
     */
    void saveFile(InputStream in, FileModel fileModel);

    /**
     * @Description: 删除缓存map中的数据,并非真正的删除文件
     * @param pathId
     * @return FileModel
     */
    FileModel removeFromMap(String pathId);

    /**
     * @Description: 回滚remove
     * @param fileModel
     * @return int
     */
    int rollbackFromMap(FileModel fileModel);

    /**
     * @Description: 删除该filemodel所有先关数据
     * @param fileModel
     * @return int
     */
    int delete(FileModel fileModel);

    List<String> getImageFilesOfPPT(String rootPath);
}
