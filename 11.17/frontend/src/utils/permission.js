import { useUserStore } from '@/store/user'

/**
 * 检查用户是否拥有指定权限
 * @param {string} permission 权限标识
 * @returns {boolean} 是否拥有权限
 */
export function hasPermission(permission) {
  const userStore = useUserStore()
  // 超级管理员拥有所有权限
  if (userStore.roles.includes('admin')) {
    return true
  }
  return userStore.permissions.includes(permission)
}

/**
 * 检查用户是否拥有指定角色
 * @param {string} role 角色标识
 * @returns {boolean} 是否拥有角色
 */
export function hasRole(role) {
  const userStore = useUserStore()
  return userStore.roles.includes(role)
}