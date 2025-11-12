package com.example.rbac.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rbac.common.Result;
import com.example.rbac.entity.SysRole;
import com.example.rbac.service.SysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/role")
public class SysRoleController {
    
    private static final Logger logger = LoggerFactory.getLogger(SysRoleController.class);
    
    @Autowired
    private SysRoleService roleService;
    
    @SaCheckPermission("role:view")
    @GetMapping("/list")
    public Result<Page<SysRole>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String roleName) {
        
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (roleName != null && !roleName.isEmpty()) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        Page<SysRole> result = roleService.page(page, wrapper);
        
        // 为每个角色填充权限和菜单信息
        result.getRecords().forEach(role -> {
            logger.info("填充角色ID={}的信息", role.getId());
            SysRole roleWithDetails = roleService.getRoleWithDetails(role.getId());
            role.setPermissions(roleWithDetails.getPermissions());
            role.setMenus(roleWithDetails.getMenus());
        });
        
        return Result.success(result);
    }
    
    @GetMapping("/all")
    public Result<List<SysRole>> all() {
        List<SysRole> roles = roleService.list();
        return Result.success(roles);
    }
    
    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        SysRole role = roleService.getById(id);
        return Result.success(role);
    }
    
    @SaCheckPermission("role:add")
    @PostMapping
    public Result<?> add(@RequestBody SysRole role) {
        role.setStatus(1);
        roleService.save(role);
        return Result.success();
    }
    
    @SaCheckPermission("role:edit")
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        roleService.updateById(role);
        return Result.success();
    }
    
    @SaCheckPermission("role:delete")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        roleService.removeById(id);
        return Result.success();
    }
    
    @SaCheckPermission("role:edit")
    @PostMapping("/{roleId}/permissions")
    public Result<?> assignPermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(roleId, permissionIds);
        return Result.success();
    }
    
    @PostMapping("/{roleId}/menus")
    public Result<?> assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        logger.info("分配菜单 - 角色ID: {}, 菜单IDs: {}", roleId, menuIds);
        roleService.assignMenus(roleId, menuIds);
        logger.info("分配菜单完成");
        return Result.success();
    }
}
