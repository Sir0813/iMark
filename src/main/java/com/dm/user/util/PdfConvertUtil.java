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

/**
 * @author cui
 * @date 2019-09-26
 */
@Component
public class PdfConvertUtil {

    @Autowired
    private CertFilesService certFilesService;

    public Integer acceptPage(String text, Integer certId) throws Exception {
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
            String s = UUID.randomUUID().toString() + ".pdf";
            if (osname.startsWith("win")) {
                outputFile = "D:\\upload\\" + s;
                fontPath = "C:\\Windows\\Fonts\\simsun.ttc";
                certFiles.setFilePath("D:\\upload\\" + s);
                certFiles.setFileUrl("http://192.168.3.101/img/" + s);
            } else {
                outputFile = "/opt/czt-upload/userTemplate/" + s;
                fontPath = "/opt/jdk1.8.0_221/jre/lib/fonts/simsun.ttc";
                certFiles.setFilePath("/opt/czt-upload/userTemplate/" + s);
                certFiles.setFileUrl("http://114.244.37.10:9082/img/userTemplate/" + s);
            }
            OutputStream os = new FileOutputStream(outputFile);
            ITextRenderer render = new ITextRenderer();
            ITextFontResolver fontResolver = render.getFontResolver();
            fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            render.setDocumentFromString(html);
            render.layout();
            render.createPDF(os);
            os.close();
            String md5 = ShaUtil.getMD5(certFiles.getFilePath());
            certFiles.setFileHash(md5);
            certFiles.setFileName(s);
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


