package com.filepreview.conventer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenchicheng
 * @date 2021/9/17
 */
@Component
@DependsOn(value = {"fileType"})
public class ConvertContext {

    @Autowired
    private ImageFileConverter imageFileConverter;
    @Autowired
    private OfficeFileConverter officeFileConverter;
    @Autowired
    private PdfFileConverter pdfFileConverter;
    @Autowired
    private TextFileConverter textFileConverter;
    @Autowired
    private CompressedFileConverter compressedFileConverter;

    private final Map<String, AbstractConverter> strategies = new HashMap<>();

    @PostConstruct
    private void init() {
        strategies.put(FileType.pdfType, pdfFileConverter);
        strategies.put(FileType.officeType, officeFileConverter);
        strategies.put(FileType.compressType, compressedFileConverter);
        strategies.put(FileType.imgType, imageFileConverter);
        strategies.put(FileType.textType, textFileConverter);
    }
    public AbstractConverter getConverter(String type) {
        for (String key : strategies.keySet()) {
            if (key.contains(type)) return strategies.get(key);
        }
        return null;
    }
}
