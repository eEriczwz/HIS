import request from './request'

// 查询部门列表
export function getDeptList() {
  return request.get('/department/list')
}

// 新增部门
export function addDept(data) {
  return request.post('/department/add', data)
}

// 逻辑删除部门
export function deleteDept(id) {
  return request.delete(`/department/del/${id}`)
}
