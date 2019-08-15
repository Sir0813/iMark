package com.dm.user.controller;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.service.CertFicateService;
import com.dm.user.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class FileController {

	@Autowired
	private FileUtil fileUtil;

	@Autowired
	private CertFicateService certFicateService;

	/**
	 * 文件上传
	 * @param request
	 * @param response
	 * @param multipartFile	文件信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public Result upload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file") MultipartFile[] multipartFile) throws Exception {
		Map<String, Object> map = fileUtil.uploadFile(request, response, multipartFile);
		if (map.containsKey("fileIds")) {
			return ResultUtil.success(map);
		}
		return ResultUtil.error();
	}

	/**
	 * 获取存证证书
	 * @param certId 存证ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getCertImg", method = RequestMethod.GET, headers = "Accept=image/jpeg")
	public ByteArrayResource getCertImg(Integer certId) throws Exception {
		ByteArrayResource bar = certFicateService.getCertImg(certId);
		return bar;
	}

}
