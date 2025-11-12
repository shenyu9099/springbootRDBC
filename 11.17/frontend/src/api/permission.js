import request from '@/utils/request'

// 获取权限列表
export function getPermissionList(params) {
  return request({
    url: '/permission/list',
    method: 'get',
    params
  })
}

// 获取所有权限
export function getAllPermissions() {
  return request({
    url: '/permission/all',
    method: 'get'
  })
}

// 获取权限树
export function getPermissionTree() {
  return request({
    url: '/permission/tree',
    method: 'get'
  })
}

// 添加权限
export function addPermission(data) {
  return request({
    url: '/permission',
    method: 'post',
    data
  })
}

// 更新权限
export function updatePermission(id, data) {
  return request({
    url: `/permission/${id}`,
    method: 'put',
    data
  })
}

// 删除权限
export function deletePermission(id) {
  return request({
    url: `/permission/${id}`,
    method: 'delete'
  })
}
