package com.example.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rbac.entity.SysRole;
import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    
    /**
     * 根据角色ID查询权限列表
     */
    List<String> getPermissionKeysByRoleId(Long roleId);
    
    /**
     * 查询角色及其权限信息
     */
    SysRole getRoleWithPermissions(Long roleId);
    
    /**
     * 查询角色及其权限和菜单信息
     */
    SysRole getRoleWithDetails(Long roleId);
    
    /**
     * 分配权限给角色
     */
    boolean assignPermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 分配菜单给角色
     */
    boolean assignMenus(Long roleId, List<Long> menuIds);
}
