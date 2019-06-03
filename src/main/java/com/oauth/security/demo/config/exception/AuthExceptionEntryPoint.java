package com.oauth.security.demo.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
* @author linjie
* 自定义AuthExceptionEntryPoint用于token校验失败返回信息
*/
@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {
   private static final Logger logger = LoggerFactory.getLogger(AuthExceptionEntryPoint.class);


   @Autowired
   private ObjectMapper objectMapper;
   @Override
   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
       logger.debug(authException.getMessage());
       Map map = new HashMap();
       map.put("code", "400");
       map.put("msg", "无效的token");
       map.put("obj", "");
       //map.put("timestamp", String.valueOf(new Date().getTime()));
       response.setContentType("application/json");
       response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
       try {
           ObjectMapper mapper = new ObjectMapper();
           mapper.writeValue(response.getOutputStream(), map);
       } catch (Exception e) {
           throw new ServletException();
       }

   }
}
