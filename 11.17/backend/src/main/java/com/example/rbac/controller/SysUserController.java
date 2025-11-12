package com.example.rbac.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rbac.common.Result;
import com.example.rbac.entity.SysUser;
import com.example.rbac.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class SysUserController {
    
    @Autowired
    private SysUserService userService;
    
    @SaCheckPermission("user:view")
    @GetMapping("/list")
    public Result<Page<SysUser>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username) {
        
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like(SysUser::getUsername, username);
        }
        Page<SysUser> result = userService.page(page, wrapper);
        
        // 清除密码
        result.getRecords().forEach(user -> user.setPassword(null));
        
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        SysUser user = userService.getUserWithRoles(id);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }
    
    @SaCheckPermission("user:add")
    @PostMapping
    public Result<?> add(@RequestBody Map<String, Object> params) {
        SysUser user = new SysUser();
        user.setUsername((String) params.get("username"));
        user.setPassword((String) params.get("password"));
        user.setNickname((String) params.get("nickname"));
        user.setEmail((String) params.get("email"));
        user.setPhone((String) params.get("phone"));
        user.setStatus(1);
        
        // 处理 roleIds 类型转换 (Integer -> Long)
        List<Long> roleIds = null;
        Object roleIdsObj = params.get("roleIds");
        if (roleIdsObj instanceof List) {
            roleIds = ((List<?>) roleIdsObj).stream()
                .map(roleId -> {
                    if (roleId instanceof Integer) {
                        return ((Integer) roleId).longValue();
                    } else if (roleId instanceof Long) {
                        return (Long) roleId;
                    } else {
                        return Long.parseLong(roleId.toString());
                    }
                })
                .collect(java.util.stream.Collectors.toList());
        }
        
        userService.addUser(user, roleIds);
        
        return Result.success();
    }
    
    @SaCheckPermission("user:edit")
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setNickname((String) params.get("nickname"));
        user.setEmail((String) params.get("email"));
        user.setPhone((String) params.get("phone"));
        user.setStatus((Integer) params.get("status"));
        
        String password = (String) params.get("password");
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        
        // 处理 roleIds 类型转换 (Integer -> Long)
        List<Long> roleIds = null;
        Object roleIdsObj = params.get("roleIds");
        if (roleIdsObj instanceof List) {
            roleIds = ((List<?>) roleIdsObj).stream()
                .map(roleId -> {
                    if (roleId instanceof Integer) {
                        return ((Integer) roleId).longValue();
                    } else if (roleId instanceof Long) {
                        return (Long) roleId;
                    } else {
                        return Long.parseLong(roleId.toString());
                    }
                })
                .collect(java.util.stream.Collectors.toList());
        }
        
        userService.updateUser(user, roleIds);
        
        return Result.success();
    }
    
    @SaCheckPermission("user:delete")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }
    
    @PostMapping("/{userId}/roles")
    public Result<?> assignRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        userService.assignRoles(userId, roleIds);
        return Result.success();
    }
}
