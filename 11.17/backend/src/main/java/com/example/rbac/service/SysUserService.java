package com.example.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rbac.entity.SysUser;
import com.example.rbac.entity.SysMenu;
import java.util.List;

public interface SysUserService extends IService<SysUser> {
    
    /**
     * 根据用户名查询用户
     */
    SysUser getUserByUsername(String username);
    
    /**
     * 根据用户ID查询角色列表
     */
    List<String> getRoleKeysByUserId(Long userId);
    
    /**
     * 根据用户ID查询权限列表
     */
    List<String> getPermissionKeysByUserId(Long userId);
    
    /**
     * 根据用户ID查询菜单列表
     */
    List<SysMenu> getMenusByUserId(Long userId);
    
    /**
     * 查询用户及其角色信息
     */
    SysUser getUserWithRoles(Long userId);
    
    /**
     * 分配角色给用户
     */
    boolean assignRoles(Long userId, List<Long> roleIds);
    
    /**
     * 添加用户
     */
    boolean addUser(SysUser user, List<Long> roleIds);
    
    /**
     * 更新用户
     */
    boolean updateUser(SysUser user, List<Long> roleIds);
}
