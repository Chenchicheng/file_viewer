package com.filepreview.dao.impl;

import com.filepreview.dao.FileDao;
import com.filepreview.model.FileModel;
import com.filepreview.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class FileDaoImpl implements FileDao {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * @Fields root ： 根路径
     */
    @Value("${tmp.root}")
    private String root;
    /**
     * @Fields rootTemp ：下载文件临时存放路径
     */
    @Value("${tmp.rootTemp}")
    private String rootTemp;
    /**
     * @Fields fileModelMap uuid作为key
     */
    private Map<String, FileModel> fileModelMap;

    @PostConstruct
    public void init() {
        logger.info("FileDaoImpl init start ...");
        // 使用同步的map
        this.fileModelMap = new ConcurrentHashMap<>((int) (32 / 0.7));
        // 根目录初始化
        File file = new File(root);
        if (!file.exists()) {
            //初始化不存在则创建目录
            file.mkdirs();
        } else {
            // 存在的话,删除下面的所有内容
            deleteDir(file);
        }
        // 临时目录初始化
        File tempFile = new File(rootTemp);
        if (!tempFile.exists()) {
            //初始化不存在则创建目录
            tempFile.mkdirs();
        } else {
            // 存在的话,删除下面的所有内容
            deleteDir(tempFile);
        }
        logger.info("FileDaoImpl init end ...");

    }

    @Override
    public List<String> findAllKeys() {
        return new ArrayList<String>(this.fileModelMap.keySet());
    }

    @Override
    public FileModel findByHashCode(String hashCode) {
        FileModel tmp = this.fileModelMap.get(hashCode);
        if (tmp == null) {
            return null;
        } else {
            return tmp;
        }
    }


    @Override
    public void saveFile(InputStream is, FileModel fileModel) {
        // 获取单次操作唯一目录
        File currDir = getOnlyDir(3, this.rootTemp);
        fileModel.setTempDir(currDir.getAbsolutePath());
        // 存储文件
        FileOutputStream out = null;
        try {
            String tempPath = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
            out = new FileOutputStream(tempPath);
            byte[] data = new byte[1024];
            int tmp = 0;
            while ((tmp = is.read(data)) != -1) {
                out.write(data, 0, tmp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 2个map中fileModel使用同一个对象,修改时会同步变化
        fileModel.setState(FileModel.STATE_YXZ);
        String filePath = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
        // 得到文件hash编码，以此作为判断唯一文件的依据
        fileModel.setPathId(FileUtil.getFileHashCode(filePath));
        this.fileModelMap.put(fileModel.getPathId(), fileModel);
    }

    /**
     * @param dir
     * @Description: 删除文件夹下所有内容, 不会删除文件夹本身
     */
    private void deleteDir(File dir) {
        if (!dir.isDirectory()) {
            return;
        }
        File[] children = dir.listFiles();
        for (File child : children) {
            if (child.isDirectory()) {
                deleteDir(child);
            }
            child.delete();

        }
    }

    /**
     * @param num  重复尝试次数
     * @param root 根目录
     * @return File
     * @Description: 得到一个唯一的目录
     */
    private File getOnlyDir(int num, String root) {
        if (num == 0) {
            return null;
        }

        File file = new File(root + File.separator + UUID.randomUUID().toString());
        num--;
        if (file.exists()) {
            return getOnlyDir(num, root);
        } else {
            file.mkdirs();
            return file;
        }
    }

    @Override
    public FileModel removeFromMap(String pathId) {
        return this.fileModelMap.remove(pathId);
    }

    @Override
    public int rollbackFromMap(FileModel fileModel) {
        this.fileModelMap.put(fileModel.getPathId(), fileModel);
        return 1;
    }

    @Override
    public int delete(FileModel fileModel) {
        File dir = new File(fileModel.getTempDir());
        deleteDir(dir);
        // 此处连续尝试删除3次,有可能文件正在被其他线程打开,而删除失败
        for (int i = 0; i < 3; i++) {
            deleteDir(dir);
            boolean flag = dir.delete();
            if (flag) {
                // 删除成功,结束循环
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (dir.exists()) {
            // 删除失败
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public List<String> getImageFilesOfPPT(FileModel fileModel) {
        File rootFile = new File(fileModel.getConvertedFileDir());
        String[] files = rootFile.list();
        if (files == null) {
            return null;
        }
        Map<Integer, String> map = new TreeMap<>();
        String img = "img";
        for (String file : files) {
            if (file.contains("jpg")) {
                String sort = file.substring(img.length(), file.lastIndexOf("."));
                map.put(Integer.valueOf(sort),  file);
            }
        }
        return new ArrayList<>(map.values());
    }


}
