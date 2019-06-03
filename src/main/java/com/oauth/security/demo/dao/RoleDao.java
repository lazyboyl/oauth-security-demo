package com.oauth.security.demo.dao;

import com.oauth.security.demo.entity.Role;
import org.springframework.data.repository.query.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleDao extends Mapper<Role> {

    /**
     * 功能描述：根据用户ID来获取角色集
     * @param userId 用户ID
     * @return 返回角色集
     */
    List<Role> getUserRoleByUSerId(@Param("userId")String userId);

}