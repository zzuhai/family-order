<template>
  <div class="member-page">
    <div class="page-toolbar">
      <div class="toolbar-left"></div>
      <div class="toolbar-right">
        <span class="member-count">{{ members.length }} 位家庭成员</span>
      </div>
    </div>

    <div class="member-grid">
      <div v-for="member in members" :key="member.id" class="member-card">
        <div class="member-avatar-wrap">
          <el-avatar v-if="member.avatar" :src="member.avatar" :size="64" />
          <el-avatar v-else :size="64" class="avatar-placeholder">{{ member.nickname.charAt(0) }}</el-avatar>
        </div>
        <h4 class="member-name">{{ member.nickname }}</h4>
        <el-tag
          :type="member.role === 'chef' ? 'success' : 'info'"
          effect="plain"
          round
          size="small"
        >
          {{ member.role === 'chef' ? '👨‍🍳 厨师' : '👤 成员' }}
        </el-tag>
        <p class="member-join">加入于 {{ member.createdAt }}</p>
        <el-button size="small" text type="primary" @click="handleEditRole(member)">
          修改角色
        </el-button>
      </div>
    </div>

    <el-dialog v-model="roleDialogVisible" title="修改角色" width="360px">
      <div class="role-options">
        <el-radio-group v-model="selectedRole" class="role-radio-group">
          <el-radio value="chef" class="role-radio">
            <div class="role-option-content">
              <span class="role-label">👨‍🍳 厨师</span>
              <span class="role-desc">可以发布每日菜单</span>
            </div>
          </el-radio>
          <el-radio value="user" class="role-radio">
            <div class="role-option-content">
              <span class="role-label">👤 普通成员</span>
              <span class="role-desc">可以点餐和查看订单</span>
            </div>
          </el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateRole">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { Member } from '@/api/types'

const members = ref<Member[]>([])
const roleDialogVisible = ref(false)
const selectedRole = ref('user')
const currentMemberId = ref(0)

onMounted(() => { loadMembers() })

const loadMembers = async () => {
  members.value = [
    { id: 1, openid: 'xxx', nickname: '爸爸', avatar: '', role: 'chef', createdAt: '2024-01-01 10:00:00' },
    { id: 2, openid: 'xxx', nickname: '妈妈', avatar: '', role: 'chef', createdAt: '2024-01-01 10:00:00' },
    { id: 3, openid: 'xxx', nickname: '小明', avatar: '', role: 'user', createdAt: '2024-01-02 12:00:00' },
    { id: 4, openid: 'xxx', nickname: '小红', avatar: '', role: 'user', createdAt: '2024-01-02 12:00:00' }
  ]
}

const handleEditRole = (row: Member) => {
  currentMemberId.value = row.id
  selectedRole.value = row.role
  roleDialogVisible.value = true
}

const handleUpdateRole = () => {
  ElMessage.success('角色更新成功')
  roleDialogVisible.value = false
  loadMembers()
}
</script>

<style scoped>
.member-page { max-width: 900px; }

.page-toolbar {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 24px;
}
.member-count { font-size: 13px; color: var(--color-muted); }

.member-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.member-card {
  background: #fff;
  border-radius: 16px;
  padding: 28px 20px 20px;
  text-align: center;
  box-shadow: var(--shadow-card);
  transition: var(--transition);
}
.member-card:hover {
  box-shadow: var(--shadow-card-hover);
  transform: translateY(-2px);
}

.member-avatar-wrap { margin-bottom: 12px; }

.avatar-placeholder {
  background: linear-gradient(135deg, #F5D4B8, #E8A87C);
  font-family: 'Noto Serif SC', serif;
  font-size: 24px;
  color: #fff;
  font-weight: 700;
}

.member-name {
  font-family: 'Noto Serif SC', serif;
  font-size: 17px;
  color: var(--color-brown);
  margin: 0 0 8px;
}

.member-join {
  font-size: 12px;
  color: var(--color-muted);
  margin: 12px 0 8px;
}

/* Role dialog */
.role-radio-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
}
.role-radio {
  width: 100%;
  padding: 14px 16px;
  border: 1px solid #F0E8E0;
  border-radius: 12px;
  transition: var(--transition);
  margin-right: 0;
}
.role-radio:hover { border-color: var(--color-clay-light); background: #FFFDF8; }
.role-radio :deep(.el-radio__label) { width: 100%; }
.role-option-content {
  display: flex; flex-direction: column; gap: 2px;
}
.role-label { font-size: 14px; font-weight: 600; color: var(--color-brown); }
.role-desc { font-size: 12px; color: var(--color-muted); }
</style>
