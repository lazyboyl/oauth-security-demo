package com.oauth.security.demo;

import com.oauth.security.demo.config.OauthUserDetailsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.oauth.security.demo.dao")
public class OauthSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthSecurityDemoApplication.class, args);
    }

    /**
     * 功能描述：初始化OauthUserDetailsService的bean
     * @return 返回初始化的bean
     */
    @Bean
    public OauthUserDetailsService oauthUserDetailsService(){
        return new OauthUserDetailsService();
    }

}
