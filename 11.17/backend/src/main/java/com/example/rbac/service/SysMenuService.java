package com.example.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rbac.entity.SysMenu;
import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    
    /**
     * 根据用户ID查询菜单树
     */
    List<SysMenu> getMenuTreeByUserId(Long userId);
    
    /**
     * 根据角色ID查询菜单列表
     */
    List<SysMenu> getMenusByRoleId(Long roleId);
    
    /**
     * 构建菜单树
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menuList);
}
