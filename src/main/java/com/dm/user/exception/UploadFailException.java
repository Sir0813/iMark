package com.dm.user.exception;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fieldError =  fieldErrors.get(i);
            builder.append(fieldError.getDefaultMessage());
        }
        /* + "\n"*/
        logger.error("error URL ===>>"+req.getRequestURL() + "<===>error msg ===>>"+builder.toString());
        Result result = new Result();
        result.setCode("110801");
        result.setMsg(builder.toString());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result exception(HttpServletRequest req, Exception ex) {
        logger.error("error URL ===>>"+req.getRequestURL()
                +"<===>error msg ===>>"+ex.getMessage());
        Result result = ResultUtil.error("系统异常，请稍后再试");
        return result;
    }
	
}
