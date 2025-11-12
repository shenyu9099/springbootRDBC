package com.example.rbac.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rbac.common.Result;
import com.example.rbac.entity.SysPermission;
import com.example.rbac.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class SysPermissionController {
    
    @Autowired
    private SysPermissionService permissionService;
    
    @GetMapping("/list")
    public Result<Page<SysPermission>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<SysPermission> page = new Page<>(pageNum, pageSize);
        Page<SysPermission> result = permissionService.page(page);
        
        return Result.success(result);
    }
    
    @GetMapping("/all")
    public Result<List<SysPermission>> all() {
        List<SysPermission> permissions = permissionService.list();
        return Result.success(permissions);
    }
    
    @GetMapping("/tree")
    public Result<List<SysPermission>> tree() {
        // 返回平铺列表，前端会自动构建树形结构
        List<SysPermission> permissions = permissionService.list();
        return Result.success(permissions);
    }
    
    @GetMapping("/{id}")
    public Result<SysPermission> getById(@PathVariable Long id) {
        SysPermission permission = permissionService.getById(id);
        return Result.success(permission);
    }
    
    @PostMapping
    public Result<?> add(@RequestBody SysPermission permission) {
        permissionService.save(permission);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody SysPermission permission) {
        permission.setId(id);
        permissionService.updateById(permission);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        permissionService.removeById(id);
        return Result.success();
    }
}
