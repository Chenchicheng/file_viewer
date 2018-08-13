package com.jjshome.dao;

import com.jjshome.model.FileModel;

import java.io.InputStream;
import java.util.List;

/**
 * Created by chicheng on 2017/12/27.
 * @author chicheng
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
     * @author liaohongwei
     * @date 2016年6月27日 下午4:12:01
     */
    FileModel removeFromMap(String pathId);

    /**
     * @Description: 回滚remove
     * @param fileModel
     * @return int
     * @author liaohongwei
     * @date 2016年6月27日 下午4:22:23
     */
    int rollbackFromMap(FileModel fileModel);

    /**
     * @Description: 删除该filemodel所有先关数据
     * @param fileModel
     * @return int
     * @author liaohongwei
     * @date 2016年6月27日 下午4:13:05
     */
    int delete(FileModel fileModel);
}
