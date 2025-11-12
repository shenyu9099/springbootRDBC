RBAC权限管理系统
项目简介
本项目是一个基于Spring Boot和Vue 3的RBAC（基于角色的访问控制）权限管理系统。系统提供了完整的用户、角色、权限和菜单管理功能，支持细粒度的权限控制。
技术栈
后端技术栈
Spring Boot 3.2.0
MyBatis Plus 3.5.6
Sa-Token 1.38.0（认证和权限框架）
MySQL 8.0+
Redis（可选，用于缓存）
Maven 3.8+
前端技术栈
Vue 3.3.4（Composition API）
Element Plus 2.4.0（UI组件库）
Pinia 2.1.6（状态管理）
Vue Router 4.2.0（路由管理）
Axios（HTTP客户端）
Vite 4.4.9（构建工具）
功能特性
用户管理
用户列表查看
用户新增、编辑、删除
用户状态管理（启用/禁用）
用户角色分配
角色管理
角色列表查看
角色新增、编辑、删除
角色状态管理
角色权限分配
角色菜单分配
权限管理
权限列表查看
权限新增、编辑、删除
权限树形结构展示
菜单管理
菜单树形结构展示
菜单新增、编辑、删除
菜单类型管理（目录、菜单、按钮）
菜单图标显示
菜单排序
权限控制
基于角色的访问控制（RBAC）
页面级权限控制（菜单显示）
操作级权限控制（按钮显示/隐藏）
接口级权限控制（后端接口访问控制）
数据库设计
核心表结构
用户表（sys_user）
用户ID
用户名
密码（BCrypt加密）
昵称
邮箱
手机号
状态（启用/禁用）
创建时间/更新时间
角色表（sys_role）
角色ID
角色标识
角色名称
描述
状态
创建时间/更新时间
权限表（sys_permission）
权限ID
权限标识
权限名称
描述
创建时间/更新时间
菜单表（sys_menu）
菜单ID
父菜单ID
菜单名称
路由路径
组件路径
图标
排序
类型（目录/菜单/按钮）
权限标识
状态
创建时间/更新时间
用户角色关联表（sys_user_role）
关联ID
用户ID
角色ID
角色权限关联表（sys_role_permission）
关联ID
角色ID
权限ID
角色菜单关联表（sys_role_menu）
关联ID
角色ID
菜单ID
权限设计
权限标识规范
系统采用简洁的权限标识命名规范：
用户管理：user:view, user:add, user:edit, user:delete
角色管理：role:view, role:add, role:edit, role:delete
权限管理：permission:view, permission:add, permission:edit, permission:delete
菜单管理：menu:view, menu:add, menu:edit, menu:delete
超级管理员
系统默认创建超级管理员角色（admin），该角色拥有所有权限，权限标识为通配符""。
项目结构
后端项目结构
plaintext
backend/
├── src/main/java/com/example/rbac/
│   ├── controller/         # 控制器层
│   ├── entity/             # 实体类
│   ├── mapper/             # 数据访问层
│   ├── service/            # 业务逻辑层
│   │   └── impl/           # 业务逻辑实现
│   ├── config/             # 配置类
│   └── common/             # 公共类
├── src/main/resources/
│   ├── mapper/             # MyBatis XML映射文件
│   └── application.yml     # 配置文件
└── sql/
    └── init.sql            # 数据库初始化脚本
前端项目结构
plaintext
frontend/
├── src/
│   ├── api/                # API接口封装
│   ├── assets/             # 静态资源
│   ├── components/         # 公共组件
│   ├── layout/             # 布局组件
│   ├── router/             # 路由配置
│   ├── store/              # 状态管理
│   ├── utils/              # 工具函数
│   ├── views/              # 页面组件
│   └── App.vue             # 根组件
├── index.html              # 入口HTML
└── vite.config.js          # 构建配置
部署说明
后端部署
确保已安装Java 17+和Maven 3.8+
创建MySQL数据库并执行sql/init.sql脚本
修改application.yml中的数据库连接配置
编译打包：mvn clean package
运行：java -jar target/rbac-system-1.0.0.jar
前端部署
确保已安装Node.js 16+
安装依赖：npm install
开发环境运行：npm run dev
生产环境构建：npm run build
API接口
认证相关
POST /api/auth/login - 用户登录
POST /api/auth/logout - 用户登出
GET /api/auth/info - 获取用户信息
GET /api/auth/menu - 获取用户菜单
用户管理
GET /api/user/list - 获取用户列表
GET /api/user/{id} - 获取用户详情
POST /api/user - 新增用户
PUT /api/user/{id} - 更新用户
DELETE /api/user/{id} - 删除用户
POST /api/user/{userId}/roles - 分配用户角色
角色管理
GET /api/role/list - 获取角色列表
GET /api/role/all - 获取所有角色
GET /api/role/{id} - 获取角色详情
POST /api/role - 新增角色
PUT /api/role/{id} - 更新角色
DELETE /api/role/{id} - 删除角色
POST /api/role/{roleId}/permissions - 分配角色权限
POST /api/role/{roleId}/menus - 分配角色菜单
权限管理
GET /api/permission/list - 获取权限列表
GET /api/permission/all - 获取所有权限
GET /api/permission/tree - 获取权限树
GET /api/permission/{id} - 获取权限详情
POST /api/permission - 新增权限
PUT /api/permission/{id} - 更新权限
DELETE /api/permission/{id} - 删除权限
菜单管理
GET /api/menu/tree - 获取菜单树
POST /api/menu - 新增菜单
PUT /api/menu/{id} - 更新菜单
DELETE /api/menu/{id} - 删除菜单
权限控制实现
后端权限控制
使用Sa-Token框架实现接口级权限控制：
@SaCheckPermission("user:view")
@GetMapping("/list")
public Result<Page<SysUser>> list() {
    // ...
}
