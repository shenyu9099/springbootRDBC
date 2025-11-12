package com.example.rbac.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.rbac.common.Result;
import com.example.rbac.entity.SysMenu;
import com.example.rbac.service.SysMenuService;
import com.example.rbac.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class SysMenuController {
    
    @Autowired
    private SysMenuService menuService;
    
    @Autowired
    private SysUserService userService;
    
    @GetMapping("/tree")
    public Result<List<SysMenu>> tree() {
        // 获取当前登录用户ID
        long userId = StpUtil.getLoginIdAsLong();
        
        // 查询用户角色
        List<String> roles = userService.getRoleKeysByUserId(userId);
        
        List<SysMenu> menuList;
        // 如果是超级管理员,返回所有菜单
        if (roles.contains("admin")) {
            menuList = menuService.list();
        } else {
            // 查询用户的菜单
            menuList = userService.getMenusByUserId(userId);
        }
        
        // 构建菜单树
        List<SysMenu> menuTree = menuService.buildMenuTree(menuList);
        return Result.success(menuTree);
    }
    
    @GetMapping("/all")
    public Result<List<SysMenu>> all() {
        List<SysMenu> menus = menuService.list();
        return Result.success(menus);
    }
    
    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        SysMenu menu = menuService.getById(id);
        return Result.success(menu);
    }
    
    @PostMapping
    public Result<?> add(@RequestBody SysMenu menu) {
        menu.setStatus(1);
        menuService.save(menu);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        menuService.updateById(menu);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.success();
    }
}
