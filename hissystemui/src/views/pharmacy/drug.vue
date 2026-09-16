<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { getDrugList, addDrug, updateDrug, deleteDrug } from '@/api/pharmacy'
import { formatDate, formatMoney } from '@/utils/format'

const loading = ref(false)
const list = ref([])

const query = reactive({
  drugName: '',
  drugCode: '',
})

const drugTypeOptions = ['西药', '中成药', '中草药', '中药饮片']
const drugDosageOptions = ['针剂', '片剂', '胶囊', '颗粒剂', '散剂', '口服液', '丸剂', '中药饮片']

const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  drugCode: '',
  drugName: '',
  drugFormat: '',
  drugUnit: '',
  manufacturer: '',
  drugDosage: '',
  drugType: '',
  drugPrice: null,
  mnemonicCode: '',
  creationDate: '',
})

const rules = {
  drugName: [{ required: true, message: '请输入药品名称', trigger: 'blur' }],
  drugType: [{ required: true, message: '请选择药品类型', trigger: 'change' }],
  drugPrice: [{ required: true, message: '请输入药品单价', trigger: 'blur' }],
}

async function fetchList() {
  loading.value = true
  try {
    const params = {}
    if (query.drugName) params.drugName = query.drugName
    if (query.drugCode) params.drugCode = query.drugCode
    const res = await getDrugList(params)
    list.value = res?.data || []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  fetchList()
}

function handleReset() {
  query.drugName = ''
  query.drugCode = ''
  fetchList()
}

function openAdd() {
  isEdit.value = false
  editId.value = null
  Object.assign(form, {
    drugCode: '',
    drugName: '',
    drugFormat: '',
    drugUnit: '',
    manufacturer: '',
    drugDosage: '',
    drugType: '',
    drugPrice: null,
    mnemonicCode: '',
    creationDate: formatDate(new Date(), 'YYYY-MM-DD'),
  })
  dialogVisible.value = true
}

function openEdit(row) {
  isEdit.value = true
  editId.value = row.id
  Object.assign(form, {
    drugCode: row.drugCode || '',
    drugName: row.drugName || '',
    drugFormat: row.drugFormat || '',
    drugUnit: row.drugUnit || '',
    manufacturer: row.manufacturer || '',
    drugDosage: row.drugDosage || '',
    drugType: row.drugType || '',
    drugPrice: row.drugPrice,
    mnemonicCode: row.mnemonicCode || '',
    creationDate: row.creationDate || '',
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    const payload = { ...form }
    if (isEdit.value) {
      payload.id = editId.value
      await updateDrug(payload)
      ElMessage.success('修改成功')
    } else {
      await addDrug(payload)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchList()
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    submitting.value = false
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确定删除药品「${row.drugName}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
  await deleteDrug(row.id)
  ElMessage.success('删除成功')
  fetchList()
}

onMounted(fetchList)
</script>

<template>
  <div class="app-container">
    <el-card>
      <div class="toolbar">
        <el-form :inline="true" :model="query" class="search-form">
          <el-form-item label="药品名称">
            <el-input v-model="query.drugName" placeholder="药品名称" clearable style="width: 180px" />
          </el-form-item>
          <el-form-item label="药品编码">
            <el-input v-model="query.drugCode" placeholder="药品编码" clearable style="width: 180px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="toolbar-actions">
          <el-button type="primary" :icon="Plus" @click="openAdd">新增药品</el-button>
          <el-button :icon="Refresh" @click="fetchList">刷新</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="list" border stripe size="small" max-height="calc(100vh - 260px)">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="drugCode" label="药品编码" width="160" show-overflow-tooltip />
        <el-table-column prop="drugName" label="药品名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="drugFormat" label="规格" min-width="120" show-overflow-tooltip />
        <el-table-column prop="drugUnit" label="单位" width="70" />
        <el-table-column prop="drugType" label="类型" width="90" />
        <el-table-column label="单价" width="90" align="right">
          <template #default="{ row }">{{ formatMoney(row.drugPrice) }}</template>
        </el-table-column>
        <el-table-column prop="manufacturer" label="生产厂家" min-width="180" show-overflow-tooltip />
        <el-table-column label="创建日期" width="110">
          <template #default="{ row }">{{ row.creationDate || '-' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑药品' : '新增药品'"
      width="640px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="药品名称" prop="drugName">
              <el-input v-model="form.drugName" placeholder="药品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="药品编码" prop="drugCode">
              <el-input v-model="form.drugCode" placeholder="药品编码（14位本位码）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格" prop="drugFormat">
              <el-input v-model="form.drugFormat" placeholder="如 0.25g×20片" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="drugUnit">
              <el-input v-model="form.drugUnit" placeholder="如 盒/支/瓶" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="药品类型" prop="drugType">
              <el-select v-model="form.drugType" placeholder="请选择" style="width: 100%">
                <el-option v-for="t in drugTypeOptions" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="剂型" prop="drugDosage">
              <el-select
                v-model="form.drugDosage"
                placeholder="请选择或输入"
                filterable
                allow-create
                style="width: 100%"
              >
                <el-option v-for="d in drugDosageOptions" :key="d" :label="d" :value="d" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单价" prop="drugPrice">
              <el-input-number
                v-model="form.drugPrice"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="助记码" prop="mnemonicCode">
              <el-input v-model="form.mnemonicCode" placeholder="拼音助记码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建日期" prop="creationDate">
              <el-date-picker
                v-model="form.creationDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="创建日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="生产厂家" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="生产厂家" />
            </el-form-item>
          </el-col>
        </el-row>
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
  display: flex;
  gap: 12px;
}
</style>
