import request from '@/utils/request'

// 获取角色列表
export function getRoleList(params) {
  return request({
    url: '/role/list',
    method: 'get',
    params
  })
}

// 获取所有角色
export function getAllRoles() {
  return request({
    url: '/role/all',
    method: 'get'
  })
}

// 获取角色详情
export function getRoleById(id) {
  return request({
    url: `/role/${id}`,
    method: 'get'
  })
}

// 添加角色
export function addRole(data) {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(id, data) {
  return request({
    url: `/role/${id}`,
    method: 'put',
    data
  })
}

// 删除角色
export function deleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

// 分配权限
export function assignPermissions(roleId, permissionIds) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: 'post',
    data: permissionIds
  })
}

// 分配菜单
export function assignMenus(roleId, menuIds) {
  return request({
    url: `/role/${roleId}/menus`,
    method: 'post',
    data: menuIds
  })
}
