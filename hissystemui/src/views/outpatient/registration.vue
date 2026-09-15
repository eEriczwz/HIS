<script setup>
import { ref, reactive, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import {
  getRegisterList,
  addRegister,
  refundRegister,
  getDeptList,
  getDoctorList,
  getRegistLevelList,
  getSettleCategoryList,
} from '@/api/registration'
import { formatDate, formatMoney } from '@/utils/format'

// 状态字典（前端写死）
const visitStateMap = {
  1: { label: '已挂号', type: 'primary' },
  2: { label: '医生接诊', type: 'warning' },
  3: { label: '看诊结束', type: 'success' },
  4: { label: '已退号', type: 'info' },
}

const genderOptions = ['男', '女']
const ageTypeOptions = ['年', '天']
const noonOptions = ['上午', '下午']
const isBookOptions = ['要', '否']
const registMethodOptions = ['现金', '银行卡', '微信', '医保卡', '支付宝']

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 搜索条件
const query = reactive({
  caseNumber: '',
  realName: '',
  visitState: '',
  visitDate: '',
  deptmentId: '',
})

// 下拉数据源
const deptOptions = ref([])
const doctorOptions = ref([])
const registLevelOptions = ref([])
const settleCategoryOptions = ref([])

const dialogVisible = ref(false)
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  caseNumber: '',
  realName: '',
  cardNumber: '',
  gender: '',
  birthday: '',
  age: null,
  ageType: '年',
  homeAddress: '',
  visitDate: '',
  noon: '上午',
  deptmentId: null,
  employeeId: null,
  registLevelId: null,
  settleCategoryId: null,
  isBook: '要',
  registMethod: '',
  registMoney: null,
})

const rules = {
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  cardNumber: [
    { pattern: /^\d{17}[\dXx]$/, message: '身份证号格式不正确', trigger: 'blur' },
  ],
  visitDate: [{ required: true, message: '请选择看诊日期', trigger: 'change' }],
  noon: [{ required: true, message: '请选择午别', trigger: 'change' }],
  deptmentId: [{ required: true, message: '请选择挂号科室', trigger: 'change' }],
  employeeId: [{ required: true, message: '请选择挂号医生', trigger: 'change' }],
  registLevelId: [{ required: true, message: '请选择号别', trigger: 'change' }],
  settleCategoryId: [{ required: true, message: '请选择结算类别', trigger: 'change' }],
  isBook: [{ required: true, message: '请选择病历本', trigger: 'change' }],
  registMethod: [{ required: true, message: '请选择收费方式', trigger: 'change' }],
}

// 组装列表查询参数，空值不传
function buildParams() {
  const p = { pageNum: pageNum.value, pageSize: pageSize.value }
  if (query.caseNumber) p.caseNumber = query.caseNumber
  if (query.realName) p.realName = query.realName
  if (query.visitState !== '' && query.visitState != null) p.visitState = query.visitState
  if (query.visitDate) p.visitDate = query.visitDate
  if (query.deptmentId !== '' && query.deptmentId != null) p.deptmentId = query.deptmentId
  return p
}

async function fetchList() {
  loading.value = true
  try {
    const res = await getRegisterList(buildParams())
    const data = res?.data || {}
    list.value = data.list || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageNum.value = 1
  fetchList()
}

function handleReset() {
  Object.assign(query, { caseNumber: '', realName: '', visitState: '', visitDate: '', deptmentId: '' })
  pageNum.value = 1
  fetchList()
}

// 计算年龄（按出生日期，周岁）
function calcAge(birthday) {
  if (!birthday) return null
  const birth = new Date(birthday)
  if (Number.isNaN(birth.getTime())) return null
  const now = new Date()
  let age = now.getFullYear() - birth.getFullYear()
  const m = now.getMonth() - birth.getMonth()
  if (m < 0 || (m === 0 && now.getDate() < birth.getDate())) age--
  return age < 0 ? 0 : age
}

// 身份证解析：出生日期（第 7-14 位）、性别（第 17 位奇偶）
function handleCardBlur() {
  const val = form.cardNumber || ''
  if (!/^\d{17}[\dXx]$/.test(val)) return
  const year = val.slice(6, 10)
  const month = val.slice(10, 12)
  const day = val.slice(12, 14)
  form.birthday = `${year}-${month}-${day}`
  form.gender = Number(val.charAt(16)) % 2 === 1 ? '男' : '女'
  form.age = calcAge(form.birthday)
  form.ageType = '年'
}

// 手动改出生日期时重算年龄
function handleBirthdayChange() {
  form.age = calcAge(form.birthday)
  form.ageType = '年'
}

// 选号别 → 自动带出挂号费
function handleLevelChange(levelId) {
  const level = registLevelOptions.value.find((l) => l.id === levelId)
  form.registMoney = level ? level.registFee : null
}

// 选科室 → 拉取该科室医生
async function handleDeptChange(deptmentId) {
  doctorOptions.value = []
  form.employeeId = null
  if (!deptmentId) return
  const res = await getDoctorList(deptmentId)
  doctorOptions.value = res?.data || []
}

async function fetchDeptOptions() {
  const res = await getDeptList()
  deptOptions.value = (res?.data || []).filter((d) => d.deptType === '门诊')
}

async function fetchRegistLevelOptions() {
  const res = await getRegistLevelList()
  registLevelOptions.value = res?.data || []
}

async function fetchSettleCategoryOptions() {
  const res = await getSettleCategoryList()
  settleCategoryOptions.value = res?.data || []
}

function openAdd() {
  Object.assign(form, {
    caseNumber: '',
    realName: '',
    cardNumber: '',
    gender: '',
    birthday: '',
    age: null,
    ageType: '年',
    homeAddress: '',
    visitDate: formatDate(new Date(), 'YYYY-MM-DD'),
    noon: '上午',
    deptmentId: null,
    employeeId: null,
    registLevelId: null,
    settleCategoryId: null,
    isBook: '要',
    registMethod: '',
    registMoney: null,
  })
  doctorOptions.value = []
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    await addRegister({
      caseNumber: form.caseNumber,
      realName: form.realName,
      cardNumber: form.cardNumber,
      gender: form.gender,
      birthday: form.birthday,
      age: form.age,
      ageType: form.ageType,
      homeAddress: form.homeAddress,
      visitDate: form.visitDate,
      noon: form.noon,
      deptmentId: form.deptmentId,
      employeeId: form.employeeId,
      registLevelId: form.registLevelId,
      settleCategoryId: form.settleCategoryId,
      isBook: form.isBook,
      registMethod: form.registMethod,
      registMoney: form.registMoney,
      visitState: 1,
    })
    ElMessage.success('挂号成功')
    dialogVisible.value = false
    fetchList()
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    submitting.value = false
  }
}

async function handleRefund(row) {
  await ElMessageBox.confirm(
    `确定对该挂号记录退号吗？将退回挂号费 ¥${formatMoney(row.registMoney)}`,
    '退号确认',
    { confirmButtonText: '确定退号', cancelButtonText: '取消', type: 'warning' },
  )
  await refundRegister(row.id)
  ElMessage.success('退号成功')
  fetchList()
}

onMounted(() => {
  fetchList()
  fetchDeptOptions()
  fetchRegistLevelOptions()
  fetchSettleCategoryOptions()
})
</script>

<template>
  <div class="app-container">
    <el-card>
      <div class="toolbar">
        <el-form :inline="true" :model="query" class="search-form">
          <el-form-item label="病历号">
            <el-input v-model="query.caseNumber" placeholder="病历号" clearable />
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="query.realName" placeholder="姓名" clearable />
          </el-form-item>
          <el-form-item label="看诊状态">
            <el-select v-model="query.visitState" placeholder="全部" clearable class="w120">
              <el-option
                v-for="(item, key) in visitStateMap"
                :key="key"
                :label="item.label"
                :value="Number(key)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="看诊日期">
            <el-date-picker
              v-model="query.visitDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="看诊日期"
              clearable
            />
          </el-form-item>
          <el-form-item label="科室">
            <el-select v-model="query.deptmentId" placeholder="全部" clearable filterable class="w160">
              <el-option v-for="d in deptOptions" :key="d.id" :label="d.deptName" :value="d.id" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>

        <div class="toolbar-actions">
          <el-button type="primary" :icon="Plus" @click="openAdd">挂号登记</el-button>
          <el-button :icon="Refresh" @click="fetchList">刷新</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="list" border stripe>
        <el-table-column prop="caseNumber" label="病历号" width="110" />
        <el-table-column prop="realName" label="姓名" width="90" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column label="年龄" width="80">
          <template #default="{ row }">
            <span v-if="row.age != null">{{ row.age }} {{ row.ageType }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="deptName" label="科室" min-width="130" />
        <el-table-column prop="doctorName" label="医生" width="90" />
        <el-table-column prop="registLevelName" label="号别" width="90" />
        <el-table-column label="看诊时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.visitDate, 'YYYY-MM-DD HH:mm') }} {{ row.noon }}
          </template>
        </el-table-column>
        <el-table-column label="挂号费" width="90" align="right">
          <template #default="{ row }">{{ formatMoney(row.registMoney) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="visitStateMap[row.visitState]?.type || 'info'">
              {{ visitStateMap[row.visitState]?.label || row.visitState }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.visitState === 1" link type="danger" @click="handleRefund(row)">
              退号
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchList"
          @current-change="fetchList"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      title="挂号登记"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <div class="form-section">患者信息</div>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="病历号" prop="caseNumber">
              <el-input v-model="form.caseNumber" placeholder="留空自动生成" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="form.realName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="身份证号" prop="cardNumber">
              <el-input
                v-model="form.cardNumber"
                placeholder="18 位，可自动解析"
                maxlength="18"
                @blur="handleCardBlur"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio v-for="g in genderOptions" :key="g" :value="g">{{ g }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker
                v-model="form.birthday"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="出生日期"
                style="width: 100%"
                @change="handleBirthdayChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年龄" prop="age">
              <el-input-number
                v-model="form.age"
                :min="0"
                :max="150"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年龄类型" prop="ageType">
              <el-select v-model="form.ageType" style="width: 100%">
                <el-option v-for="t in ageTypeOptions" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="家庭住址" prop="homeAddress">
              <el-input v-model="form.homeAddress" placeholder="家庭住址" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section">看诊信息</div>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="看诊日期" prop="visitDate">
              <el-date-picker
                v-model="form.visitDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="看诊日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="午别" prop="noon">
              <el-radio-group v-model="form.noon">
                <el-radio v-for="n in noonOptions" :key="n" :value="n">{{ n }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="挂号科室" prop="deptmentId">
              <el-select
                v-model="form.deptmentId"
                placeholder="请选择科室"
                filterable
                style="width: 100%"
                @change="handleDeptChange"
              >
                <el-option v-for="d in deptOptions" :key="d.id" :label="d.deptName" :value="d.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="挂号医生" prop="employeeId">
              <el-select
                v-model="form.employeeId"
                placeholder="请选择医生"
                filterable
                style="width: 100%"
                :disabled="!form.deptmentId"
              >
                <el-option
                  v-for="d in doctorOptions"
                  :key="d.id"
                  :label="d.realname"
                  :value="d.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="号别" prop="registLevelId">
              <el-select
                v-model="form.registLevelId"
                placeholder="请选择号别"
                style="width: 100%"
                @change="handleLevelChange"
              >
                <el-option
                  v-for="l in registLevelOptions"
                  :key="l.id"
                  :label="l.registName"
                  :value="l.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section">收费信息</div>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="结算类别" prop="settleCategoryId">
              <el-select v-model="form.settleCategoryId" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="s in settleCategoryOptions"
                  :key="s.id"
                  :label="s.settleName"
                  :value="s.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="病历本" prop="isBook">
              <el-radio-group v-model="form.isBook">
                <el-radio v-for="b in isBookOptions" :key="b" :value="b">{{ b }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="收费方式" prop="registMethod">
              <el-select v-model="form.registMethod" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="m in registMethodOptions"
                  :key="m"
                  :label="m"
                  :value="m"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="挂号费" prop="registMoney">
              <el-input :model-value="formatMoney(form.registMoney)" readonly />
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

.w120 {
  width: 120px;
}

.w160 {
  width: 160px;
}

.form-section {
  margin: 4px 0 12px;
  padding-left: 8px;
  border-left: 3px solid var(--el-color-primary);
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
