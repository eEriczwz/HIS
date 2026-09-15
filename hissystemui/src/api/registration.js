import request from './request'

// 挂号记录分页查询
export function getRegisterList(params) {
  return request.get('/register/list', { params })
}

// 新增挂号
export function addRegister(data) {
  return request.post('/register/add', data)
}

// 退号（visit_state → 4）
export function refundRegister(id) {
  return request.put(`/register/refund/${id}`)
}

// 科室下拉（复用已实现的 department 接口，前端过滤 deptType=门诊）
export function getDeptList() {
  return request.get('/department/list')
}

// 某科室下的医生下拉
export function getDoctorList(deptmentId) {
  return request.get('/employee/list', { params: { deptmentId } })
}

// 号别下拉（专家/普通/主任）
export function getRegistLevelList() {
  return request.get('/regist-level/list')
}

// 结算类别下拉（自费/市医保）
export function getSettleCategoryList() {
  return request.get('/settle-category/list')
}
