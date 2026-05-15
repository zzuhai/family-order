<template>
  <div class="login-page">
    <div class="login-bg">
      <div class="bg-circle circle-1"></div>
      <div class="bg-circle circle-2"></div>
      <div class="bg-circle circle-3"></div>
    </div>
    <div class="login-card">
      <div class="login-brand">
        <div class="brand-icon">🍳</div>
        <h1 class="brand-title">家庭食记</h1>
        <p class="brand-subtitle">温暖每一餐 · 点餐管理系统</p>
      </div>
      <el-form :model="form" class="login-form" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="用户名"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="login-btn" @click="handleLogin">
            进入厨房
          </el-button>
        </el-form-item>
      </el-form>
      <p class="login-hint">
        使用家庭成员小程序登录后获取的账号登录
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  username: '',
  password: ''
})

const handleLogin = () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  authStore.setAuth({
    token: 'mock-token-for-admin',
    memberId: 1,
    nickname: form.value.username,
    role: 'chef'
  })

  ElMessage.success('登录成功')
  router.push('/')
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #3D2C2E 0%, #5A484A 50%, #8A7A7B 100%);
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.08;
}

.circle-1 {
  width: 600px;
  height: 600px;
  background: #D4764A;
  top: -200px;
  right: -150px;
}

.circle-2 {
  width: 400px;
  height: 400px;
  background: #E8A87C;
  bottom: -100px;
  left: -100px;
}

.circle-3 {
  width: 300px;
  height: 300px;
  background: #7A8B5E;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.login-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 48px 40px 36px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.login-brand {
  text-align: center;
  margin-bottom: 40px;
}

.brand-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.brand-title {
  font-family: 'Noto Serif SC', 'STSong', serif;
  font-size: 28px;
  font-weight: 700;
  color: var(--color-brown);
  margin: 0 0 8px;
  letter-spacing: 4px;
}

.brand-subtitle {
  font-size: 13px;
  color: var(--color-muted);
  letter-spacing: 3px;
  margin: 0;
}

.login-form {
  margin-bottom: 24px;
}

.login-form :deep(.el-input__wrapper) {
  background: #FFFDF8;
  border-radius: 12px;
  padding: 4px 16px;
  box-shadow: 0 0 0 1px #F0E8E0 inset;
}

.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--color-clay-light) inset;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--color-clay) inset !important;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  letter-spacing: 4px;
  border-radius: 12px;
  margin-top: 8px;
  background: linear-gradient(135deg, #D4764A, #B85E34);
  border: none;
}

.login-btn:hover {
  background: linear-gradient(135deg, #E8A87C, #D4764A) !important;
}

.login-hint {
  text-align: center;
  font-size: 12px;
  color: var(--color-muted);
  margin: 0;
  letter-spacing: 0.5px;
}
</style>
