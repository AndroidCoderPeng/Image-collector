package com.pengxh.web.imagecollector.base.response;

/**
 * @author a203
 */
public class SuccessResponse extends ResponseData {
    public SuccessResponse() {
        super(true, DEFAULT_SUCCESS_CODE, "请求成功", (Object) null);
    }

    public SuccessResponse(Object object) {
        super(true, DEFAULT_SUCCESS_CODE, "请求成功", object);
    }

    public SuccessResponse(Integer code, String message, Object object) {
        super(true, code, message, object);
    }
}
