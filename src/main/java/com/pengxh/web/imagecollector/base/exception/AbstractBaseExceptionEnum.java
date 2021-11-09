package com.pengxh.web.imagecollector.base.exception;

/**
 * @author Administrator
 */
public interface AbstractBaseExceptionEnum {
    /**
     * 获取异常的状态码
     *
     * @return 异常状态码
     */
    Integer getCode();


    /**
     * 获取异常的提示信息
     *
     * @return 异常提示消息
     */
    String getMessage();
}
