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
     * @param  hashCode hashCode
     * @return FileModel
     */
    FileModel findByHashCode(String hashCode);

    /**
     * 保存文件
     * @param in in
     * @param fileModel fileModel
     */
    void saveFile(InputStream in, FileModel fileModel);

    /**
     * @Description: 删除缓存map中的数据,并非真正的删除文件
     * @param pathId pathId
     * @return FileModel
     */
    FileModel removeFromMap(String pathId);

    /**
     * @Description: 回滚remove
     * @param fileModel fileModel
     * @return int
     */
    int rollbackFromMap(FileModel fileModel);

    /**
     * @Description: 删除该fileModel所有相关数据
     * @param fileModel fileModel
     * @return int
     */
    int delete(FileModel fileModel);

    /**
     * 获取转换后的所有图片文件路径
     * @param fileModel fileModel
     * @return List<String>
     */
    List<String> getImageFilesOfPPT(FileModel fileModel);
}
