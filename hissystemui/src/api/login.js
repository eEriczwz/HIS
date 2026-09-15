import request from './request'

// 登录相关接口 —— 请按后端实际路径调整
export function login(data) {
  return request.post('/auth/login', data)
}

export function getUserInfo() {
  return request.get('/auth/info')
}

export function logout() {
  return request.post('/auth/logout')
}
