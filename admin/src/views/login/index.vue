<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">家庭点餐管理系统</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
      <p style="text-align: center; color: #999; font-size: 12px;">
        提示: 使用家庭成员小程序登录后获取的账号登录
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
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