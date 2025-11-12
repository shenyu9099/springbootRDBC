package com.example.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rbac.entity.SysUser;
import com.example.rbac.entity.SysUserRole;
import com.example.rbac.entity.SysMenu;
import com.example.rbac.mapper.SysUserMapper;
import com.example.rbac.service.SysUserRoleService;
import com.example.rbac.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    
    @Autowired
    private SysUserRoleService userRoleService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return this.getOne(wrapper);
    }
    
    @Override
    public List<String> getRoleKeysByUserId(Long userId) {
        return baseMapper.selectRoleKeysByUserId(userId);
    }
    
    @Override
    public List<String> getPermissionKeysByUserId(Long userId) {
        return baseMapper.selectPermissionKeysByUserId(userId);
    }
    
    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        return baseMapper.selectMenusByUserId(userId);
    }
    
    @Override
    public SysUser getUserWithRoles(Long userId) {
        return baseMapper.selectUserWithRoles(userId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 删除原有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        userRoleService.remove(wrapper);
        
        // 分配新角色
        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> userRoles = roleIds.stream()
                    .map(roleId -> {
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);
        }
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUser user, List<Long> roleIds) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        
        // 分配角色
        if (roleIds != null && !roleIds.isEmpty()) {
            assignRoles(user.getId(), roleIds);
        }
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user, List<Long> roleIds) {
        // 如果密码不为空，则加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        this.updateById(user);
        
        // 分配角色
        if (roleIds != null) {
            assignRoles(user.getId(), roleIds);
        }
        return true;
    }
}
