package com.pengxh.web.imagecollector.base.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author a203
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorResponse extends ResponseData {
    private String exceptionClazz;

    public ErrorResponse(String message) {
        super(false, ResponseData.DEFAULT_ERROR_CODE, message, null);
    }

    public ErrorResponse(Integer code, String message) {
        super(false, code, message, null);
    }

    public ErrorResponse(Integer code, String message, Object object) {
        super(false, code, message, object);
    }
}
