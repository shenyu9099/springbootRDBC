<template>
  <div class="sidebar-container">
    <div class="sidebar-logo-container">
      <div class="sidebar-logo-link">
        <h1 class="sidebar-title">RBAC系统</h1>
      </div>
    </div>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="false"
        :collapse-transition="false"
        mode="vertical"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <sidebar-item
          v-for="route in routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store/app'
import { usePermissionStore } from '@/store/permission'
import { useUserStore } from '@/store/user'
import { asyncRoutes, constantRoutes } from '@/router'
import SidebarItem from './SidebarItem.vue'

const route = useRoute()
const appStore = useAppStore()
const permissionStore = usePermissionStore()
const userStore = useUserStore()

const routes = computed(() => {
  // 检查是否是超级管理员
  const isAdmin = userStore.roles && userStore.roles.includes('admin')
  
  if (isAdmin) {
    // 超级管理员显示所有菜单
    return [...constantRoutes, ...asyncRoutes]
  } else {
    // 普通用户使用权限存储中的菜单树
    return permissionStore.menuTree
  }
})

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

const isCollapse = computed(() => !appStore.sidebar.opened)
</script>

<style lang="scss" scoped>
.sidebar-container {
  height: 100%;
  background-color: #304156;
  display: flex;
  flex-direction: column;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;
  flex-shrink: 0;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 18px;
      vertical-align: middle;
    }
  }
}

.el-scrollbar {
  flex: 1;
  height: 0;
}

:deep(.el-menu) {
  border: none;
  height: 100%;
  background-color: #304156;
}
</style>
