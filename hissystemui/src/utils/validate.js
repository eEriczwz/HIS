// 常用表单校验工具

export function isValidUsername(str) {
  return /^[a-zA-Z0-9_]{3,20}$/.test(str)
}

export function isValidPhone(str) {
  return /^1[3-9]\d{9}$/.test(str)
}

export function isValidEmail(str) {
  return /^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,}$/.test(str)
}

export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}
