package com.oauth.security.demo.config;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * @author linzf
 * @since 2019/5/31
 * 类描述：用于捕获oauth登录的时候账号或者密码错误的时候的异常
 */
@Component("bootWebResponseExceptionTranslator")
public class BootOAuth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print("{\"msg\":\"账号密码错误\"}");
        System.out.println("------"+e.getMessage());
        return null;
    }
}
