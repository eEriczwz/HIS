

import request from './request'

// ============ 门诊医生工作站 ============

// 查询当前医生待接诊患者列表
export function getWaitPatients(doctorId) {
  return request.get(`/outpatient/doctor/waitPatient/${doctorId}`)
}

// 接诊（更新挂号单就诊状态为 2 已接诊）
export function receivePatient(registerId) {
  return request.put(`/outpatient/doctor/receive/${registerId}`)
}

// 保存病历 + 一次绑定多种疾病
// data: { registerId, diseaseIdList: [..], record: { readme, present, ... } }
export function saveMedicalRecord(data) {
  return request.post('/outpatient/doctor/saveMedicalRecord', data)
}

// 疾病字典（按名称/编码/ICD 模糊查询，用于诊断多选）
export function getDiseases(keyword) {
  return request.get('/outpatient/doctor/diseases', { params: { keyword } })
}

// 医技项目字典（techType: 检查/检验/处置）
export function getMedicalTechnologies(params) {
  return request.get('/outpatient/doctor/medicalTechnologies', { params })
}

// 检查申请单：开立 + 查询
export function addCheckRequest(data) {
  return request.post('/outpatient/doctor/addCheckRequest', data)
}
export function getCheckList(registerId) {
  return request.get(`/outpatient/doctor/listCheck/${registerId}`)
}

// 处置申请单：开立 + 查询
export function addDisposalRequest(data) {
  return request.post('/outpatient/doctor/addDisposalRequest', data)
}
export function getDisposalList(registerId) {
  return request.get(`/outpatient/doctor/listDisposal/${registerId}`)
}

// 处方：开立 + 查询
export function addPrescription(data) {
  return request.post('/outpatient/doctor/addPrescription', data)
}
export function getPrescriptionList(registerId) {
  return request.get(`/outpatient/doctor/listPrescription/${registerId}`)
}

// 综合查询单次就诊全套资料：挂号、病历、疾病、检查、处置、处方
export function getVisitDetail(registerId) {
  return request.get(`/outpatient/doctor/detail/${registerId}`)
}
