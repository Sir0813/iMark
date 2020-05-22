package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.CertFiles;
import com.dm.user.entity.ItemNotice;
import com.dm.user.entity.ItemNoticeApply;
import com.dm.user.mapper.ItemNoticeMapper;
import com.dm.user.service.CertFilesService;
import com.dm.user.service.ItemNoticeApplyService;
import com.dm.user.service.ItemNoticeService;
import com.dm.user.service.UserService;
import com.dm.user.util.FileUtil;
import com.dm.user.util.PdfKeywordFinder;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemNoticeServiceImpl implements ItemNoticeService {

    @Autowired
    private ItemNoticeMapper itemNoticeMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CertFilesService certFilesService;

    @Autowired
    private ItemNoticeApplyService noticeApplyService;

    @Autowired
    private FileUtil fileUtil;

    @Value("${upload.headFilePrefix}")
    private String headFilePrefix;

    @Value("${upload.headFilePath}")
    private String headFilePath;

    @Override
    public List<ItemNotice> noticeList() {
        List<ItemNotice> itemNoticeList = itemNoticeMapper.noticeList();
        return itemNoticeList;
    }

    @Override
    public List<ItemNoticeApply> signature(HttpServletRequest request, HttpServletResponse response, MultipartFile multipartFile) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
        UUID randomUuid = UUID.randomUUID();
        String fileUrl = headFilePrefix + File.separator + randomUuid + suffix;
        fileUtil.uploadFile(headFilePath + File.separator, randomUuid + suffix, multipartFile);
        CertFiles certFiles = new CertFiles();
        certFiles.setFileName(fileName);
        certFiles.setFileUrl(fileUrl);
        certFiles.setFilePath(headFilePath + File.separator + randomUuid + suffix);
        certFiles.setFileSize((double) multipartFile.getSize());
        certFiles.setFileType(suffix);
        certFiles.setFileSeq("0");
        certFilesService.insertSelective(certFiles);
        List<ItemNoticeApply> noticeApplies = new ArrayList<>(16);
        List<ItemNotice> itemNoticeList = itemNoticeMapper.noticeList();
        for (int i = 0; i < itemNoticeList.size(); i++) {
            ItemNotice itemNotice = itemNoticeList.get(i);
            float[] position = PdfKeywordFinder.getAddImagePositionXY(itemNotice.getFilePath(), "委托人（签字）：");
            PdfReader pdfReader = new PdfReader(new FileInputStream(new File(itemNotice.getFilePath())));
            //System.out.println("x:" + position[1] + " y:" + position[2]);
            String path = "/opt/czt-upload/apply/" + LoginUserHelper.getUserId() + "/signature/" + itemNotice.getFileName() + itemNotice.getFileType();
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(path));
            Image image = Image.getInstance(certFiles.getFilePath());
            image.setAbsolutePosition(position[1], position[2]);
            //System.out.println("pages:" + pdfReader.getNumberOfPages());
            PdfContentByte content = pdfStamper.getOverContent(1);
            ItemNoticeApply itemNoticeApply = new ItemNoticeApply();
            itemNoticeApply.setCreatedDate(new Date());
            itemNoticeApply.setFielPath(path);
            itemNoticeApply.setFileUrl("apply/" + LoginUserHelper.getUserId() + "/signature/" + itemNotice.getFileName() + itemNotice.getFileType());
            itemNoticeApply.setIsSignature(1);
            itemNoticeApply.setNoticeId(itemNotice.getId());
            itemNoticeApply.setUserId(Integer.parseInt(LoginUserHelper.getUserId()));
            itemNoticeApply.setFileName(itemNotice.getFileName());
            itemNoticeApply.setFileType(itemNotice.getFileType());
            noticeApplyService.insertData(itemNoticeApply);
            noticeApplies.add(itemNoticeApply);
            try {
                content.addImage(image);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            try {
                pdfStamper.close();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return noticeApplies;
    }
}
