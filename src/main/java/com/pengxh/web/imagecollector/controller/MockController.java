package com.pengxh.web.imagecollector.controller;

import cn.hutool.core.util.StrUtil;
import com.pengxh.web.imagecollector.base.BaseController;
import com.pengxh.web.imagecollector.base.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 仅为mock登录使用
 *
 * @author a203
 */
@Slf4j
@Controller
@RequestMapping("/route")
public class MockController extends BaseController {
    /**
     * 获取mockToken
     */
    @GetMapping("/mockToken")
    @ResponseBody
    public Object mockToken(String username, String password) {
        SuccessResponse resultData = new SuccessResponse();
        if (StrUtil.hasEmpty(username, password)) {
            username = "admin";
            password = "111111";
        }
//        Subject currentUser = ShiroKit.getSubject();
//
//        try {
//            Map<String, String> key = RSAUtils.genKeyPair();
//            ShiroKit.getSession().setAttribute(PermissionConstant.PRIVATE_KEY, key.get(RSAUtils.RSAPrivateKey));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//        //token自定义
//        CustomToken token = new CustomToken(username, password);
//        token.setRememberMe(false);
//
//        try {
//            currentUser.login(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        AuthUser shiroUser = ShiroKit.getUser();
//        super.getSession().setAttribute("shiroUser", shiroUser);
//        super.getSession().setAttribute("username", shiroUser.getAccount());
//        super.getSession().setAttribute(PermissionConstant.SESSION_KEY, shiroUser.getId());
//
//        ShiroKit.getSession().setAttribute("sessionFlag", true);
//        resultData.setData(new LoginSuccessDTO(ShiroKit.getSession().getId().toString(), null));
        resultData.setMessage("登录成功");
        return resultData;
    }
}
