<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">
        <h3>家庭点餐</h3>
      </div>
      <el-menu :default-active="$route.path" router>
        <el-menu-item index="/dish">
          <el-icon><Food /></el-icon>
          <span>菜品管理</span>
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
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-content">
          <span>{{ $route.meta.title }}</span>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { Food, List, User } from '@element-plus/icons-vue'
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-aside {
  background-color: #545c64;
  color: #fff;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #434a50;
}

.logo h3 {
  color: #fff;
  font-size: 18px;
}

.el-menu {
  border: none;
  background-color: #545c64;
}

.el-menu-item {
  color: #fff;
}

.el-menu-item:hover,
.el-menu-item.is-active {
  background-color: #434a50;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-main {
  background-color: #f5f5f5;
  padding: 20px;
}
</style>