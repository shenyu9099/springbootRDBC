package com.example.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rbac.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    
    /**
     * 根据角色ID查询权限列表
     */
    List<String> selectPermissionKeysByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查询角色及其权限信息
     */
    SysRole selectRoleWithPermissions(@Param("roleId") Long roleId);
}
