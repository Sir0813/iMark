package com.dm.user.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import com.dm.frame.jboot.locale.I18nUtil;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;

@RestControllerAdvice
public class UploadFailException {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        new ResultUtil();
		return ResultUtil.info(I18nUtil.getMessage("file.upload.max.size.code"),I18nUtil.getMessage("file.upload.max.size.msg"));
    }
	
}
