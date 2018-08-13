package com.filepreview;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chicheng on 2018/1/2.
 */
public class FileTest {

    public static void main(String args[]) throws IOException{
        String filePath = "E:\\JJS_IM_DEV-updater-1.0.2";
        String filePath2 = "E:\\JJS_IM_DEV-updater-1.0.2.zip";
        List<FileNode> list = traverseFolder2(filePath);
         /*for(int i=0; i< list.size(); i++) {
             System.out.println(list.get(i).toString());
         }*/
        JsonParser jsonParser = new JsonParser();
        JsonObject json=(JsonObject) jsonParser.parse(list.get(0).toString());
        System.out.println(json.toString());

    }




    public static List<FileNode>  traverseFolder2(String path) {

        File file = new File(path);
        FileNode fileNode = null;
        if (file.exists()) {
            File[] files = file.listFiles();
            File[] files2 = new File[files.length];
            int i = 0;
            for(File file3 : files) {
                if(file3.isDirectory()) {
                    files2[i] = file3;
                    i++;
                }
            }

            List<FileNode> fileNodes = new ArrayList<>();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return null;
            } else {

                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        fileNode = new FileNode(file2.getName(),file2.getParent(),traverseFolder2(file2.getAbsolutePath()), true, null);
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                        fileNode = new FileNode(file2.getName(),file2.getParent(),null, false, null);
                    }
                }
                fileNodes.add(fileNode);

            }
            return fileNodes;
        } else {
            System.out.println("文件不存在!");
            return null;
        }
    }

    /*
     * 通过递归得到某一路径下所有的目录及其文件
     */
    public static List<FileNode>  getFiles(String filePath) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        String originName = "";
        String parentFileName = "";
        boolean isDirectory = false;
        String fullPath = "";

        List<FileNode> fileNodes = new ArrayList<>();
        for(File file:files) {

            List<FileNode> childList = Lists.newArrayList();
            FileNode fileNode = null;
            if(file.isDirectory()) {
                 isDirectory = true;
                 originName = file.getName();
                 parentFileName = file.getParent();
                 fullPath = file.getAbsolutePath();
                 childList = getFiles(file.getPath());
                fileNode = new FileNode(originName, parentFileName, childList, isDirectory, fullPath);
            } else {
                 originName = file.getName();
                 parentFileName = file.getParent();
                 isDirectory = false;
                 fullPath = file.getAbsolutePath();
                fileNode = new FileNode(originName, parentFileName, childList, isDirectory, fullPath);

            }

            fileNodes.add(fileNode);
        }
        return fileNodes;
    }

    /**
     * 文件节点(区分文件上下级)
     */
    public static class FileNode {

        private String originName;
        private String parentFileName;
        private boolean directory;
        private String fullPath;

        private List<FileNode> childList;

        public FileNode(String originName, String parentFileName, List<FileNode> childList, boolean directory, String fullPath) {
            this.originName = originName;
            this.parentFileName = parentFileName;
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



        public String getParentFileName() {
            return parentFileName;
        }

        public void setParentFileName(String parentFileName) {
            this.parentFileName = parentFileName;
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


}
