import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo as getUserInfoApi } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const USER_KEY = 'his_user'

function readUser() {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || 'null')
  } catch (e) {
    return null
  }
}

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userInfo: readUser(),
  }),
  actions: {
    async login(form) {
      const res = await loginApi(form)
      const data = res?.data
      // 新版后端：data = { token, id, realname, ... }
      // 旧版后端：Result.success(token) 因 String 重载把 token 放进了 msg、data 为 null
      let token = ''
      if (typeof data === 'string') {
        token = data
      } else if (data && typeof data === 'object') {
        token = data.token || ''
      }
      if (!token && typeof res?.msg === 'string' && res.msg !== '操作成功') {
        token = res.msg
      }
      if (!token) {
        throw new Error('登录响应缺少 token')
      }
      this.token = token
      setToken(token)

      const userInfo =
        data && typeof data === 'object'
          ? {
              id: data.id,
              name: data.realname,
              realname: data.realname,
              deptmentId: data.deptmentId,
              registLevelId: data.registLevelId,
            }
          : null
      this.userInfo = userInfo
      localStorage.setItem(USER_KEY, JSON.stringify(userInfo))
      return res
    },
    async getInfo() {
      const res = await getUserInfoApi()
      this.userInfo = res?.data || res
      localStorage.setItem(USER_KEY, JSON.stringify(this.userInfo))
      return this.userInfo
    },
    logout() {
      this.token = ''
      this.userInfo = null
      removeToken()
      localStorage.removeItem(USER_KEY)
    },
  },
})
