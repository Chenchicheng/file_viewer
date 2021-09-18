package com.filepreview.conventer;

import com.filepreview.model.FileModel;
import com.filepreview.util.FileCharsetDetector;
import com.filepreview.util.FileUtil;
import org.mozilla.intl.chardet.nsPSMDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by chicheng on 2017/12/27.
 */
@Component
public class TextFileConverter extends AbstractConverter {

    private static final Logger logger = LoggerFactory.getLogger(TextFileConverter.class);

    @Override
    public void convert(FileModel fileModel) {
        File resourceDir = super.createConvertResource(fileModel);
        if (resourceDir.exists() && resourceDir.isDirectory()) {
            // 纯文本,涉及到文件编码问题
            try {
                String charset = getCharset(fileModel);
                // 纯文本一律先转化为UTF-8的txt文件再进行转码
                String outFile = resourceDir.getPath() + File.separator + FileUtil.getFileName(fileModel.getOriginalFile()) + "-utf8.txt";
                // 将文本文件复制到resource目录
                FileUtil.copyFile(fileModel.getFilePath(), charset, outFile, "UTF-8");
                fileModel.setState(FileModel.STATE_YZH);
                fileModel.setConvertedFileDir(resourceDir.getPath());
                fileModel.setConvertedFileName(FileUtil.getFileName(fileModel.getOriginalFile()) + "-utf8.txt");
                fileModel.setConvertedFilePath(resourceDir + File.separator + fileModel.getConvertedFileName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.createMetaFile(fileModel);
    }

    /**
     * 获取文件编码
     *
     * @param fileModel fileModel
     * @return String
     * @throws IOException
     */
    private String getCharset(FileModel fileModel) throws IOException {
        File original = new File(fileModel.getFilePath());
        FileCharsetDetector.Observer oCharset = FileCharsetDetector.guessFileEncoding(original, nsPSMDetector.CHINESE);
        String charset = null;
        if (oCharset.isFound()) {
            // 探测到编码
            charset = oCharset.getEncoding();
        } else if (oCharset.getEncoding() != null && !oCharset.isFound()) {
            // 猜测到编码
            logger.error("Doc2PdfServiceImpl@convert error:" + fileModel.getOriginal() + ",use the guess charset:" + oCharset.getEncoding());
            charset = oCharset.getEncoding();
        } else {
            // 未找到编码,大部分情况 文档都是来自同一个window系统,使用GBK
            logger.error("Doc2PdfServiceImpl@convert error:" + fileModel.getOriginal() + ",can't find the charset.use :GBK");
            charset = "GBK";
        }
        return charset;
    }
}
