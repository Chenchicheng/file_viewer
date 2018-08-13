package com.filepreview;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chicheng on 2017/12/29.
 */
public class DownLoadTest {
    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 得到输入流
        BufferedInputStream bis = null;
        InputStream inputStream = conn.getInputStream();
        bis = new BufferedInputStream(inputStream);
        String fileType = HttpURLConnection.guessContentTypeFromStream(bis);
        fileType = fileType.substring(fileType.lastIndexOf("/"));
        System.out.println("file type:" + fileType);
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        // 文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir + "/" + "aaa" + fileType);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }



    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void main(String[] args) {
        try{
            downLoadFromUrl("http://o8oy36u98.bkt.clouddn.com/my-java.xlsx", "E:/temp");
           /* downLoadFromUrl("http://file3.data.weipan.cn.wscdns.com/61146141/d30ba2094908f8dbdd17e8933641e89db72cdd9c?ip=1514540393,183.17.145.6&ssig=EMcdjWcnY4&Expires=1514540993&KID=sae,l30zoo1wmz&fn=%E7%B4%A0%E8%8F%9C%E9%A3%9F%E8%B0%B1%E2%80%94%E2%80%94%E7%BE%8E%E5%91%B3%E5%81%A5%E5%BA%B7%E7%94%9F%E6%B4%BB.pdf&skiprd=2&se_ip_debug=183.17.145.6&corp=2&from=1221134",
                    "百度.jpg","E:/temp");*/
        }catch (Exception e) {
            // TODO: handle exception
        }
    }
}
