import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const memberId = ref<number>(0)
  const nickname = ref<string>('')
  const role = ref<string>('')

  function setAuth(data: { token: string; memberId: number; nickname: string; role: string }) {
    token.value = data.token
    memberId.value = data.memberId
    nickname.value = data.nickname
    role.value = data.role
    localStorage.setItem('token', data.token)
  }

  function logout() {
    token.value = ''
    memberId.value = 0
    nickname.value = ''
    role.value = ''
    localStorage.removeItem('token')
  }

  return { token, memberId, nickname, role, setAuth, logout }
})