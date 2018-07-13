package com.mdiai.seckill.exception;

import com.mdiai.seckill.common.codemsg.CodeMsg;
import com.mdiai.seckill.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  18:41
 * @Description
 */
@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobleExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception ex) {
        logger.error("拦截器发现异常:",ex);
        if (ex instanceof GlodleException){
            GlodleException gx = (GlodleException)ex;
            return Result.eorror(gx.getCodeMsg());
        } else if (ex instanceof BindException) {
            BindException bx = (BindException) ex;
            List<ObjectError> allErrors = bx.getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String msg = objectError.getDefaultMessage();
            return Result.eorror(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else {
            return Result.eorror(CodeMsg.SERVICE_ERROR);
        }
    }

}
