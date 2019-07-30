package com.dm.user.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.util.FileUtil;

@Controller
@RequestMapping("/api")
public class FileController {

	@Autowired
	private FileUtil fileUtil;

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

}
