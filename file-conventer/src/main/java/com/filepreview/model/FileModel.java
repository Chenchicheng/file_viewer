package com.filepreview.model;

import java.io.Serializable;

/**
 * Created by chicheng on 2017/12/27.
 */
public class FileModel implements Serializable {

    /**
     * @Fields STATE_WX : 无效
     */
    public static final int STATE_WX = 0;
    /**
     * @Fields STATE_WXZ : 未下载
     */
    public static final int STATE_WXZ = 1;
    /**
     * @Fields STATE_YXZ : 已下载
     */
    public static final int STATE_YXZ = 2;
    /**
     * @Fields STATE_YZH : 已转换
     */
    public static final int STATE_YZH = 3;
    /**
     * @Fields original : 来源,具有唯一性
     */
    private String original;
    /**
     * @Fields originalFile : 从源下载下来的文件
     */
    private String originalFile;

    /**
     * @Fields fileSize  : 文件大小,以byte为单位
     */
    public long fileSize;

    /**
     * @Fields createMs : 创建时间,毫秒数
     */
    private long createMs;

    /**
     * @Fields limitMs : 过期毫秒数
     */
    private long limitMs;

    /**
     * @Fields originalMIMEType : 从数据源获取的多媒体类型
     */
    private String originalMIMEType;

    /**
     * @Fields tempDir ： 临时文件目录,用于存放临时文件
     */
    private String tempDir;

    /**
     * @Fields pathId : 判断url路径,此处以文件hashcode值作为文件目录
     */
    private String pathId;

    /**
     * @Field fileTree : 压缩文件列表
     */
    private String fileTree;

    /**
     * @Field conventedFileName 转换后的文件名称
     */
    private String conventedFileName;
    /**
     * @Fields state : 文件状态 0 无效,1 未下载,2 已下载,3 已转换
     */
    private int state;


    public String getConventedFileName() {
        return conventedFileName;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
    }


    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCreateMs() {
        return createMs;
    }

    public void setCreateMs(long createMs) {
        this.createMs = createMs;
    }

    public String getOriginalMIMEType() {
        return originalMIMEType;
    }

    public void setOriginalMIMEType(String originalMIMEType) {
        this.originalMIMEType = originalMIMEType;
    }

    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public String getFileTree() {
        return fileTree;
    }

    public void setFileTree(String fileTree) {
        this.fileTree = fileTree;
    }

    public long getLimitMs() {
        return limitMs;
    }

    public void setLimitMs(long limitMs) {
        this.limitMs = limitMs;
    }

    public void setConventedFileName(String conventedFileName) {
        this.conventedFileName = conventedFileName;
    }
}
