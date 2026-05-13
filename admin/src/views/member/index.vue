<template>
  <div class="member-page">
    <el-table :data="members" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="nickname" label="昵称" width="150" />
      <el-table-column label="头像" width="100">
        <template #default="{ row }">
          <el-avatar v-if="row.avatar" :src="row.avatar" />
          <el-avatar v-else>{{ row.nickname }}</el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 'chef' ? 'success' : 'info'">
            {{ row.role === 'chef' ? '厨师' : '成员' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="加入时间" width="180" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="handleEditRole(row)">修改角色</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="roleDialogVisible" title="修改角色" width="300px">
      <el-radio-group v-model="selectedRole">
        <el-radio value="chef">厨师</el-radio>
        <el-radio value="user">普通成员</el-radio>
      </el-radio-group>
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

onMounted(() => {
  loadMembers()
})

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