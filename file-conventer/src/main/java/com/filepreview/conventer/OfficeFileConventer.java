package com.filepreview.conventer;

import com.filepreview.model.FileModel;
import com.filepreview.util.FileUtil;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class OfficeFileConventer {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${tmp.root}")
    private String root;

    @Value("${soffice.home}")
    private String officeHome;

    @Value("${soffice.port}")
    private int port;

    /**
     * @Fields officeManager : openoffice管理器
     */
    private OfficeManager officeManager;

    /**
     * office文件统一转为html格式文件
     * @param fileModel
     */
    public void conventerToHtml(FileModel fileModel) {

        // 创建hash目录
        String hashDirPath = root + File.separator + fileModel.getPathId();
        File hashDir = FileUtil.createDir(hashDirPath);
        if (hashDir.exists() && hashDir.isDirectory()) {
            // 复制源文件到hash目录
            String filePath = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
            FileUtil.copyFile(filePath, hashDirPath);
            // 计算文件大小
            fileModel.setFileSize(FileUtil.getFileSize(filePath));
            // 创建resource目录，存放源文件
            String resourceDirPath = hashDirPath + File.separator + "resource";
            File resourceDir = FileUtil.createDir(resourceDirPath);
            if (resourceDir.exists() && resourceDir.isDirectory()) {
                // 进行文件转换
                String fileName = FileUtil
                        .getFileName(fileModel.getOriginal());
                String htmlFilePath = fileName + ".html";
                String inputFile = fileModel.getTempDir() + File.separator
                        + fileModel.getOriginalFile();
                // 转换后的文件放在resource目录中
                String outputFile = resourceDirPath + File.separator
                        + htmlFilePath;
                this.logger.info("进行文档转换:" + inputFile + " --> " + outputFile);
                OfficeDocumentConverter converter = new OfficeDocumentConverter(this.officeManager);
                File input = new File(inputFile);
                File html = new File(outputFile);
                converter.convert(input, html);
                fileModel.setState(FileModel.STATE_YZH);
                fileModel.setConventedFileName(htmlFilePath);
                // 设置content-type
                fileModel.setOriginalMIMEType("text/html");
            }
            // 创建meta文件，存放文件基本信息
            String metaPath = hashDirPath + File.separator + "meta";
            File metaFile = FileUtil.createFile(metaPath);
            FileUtil.writeContent(metaFile, fileModel, "GBK");
        }
    }

    /**
     * office文件转为pdf格式
     */
    public void conventerToPdf(FileModel fileModel) {

        // 创建hash目录
        String hashDirPath = root + File.separator + fileModel.getPathId();
        File hashDir = FileUtil.createDir(hashDirPath);
        if (hashDir.exists() && hashDir.isDirectory()) {
            // 复制源文件到hash目录
            String filePath = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
            FileUtil.copyFile(filePath, hashDirPath);
            // 计算文件大小
            fileModel.setFileSize(FileUtil.getFileSize(filePath));
            // 创建resource目录，存放源文件
            String resourceDirPath = hashDirPath + File.separator + "resource";
            File resourceDir = FileUtil.createDir(resourceDirPath);
            if (resourceDir.exists() && resourceDir.isDirectory()) {
                // 进行文件转换
                String fileName = FileUtil
                        .getFileName(fileModel.getOriginal());
                String htmlFilePath = fileName + ".pdf";
                String inputFile = fileModel.getTempDir() + File.separator
                        + fileModel.getOriginalFile();
                // 转换后的文件放在resource目录中
                String outputFile = resourceDirPath + File.separator
                        + htmlFilePath;
                this.logger.info("进行文档转换:" + inputFile + " --> " + outputFile);
                OfficeDocumentConverter converter = new OfficeDocumentConverter(this.officeManager);
                File input = new File(inputFile);
                File html = new File(outputFile);
                converter.convert(input, html);
                fileModel.setState(FileModel.STATE_YZH);
                fileModel.setConventedFileName(htmlFilePath);
                // 设置content-type
                fileModel.setOriginalMIMEType("application/pdf");
            }
            // 创建meta文件，存放文件基本信息
            String metaPath = hashDirPath + File.separator + "meta";
            File metaFile = FileUtil.createFile(metaPath);
            FileUtil.writeContent(metaFile, fileModel, "GBK");
        }
    }

    @PostConstruct
    public void init() {
        // 开启openoffice服务
        startService();
    }

    @PreDestroy
    public void destroy() {
        stopService();
    }

    private void startService() {
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        this.logger.warn("start openoffice....");
        // 设置OpenOffice.org安装目录
        configuration.setOfficeHome(officeHome);
        // 设置转换端口，默认为8100
        configuration.setPortNumbers(port);
        // 设置任务执行超时为5分钟
        configuration.setTaskExecutionTimeout(1000 * 60 * 5L);
        // 设置任务队列超时为24小时
        configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);

        officeManager = configuration.buildOfficeManager();
        officeManager.start(); // 启动服务
        this.logger.warn("openoffice start success!");
    }

    private void stopService() {
        this.logger.warn("stop openoffice...");
        if (officeManager != null) {
            officeManager.stop();
        }
        this.logger.warn("stop openoffice success!");
    }
}
