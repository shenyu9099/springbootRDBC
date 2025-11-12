package com.example.rbac.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.example.rbac.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer, StpInterface {
    
    @Autowired
    private SysUserService userService;
    
    /**
     * 注册 Sa-Token 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 检查登录状态
            StpUtil.checkLogin();
        }))
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login", "/auth/reset-admin-password"); // 排除登录接口
    }
    
    /**
     * 返回指定账号id所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        List<String> permissions = userService.getPermissionKeysByUserId(userId);
        
        // 如果是超级管理员,返回通配符权限
        List<String> roles = userService.getRoleKeysByUserId(userId);
        if (roles.contains("admin")) {
            permissions.add("*"); // 超级管理员拥有所有权限
        }
        
        return permissions;
    }
    
    /**
     * 返回指定账号id所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        return userService.getRoleKeysByUserId(userId);
    }
}
