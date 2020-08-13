package com.filepreview.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chicheng on 2017/12/29.
 * 解压文件到指定目录并返回文件列表
 */
public class ZipUtil {
    public static int BUFFER_SIZE = 2048;

    private static List<String> unTar(InputStream inputStream, String destDir) throws Exception {
        List<String> fileNames = new ArrayList<String>();
        TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream, BUFFER_SIZE);
        TarArchiveEntry entry = null;
        try {
            while ((entry = tarIn.getNextTarEntry()) != null) {
                fileNames.add(entry.getName());
                // 是目录
                if (entry.isDirectory()) {
                    // 创建空目录
                    createDirectory(destDir, entry.getName());
                } else {
                    // 是文件
                    File tmpFile = new File(destDir + File.separator + entry.getName());
                    // 创建输出目录
                    createDirectory(tmpFile.getParent() + File.separator, null);
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(tmpFile);
                        int length = 0;
                        byte[] b = new byte[2048];
                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }
                    } finally {
                        IOUtils.closeQuietly(out);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(tarIn);
        }

        return fileNames;
    }

    public static List<String> unTar(String tarFile, String destDir) throws Exception {
        File file = new File(tarFile);
        return unTar(file, destDir);
    }

    public static List<String> unTar(File tarFile, String destDir) throws Exception {
        if(StringUtils.isBlank(destDir)) {
            destDir = tarFile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        return unTar(new FileInputStream(tarFile), destDir);
    }

    public static List<String> unTarBZip2(File tarFile,String destDir) throws Exception{
        if(StringUtils.isBlank(destDir)) {
            destDir = tarFile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        return unTar(new BZip2CompressorInputStream(new FileInputStream(tarFile)), destDir);
    }

    public static List<String> unTarBZip2(String file,String destDir) throws Exception{
        File tarFile = new File(file);
        return unTarBZip2(tarFile, destDir);
    }

    public static List<String> unBZip2(String bzip2File, String destDir) throws IOException {
        File file = new File(bzip2File);
        return unBZip2(file, destDir);
    }

    public static List<String> unBZip2(File srcFile, String destDir) throws IOException {
        if(StringUtils.isBlank(destDir)) {
            destDir = srcFile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        List<String> fileNames = new ArrayList<String>();
        InputStream is = null;
        OutputStream os = null;
        try {
            File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile.toString()));
            fileNames.add(FilenameUtils.getBaseName(srcFile.toString()));
            is = new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(srcFile), BUFFER_SIZE));
            os = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
        return fileNames;
    }

    public static List<String> unGZ(String gzFile, String destDir) throws IOException {
        File file = new File(gzFile);
        return unGZ(file, destDir);
    }

    public static List<String> unGZ(File srcFile, String destDir) throws IOException {
        if(StringUtils.isBlank(destDir)) {
            destDir = srcFile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        List<String> fileNames = new ArrayList<String>();
        InputStream is = null;
        OutputStream os = null;
        try {
            File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile.toString()));
            fileNames.add(FilenameUtils.getBaseName(srcFile.toString()));
            is = new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(srcFile), BUFFER_SIZE));
            os = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
        return fileNames;
    }

    public static List<String> unTarGZ(File tarFile,String destDir) throws Exception{
        if(StringUtils.isBlank(destDir)) {
            destDir = tarFile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        return unTar(new GzipCompressorInputStream(new FileInputStream(tarFile)), destDir);
    }

    public static List<String> unTarGZ(String file,String destDir) throws Exception{
        File tarFile = new File(file);
        return unTarGZ(tarFile, destDir);
    }

    public static void createDirectory(String outputDir,String subDir){
        File file = new File(outputDir);
        // 子目录不为空
        if(!(subDir == null || subDir.trim().equals(""))){
            file = new File(outputDir + File.separator + subDir);
        }
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     * @param zipFile 指定的ZIP压缩文件
     * @param dest 解压目录
     * @return  解压后文件链表
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static List<String> unZip(String zipFile, String dest) {
        File file = new File(zipFile);
        try {
            ZipFile zFile = new ZipFile(file);
            zFile.setFileNameCharset("GBK");
            if (!zFile.isValidZipFile()) {
                throw new ZipException("压缩文件不合法,可能被损坏.");
            }
            File destDir = new File(dest);
            if (destDir.isDirectory() && !destDir.exists()) {
                destDir.mkdir();
            }

            zFile.extractAll(dest);

            List<net.lingala.zip4j.model.FileHeader> headerList = zFile.getFileHeaders();
            List<String> extractedFileList = new ArrayList<>();
            for (net.lingala.zip4j.model.FileHeader fileHeader : headerList) {
                if (!fileHeader.isDirectory()) {
                    extractedFileList.add( fileHeader.getFileName());
                }
            }
            return extractedFileList;
        }catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解压Rar压缩文件
     * @param rarfile
     * @param destDir
     * @return
     */
    public static List<String> unRarFile(String rarfile, String destDir) {
        if (!rarfile.toLowerCase().endsWith(".rar")) {
            System.out.println("非rar文件！");
            return null;
        }
        File dstDiretory = new File(destDir);
        // 目标目录不存在时，创建该文件夹
        if (!dstDiretory.exists()) {
            dstDiretory.mkdirs();
        }
        List<String> fileNames = new ArrayList<String>();
        Archive a = null;
        try {
            a = new Archive(new File(rarfile));
            if (a != null) {
                FileHeader fh = a.nextFileHeader();
                while (fh != null) {
                    // 防止文件名中文乱码问题的处理
                    String fileName = fh.getFileNameW().isEmpty()?fh.getFileNameString():fh.getFileNameW();
                    fileNames.add(fileName);
                    // 文件夹
                    if (fh.isDirectory()) {
                        File fol = new File(destDir + File.separator + fileName);
                        fol.mkdirs();
                    } else {
                        // 文件
                        File out = new File(destDir + File.separator + fileName.trim());
                        try {
                            if (!out.exists()) {
                                // 相对路径可能多级，可能需要创建父目录.
                                if (!out.getParentFile().exists()) {
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = a.nextFileHeader();
                }
                a.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    /**
     * 通过递归得到某一路径下所有的目录及其文件
     */
    public static List<FileNode> getFiles(String filePath){
        File root = new File(filePath);
        File[] files = root.listFiles();
        String originName = "";
        boolean isDirectory = false;
        String fullPath = "";

        List<FileNode> fileNodes = new ArrayList<>();
        for(File file:files){

            List<FileNode> childList = new ArrayList<>();

            if(file.isDirectory()){
                isDirectory = true;
                originName = file.getName();
                fullPath = file.getAbsolutePath();
                childList = getFiles(file.getPath());
            } else {
                originName = file.getName();
                isDirectory = false;
                fullPath = file.getAbsolutePath();
            }
            // 进行转义，否则json解析不了
            fullPath = fullPath.replace("\\", "/");
            FileNode fileNode = new FileNode(originName, childList, isDirectory, fullPath);
            fileNodes.add(fileNode);
        }
        return fileNodes;
    }

    /**
     * 文件节点(区分文件上下级)
     */
    public static class FileNode{

        private String originName;
        private boolean directory;
        private String fullPath;

        private List<FileNode> childList;

        public FileNode(String originName, List<FileNode> childList, boolean directory, String fullPath) {
            this.originName = originName;
            this.childList = childList;
            this.directory = directory;
            this.fullPath = fullPath;

        }

        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

        public List<FileNode> getChildList() {
            return childList;
        }

        public void setChildList(List<FileNode> childList) {
            this.childList = childList;
        }

        @Override
        public String toString() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "";
            }
        }

        public String getOriginName() {
            return originName;
        }

        public void setOriginName(String originName) {
            this.originName = originName;
        }

        public boolean isDirectory() {
            return directory;
        }

        public void setDirectory(boolean directory) {
            this.directory = directory;
        }
    }



    public static String  unCompress(String compressFile, String destDir) throws Exception {
        String upperName= compressFile.toUpperCase();
        String ret = null;
        if(upperName.endsWith(".ZIP") || upperName.endsWith(".JAR") || upperName.endsWith(".GZIP")) {
             unZip(compressFile, destDir);
        } else if(upperName.endsWith(".TAR")) {
            unTar(compressFile, destDir);
        } else if(upperName.endsWith(".TAR.BZ2")) {
             unTarBZip2(compressFile, destDir);

        } else if(upperName.endsWith(".BZ2")) {
             unBZip2(compressFile, destDir);

        } else if(upperName.endsWith(".TAR.GZ")) {
             unTarGZ(compressFile, destDir);

        } else if(upperName.endsWith(".GZ")) {
             unGZ(compressFile, destDir);

        } else if(upperName.endsWith(".RAR")) {
             unRarFile(compressFile, destDir);
        }
        List list = getFiles(destDir);
        for(int i = 0; i< list.size(); i++) {
            ret = list.get(i).toString();
        }
        return ret;
    }

}
