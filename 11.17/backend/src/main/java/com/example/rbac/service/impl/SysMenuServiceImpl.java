package com.example.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rbac.entity.SysMenu;
import com.example.rbac.mapper.SysMenuMapper;
import com.example.rbac.service.SysMenuService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    
    @Override
    public List<SysMenu> getMenuTreeByUserId(Long userId) {
        List<SysMenu> menuList = baseMapper.selectMenusByUserId(userId);
        return buildMenuTree(menuList);
    }
    
    @Override
    public List<SysMenu> getMenusByRoleId(Long roleId) {
        return baseMapper.selectMenusByRoleId(roleId);
    }
    
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menuList) {
        List<SysMenu> tree = new ArrayList<>();
        
        for (SysMenu menu : menuList) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                tree.add(menu);
            }
        }
        
        for (SysMenu parent : tree) {
            parent.setChildren(getChildren(parent, menuList));
        }
        
        return tree;
    }
    
    private List<SysMenu> getChildren(SysMenu parent, List<SysMenu> allMenus) {
        List<SysMenu> children = allMenus.stream()
                .filter(menu -> parent.getId().equals(menu.getParentId()))
                .collect(Collectors.toList());
        
        for (SysMenu child : children) {
            child.setChildren(getChildren(child, allMenus));
        }
        
        return children.isEmpty() ? null : children;
    }
}
