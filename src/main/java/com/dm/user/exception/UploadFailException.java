package com.dm.user.exception;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cui
 * @date 2019-09-26
 */
@RestControllerAdvice
public class UploadFailException {

    private static Logger logger = LoggerFactory.getLogger(UploadFailException.class);

	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSizeExceededException(HttpServletRequest req,MaxUploadSizeExceededException e) {
        new ResultUtil();
        logger.error("error URL ===>>"+req.getRequestURL()
                +"<===>error msg ===>>"+e.getMessage());
		return ResultUtil.info("file.upload.max.size.code","file.upload.max.size.msg");
    }

    @ExceptionHandler(Exception.class)
    public Result Exception(HttpServletRequest req, Exception ex) {
        logger.error("error URL ===>>"+req.getRequestURL()
                +"<===>error msg ===>>"+ex.getMessage());
        Result result = ResultUtil.error("系统异常，请稍后再试");
        return result;
    }
	
}
