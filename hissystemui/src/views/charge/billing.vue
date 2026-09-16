<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getFeeDetail, settleCharge } from '@/api/charge'
import { formatMoney } from '@/utils/format'

const queryKey = ref('')
const loading = ref(false)
const settling = ref(false)
const detail = ref(null)

// 费用明细行（合计单独高亮展示）
const feeItems = computed(() => {
  if (!detail.value) return []
  return [
    { label: '挂号费', field: 'registFee' },
    { label: '药品总费用', field: 'drugTotalFee' },
    { label: '检验项目总费用', field: 'checkTotalFee' },
    { label: '检查项目总费用', field: 'inspectionTotalFee' },
    { label: '处置治疗总费用', field: 'disposalTotalFee' },
  ]
})

const totalAmount = computed(() => detail.value?.totalAmount ?? 0)

async function handleQuery() {
  const key = queryKey.value.trim()
  if (!key) {
    ElMessage.warning('请输入病历号或挂号 ID')
    return
  }
  loading.value = true
  try {
    const res = await getFeeDetail(key)
    detail.value = res?.data || null
    if (!detail.value) ElMessage.info('未查询到该挂号单的费用')
  } catch (e) {
    // 错误提示已由 request.js 统一处理
    detail.value = null
  } finally {
    loading.value = false
  }
}

async function handleSettle() {
  const key = queryKey.value.trim()
  if (!key) {
    ElMessage.warning('请输入病历号或挂号 ID')
    return
  }
  settling.value = true
  try {
    await settleCharge(key)
    ElMessage.success('收费成功')
    // 收费完成后清空，方便录入下一单
    detail.value = null
    queryKey.value = ''
  } catch (e) {
    // 错误提示已由 request.js 统一处理
  } finally {
    settling.value = false
  }
}
</script>

<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <span class="page-header">收费</span>
      </template>

      <!-- 查询挂号单 -->
      <div class="query-bar">
        <el-input
          v-model="queryKey"
          placeholder="请输入病历号或挂号 ID"
          clearable
          class="register-input"
          @keyup.enter="handleQuery"
        />
        <el-button type="primary" :icon="Search" :loading="loading" @click="handleQuery">
          查询费用
        </el-button>
        <el-button :icon="Refresh" @click="queryKey = ''">清空</el-button>
      </div>

      <el-empty
        v-if="!detail && !loading"
        description="输入病历号或挂号 ID 后，自动计算该挂号单的所有费用明细"
      />

      <template v-if="detail">
        <div class="register-tip">病历号：{{ detail.caseNumber }}｜挂号 ID：{{ detail.registerId }}</div>

        <el-table :data="feeItems" border stripe size="default" class="fee-table">
          <el-table-column prop="label" label="费用项目" min-width="200" />
          <el-table-column label="金额（元）" min-width="160" align="right">
            <template #default="{ row }">{{ formatMoney(detail[row.field]) }}</template>
          </el-table-column>
        </el-table>

        <div class="total-bar">
          <span class="total-label">合计总金额</span>
          <span class="total-amount">¥ {{ formatMoney(totalAmount) }}</span>
        </div>

        <div class="footer-bar">
          <el-button type="primary" :loading="settling" @click="handleSettle">确认收费</el-button>
        </div>
      </template>
    </el-card>
  </div>
</template>

<style scoped>
.query-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.register-input {
  width: 240px;
}

.register-tip {
  margin-bottom: 12px;
  font-size: 14px;
  color: var(--el-text-color-primary);
  font-weight: 600;
}

.fee-table {
  max-width: 720px;
}

.total-bar {
  display: flex;
  align-items: baseline;
  gap: 16px;
  margin-top: 16px;
  padding: 16px 20px;
  background: var(--el-fill-color-light);
  border-radius: 6px;
  max-width: 720px;
}

.total-label {
  font-size: 15px;
  color: var(--el-text-color-regular);
}

.total-amount {
  font-size: 26px;
  font-weight: 700;
  color: var(--el-color-danger);
}

.footer-bar {
  margin-top: 16px;
}
</style>
