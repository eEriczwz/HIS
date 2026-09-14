import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '@/utils/auth'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

// 请求拦截器：自动携带 token
service.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

// 响应拦截器：统一处理返回结构与错误
service.interceptors.response.use(
  (response) => {
    const res = response.data
    // 约定后端返回结构为 { code, data, msg }，code === 200 表示成功
    if (res && typeof res === 'object' && 'code' in res && res.code !== 200) {
      ElMessage.error(res.msg || res.message || '请求失败')
      if (res.code === 401) {
        removeToken()
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.msg || res.message || 'Error'))
    }
    return res
  },
  (error) => {
    const status = error.response?.status
    let msg = error.message || '网络错误'
    if (status === 401) {
      msg = '登录已过期，请重新登录'
      removeToken()
      window.location.href = '/login'
    } else if (status === 403) {
      msg = '没有权限访问'
    } else if (status === 404) {
      msg = '请求的资源不存在'
    } else if (status >= 500) {
      msg = '服务器错误'
    }
    ElMessage.error(msg)
    return Promise.reject(error)
  },
)

export default service
