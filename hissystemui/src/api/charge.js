import request from './request'

// ============ 收费管理 ============
// 说明：后端收费接口尚未实现，当前用本地 mock 联调前端页面。
// 后端就绪后：
//   1) 把 USE_MOCK 改为 false；
//   2) 核对下方请求路径（/charge 前缀需要在 vite.config.js 的 proxy 里新增对应后端服务的转发）；
//   3) 若后端字段名有出入，只需调整本文件与页面里的字段映射即可。

const USE_MOCK = false

// 根据病历号或挂号 ID 计算费用明细
// 返回字段：registFee 挂号费 / drugTotalFee 药品总费用 / checkTotalFee 检验项目总费用
//          inspectionTotalFee 检查项目总费用 / disposalTotalFee 处置治疗总费用 / totalAmount 合计总金额
export function getFeeDetail(key) {
  if (USE_MOCK) return Promise.resolve({ code: 200, msg: 'ok', data: mockFeeDetail(key) })
  return request.get(`/charge/fee/${key}`)
}

// 收费结算（支持病历号或挂号 ID）
export function settleCharge(key) {
  if (USE_MOCK) return Promise.resolve({ code: 200, msg: '收费成功' })
  return request.post(`/charge/settle/${key}`)
}

// 已收费记录列表（退费页使用）
export function getChargedList(params) {
  if (USE_MOCK) return Promise.resolve({ code: 200, msg: 'ok', data: mockChargedList() })
  return request.get('/charge/charged/list', { params })
}

// 退费
export function refundCharge(chargeId) {
  if (USE_MOCK) return Promise.resolve({ code: 200, msg: '退费成功' })
  return request.post(`/charge/refund/${chargeId}`)
}

// ---------------------- 本地 mock 数据 ----------------------
function mockFeeDetail(registerId) {
  const id = Number(registerId) || 1
  const registFee = [8, 20, 50][id % 3] // 挂号费：普通号 / 主任医生号 / 专家号
  const drugTotalFee = 30 + (id * 37) % 900 // 药品
  const checkTotalFee = 15 + (id * 53) % 400 // 检验
  const inspectionTotalFee = 25 + (id * 71) % 600 // 检查
  const disposalTotalFee = 10 + (id * 29) % 300 // 处置治疗
  const totalAmount =
    registFee + drugTotalFee + checkTotalFee + inspectionTotalFee + disposalTotalFee
  return {
    registFee,
    drugTotalFee,
    checkTotalFee,
    inspectionTotalFee,
    disposalTotalFee,
    totalAmount,
  }
}

function mockChargedList() {
  const now = '2026-09-16'
  return [
    { id: 1, caseNumber: '1001', realName: '张三', totalAmount: 256.5, chargeTime: `${now} 09:30:00`, chargeState: '已收费' },
    { id: 2, caseNumber: '1002', realName: '李四', totalAmount: 128.0, chargeTime: `${now} 10:12:00`, chargeState: '已收费' },
    { id: 3, caseNumber: '1003', realName: '王五', totalAmount: 532.8, chargeTime: `${now} 11:05:00`, chargeState: '已收费' },
  ]
}
