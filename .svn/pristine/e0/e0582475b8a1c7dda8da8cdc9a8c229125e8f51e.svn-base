package com.jjshome.util;

import com.jjshome.model.FileModel;

import java.io.*;
import java.lang.reflect.Field;

/**
 * Created by chicheng on 2017/12/27.
 * 文件基本信息
 * @author chicheng
 */
public class FileUtil {

    /**
     * 获取文件名
     * @param filePath
     * @return
     */
   public static String getFileName(String filePath) {
       File tempFile =new File( filePath.trim());
       String fileName = tempFile.getName();
       return fileName;
   }

    /**
     *  获取文件hash值
     * @param filePath
     * @return
     */
   public static String getFileHashCode(String filePath) {
       String hashCode = SHAUtil.SHAHashCode(filePath);
       return hashCode;
   }

    /**
     *  获取文件大小
     * @param filePath
     * @return
     */
   public static long getFileSize(String filePath) {
      File file = new File(filePath);
      if(file.exists() && file.isFile()) {
          return file.length();
      }else {
          return 0;
      }
   }

    /**
     * 创建文件目录
     * @param destDirName
     * @return
     */
   public static File createDir(String destDirName) {
       File dir = new File(destDirName);
       if(!dir.exists()) {
           dir.mkdirs();
       }
       return dir;
   }

    /**
     * 文件复制
     * @param sourcePath
     * @param destPath
     */
   public static void copyFile(String sourcePath, String destPath) {
       InputStream fis = null;
       OutputStream fos = null;
       try {
           File source = FileUtil.createFile(sourcePath);
           int flag = 0;
           if(sourcePath.contains("\\")) {
               flag = sourcePath.lastIndexOf("\\");
           }else if(sourcePath.contains("/")) {
               flag = sourcePath.lastIndexOf("/");
           }
           String txts= sourcePath.substring(flag);
           File target = FileUtil.createFile(destPath + File.separator + txts);
           fis = new FileInputStream(source);
           fos = new FileOutputStream(target);
           byte[] buf = new byte[4096];
           int i;
           while ((i = fis.read(buf)) != -1) {
               fos.write(buf, 0, i);
           }
           fis.close();
           fos.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

    /**
     * 文件按指定编码复制
     * @param inputFile
     * @param inputCharset
     * @param outputFile
     * @param outputCharset
     * @throws Exception
     */
    public static void copyFile(String inputFile, String inputCharset,
                                String outputFile, String outputCharset) throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(new File(
                inputFile)), inputCharset);
        Writer writer = new OutputStreamWriter(new FileOutputStream(new File(
                outputFile)), outputCharset);
        int temp = 0;
        try {
            while ((temp = reader.read()) != -1) {
                writer.write(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件
     * @param filePath
     */
    public static File createFile (String filePath) {
        try {
            File file = new File(filePath);
            if(!file.exists()) {
                // 先得到文件的上级目录，并创建上级目录，在创建文件
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            return file;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 写入文件
     * @param metaFile   目标文件
     * @param filemodel  写入内容
     * @param encoding   指定文件编码
     */
    public static void writeContent(File metaFile, FileModel filemodel, String encoding) {
        FileOutputStream fos = null;
        OutputStreamWriter os = null;
        try {
            fos = new FileOutputStream(metaFile.getPath());
            os = new OutputStreamWriter(fos, encoding);
            // 遍历对象属性
            for (Field field : filemodel.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                os.write(field.getName() + ":" + field.get(filemodel) + "\n");
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally{
            try {
                if(os != null) {
                    os.flush();
                    os.close();
                }
                if(fos != null) {
                    fos.flush();
                    fos.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件后缀名
     * @param fileName
     * @return String
     */
    public static String getFileSufix(String fileName){
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }
}
