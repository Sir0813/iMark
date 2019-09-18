package com.dm.user.util;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.CertFiles;
import com.dm.user.entity.User;
import com.dm.user.mapper.UserMapper;
import com.dm.user.service.CertFilesService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FileUtil {

	@Value("${upload.certFilePath}")
	private String uploadFilePath;
	
	@Value("${upload.certFilePrefix}")
	private String filePrefix;

	@Value("${upload.realFilePath}")
	private String realFilePath;

	@Value("${upload.realFilePrefix}")
	private String realFilePrefix;
	
	@Autowired
	private CertFilesService certFilesService;
	
	@Autowired
	private UserMapper userMapper;

	public Map<String,Object> uploadFile(HttpServletRequest request, HttpServletResponse response,
			MultipartFile[] multipartFile) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Map<String,Object>map = new HashMap<String,Object>();
		String certIds[] = new String[multipartFile.length];
		for (int i = 0; i < multipartFile.length; i++) {
			String fileName = multipartFile[i].getOriginalFilename();
			String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
			UUID randomUUID = UUID.randomUUID();
			String filePath = uploadFilePath + File.separator + randomUUID+suffix;
			String fileUrl = filePrefix+File.separator+randomUUID+suffix;
			boolean uploadBoolean = FileUtil.uploadFile(uploadFilePath+File.separator, randomUUID+suffix, multipartFile[i]);
			if (!uploadBoolean) {
				throw new Exception();
			}
			CertFiles certFiles = new CertFiles();
			boolean osWin = false;
			String osname = System.getProperty("os.name").toLowerCase();
			if (osname.startsWith("win")){
				osWin = true;
			}
			if (suffix.equals(".mp4")||suffix.equals(".mov")){
				String uuid = UUID.randomUUID().toString();
				if (osWin){
					FileUtil.fetchFrame(filePath,"D:\\upload\\vidopng\\"+uuid+".png");
					certFiles.setFileName("http://192.168.3.101/img/vidopng/"+uuid+".png");
				}else{
					FileUtil.fetchFrame(filePath,"/opt/czt-upload/vidopng/"+uuid+".png");
					certFiles.setFileName("http://114.244.37.10:7080/img/vidopng/"+uuid+".png");
				}
			}else{
				certFiles.setFileName(fileName);
			}
			certFiles.setFileUrl(fileUrl);
			certFiles.setFilePath(filePath);
			certFiles.setFileSize(Double.valueOf(multipartFile[i].getSize()));
			certFiles.setFileType(suffix);
			certFiles.setFileSeq(i+"");
			certFilesService.insertSelective(certFiles);
			certIds[i]=String.valueOf(certFiles.getFileId());
		}
		map.put("fileIds", certIds);
		return map;
	}

	public Result uploadHeadPhoto(HttpServletRequest request, HttpServletResponse response,
			MultipartFile multipartFile) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			String fileName = multipartFile.getOriginalFilename();
			String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
			UUID randomUUID = UUID.randomUUID();
			String fileUrl = filePrefix+File.separator+randomUUID+suffix;
			boolean uploadBoolean = FileUtil.uploadFile(uploadFilePath+File.separator, randomUUID+suffix, multipartFile);
			if (!uploadBoolean) {
				throw new Exception();
			}
			User user = userMapper.findByName(LoginUserHelper.getUserName());
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
	 * @param filePath 文件路径
	 * @param fileName 文件名称
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
		BufferedReader in ;
		String str ;
		StringBuffer sb = new StringBuffer();
		try {
			in = new BufferedReader(new FileReader(new File(filePath)));
			while((str=in.readLine())!=null){
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
	 * @param videofile  源视频文件路径
	 * @param framefile  截取帧的图片存放路径
	 * @throws Exception
	 */
	public static void fetchFrame(String videofile, String framefile)
			throws Exception {
		//long start = System.currentTimeMillis();
		File targetFile = new File(framefile);
		FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
		ff.start();
		int lenght = ff.getLengthInFrames();
		int i = 0;
		Frame f = null;
		while (i < lenght) {
			// 过滤前5帧，避免出现全黑的图片，依自己情况而定
			f = ff.grabFrame();
			if ((i > 3) && (f.image != null)) {
				break;
			}
			i++;
		}
		int owidth = f.imageWidth;
		int oheight = f.imageHeight;
		// 对截取的帧进行等比例缩放
		int width = 800;
		int height = (int) (((double) width / owidth) * oheight);
		Java2DFrameConverter converter = new Java2DFrameConverter();
		BufferedImage fecthedImage = converter.getBufferedImage(f);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width,height, Image.SCALE_SMOOTH),
				0, 0, null);
		ImageIO.write(bi, "jpg", targetFile);
		ff.flush();
		ff.stop();
	}

	/**
	 * 文件转为文件流
	 * @param imagePath 图片路径
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String imageChangeBase64(String imagePath){
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imagePath);
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
		OutputStream os = null;
		try {
			os = response.getOutputStream();
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

	public Map<String, Object> realUpload(HttpServletRequest request, HttpServletResponse response, MultipartFile[] multipartFile)
			throws Exception{
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			Map<String,Object>map = new HashMap<String,Object>();
			String certIds[] = new String[multipartFile.length];
			for (int i = 0; i < multipartFile.length; i++) {
				String fileName = multipartFile[i].getOriginalFilename();
				String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
				UUID randomUUID = UUID.randomUUID();
				String filePath = realFilePath + File.separator + randomUUID+suffix;
				String fileUrl = realFilePrefix+File.separator+randomUUID+suffix;
				boolean uploadBoolean = FileUtil.uploadFile(realFilePath+File.separator, randomUUID+suffix, multipartFile[i]);
				if (!uploadBoolean) {
					throw new Exception();
				}
				CertFiles certFiles = new CertFiles();
				certFiles.setFileName(fileName);
				certFiles.setFileUrl(fileUrl);
				certFiles.setFilePath(filePath);
				certFiles.setFileSize(Double.valueOf(multipartFile[i].getSize()));
				certFiles.setFileType(suffix);
				certFiles.setFileSeq(i+"");
				certFilesService.insertSelective(certFiles);
				certIds[i]=String.valueOf(certFiles.getFileId());
			}
			map.put("fileIds", certIds);
			return map;
		}
}
