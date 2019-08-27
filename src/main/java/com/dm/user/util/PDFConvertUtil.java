
package com.dm.user.util;

import com.dm.user.entity.CertFiles;
import com.dm.user.service.CertFilesService;
import com.itextpdf.text.pdf.BaseFont;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class PDFConvertUtil {

    @Autowired
    private CertFilesService certFilesService;

    public Integer acceptPage(String text,Integer certId) throws Exception {
        Document parse = Jsoup.parse(text);
        Elements meta = parse.getElementsByTag("meta");
        String html = parse.html();
        for (org.jsoup.nodes.Element link : meta) {
            String s = link.outerHtml();
            String s1 = s.replace("/>", ">");
            html = html.replace(s, s1 + "</meta>");
        }
        Elements link1 = parse.getElementsByTag("link");
        for (Element link : link1) {
            String s = link.outerHtml();
            String s1 = s.replace("/>", ">");
            html = html.replace(s, s1 + "</link>");

        }
        html = html.replaceAll("&nbsp;", "");
        try {
            String osname = System.getProperty("os.name").toLowerCase();
            String outputFile;
            String fontPath;
            CertFiles certFiles = new CertFiles();
            String s = UUID.randomUUID().toString();
            if (osname.startsWith("win")) {
                outputFile = "D:\\upload\\" + s + ".pdf";
                fontPath = "C:\\Windows\\Fonts\\simsun.ttc";
                certFiles.setFilePath("D:\\upload"+s+".pdf");
                certFiles.setFileUrl("http:\\\\192.168.3.101\\img\\"+s+".pdf");
            } else {
                outputFile = "/opt/czt-upload/userTemplate/" + s + ".pdf";
                fontPath = "/opt/jdk1.8.0_221/jre/lib/fonts/simhei.ttf";
                certFiles.setFileUrl("/opt/czt-upload/"+s+".pdf");
                certFiles.setFilePath("http://114.244.37.10:7080/img/"+s+".pdf");
            }
            OutputStream os = new FileOutputStream(outputFile);
            ITextRenderer render = new ITextRenderer();
            ITextFontResolver fontResolver = render.getFontResolver();
            fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            render.setDocumentFromString(html);
            render.layout();
            render.createPDF(os);
            os.close();
            certFiles.setFileName(s + ".pdf");
            certFiles.setFileType("pdf");
            certFiles.setFileSeq("1");
            certFiles.setCertId(certId);
            certFilesService.insertSelective(certFiles);
            return certFiles.getFileId();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}


