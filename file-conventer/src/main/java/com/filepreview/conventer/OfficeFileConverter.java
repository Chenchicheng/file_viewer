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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class OfficeFileConverter extends AbstractConverter {

    private static final Logger logger = LoggerFactory.getLogger(OfficeFileConverter.class);

    @Value("${soffice.home}")
    private String officeHome;

    @Value("${soffice.port}")
    private int port;

    /**
     * @Fields officeManager : openoffice管理器
     */
    private OfficeManager officeManager;

    private static final List<String> htmlFormats = new ArrayList<>(Arrays.asList("xlsx", "xls", "pptx", "ppt"));
    private static final String htmlSubFix = ".html";
    private static final String pdfSubFix = ".pdf";
    private static final String htmlMIMEType = "text/html";
    private static final String pdfMIMEType = "application/pdf";

    @Override
    public void convert(FileModel fileModel) {
        String subFix = FileUtil.getFileSufix(fileModel.getOriginalFile());
        File convertResource = super.createConvertResource(fileModel);
        if (htmlFormats.contains(subFix)) {
            this.convertToHtml(fileModel, convertResource);
        } else {
            this.convertToPdf(fileModel, convertResource);
        }
        super.createMetaFile(fileModel);
    }

    /**
     * office文件统一转为html格式文件
     *
     * @param fileModel   fileModel
     * @param resourceDir resourceDir
     */
    private void convertToHtml(FileModel fileModel, File resourceDir) {
        if (resourceDir.exists() && resourceDir.isDirectory()) {
            // 进行文件转换
            String fileName = FileUtil.getFileName(fileModel.getOriginal());
            String htmlFilePath = fileName + htmlSubFix;
            // 转换后的文件放在resource目录中
            String outputFile = resourceDir.getPath() + File.separator + htmlFilePath;
            logger.info("进行文档转换:" + fileModel.getFilePath() + " --> " + outputFile);
            OfficeDocumentConverter converter = new OfficeDocumentConverter(this.officeManager);
            File input = new File(fileModel.getFilePath());
            File html = new File(outputFile);
            converter.convert(input, html);
            fileModel.setState(FileModel.STATE_YZH);
            fileModel.setConvertedFileName(htmlFilePath);
            fileModel.setConvertedFilePath(outputFile);
            fileModel.setConvertedFileDir(resourceDir.getPath());
            // 设置content-type
            fileModel.setOriginalMIMEType(htmlMIMEType);
        }
    }

    /**
     * office文件转为pdf格式
     *
     * @param fileModel   fileModel
     * @param resourceDir resourceDir
     */
    private void convertToPdf(FileModel fileModel, File resourceDir) {
        if (resourceDir.exists() && resourceDir.isDirectory()) {
            // 进行文件转换
            String fileName = FileUtil.getFileName(fileModel.getOriginal());
            String htmlFilePath = fileName + pdfSubFix;
            // 转换后的文件放在resource目录中
            String outputFile = resourceDir.getPath() + File.separator + htmlFilePath;
            logger.info("进行文档转换:" + fileModel.getFilePath() + " --> " + outputFile);
            OfficeDocumentConverter converter = new OfficeDocumentConverter(this.officeManager);
            File input = new File(fileModel.getFilePath());
            File html = new File(outputFile);
            converter.convert(input, html);
            fileModel.setState(FileModel.STATE_YZH);
            fileModel.setConvertedFileName(htmlFilePath);
            fileModel.setConvertedFilePath(outputFile);
            fileModel.setConvertedFileDir(resourceDir.getPath());
            // 设置content-type
            fileModel.setOriginalMIMEType(pdfMIMEType);
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
        logger.warn("start openoffice....");
        // 设置OpenOffice.org安装目录
        configuration.setOfficeHome(officeHome);
        // 设置转换端口，默认为8100
        configuration.setPortNumbers(port);
        // 设置任务执行超时为5分钟
        configuration.setTaskExecutionTimeout(1000 * 60 * 5L);
        // 设置任务队列超时为24小时
        configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);

        officeManager = configuration.buildOfficeManager();
        // 启动服务
        officeManager.start();
        logger.warn("openoffice start success!");
    }

    private void stopService() {
        logger.warn("stop openoffice...");
        if (officeManager != null) {
            officeManager.stop();
        }
        logger.warn("stop openoffice success!");
    }
}
