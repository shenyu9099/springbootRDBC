import { defineStore } from 'pinia'
import { login, getUserInfo, logout } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userInfo: null,
    roles: [],
    permissions: []
  }),
  
  actions: {
    // 登录
    async login(loginForm) {
      try {
        const { data } = await login(loginForm)
        this.token = data.token
        this.roles = data.roles || []
        this.permissions = data.permissions || []
        setToken(data.token)
        return data
      } catch (error) {
        return Promise.reject(error)
      }
    },
    
    // 获取用户信息
    async getInfo() {
      try {
        const { data } = await getUserInfo()
        this.userInfo = data
        this.roles = data.roles || []
        this.permissions = data.permissions || []
        return data
      } catch (error) {
        return Promise.reject(error)
      }
    },
    
    // 登出
    async logout() {
      try {
        await logout()
      } catch (error) {
        console.error('登出失败:', error)
      } finally {
        this.token = ''
        this.userInfo = null
        this.roles = []
        this.permissions = []
        removeToken()
      }
    },
    
    // 重置token
    resetToken() {
      this.token = ''
      this.userInfo = null
      this.roles = []
      this.permissions = []
      removeToken()
    }
  }
})
