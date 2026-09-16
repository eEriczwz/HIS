<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import { getDrugList, stockIn, stockOut } from '@/api/pharmacy'
import { formatMoney } from '@/utils/format'

const loading = ref(false)
const list = ref([])
const query = reactive({ drugName: '' })

const LOW_STOCK = 10

const dialogVisible = ref(false)
const dialogType = ref('in') // in=入库 out=出库
const currentRow = ref(null)
const qty = ref(1)
const submitting = ref(false)

async function fetchList() {
  loading.value = true
  try {
    const params = {}
    if (query.drugName) params.drugName = query.drugName
    const res = await getDrugList(params)
    list.value = res?.data || []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  fetchList()
}

function openStock(row, type) {
  currentRow.value = row
  dialogType.value = type
  qty.value = 1
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!qty.value || qty.value <= 0) {
    ElMessage.warning('请输入大于 0 的数量')
    return
  }
  submitting.value = true
  try {
    if (dialogType.value === 'in') {
      await stockIn(currentRow.value.id, qty.value)
      ElMessage.success('入库成功')
    } else {
      await stockOut(currentRow.value.id, qty.value)
      ElMessage.success('出库成功')
    }
    dialogVisible.value = false
    fetchList()
  } catch (e) {
    // 错误提示已在 request.js 统一处理（出库库存不足会提示）
  } finally {
    submitting.value = false
  }
}

onMounted(fetchList)
</script>

<template>
  <div class="app-container">
    <el-card>
      <div class="toolbar">
        <el-form :inline="true" :model="query" class="search-form">
          <el-form-item label="药品名称">
            <el-input v-model="query.drugName" placeholder="药品名称" clearable style="width: 200px" />
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
        <el-table-column prop="drugName" label="药品名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="drugFormat" label="规格" min-width="120" show-overflow-tooltip />
        <el-table-column prop="drugUnit" label="单位" width="70" />
        <el-table-column label="单价" width="90" align="right">
          <template #default="{ row }">{{ formatMoney(row.drugPrice) }}</template>
        </el-table-column>
        <el-table-column label="库存数量" width="110" align="center">
          <template #default="{ row }">
            <el-tag
              :type="row.drugStock != null && row.drugStock <= LOW_STOCK ? 'danger' : 'success'"
              effect="light"
            >
              {{ row.drugStock ?? 0 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <span v-if="row.drugStock == null || row.drugStock <= 0" class="stock-zero">无库存</span>
            <span v-else-if="row.drugStock <= LOW_STOCK" class="stock-low">库存偏低</span>
            <span v-else class="stock-ok">正常</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openStock(row, 'in')">入库</el-button>
            <el-button link type="warning" @click="openStock(row, 'out')">出库</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'in' ? '药品入库' : '药品出库'"
      width="420px"
      :close-on-click-modal="false"
    >
      <el-form label-width="90px">
        <el-form-item label="药品">
          <span class="drug-label">{{ currentRow?.drugName }}</span>
          <span v-if="currentRow" class="drug-sub">
            （当前库存 {{ currentRow.drugStock ?? 0 }}）
          </span>
        </el-form-item>
        <el-form-item :label="dialogType === 'in' ? '入库数量' : '出库数量'">
          <el-input-number v-model="qty" :min="1" :max="99999" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
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

.drug-label {
  font-weight: 600;
}

.drug-sub {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.stock-zero {
  color: var(--el-color-danger);
}
.stock-low {
  color: var(--el-color-warning);
}
.stock-ok {
  color: var(--el-color-success);
}
</style>
