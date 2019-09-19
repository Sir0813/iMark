package com.dm.user.controller;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.service.CertFicateService;
import com.dm.user.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping
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
	@RequestMapping("/api/upload")
	public Result upload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file") MultipartFile[] multipartFile) throws Exception {
		Map<String, Object> map = fileUtil.uploadFile(request, response, multipartFile);
		if (map.containsKey("fileIds")) {
			return ResultUtil.success(map);
		}
		return ResultUtil.error();
	}

	/**
	 * 实名认证上传图片
	 * @param request
	 * @param response
	 * @param multipartFile
	 * @param type real 实名认证上传 outcert 出证上传
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/real/upload")
	public Result realUpload(HttpServletRequest request, HttpServletResponse response,
						 @RequestParam(value = "file") MultipartFile[] multipartFile, String type) throws Exception {
		Map<String, Object> map = fileUtil.realUpload(request, response, multipartFile, type);
		if (map.containsKey("fileIds")) {
			return ResultUtil.success(map);
		}
		return ResultUtil.error();
	}

}
