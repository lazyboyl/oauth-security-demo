package com.oauth.security.demo.dao;

import com.oauth.security.demo.entity.User;
import org.springframework.data.repository.query.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author linzf
 * @since 2019-05-31
 * 类描述：用户的dao层
 */
public interface UserDao extends Mapper<User> {

    /**
     * 功能描述：根据用户账号来获取用户数据
     * @param loginAccount 用户账号
     * @return 返回登录结果
     */
    User login(@Param("loginAccount") String loginAccount);

}