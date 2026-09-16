<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import { getDispenseList, issuePrescription, returnPrescription } from '@/api/pharmacy'
import { formatDate, formatMoney } from '@/utils/format'

const loading = ref(false)
const list = ref([])

const stateMap = {
  已开立: { type: 'info' },
  已缴费: { type: 'primary' },
  已发药: { type: 'success' },
  已退药: { type: 'warning' },
  已退费: { type: 'danger' },
}

const stateOptions = [
  { value: 'pending', label: '待发药' },
  { value: '已开立', label: '已开立' },
  { value: '已缴费', label: '已缴费' },
  { value: '已发药', label: '已发药' },
  { value: '已退药', label: '已退药' },
  { value: '已退费', label: '已退费' },
]

const query = reactive({
  state: 'pending',
  caseNumber: '',
  drugName: '',
})

async function fetchList() {
  loading.value = true
  try {
    const params = {}
    if (query.state) params.state = query.state
    if (query.caseNumber) params.caseNumber = query.caseNumber
    if (query.drugName) params.drugName = query.drugName
    const res = await getDispenseList(params)
    list.value = res?.data || []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  fetchList()
}

async function handleIssue(row) {
  try {
    await ElMessageBox.confirm(
      `确认为患者「${row.realName || '-'}」发放药品「${row.drugName}」×${row.drugNumber}${row.drugUnit || ''}？`,
      '发药确认',
      { confirmButtonText: '发药', cancelButtonText: '取消', type: 'info' },
    )
    await issuePrescription(row.id)
    ElMessage.success('发药成功')
    fetchList()
  } catch (e) {
    // 取消确认或发药失败（如库存不足）均无需重复提示
  }
}

async function handleReturn(row) {
  try {
    await ElMessageBox.confirm(
      `确定退回药品「${row.drugName}」吗？`,
      '退药确认',
      { confirmButtonText: '退药', cancelButtonText: '取消', type: 'warning' },
    )
    await returnPrescription(row.id)
    ElMessage.success('退药成功')
    fetchList()
  } catch (e) {
    // 取消确认或退药失败均无需重复提示
  }
}

onMounted(fetchList)
</script>

<template>
  <div class="app-container">
    <el-card>
      <div class="toolbar">
        <el-form :inline="true" :model="query" class="search-form">
          <el-form-item label="状态">
            <el-select v-model="query.state" placeholder="状态" clearable style="width: 130px">
              <el-option v-for="s in stateOptions" :key="s.value" :label="s.label" :value="s.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="病历号">
            <el-input v-model="query.caseNumber" placeholder="病历号" clearable style="width: 130px" />
          </el-form-item>
          <el-form-item label="药品名称">
            <el-input v-model="query.drugName" placeholder="药品名称" clearable style="width: 160px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          </el-form-item>
        </el-form>
        <div class="toolbar-actions">
          <el-button :icon="Refresh" @click="fetchList">刷新</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="list" border stripe size="small" max-height="calc(100vh - 260px)">
        <el-table-column prop="caseNumber" label="病历号" width="100" />
        <el-table-column prop="realName" label="患者" width="90" />
        <el-table-column prop="drugName" label="药品名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="drugFormat" label="规格" min-width="120" show-overflow-tooltip />
        <el-table-column prop="drugUsage" label="用法用量" min-width="150" show-overflow-tooltip />
        <el-table-column label="数量" width="70" align="center">
          <template #default="{ row }">{{ row.drugNumber }}</template>
        </el-table-column>
        <el-table-column label="单价" width="90" align="right">
          <template #default="{ row }">
            {{ row.drugPrice != null ? formatMoney(row.drugPrice) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="stateMap[row.drugState]?.type || 'info'" size="small">
              {{ row.drugState || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="开立时间" width="160">
          <template #default="{ row }">{{ formatDate(row.creationTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="90" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.drugState === '已开立' || row.drugState === '已缴费'"
              link
              type="primary"
              @click="handleIssue(row)"
            >
              发药
            </el-button>
            <el-button
              v-if="row.drugState === '已发药'"
              link
              type="warning"
              @click="handleReturn(row)"
            >
              退药
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
}
</style>
