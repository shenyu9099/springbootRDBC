<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#409EFF" :size="48"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#67C23A" :size="48"><UserFilled /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.roleCount }}</div>
              <div class="stat-label">角色总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#E6A23C" :size="48"><Lock /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.permissionCount }}</div>
              <div class="stat-label">权限总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#F56C6C" :size="48"><Menu /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.menuCount }}</div>
              <div class="stat-label">菜单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>欢迎使用 RBAC 权限管理系统</span>
            </div>
          </template>
          <div class="welcome-content">
            <h2>系统介绍</h2>
            <p>这是一个基于 Spring Boot + Vue 3 + Element Plus + Sa-Token 的RBAC权限管理系统</p>
            <h3>技术栈</h3>
            <ul>
              <li><strong>后端:</strong> Spring Boot 2.7 + MyBatis Plus + Sa-Token + MySQL + Redis</li>
              <li><strong>前端:</strong> Vue 3 + Vite + Element Plus + Pinia + Axios</li>
            </ul>
            <h3>功能特性</h3>
            <ul>
              <li>✅ 用户管理 - 用户的增删改查、状态管理、角色分配</li>
              <li>✅ 角色管理 - 角色的增删改查、权限分配、菜单分配</li>
              <li>✅ 权限管理 - 权限的增删改查</li>
              <li>✅ 菜单管理 - 菜单的增删改查、树形结构</li>
              <li>✅ Sa-Token认证 - 登录认证、权限校验、角色校验</li>
              <li>✅ 动态路由 - 根据用户权限动态加载菜单和路由</li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const stats = reactive({
  userCount: 0,
  roleCount: 0,
  permissionCount: 0,
  menuCount: 0
})

// 这里可以调用API获取统计数据
stats.userCount = 1
stats.roleCount = 2
stats.permissionCount = 8
stats.menuCount = 5
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 0;

  .stat-card {
    margin-bottom: 20px;

    .stat-content {
      display: flex;
      align-items: center;
      gap: 20px;

      .stat-icon {
        flex-shrink: 0;
      }

      .stat-info {
        flex: 1;

        .stat-value {
          font-size: 32px;
          font-weight: bold;
          color: #333;
          line-height: 1;
          margin-bottom: 8px;
        }

        .stat-label {
          font-size: 14px;
          color: #999;
        }
      }
    }
  }

  .card-header {
    font-size: 18px;
    font-weight: bold;
  }

  .welcome-content {
    h2 {
      font-size: 24px;
      margin-bottom: 16px;
      color: #333;
    }

    h3 {
      font-size: 18px;
      margin: 20px 0 12px;
      color: #666;
    }

    p {
      font-size: 16px;
      line-height: 1.6;
      color: #666;
      margin-bottom: 20px;
    }

    ul {
      padding-left: 20px;

      li {
        font-size: 14px;
        line-height: 2;
        color: #666;

        strong {
          color: #333;
        }
      }
    }
  }
}
</style>
