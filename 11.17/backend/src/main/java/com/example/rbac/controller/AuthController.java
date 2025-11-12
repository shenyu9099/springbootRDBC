package com.example.rbac.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.rbac.common.Result;
import com.example.rbac.dto.LoginRequest;
import com.example.rbac.dto.LoginResponse;
import com.example.rbac.entity.SysMenu;
import com.example.rbac.entity.SysUser;
import com.example.rbac.service.SysMenuService;
import com.example.rbac.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private SysUserService userService;
    
    @Autowired
    private SysMenuService menuService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // 查询用户
        SysUser user = userService.getUserByUsername(request.getUsername());
        if (user == null) {
            return Result.fail("用户名或密码错误");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return Result.fail("用户名或密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            return Result.fail("用户已被禁用");
        }
        
        // 使用 Sa-Token 登录
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        // 查询角色和权限
        List<String> roles = userService.getRoleKeysByUserId(user.getId());
        List<String> permissions = userService.getPermissionKeysByUserId(user.getId());
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setRoles(roles);
        response.setPermissions(permissions);
        
        return Result.success(response);
    }
    
    @GetMapping("/menu")
    public Result<List<SysMenu>> getMenuTree() {
        // 从 Sa-Token 获取当前登录用户ID
        long userId = StpUtil.getLoginIdAsLong();
        List<SysMenu> menuTree = menuService.getMenuTreeByUserId(userId);
        return Result.success(menuTree);
    }
    
    @PostMapping("/logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.success();
    }
    
    @GetMapping("/info")
    public Result<LoginResponse> getUserInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userService.getById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        
        // 查询角色和权限
        List<String> roles = userService.getRoleKeysByUserId(userId);
        List<String> permissions = userService.getPermissionKeysByUserId(userId);
        
        LoginResponse response = new LoginResponse();
        response.setToken(StpUtil.getTokenValue());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setRoles(roles);
        response.setPermissions(permissions);
        
        return Result.success(response);
    }
    
    // 临时接口: 重置管理员密码
    @GetMapping("/reset-admin-password")
    public Result<?> resetAdminPassword() {
        SysUser user = userService.getUserByUsername("admin");
        if (user != null) {
            String newPassword = passwordEncoder.encode("admin123");
            user.setPassword(newPassword);
            userService.updateById(user);
            return Result.success("密码已重置为: admin123, 新哈希: " + newPassword);
        }
        return Result.fail("用户不存在");
    }
}
