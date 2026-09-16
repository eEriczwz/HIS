import request from './request'

// 登录接口 —— 后端在 employee 服务（8094）：POST /employee/login，body 为 { realname, password }
export function login(data) {
  return request.post('/employee/login', data)
}

export function getUserInfo() {
  return request.get('/auth/info')
}

export function logout() {
  return request.post('/auth/logout')
}
