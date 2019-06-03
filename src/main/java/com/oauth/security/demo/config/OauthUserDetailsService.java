package com.oauth.security.demo.config;

import com.oauth.security.demo.dao.RoleDao;
import com.oauth.security.demo.dao.UserDao;
import com.oauth.security.demo.entity.Role;
import com.oauth.security.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linzf
 * @since 2019/5/31
 * 类描述：以数据库的层面来实现用户的登录
 */
public class OauthUserDetailsService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.login(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        List<Role> userRoles = roleDao.getUserRoleByUSerId(user.getUserId());
        for (Role role : userRoles) {
            //这个权限牵涉到底层的投票机制，默认是一票制AffirmativeBased：如果有任何一个投票器运行访问，请求将被立刻允许，而不管之前可能有的拒绝决定
            // RoleVoter投票器识别以"ROLE_"为前缀的role,这里配置已ROLE_前缀开头的role
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
        }
        return new org.springframework.security.core.userdetails.User(username, user.getLoginPassword(), authorities);
    }
}
