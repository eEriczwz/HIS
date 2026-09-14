<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh } from '@element-plus/icons-vue'
import { getDeptList, addDept, deleteDept, updateDept } from '@/api/department'

const loading = ref(false)
const list = ref([])
const keyword = ref('')

const dialogVisible = ref(false)
const dialogTitle = ref('新增部门')
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  id: null,
  deptCode: '',
  deptName: '',
  deptType: '',
})

const deptTypeOptions = ['门诊', '住院', '医技', '行政', '后勤']

const rules = {
  deptCode: [{ required: true, message: '请输入部门编码', trigger: 'blur' }],
  deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
  deptType: [{ required: true, message: '请选择部门类型', trigger: 'change' }],
}

const filteredList = computed(() => {
  if (!keyword.value) return list.value
  const kw = keyword.value.trim().toLowerCase()
  return list.value.filter(
    (d) =>
      (d.deptCode && d.deptCode.toLowerCase().includes(kw)) ||
      (d.deptName && d.deptName.toLowerCase().includes(kw)),
  )
})

async function fetchList() {
  loading.value = true
  try {
    const res = await getDeptList()
    list.value = res?.data || []
  } finally {
    loading.value = false
  }
}

function openAdd() {
  dialogTitle.value = '新增部门'
  Object.assign(form, { id: null, deptCode: '', deptName: '', deptType: '' })
  dialogVisible.value = true
}

function openEdit(row) {
  dialogTitle.value = '编辑部门'
  Object.assign(form, {
    id: row.id,
    deptCode: row.deptCode,
    deptName: row.deptName,
    deptType: row.deptType,
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (form.id) {
      await updateDept({ ...form })
      ElMessage.success('修改成功')
    } else {
      await addDept({
        deptCode: form.deptCode,
        deptName: form.deptName,
        deptType: form.deptType,
      })
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
  await ElMessageBox.confirm(`确定删除部门「${row.deptName}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
  await deleteDept(row.id)
  ElMessage.success('删除成功')
  fetchList()
}

onMounted(fetchList)
</script>

<template>
  <div class="app-container">
    <el-card>
      <div class="toolbar">
        <el-input
          v-model="keyword"
          placeholder="按部门编码 / 名称搜索"
          clearable
          class="toolbar-search"
        />
        <el-button type="primary" :icon="Plus" @click="openAdd">新增部门</el-button>
        <el-button :icon="Refresh" @click="fetchList">刷新</el-button>
      </div>

      <el-table v-loading="loading" :data="filteredList" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="deptCode" label="部门编码" min-width="120" />
        <el-table-column prop="deptName" label="部门名称" min-width="160" />
        <el-table-column prop="deptType" label="部门类型" width="120" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="部门编码" prop="deptCode">
          <el-input v-model="form.deptCode" placeholder="如 TEST001" />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="如 心血管内科" />
        </el-form-item>
        <el-form-item label="部门类型" prop="deptType">
          <el-select v-model="form.deptType" placeholder="请选择或输入" filterable allow-create>
            <el-option v-for="t in deptTypeOptions" :key="t" :label="t" :value="t" />
          </el-select>
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
  gap: 12px;
  margin-bottom: 16px;
}

.toolbar-search {
  width: 240px;
}
</style>
