<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getChargedList, refundCharge } from '@/api/charge'
import { formatDate, formatMoney } from '@/utils/format'

const loading = ref(false)
const list = ref([])

const query = reactive({
  caseNumber: '',
  realName: '',
})

const chargeStateMap = {
  已收费: { type: 'success' },
  已退费: { type: 'info' },
}

async function fetchList() {
  loading.value = true
  try {
    const params = {}
    if (query.caseNumber) params.caseNumber = query.caseNumber
    if (query.realName) params.realName = query.realName
    const res = await getChargedList(params)
    list.value = res?.data || []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  fetchList()
}

function handleReset() {
  query.caseNumber = ''
  query.realName = ''
  fetchList()
}

async function handleRefund(row) {
  try {
    await ElMessageBox.confirm(
      `确定为「${row.realName}」（病历号 ${row.caseNumber}）退费 ¥${formatMoney(row.totalAmount)} 吗？`,
      '退费确认',
      { confirmButtonText: '确定退费', cancelButtonText: '取消', type: 'warning' },
    )
    await refundCharge(row.id)
    ElMessage.success('退费成功')
    // 后端就绪后此处应重新 fetchList；mock 阶段本地更新状态便于联调
    row.chargeState = '已退费'
  } catch (e) {
    // 取消确认或退费失败均无需重复提示
  }
}

onMounted(fetchList)
</script>

<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <span class="page-header">退费</span>
      </template>

      <div class="toolbar">
        <el-form :inline="true" :model="query" class="search-form">
          <el-form-item label="病历号">
            <el-input v-model="query.caseNumber" placeholder="病历号" clearable style="width: 140px" />
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="query.realName" placeholder="姓名" clearable style="width: 140px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>

        <div class="toolbar-actions">
          <el-button :icon="Refresh" @click="fetchList">刷新</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="list" border stripe size="default">
        <el-table-column prop="id" label="收费单号" width="100" />
        <el-table-column prop="caseNumber" label="病历号" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column label="收费金额" width="140" align="right">
          <template #default="{ row }">{{ formatMoney(row.totalAmount) }}</template>
        </el-table-column>
        <el-table-column label="收费时间" width="180">
          <template #default="{ row }">{{ formatDate(row.chargeTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="chargeStateMap[row.chargeState]?.type || 'info'" size="small">
              {{ row.chargeState || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.chargeState === '已收费'"
              link
              type="danger"
              @click="handleRefund(row)"
            >
              退费
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 16px;
}

.search-form {
  flex: 1;
}

.search-form :deep(.el-form-item) {
  margin-bottom: 12px;
}

.toolbar-actions {
  flex-shrink: 0;
  display: flex;
  gap: 12px;
}
</style>
