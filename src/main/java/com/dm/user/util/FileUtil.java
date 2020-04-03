package com.dm.user.util;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.AppUser;
import com.dm.user.entity.CertFiles;
import com.dm.user.mapper.UserMapper;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.CertFilesService;
import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author cui
 * @date 2019-09-26
 */
@Component
public class FileUtil {

    @Value("${upload.certFilePath}")
    private String certFilePath;

    @Value("${upload.certFilePrefix}")
    private String certFilePrefix;

    @Value("${upload.outCertFilePath}")
    private String outCertFilePath;

    @Value("${upload.outCertFilePrefix}")
    private String outCertFilePrefix;

    @Value("${upload.headFilePrefix}")
    private String headFilePrefix;

    @Value("${upload.headFilePath}")
    private String headFilePath;

    @Value("${upload.applyFilePath}")
    private String applyFilePath;

    @Value("${upload.applyFilePrefix}")
    private String applyFilePrefix;

    @Autowired
    private CertFilesService certFilesService;

    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> uploadFile(HttpServletRequest request, HttpServletResponse response,
                                          MultipartFile[] multipartFile, String aid, String signature) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>(16);
            if (multipartFile.length == 0) {
                map.put("nofile", "请选择文件");
                return map;
            }
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            String[] certIds = new String[multipartFile.length];
            StringBuilder inData = new StringBuilder();
            for (int i = 0; i < multipartFile.length; i++) {
                String fileName = multipartFile[i].getOriginalFilename();
                int indexOf = fileName.lastIndexOf(".");
                String suffix = "";
                if (indexOf >= 0) {
                    suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
                }
                UUID randomUuid = UUID.randomUUID();
                String filePath = certFilePath + File.separator + randomUuid + suffix;
                String fileUrl = certFilePrefix + File.separator + randomUuid + suffix;
                boolean uploadBoolean = FileUtil.uploadFile(certFilePath + File.separator, randomUuid + suffix, multipartFile[i]);
                if (!uploadBoolean) {
                    throw new Exception();
                }
                CertFiles certFiles = new CertFiles();
                String osname = System.getProperty("os.name").toLowerCase();
                if (".mp4".equals(suffix) || ".mov".equals(suffix)) {
                    String uuid = UUID.randomUUID().toString() + ".png";
                    if (osname.startsWith(StateMsg.OS_NAME)) {
                        FileUtil.fetchFrame(filePath, "D:\\upload\\vidopng\\" + uuid);
                        certFiles.setFileName("img/vidopng/" + uuid);
                    } else {
                        FileUtil.fetchFrame(filePath, "/opt/czt-upload/vidopng/" + uuid);
                        certFiles.setFileName("vidopng/" + uuid);
                    }
                } else {
                    certFiles.setFileName(fileName);
                }
                String md5 = ShaUtil.getMD5(filePath);
                inData.append(md5);
                certFiles.setFileHash(md5);
                certFiles.setFileUrl(fileUrl);
                certFiles.setFilePath(filePath);
                certFiles.setFileSize(Double.valueOf(multipartFile[i].getSize()));
                certFiles.setFileType(suffix);
                certFiles.setFileSeq(i + "");
                certFilesService.insertSelective(certFiles);
                certIds[i] = String.valueOf(certFiles.getFileId());
            }
            /*boolean verifyState = false;
            if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(inData) && StringUtils.isNotBlank(signature)) {
                verifyState = IkiUtil.verifyData(aid, inData.toString(), signature);
            }
            if (!verifyState) {
                map.put("verifyFailed", "verifyFailed");
            }*/
            map.put("fileIds", certIds);
            return map;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Result uploadHeadPhoto(HttpServletRequest request, HttpServletResponse response,
                                  MultipartFile multipartFile) {
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            String fileName = multipartFile.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
            UUID randomUuid = UUID.randomUUID();
            String fileUrl = headFilePrefix + File.separator + randomUuid + suffix;
            boolean uploadBoolean = FileUtil.uploadFile(headFilePath + File.separator, randomUuid + suffix, multipartFile);
            if (!uploadBoolean) {
                throw new Exception();
            }
            CertFiles certFiles = new CertFiles();
            certFiles.setFileName(fileName);
            certFiles.setFileUrl(fileUrl);
            certFiles.setFilePath(headFilePath + File.separator + randomUuid + suffix);
            certFiles.setFileSize(Double.valueOf(multipartFile.getSize()));
            certFiles.setFileType(suffix);
            certFiles.setFileSeq("0");
            certFilesService.insertSelective(certFiles);
            AppUser user = userMapper.findByName(LoginUserHelper.getUserName());
            CertFiles cf = certFilesService.selectByUrl(user.getHeadPhoto());
            if (null != cf) {
                File file = new File(cf.getFilePath());
                file.delete();
                certFilesService.deleteByPrimaryKey(cf.getFileId());
            }
            user.setHeadPhoto(fileUrl);
            userMapper.updateByPrimaryKeySelective(user);
            return ResultUtil.success();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error();
    }

    public static boolean uploadFile(String filePath, String fileName, MultipartFile multipartFile) {
        try {
            File file = new File(filePath + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            multipartFile.transferTo(file);
            return true;
        } catch (IllegalStateException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static String getHtmlTemplate(String filePath) {
        BufferedReader in;
        String str;
        StringBuffer sb = new StringBuffer();
        try {
            in = new BufferedReader(new FileReader(new File(filePath)));
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void fetchFrame(String filePath, String targerFilePath) {
        Frame frame = null;
        int flag = 0;
        try {
            FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(filePath);
            fFmpegFrameGrabber.start();
            String rotate = fFmpegFrameGrabber.getVideoMetadata("rotate");
            int ftp = fFmpegFrameGrabber.getLengthInFrames();
            while (flag <= ftp) {
                frame = fFmpegFrameGrabber.grabImage();
                if (frame != null && flag == 5) {
                    File outPut = new File(targerFilePath);
                    ImageIO.write(FrameToBufferedImage(frame), "jpg", outPut);
                    break;
                }
                flag++;
            }
            fFmpegFrameGrabber.stop();
            fFmpegFrameGrabber.close();
            if (rotate != null && !rotate.isEmpty()) {
                int rotate1 = Integer.parseInt(rotate);
                rotatePhonePhoto(targerFilePath, rotate1);
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static String rotatePhonePhoto(String fullPath, int angel) {
        BufferedImage src;
        try {
            src = ImageIO.read(new File(fullPath));
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);
            int swidth = src_width;
            int sheight = src_height;
            if (angel == 90 || angel == 270) {
                swidth = src_height;
                sheight = src_width;
            }
            Rectangle rect_des = new Rectangle(new Dimension(swidth, sheight));
            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
            g2.drawImage(src, null, null);
            ImageIO.write(res, "jpg", new File(fullPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullPath;
    }

    public static BufferedImage FrameToBufferedImage(Frame frame) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    /**
     * 打包下载
     *
     * @param files       文件
     * @param zipFilePath 打成压缩包的路径
     * @param fileName    文件名称
     * @return
     */
    public boolean fileToZip(List<File> files, String zipFilePath, String fileName) {
        FileInputStream fis;
        BufferedInputStream bis = null;
        FileOutputStream fos;
        ZipOutputStream zos = null;
        try {
            File zipFile = new File(zipFilePath + File.separator + fileName + ".zip");
            if (files.size() == 0) {
                return false;
            } else {
                fos = new FileOutputStream(zipFile);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                byte[] bufs = new byte[1024 * 10];
                for (int i = 0; i < files.size(); i++) {
                    //创建ZIP实体，并添加进压缩包
                    ZipEntry zipEntry = new ZipEntry(files.get(i).getName());
                    zos.putNextEntry(zipEntry);
                    //读取待压缩的文件并写进压缩包里
                    fis = new FileInputStream(files.get(i));
                    bis = new BufferedInputStream(fis, 1024 * 10);
                    int read = 0;
                    while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                        zos.write(bufs, 0, read);
                    }
                }
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != zos) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public Map<String, Object> realUpload(HttpServletRequest request, HttpServletResponse response,
                                          MultipartFile[] multipartFile, String type)
            throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> map = new HashMap<>(16);
        String[] fileIds = new String[multipartFile.length];
        for (int i = 0; i < multipartFile.length; i++) {
            String fileName = multipartFile[i].getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
            String fileNewName = UUID.randomUUID().toString() + suffix;
            String filePath = "";
            String fileUrl = "";
            boolean uploadBoolean = false;
            if ("outcert".equals(type)) {
                filePath = outCertFilePath + File.separator + fileNewName;
                fileUrl = outCertFilePrefix + File.separator + fileNewName;
                uploadBoolean = FileUtil.uploadFile(outCertFilePath + File.separator, fileNewName, multipartFile[i]);
            }
            if (!uploadBoolean) {
                throw new Exception("文件上传失败!");
            }
            CertFiles certFiles = new CertFiles();
            certFiles.setFileName(fileName);
            certFiles.setFileUrl(fileUrl);
            certFiles.setFilePath(filePath);
            certFiles.setFileSize(Double.valueOf(multipartFile[i].getSize()));
            certFiles.setFileType(suffix);
            certFiles.setFileSeq(i + "");
            certFilesService.insertSelective(certFiles);
            fileIds[i] = String.valueOf(certFiles.getFileId());
        }
        map.put("fileIds", fileIds);
        return map;
    }

    public Map<String, Object> itemUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            String aid = request.getParameter("aid");
            String signature = request.getParameter("signature");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            String requeredids = multipartHttpServletRequest.getParameter("requeredids");
            String[] split = requeredids.split(",");
            Map<String, Object> map = new LinkedHashMap<>(16);
            StringBuilder inData = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                List<MultipartFile> file = multipartHttpServletRequest.getFiles("row" + s);
                String fileIds = "";
                for (int j = 0; j < file.size(); j++) {
                    MultipartFile multipartFile = file.get(j);
                    String fileName = multipartFile.getOriginalFilename();
                    int indexOf = fileName.lastIndexOf(".");
                    String suffix = "";
                    if (indexOf >= 0) {
                        suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
                    }
                    UUID randomUuid = UUID.randomUUID();
                    String filePath = applyFilePath + File.separator + LoginUserHelper.getUserId() + File.separator + randomUuid + suffix;
                    String fileUrl = applyFilePrefix + File.separator + LoginUserHelper.getUserId() + File.separator + randomUuid + suffix;
                    boolean uploadBoolean = uploadFile(filePath, "", multipartFile);
                    if (!uploadBoolean) {
                        throw new Exception("上传失败");
                    }
                    CertFiles certFiles = new CertFiles();
                    // 缩略图
                    if (".bmp.jpg.wbmp.jpeg.png.gif".contains(suffix.toLowerCase())) {
                        Thumbnails.of(filePath).size(200, 300).toFile(applyFilePath + File.separator + LoginUserHelper.getUserId() + File.separator + ImageUtil.DEFAULT_PREVFIX + randomUuid + suffix);
                        String thumbUrl = applyFilePrefix + File.separator + LoginUserHelper.getUserId() + File.separator + ImageUtil.DEFAULT_PREVFIX + randomUuid + suffix;
                        certFiles.setThumbUrl(thumbUrl);
                    }
                    /*boolean b = ImageUtil.thumbnailImage(filePath, 100, 150, ImageUtil.DEFAULT_PREVFIX, ImageUtil.DEFAULT_FORCE);
                    if (b) {
                        String thumbUrl = applyFilePrefix + File.separator + LoginUserHelper.getUserId() + File.separator + ImageUtil.DEFAULT_PREVFIX + randomUuid + suffix;
                        certFiles.setThumbUrl(thumbUrl);
                    }*/
                    // 文件表插入文件
                    certFiles.setFileName(fileName);
                    certFiles.setFileUrl(fileUrl);
                    certFiles.setFilePath(filePath);
                    certFiles.setFileSize(Double.valueOf(multipartFile.getSize()));
                    certFiles.setFileType(suffix);
                    certFiles.setFileSeq(j + "");
                    certFilesService.insertSelective(certFiles);
                    fileIds += (certFiles.getFileId() + ",");
                    // String md5 = ShaUtil.getMD5(filePath);
                    // inData.append(md5);
                }
                map.put("row" + s, fileIds);
            }
            /*boolean verifyState = false;
            if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(inData) && StringUtils.isNotBlank(signature)) {
                verifyState = IkiUtil.verifyData(aid, inData.toString(), signature);
            }
            if (!verifyState) {
                map.put("verifyFailed", "verifyFailed");
            }*/
            return map;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Map<String, Object> applyUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            String aid = request.getParameter("aid");
            String signature = request.getParameter("signature");
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> file = multipartHttpServletRequest.getFiles("file");
            String fileName = file.get(0).getOriginalFilename();
            int indexOf = fileName.lastIndexOf(".");
            String suffix = "";
            if (indexOf >= 0) {
                suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
            }
            UUID randomUuid = UUID.randomUUID();
            String filePath = applyFilePath + File.separator + LoginUserHelper.getUserId() + File.separator + randomUuid + suffix;
            String fileUrl = applyFilePrefix + File.separator + LoginUserHelper.getUserId() + File.separator + randomUuid + suffix;
            boolean uploadBoolean = uploadFile(filePath, "", file.get(0));
            if (!uploadBoolean) {
                throw new Exception("上传失败");
            }
            CertFiles certFiles = new CertFiles();
            certFiles.setFileName(fileName);
            certFiles.setFileUrl(fileUrl);
            certFiles.setFilePath(filePath);
            certFiles.setFileSize(Double.valueOf(file.get(0).getSize()));
            certFiles.setFileType(suffix);
            certFiles.setFileSeq("0");
            certFilesService.insertSelective(certFiles);
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("fileid", certFiles.getFileId());
            StringBuilder inData = new StringBuilder();
            // String md5 = ShaUtil.getMD5(filePath);
            // inData.append(md5);
            /*boolean verifyState = false;
            if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(inData) && StringUtils.isNotBlank(signature)) {
                verifyState = IkiUtil.verifyData(aid, inData.toString(), signature);
            }
            if (!verifyState) {
                map.put("verifyFailed", "verifyFailed");
            }*/
            return map;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Result itemUploadOne(HttpServletRequest request, HttpServletResponse response, MultipartFile multipartFile) throws Exception {
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            String row = request.getParameter("row");
            String item = request.getParameter("item");
            String fileName = multipartFile.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
            UUID randomUuid = UUID.randomUUID();
            String filePath = applyFilePath + File.separator + LoginUserHelper.getUserId() + File.separator + randomUuid + suffix;
            String fileUrl = applyFilePrefix + File.separator + LoginUserHelper.getUserId() + File.separator + randomUuid + suffix;
            boolean uploadBoolean = uploadFile(filePath, "", multipartFile);
            if (!uploadBoolean) {
                throw new Exception();
            }
            CertFiles certFiles = new CertFiles();
            if (".bmp.jpg.wbmp.jpeg.png.gif".contains(suffix.toLowerCase())) {
                Thumbnails.of(filePath).size(200, 300).toFile(applyFilePath + File.separator + LoginUserHelper.getUserId() + File.separator + ImageUtil.DEFAULT_PREVFIX + randomUuid + suffix);
                String thumbUrl = applyFilePrefix + File.separator + LoginUserHelper.getUserId() + File.separator + ImageUtil.DEFAULT_PREVFIX + randomUuid + suffix;
                certFiles.setThumbUrl(thumbUrl);
            }
            String osname = System.getProperty("os.name").toLowerCase();
            if (".mp4".equals(suffix) || ".mov".equals(suffix)) {
                String uuid = UUID.randomUUID().toString() + ".png";
                if (osname.startsWith(StateMsg.OS_NAME)) {
                    FileUtil.fetchFrame(filePath, "D:\\upload\\vidopng\\" + uuid);
                    certFiles.setThumbUrl("vidopng" + File.separator + uuid);
                } else {
                    FileUtil.fetchFrame(filePath, applyFilePath + File.separator + LoginUserHelper.getUserId() + File.separator + uuid);
                    certFiles.setThumbUrl(applyFilePrefix + File.separator + LoginUserHelper.getUserId() + File.separator + uuid);
                }
            }
            certFiles.setFileName(fileName);
            certFiles.setFileUrl(fileUrl);
            certFiles.setFilePath(filePath);
            certFiles.setFileSize(Double.valueOf(multipartFile.getSize()));
            certFiles.setFileType(suffix);
            certFiles.setFileSeq("0");
            certFilesService.insertSelective(certFiles);
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("row", Integer.parseInt(row));
            map.put("item", Integer.parseInt(item));
            map.put("fileid", certFiles.getFileId());
            map.put("thumbUrl", certFiles.getThumbUrl());
            return ResultUtil.success(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
