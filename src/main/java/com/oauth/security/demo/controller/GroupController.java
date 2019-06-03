package com.oauth.security.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linzf
 * @since 2019/6/3
 * 类描述：
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @GetMapping("test")
    public String test(){
        return "test";
    }

}
