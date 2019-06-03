package com.oauth.security.demo.controller;

import com.oauth.security.demo.config.ResourceServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author linzf
 * @since 2019/6/3
 * 类描述：
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("test")
    public String test(){
        return "test";
    }

    /**
     * 功能描述：根据用户的账号和密码实现用户的登录
     *
     * @param username 账号
     * @param password 密码
     * @return 返回调用结果
     */
    @GetMapping("login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        MultiValueMap<String, Object> loginInfo = new LinkedMultiValueMap<>();
        loginInfo.add("grant_type","password");
        loginInfo.add("client_id","demoClient");
        loginInfo.add("client_secret","demoSecret");
        loginInfo.add("username",username);
        loginInfo.add("password",password);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(loginInfo, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:" + serverPort+"/oauth/token", requestEntity, String.class);
        return responseEntity.getBody();
    }

}
