package com.pengxh.web.imagecollector.base.exception;

import java.util.Optional;

/**
 * @author Administrator
 */
public class ServiceException extends RuntimeException {
    private Optional<StackTraceElement> stackTraceElement = Optional.empty();
    private Integer code;
    private String errorMessage;

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(Integer code, String errorMessage, Optional<StackTraceElement> element) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
        this.stackTraceElement = element;
    }

    public ServiceException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

    public ServiceException(AbstractBaseExceptionEnum exception, Optional<StackTraceElement> element) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
        this.stackTraceElement = element;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 不拷贝栈信息，提高性能
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public Optional<StackTraceElement> getStackTraceElement() {
        return stackTraceElement;
    }

    public void setStackTraceElement(Optional<StackTraceElement> stackTraceElement) {
        this.stackTraceElement = stackTraceElement;
    }
}
