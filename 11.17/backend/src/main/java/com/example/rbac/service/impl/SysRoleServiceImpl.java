package com.example.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rbac.entity.SysRole;
import com.example.rbac.entity.SysRolePermission;
import com.example.rbac.entity.SysRoleMenu;
import com.example.rbac.entity.SysPermission;
import com.example.rbac.entity.SysMenu;
import com.example.rbac.mapper.SysRoleMapper;
import com.example.rbac.service.SysRolePermissionService;
import com.example.rbac.service.SysRoleMenuService;
import com.example.rbac.service.SysRoleService;
import com.example.rbac.service.SysPermissionService;
import com.example.rbac.service.SysMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    
    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);
    
    @Autowired
    private SysRolePermissionService rolePermissionService;
    
    @Autowired
    private SysRoleMenuService roleMenuService;
    
    @Autowired
    private SysPermissionService permissionService;
    
    @Autowired
    private SysMenuService menuService;
    
    @Override
    public List<String> getPermissionKeysByRoleId(Long roleId) {
        return baseMapper.selectPermissionKeysByRoleId(roleId);
    }
    
    @Override
    public SysRole getRoleWithPermissions(Long roleId) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            return null;
        }
        
        // 查询角色权限关联
        LambdaQueryWrapper<SysRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRolePermission::getRoleId, roleId);
        List<SysRolePermission> rolePermissions = rolePermissionService.list(wrapper);
        
        // 获取权限ID列表
        List<Long> permissionIds = rolePermissions.stream()
                .map(SysRolePermission::getPermissionId)
                .collect(Collectors.toList());
        
        // 查询权限详情
        if (!permissionIds.isEmpty()) {
            List<SysPermission> permissions = permissionService.listByIds(permissionIds);
            role.setPermissions(permissions);
        }
        
        return role;
    }
    
    @Override
    public SysRole getRoleWithDetails(Long roleId) {
        logger.info("开始查询角色ID={}的详细信息", roleId);
        SysRole role = this.getById(roleId);
        if (role == null) {
            logger.warn("角色ID={}不存在", roleId);
            return null;
        }
        
        try {
            // 查询角色权限关联
            LambdaQueryWrapper<SysRolePermission> permissionWrapper = new LambdaQueryWrapper<>();
            permissionWrapper.eq(SysRolePermission::getRoleId, roleId);
            List<SysRolePermission> rolePermissions = rolePermissionService.list(permissionWrapper);
            
            // 获取权限ID列表
            List<Long> permissionIds = rolePermissions.stream()
                    .map(SysRolePermission::getPermissionId)
                    .collect(Collectors.toList());
            
            // 查询权限详情
            if (!permissionIds.isEmpty()) {
                List<SysPermission> permissions = permissionService.listByIds(permissionIds);
                role.setPermissions(permissions);
            }
            
            // 查询角色菜单关联
            LambdaQueryWrapper<SysRoleMenu> menuWrapper = new LambdaQueryWrapper<>();
            menuWrapper.eq(SysRoleMenu::getRoleId, roleId);
            List<SysRoleMenu> roleMenus = roleMenuService.list(menuWrapper);
            logger.info("查询角色ID={}的菜单关联，共{}  条", roleId, roleMenus.size());
            
            // 获取菜单ID列表
            List<Long> menuIds = roleMenus.stream()
                    .map(SysRoleMenu::getMenuId)
                    .collect(Collectors.toList());
            logger.info("菜单IDs: {}", menuIds);
            
            // 查询菜单详情
            if (!menuIds.isEmpty()) {
                List<SysMenu> menus = menuService.listByIds(menuIds);
                role.setMenus(menus);
            }
        } catch (Exception e) {
            logger.error("查询角色ID={}的详细信息时发生异常", roleId, e);
        }
        
        logger.info("完成查询角色ID={}的详细信息", roleId);
        return role;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        // 删除原有权限
        LambdaQueryWrapper<SysRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRolePermission::getRoleId, roleId);
        rolePermissionService.remove(wrapper);
        
        // 分配新权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<SysRolePermission> rolePermissions = permissionIds.stream()
                    .map(permissionId -> {
                        SysRolePermission rp = new SysRolePermission();
                        rp.setRoleId(roleId);
                        rp.setPermissionId(permissionId);
                        return rp;
                    })
                    .collect(Collectors.toList());
            rolePermissionService.saveBatch(rolePermissions);
        }
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        logger.info("开始分配菜单 - 角色ID: {}, 菜单IDs: {}", roleId, menuIds);
        
        // 删除原有菜单
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        boolean removed = roleMenuService.remove(wrapper);
        logger.info("删除原有菜单结果: {}", removed);
        
        // 分配新菜单
        if (menuIds != null && !menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> {
                        SysRoleMenu rm = new SysRoleMenu();
                        rm.setRoleId(roleId);
                        rm.setMenuId(menuId);
                        return rm;
                    })
                    .collect(Collectors.toList());
            logger.info("准备保存菜单关联: {}", roleMenus.size());
            boolean saved = roleMenuService.saveBatch(roleMenus);
            logger.info("保存菜单关联结果: {}", saved);
        }
        return true;
    }
}
