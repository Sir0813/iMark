package com.dm.user.exception;

import com.dm.frame.jboot.locale.I18nUtil;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class UploadFailException {

    private static Logger logger = LoggerFactory.getLogger(UploadFailException.class);

	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        new ResultUtil();
        logger.error(e.getMessage());
		return ResultUtil.info(I18nUtil.getMessage("file.upload.max.size.code"),I18nUtil.getMessage("file.upload.max.size.msg"));
    }

    @ExceptionHandler(Exception.class)
    public Result Exception(Exception ex) {
        new ResultUtil();
        logger.error(ex.getMessage());
        return ResultUtil.info("100000",ex.getMessage());
    }
	
}
