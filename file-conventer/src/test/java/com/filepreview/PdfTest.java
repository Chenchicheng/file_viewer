package com.filepreview;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by chicheng on 2018/1/3.
 */
public class PdfTest {

    public static void findPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        FileInputStream in = new FileInputStream(new File(""));
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[512];
        while ((in.read(b))!=-1) {
            out.write(b);
        }
        out.flush();
        in.close();
        out.close();
    }

}
