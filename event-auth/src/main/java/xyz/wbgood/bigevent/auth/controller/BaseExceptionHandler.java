package xyz.wbgood.bigevent.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.wbgood.bigevent.common.utils.Result;
import xyz.wbgood.bigevent.common.utils.ResultCode;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理类
 */
@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handler(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        log.error(sw.toString());
        return Result.error(ResultCode.ERROR, "服务错误，请稍候再试");
    }


}