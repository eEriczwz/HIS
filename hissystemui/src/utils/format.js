// 格式化工具

export function formatDate(date, fmt = 'YYYY-MM-DD HH:mm:ss') {
  if (!date) return ''
  const d = new Date(date)
  if (Number.isNaN(d.getTime())) return ''
  const pad = (n) => String(n).padStart(2, '0')
  return fmt
    .replace('YYYY', d.getFullYear())
    .replace('MM', pad(d.getMonth() + 1))
    .replace('DD', pad(d.getDate()))
    .replace('HH', pad(d.getHours()))
    .replace('mm', pad(d.getMinutes()))
    .replace('ss', pad(d.getSeconds()))
}

export function formatMoney(num) {
  return Number(num || 0).toFixed(2)
}
