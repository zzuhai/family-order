<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="app-sidebar">
      <div class="sidebar-header">
        <div class="brand-icon">🍳</div>
        <div class="brand-text">
          <h3>家庭食记</h3>
          <span class="brand-sub">点餐管理</span>
        </div>
      </div>
      <el-menu :default-active="$route.path" router class="sidebar-menu">
        <el-menu-item index="/dish">
          <el-icon><Food /></el-icon>
          <span>菜品管理</span>
        </el-menu-item>
        <el-menu-item index="/menu">
          <el-icon><Calendar /></el-icon>
          <span>每日菜单</span>
        </el-menu-item>
        <el-menu-item index="/order">
          <el-icon><List /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/member">
          <el-icon><User /></el-icon>
          <span>成员管理</span>
        </el-menu-item>
      </el-menu>
      <div class="sidebar-footer">
        <span class="footer-text">温暖每一餐</span>
      </div>
    </el-aside>
    <el-container>
      <el-header class="app-header">
        <div class="header-left">
          <h2 class="page-title">{{ $route.meta.title }}</h2>
        </div>
        <div class="header-right">
          <span class="header-tagline">家的味道，从点餐开始</span>
        </div>
      </el-header>
      <el-main class="app-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { Food, List, User, Calendar } from '@element-plus/icons-vue'
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.app-sidebar {
  background: linear-gradient(180deg, #3D2C2E 0%, #2E1F21 100%);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 24px 20px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.brand-icon {
  font-size: 28px;
  width: 44px;
  height: 44px;
  background: rgba(212, 118, 74, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.brand-text h3 {
  margin: 0;
  color: #FFF8F0;
  font-family: 'Noto Serif SC', 'STSong', serif;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 2px;
}

.brand-sub {
  font-size: 11px;
  color: rgba(255, 248, 240, 0.5);
  letter-spacing: 3px;
  text-transform: uppercase;
}

.sidebar-menu {
  flex: 1;
  background: transparent;
  padding: 12px 0;
}

.sidebar-menu .el-menu-item {
  color: rgba(255, 248, 240, 0.65);
  margin: 2px 10px;
  border-radius: 10px;
  height: 46px;
  line-height: 46px;
  font-size: 14px;
  letter-spacing: 1px;
  transition: all 0.3s ease;
}

.sidebar-menu .el-menu-item:hover {
  color: #FFF8F0;
  background: rgba(255, 248, 240, 0.06);
}

.sidebar-menu .el-menu-item.is-active {
  color: #FFF8F0;
  background: rgba(212, 118, 74, 0.25);
  font-weight: 600;
}

.sidebar-menu .el-menu-item .el-icon {
  color: inherit;
  font-size: 18px;
  margin-right: 10px;
}

.sidebar-menu .el-menu-item.is-active::before {
  content: '';
  position: absolute;
  left: -10px;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: #D4764A;
  border-radius: 0 3px 3px 0;
}

.sidebar-footer {
  padding: 16px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  text-align: center;
}

.footer-text {
  font-size: 11px;
  color: rgba(255, 248, 240, 0.3);
  letter-spacing: 2px;
  font-family: 'Noto Serif SC', 'STSong', serif;
}

/* ---- Header ---- */
.app-header {
  background: #FFFFFF;
  border-bottom: 1px solid var(--el-border-color-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px !important;
  padding: 0 32px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title {
  font-family: 'Noto Serif SC', 'STSong', serif;
  font-size: 20px;
  font-weight: 700;
  color: var(--color-brown);
  margin: 0;
  letter-spacing: 1px;
}

.header-tagline {
  font-size: 13px;
  color: var(--color-muted);
  letter-spacing: 2px;
  font-style: italic;
}

/* ---- Main ---- */
.app-main {
  background: var(--el-bg-color);
  padding: 24px 32px;
  overflow-y: auto;
}
</style>
