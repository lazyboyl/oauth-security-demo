package com.oauth.security.demo.config;

import com.oauth.security.demo.config.exception.AuthExceptionEntryPoint;
import com.oauth.security.demo.config.exception.CustomAccessDeniedHandler;
import com.oauth.security.demo.dao.RoleDao;
import com.oauth.security.demo.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import java.util.List;

/**
 * @author linzf
 * @since 2019/6/3
 * 类描述：配置节点访问的权限
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private RoleDao roleDao;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.accessDeniedHandler(new CustomAccessDeniedHandler());
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<Role> roleList = roleDao.getUserRoleByUSerId("1");
        System.out.println("-------------初始化权限--------------");
        http.authorizeRequests().
                /**
                 * 角色为system-admin的权限的用户可以访问/user/**的节点
                 * 角色为system-admin-group的权限的用户可以访问/group/**的节点
                 */
                antMatchers("/user/**").hasRole("system-admin").
                antMatchers("/group/**").hasRole("system-admin-group").
                anyRequest().authenticated();
    }
}
