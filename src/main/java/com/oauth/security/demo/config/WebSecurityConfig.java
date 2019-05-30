package com.oauth.security.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author linzf
 * @since 2019/5/30
 * 类描述：
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Bean
    public TokenStore tokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }

    //配置用户存储在内存中，可以设置用户账号、密码、角色等
    @Override
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password(new BCryptPasswordEncoder().encode("123456"))
                .roles("user").build());
        manager.createUser(User.withUsername("admin").password(new BCryptPasswordEncoder().encode("123456"))
                .roles("admin").build());
        return manager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //将用户信息添加认证管理中，这里有两种方式
        //方法一：
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        //方法二：
        //auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder().encode("123456")).roles("user");
    }

    /**
     * 进行HTTP配置拦截，过滤url，这里主要配置服务端的请求拦截
     * permitAll表示该请求任何人都可以访问，authenticated()表示其他的请求都必须要有权限认证
     * 本例子中，以下配置其实用不到，也可以省略，因为本文使用了4中ResourceServerConfig，
     * 导致该例子会用ResourceServerConfig中的拦截配置
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .and().logout().logoutUrl("/logout").deleteCookies("JSESSIONID").permitAll()
                .and().authorizeRequests()
                .antMatchers("/login.html").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }

    /**
     * spring5.x 需要注入一个PasswordEncoder，否则会报这个错There is no PasswordEncoder mapped for the id \"null\"
     * 加了密码加密，所有用到的密码都需要这个加密方法加密
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 需要配置这个支持password模式
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }

    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }

}
