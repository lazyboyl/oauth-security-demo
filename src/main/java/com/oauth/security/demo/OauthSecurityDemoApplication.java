package com.oauth.security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.oauth.security.demo.dao")
public class OauthSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthSecurityDemoApplication.class, args);
    }

}
