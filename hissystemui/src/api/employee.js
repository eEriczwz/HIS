import request from './request'

// 医生列表（医生工作站用于选择当前登录医生）
export function getEmployeeList() {
  return request.get('/employee/list')
}
