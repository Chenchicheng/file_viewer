package com.filepreview;


import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.*;
import java.io.*;


/**
 * Created by chicheng on 2017/12/27.
 */
public class ConventerTest {

    static String soffice_host = "127.0.0.1";
    static String soffice_port = "8100";

    private static OfficeManager officeManager;
    /**
     * 转换文件
     *
     * @throws FileNotFoundException
     * */
    public static String doc2Html(String inputFilePath, String outputFolder) throws FileNotFoundException {
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new FileNotFoundException("要转换的文件不存在：" + inputFilePath);
        }

        File toFileFolder = new File(outputFolder);
        if (!toFileFolder.exists()) {
            toFileFolder.mkdirs();
        }

        InputStream fromFileInputStream = new FileInputStream(inputFile);


        String htmFileName = "htmlfile"  + ".pdf";
        String docFileName = "docfile"  + inputFilePath.substring(inputFilePath.lastIndexOf("."));

        File htmlOutputFile = new File(toFileFolder.toString() + File.separatorChar + htmFileName);
        File docInputFile = new File(toFileFolder.toString() + File.separatorChar + docFileName);

        /**
         * 由fromFileInputStream构建输入文件
         * */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fromFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();

        configuration.setOfficeHome("C:/Program Files (x86)/OpenOffice 4");// 设置OpenOffice.org安装目录
        configuration.setPortNumbers(8101); // 设置转换端口，默认为8100
        configuration.setTaskExecutionTimeout(1000 * 60 * 5L);// 设置任务执行超时为5分钟
        configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);// 设置任务队列超时为24小时


        officeManager = configuration.buildOfficeManager();
        officeManager.start(); // 启动服务

        // convert
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(docInputFile, htmlOutputFile);

        // 转换完之后删除word文件
        // docInputFile.delete();
        return htmFileName;
    }


    public static void main(String[] args) throws IOException {
        //Doc2HtmlUtil.doc2Html("D://pdf//转换用.ppt", "D://pdf//ttttt444//");
        ConventerTest.doc2Html("d:\\Documents\\Desktop\\相关文档\\云办公开发规范.pptx", "d:\\temp");

    }
}
