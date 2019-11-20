package com.dm.user.util;

import com.ahdms.exception.*;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.CertFiles;
import com.dm.user.entity.User;
import com.dm.user.mapper.UserMapper;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.CertFilesService;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
                        certFiles.setFileName("http://192.168.3.101/img/vidopng/" + uuid);
                    } else {
                        FileUtil.fetchFrame(filePath, "/opt/czt-upload/vidopng/" + uuid);
                        certFiles.setFileName("http://192.168.3.123:8082/img/vidopng/" + uuid);
                    }
                } else {
                    certFiles.setFileName(fileName);
                }
                if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(signature)) {
                    String md5 = ShaUtil.getMD5(filePath);
                    inData.append(md5);
                    certFiles.setFileHash(md5);
                }
                certFiles.setFileUrl(fileUrl);
                certFiles.setFilePath(filePath);
                certFiles.setFileSize(Double.valueOf(multipartFile[i].getSize()));
                certFiles.setFileType(suffix);
                certFiles.setFileSeq(i + "");
                certFilesService.insertSelective(certFiles);
                certIds[i] = String.valueOf(certFiles.getFileId());
            }
            boolean verifyState = false;
            if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(inData) && StringUtils.isNotBlank(signature)) {
                try {
                    verifyState = IkiUtil.verifyData(aid, inData.toString(), signature);
                } catch (SVS_ServerConnectException e) {
                    e.printStackTrace();
                } catch (SVS_SignatureEncodeException e) {
                    e.printStackTrace();
                } catch (SVS_CertExpiredException e) {
                    e.printStackTrace();
                } catch (SVS_NotFoundPKMException e) {
                    e.printStackTrace();
                } catch (SVS_CertException e) {
                    e.printStackTrace();
                } catch (SVS_SignatureException e) {
                    e.printStackTrace();
                } catch (SVS_VerifyDataException e) {
                    e.printStackTrace();
                } catch (SVS_InvalidParameterException e) {
                    e.printStackTrace();
                } catch (SVS_CheckIRLException e) {
                    e.printStackTrace();
                } catch (SVS_CertNotTrustException e) {
                    e.printStackTrace();
                } catch (SVS_CertIneffectiveException e) {
                    e.printStackTrace();
                } catch (SVS_CertTypeException e) {
                    e.printStackTrace();
                } catch (SVS_CertCancelException e) {
                    e.printStackTrace();
                } catch (SVS_GetIRLException e) {
                    e.printStackTrace();
                }
            }
            if (!verifyState) {
                map.put("verifyFailed", "verifyFailed");
            }
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
            User user = userMapper.findByName(LoginUserHelper.getUserName());
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

    /**
     * 文件上传
     *
     * @param filePath      文件路径
     * @param fileName      文件名称
     * @param multipartFile 文件
     * @return
     */
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

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param videofile 源视频文件路径
     * @param framefile 截取帧的图片存放路径
     * @throws Exception
     */
    public static void fetchFrame(String videofile, String framefile)
            throws Exception {
        try {
            FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(videofile);
            ff.start();
            String rotate = ff.getVideoMetadata("rotate");
            Frame f;
            int i = 0;
            while (i < 1) {
                f = ff.grabImage();
                opencv_core.IplImage src = null;
                if (null != rotate && rotate.length() > 1) {
                    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
                    src = converter.convert(f);
                    f = converter.convert(rotate(src, Integer.valueOf(rotate)));
                }
                doExecuteFrame(f, framefile);
                i++;
            }
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 旋转角度的
     */
    public static opencv_core.IplImage rotate(opencv_core.IplImage src, int angle) {
        opencv_core.IplImage img = opencv_core.IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        opencv_core.cvTranspose(src, img);
        opencv_core.cvFlip(img, img, angle);
        return img;
    }

    public static void doExecuteFrame(Frame f, String targerFilePath) {
        if (null == f || null == f.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "jpg";
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(targerFilePath);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打包下载
     *
     * @param sourceFilePath 指定文件路径
     * @param zipFilePath    打成压缩包的路径
     * @param fileName       文件名称
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

    /**
     * 文件转为文件流
     *
     * @param imagePath 图片路径
     * @return
     */
    @SuppressWarnings("restriction")
    public static String imageChangeBase64(String imagePath) {
        byte[] data = null;
        try {
            InputStream inputStream = new FileInputStream(imagePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public void fileDownload(HttpServletResponse response, String fileName, String filePath) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        try {
            OutputStream os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(filePath + fileName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
}
