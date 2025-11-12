import request from '@/utils/request'

// 获取菜单树
export function getMenuTree() {
  return request({
    url: '/menu/tree',
    method: 'get'
  })
}

// 获取所有菜单
export function getAllMenus() {
  return request({
    url: '/menu/all',
    method: 'get'
  })
}

// 添加菜单
export function addMenu(data) {
  return request({
    url: '/menu',
    method: 'post',
    data
  })
}

// 更新菜单
export function updateMenu(id, data) {
  return request({
    url: `/menu/${id}`,
    method: 'put',
    data
  })
}

// 删除菜单
export function deleteMenu(id) {
  return request({
    url: `/menu/${id}`,
    method: 'delete'
  })
}
