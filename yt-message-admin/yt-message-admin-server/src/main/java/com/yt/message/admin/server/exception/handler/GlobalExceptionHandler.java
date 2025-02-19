package com.yt.message.admin.server.exception.handler;

import com.yt.message.admin.server.exception.BusinessException;
import com.yt.message.admin.server.exception.IllegalRequestException;
import com.yt.message.admin.server.pojo.dto.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public R handleBusinessException(BusinessException e, HttpServletRequest request) {
        return R.fail(e.getMessage());
    }

    /**
     * 非法请求
     */
    @ExceptionHandler(IllegalRequestException.class)
    public R handleIllegalRequestException(IllegalRequestException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.warn("请求地址'{}',非法请求异常", requestURI, e);
        return R.fail(e.getMessage());
    }
    /**
     * 验证异常
     */
    @ExceptionHandler(BindException.class)
    public R handleBindException(BindException e) {
//        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(message);
    }


    @ExceptionHandler(RuntimeException.class)
    public R handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',运行时异常", requestURI, e);
        return R.fail("系统错误，请联系管理员");
    }


    @ExceptionHandler(Exception.class)
    public R handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',系统异常", requestURI, e);
        return R.fail("系统错误，请联系管理员");
    }


}
