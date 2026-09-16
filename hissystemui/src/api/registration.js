import request from './request'

// 挂号记录查询（后端当前无分页、无 JOIN，直接返回数组）
export function getRegisterList(params) {
  return request.get('/register/list', { params })
}

// 新增挂号（病历号后端自动生成，body 无需传 caseNumber）
export function addRegister(data) {
  return request.post('/register/add', data)
}

// 修改就诊状态（退号 = 传 visitState 4）
export function updateRegisterState(id, state) {
  return request.put(`/register/state/${id}/${state}`)
}

// 科室下拉（复用已实现的 department 接口）
export function getDeptList() {
  return request.get('/department/list')
}
