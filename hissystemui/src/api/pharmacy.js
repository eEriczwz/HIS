import request from './request'

// ============ 药品管理 ============
// 药品列表（支持药品名称/编码模糊查询）
export function getDrugList(params) {
  return request.get('/drugstore/drug/list', { params })
}
export function addDrug(data) {
  return request.post('/drugstore/drug/add', data)
}
export function updateDrug(data) {
  return request.put('/drugstore/drug/update', data)
}
export function deleteDrug(id) {
  return request.delete(`/drugstore/drug/del/${id}`)
}

// ============ 库存管理 ============
export function stockIn(id, qty) {
  return request.put(`/drugstore/drug/stockIn/${id}/${qty}`)
}
export function stockOut(id, qty) {
  return request.put(`/drugstore/drug/stockOut/${id}/${qty}`)
}

// ============ 发药 ============
// 发药列表：state 传 'pending' 表示待发药（已开立/已缴费）
export function getDispenseList(params) {
  return request.get('/drugstore/dispense/list', { params })
}
export function issuePrescription(id) {
  return request.put(`/drugstore/dispense/issue/${id}`)
}
export function returnPrescription(id) {
  return request.put(`/drugstore/dispense/return/${id}`)
}
