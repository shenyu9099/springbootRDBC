import { defineStore } from 'pinia'
import { getMenuTree } from '@/api/auth'

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    routes: [],
    menuTree: []
  }),
  
  actions: {
    async generateRoutes() {
      try {
        const { data } = await getMenuTree()
        this.menuTree = data
        return data
      } catch (error) {
        return Promise.reject(error)
      }
    },
    
    setRoutes(routes) {
      this.routes = routes
    }
  }
})
