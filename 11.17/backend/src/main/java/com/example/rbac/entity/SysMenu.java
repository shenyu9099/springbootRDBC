package com.example.rbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long parentId;
    
    private String menuName;
    
    private String path;
    
    private String component;
    
    private String icon;
    
    private Integer sort;
    
    private Integer type; // 0:目录 1:菜单 2:按钮
    
    private String permission;
    
    private Integer status; // 0:禁用 1:启用
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(exist = false)
    private List<SysMenu> children;
    
    /**
     * 转换为前端路由格式
     */
    public Map<String, Object> toRoute() {
        Map<String, Object> route = new HashMap<>();
        route.put("path", this.path);
        route.put("component", this.component);
        route.put("name", this.menuName);
        
        // 构建 meta 对象
        Map<String, Object> meta = new HashMap<>();
        meta.put("title", this.menuName);
        meta.put("icon", this.icon);
        route.put("meta", meta);
        
        // 添加子菜单
        if (this.children != null && !this.children.isEmpty()) {
            route.put("children", this.children.stream().map(SysMenu::toRoute).toArray());
        }
        
        return route;
    }
}
