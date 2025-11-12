package com.example.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rbac.entity.SysUser;
import com.example.rbac.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 根据用户ID查询角色列表
     */
    List<String> selectRoleKeysByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询权限列表
     */
    List<String> selectPermissionKeysByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询菜单列表
     */
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户及其角色信息
     */
    SysUser selectUserWithRoles(@Param("userId") Long userId);
}
