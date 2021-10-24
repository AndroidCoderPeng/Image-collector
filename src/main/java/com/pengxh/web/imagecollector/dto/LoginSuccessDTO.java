package com.pengxh.web.imagecollector.dto;

import lombok.Data;

/**
 * @author a203
 */
@Data
public class LoginSuccessDTO {
    private String token;
    private String kaptcha;

    public LoginSuccessDTO(String token, String kaptcha) {
        this.token = token;
        this.kaptcha = kaptcha;
    }
}
