package com.pengxh.web.imagecollector.base.request;

import cn.hutool.core.util.StrUtil;
import com.pengxh.web.imagecollector.base.exception.ServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
public class RequestValidException extends ServiceException {
    public RequestValidException() {
        super(2400, "请求数据不完整或格式错误！");
    }


    public RequestValidException(String errorMessage) {
        super(2400, errorMessage);
    }

    public RequestValidException(String errorMessage, Optional<StackTraceElement> element) {
        super(2400, errorMessage, element);
    }

    public RequestValidException(BindingResult result) {
        super(2400, StrUtil.join(",", result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList())));
    }

    public RequestValidException(BindingResult result, Optional<StackTraceElement> element) {
        super(2400, StrUtil.join(",", result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList())), element);
    }

    /**
     * 不拷贝栈信息，提高性能
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
