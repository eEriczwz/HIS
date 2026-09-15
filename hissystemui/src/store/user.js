import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo as getUserInfoApi } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userInfo: null,
  }),
  actions: {
    async login(form) {
      const res = await loginApi(form)
      // 按后端返回结构调整，token 可能在 res.data.token 或 res.token
      const token = res?.data?.token || res?.token
      this.token = token
      setToken(token)
      return res
    },
    async getInfo() {
      const res = await getUserInfoApi()
      this.userInfo = res?.data || res
      return this.userInfo
    },
    logout() {
      this.token = ''
      this.userInfo = null
      removeToken()
    },
  },
})
