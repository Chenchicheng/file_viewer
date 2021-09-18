package com.filepreview.conventer;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenchicheng
 * @date 2021/9/17
 */
@Component
public class FileType implements InitializingBean {

    public static String textType;
    public static String imgType;
    public static String officeType;
    public static String compressType;
    public static String pdfType;

    @Value("${text.type}")
    public void setTextType(String textType) {
        FileType.textType = textType;
    }
    @Value("${img.type}")
    public void setImgType(String imgType) {
        FileType.imgType = imgType;
    }
    @Value("${office.type}")
    public void setOfficeType(String officeType) {
        FileType.officeType = officeType;
    }
    @Value("${compress.type}")
    public void setCompressType(String compressType) {
        FileType.compressType = compressType;
    }
    @Value("${pdf.type}")
    public void setPdfType(String pdfType) {
        FileType.pdfType = pdfType;
    }

    private static final List<String> typeList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        typeList.add(textType);
        typeList.add(imgType);
        typeList.add(officeType);
        typeList.add(compressType);
        typeList.add(pdfType);
    }

    public static boolean contains(String type) {
        for (String s : typeList) {
            if (s.contains(type)) {
                return true;
            }
        }
        return false;
    }
}
