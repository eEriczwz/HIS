<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Plus } from '@element-plus/icons-vue'
import { getEmployeeList } from '@/api/employee'
import { getDeptList } from '@/api/department'
import { getDrugList } from '@/api/pharmacy'
import {
  getWaitPatients,
  receivePatient,
  saveMedicalRecord,
  getDiseases,
  getMedicalTechnologies,
  addCheckRequest,
  addDisposalRequest,
  addPrescription,
  getVisitDetail,
} from '@/api/outpatient'
import { formatDate, formatMoney } from '@/utils/format'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

const visitStateMap = {
  1: { label: '已挂号', type: 'primary' },
  2: { label: '医生接诊', type: 'warning' },
  3: { label: '看诊结束', type: 'success' },
  4: { label: '已退号', type: 'info' },
}

const checkTechTypes = ['检查', '检验']
const disposalTechTypes = ['处置']

// ===== 医生 / 科室 =====
const doctorId = ref(null)
const doctorList = ref([])
const deptList = ref([])
const deptNameMap = computed(() => Object.fromEntries(deptList.value.map((d) => [d.id, d.deptName])))

// ===== 待接诊列表 =====
const waitList = ref([])
const waitLoading = ref(false)

// ===== 当前接诊患者 =====
const currentRegister = ref(null)
const activeTab = ref('record')
const detail = ref(null) // MedicalRecordVO
const detailLoading = ref(false)

// ===== 病历 =====
const recordForm = reactive({
  readme: '',
  present: '',
  presentTreat: '',
  history: '',
  allergy: '',
  physique: '',
  proposal: '',
  careful: '',
  diagnosis: '',
  cure: '',
})
const recordSaving = ref(false)
const selectedDiseaseIds = ref([])
const diseaseOptions = ref([])
const diseaseLoading = ref(false)
const recordSaved = computed(() => !!detail.value?.medicalRecord)

// ===== 检查申请 =====
const checkDialog = ref(false)
const checkSaving = ref(false)
const checkForm = reactive({
  techType: '检查',
  medicalTechnologyId: null,
  checkInfo: '',
  checkPosition: '',
  checkRemark: '',
})
const checkTechOptions = ref([])
const checkTechLoading = ref(false)

// ===== 处置申请 =====
const disposalDialog = ref(false)
const disposalSaving = ref(false)
const disposalForm = reactive({
  medicalTechnologyId: null,
  disposalInfo: '',
  disposalPosition: '',
  disposalRemark: '',
})
const disposalTechOptions = ref([])
const disposalTechLoading = ref(false)

// ===== 处方 =====
const prescriptionDialog = ref(false)
const prescriptionSaving = ref(false)
const prescriptionForm = reactive({
  drugId: null,
  drugUsage: '',
  drugNumber: 1,
})
const drugOptions = ref([])
const drugLoading = ref(false)

// ===================== 初始化 =====================
async function fetchDoctors() {
  try {
    const res = await getEmployeeList()
    doctorList.value = res?.data || []
  } catch (e) {
    doctorList.value = []
  }
}

async function fetchDept() {
  try {
    const res = await getDeptList()
    deptList.value = res?.data || []
  } catch (e) {
    deptList.value = []
  }
}

async function fetchWaitList() {
  if (!doctorId.value) {
    waitList.value = []
    return
  }
  waitLoading.value = true
  try {
    const res = await getWaitPatients(doctorId.value)
    waitList.value = res?.data || []
  } catch (e) {
    waitList.value = []
  } finally {
    waitLoading.value = false
  }
}

function handleDoctorChange() {
  currentRegister.value = null
  detail.value = null
  fetchWaitList()
}

// ===================== 接诊 =====================
async function handleReceive(row) {
  await ElMessageBox.confirm(`确认接诊患者「${row.realName}」？`, '接诊确认', {
    confirmButtonText: '接诊',
    cancelButtonText: '取消',
    type: 'info',
  })
  await receivePatient(row.id)
  ElMessage.success('接诊成功，可以书写病历')
  currentRegister.value = { ...row, visitState: 2 }
  activeTab.value = 'record'
  await fetchWaitList()
  await loadDetail(row.id)
}

// ===================== 加载就诊全套资料 =====================
async function loadDetail(registerId) {
  detailLoading.value = true
  try {
    const res = await getVisitDetail(registerId)
    detail.value = res?.data || null
    applyDetailToForm()
  } finally {
    detailLoading.value = false
  }
}

function applyDetailToForm() {
  const d = detail.value
  // 病历回填
  const r = d?.medicalRecord
  if (r) {
    Object.assign(recordForm, {
      readme: r.readme || '',
      present: r.present || '',
      presentTreat: r.presentTreat || '',
      history: r.history || '',
      allergy: r.allergy || '',
      physique: r.physique || '',
      proposal: r.proposal || '',
      careful: r.careful || '',
      diagnosis: r.diagnosis || '',
      cure: r.cure || '',
    })
  } else {
    resetRecordForm()
  }
  // 已绑定疾病回填
  selectedDiseaseIds.value = (d?.diseaseList || []).map((x) => x.id)
  diseaseOptions.value = d?.diseaseList || []
}

function resetRecordForm() {
  Object.assign(recordForm, {
    readme: '',
    present: '',
    presentTreat: '',
    history: '',
    allergy: '',
    physique: '',
    proposal: '',
    careful: '',
    diagnosis: '',
    cure: '',
  })
  selectedDiseaseIds.value = []
  diseaseOptions.value = []
}

// ===================== 病历保存 =====================
async function searchDiseases(keyword) {
  if (!keyword) return
  diseaseLoading.value = true
  try {
    const res = await getDiseases(keyword)
    const list = res?.data || []
    const map = new Map(diseaseOptions.value.map((d) => [d.id, d]))
    list.forEach((d) => map.set(d.id, d))
    diseaseOptions.value = Array.from(map.values())
  } finally {
    diseaseLoading.value = false
  }
}

async function handleSaveRecord() {
  if (!currentRegister.value) return
  recordSaving.value = true
  try {
    await saveMedicalRecord({
      registerId: currentRegister.value.id,
      diseaseIdList: selectedDiseaseIds.value,
      record: { ...recordForm },
    })
    ElMessage.success('病历保存成功')
    await loadDetail(currentRegister.value.id)
  } finally {
    recordSaving.value = false
  }
}

// ===================== 检查申请 =====================
async function searchCheckTech(keyword) {
  checkTechLoading.value = true
  try {
    const res = await getMedicalTechnologies({ techType: checkForm.techType, keyword })
    checkTechOptions.value = res?.data || []
  } finally {
    checkTechLoading.value = false
  }
}

function openCheckDialog() {
  Object.assign(checkForm, {
    techType: '检查',
    medicalTechnologyId: null,
    checkInfo: '',
    checkPosition: '',
    checkRemark: '',
  })
  checkDialog.value = true
  searchCheckTech('')
}

async function handleAddCheck() {
  checkSaving.value = true
  try {
    await addCheckRequest({
      registerId: currentRegister.value.id,
      medicalTechnologyId: checkForm.medicalTechnologyId,
      checkInfo: checkForm.checkInfo,
      checkPosition: checkForm.checkPosition,
      checkRemark: checkForm.checkRemark,
    })
    ElMessage.success('检查申请单开立成功')
    checkDialog.value = false
    await loadDetail(currentRegister.value.id)
  } finally {
    checkSaving.value = false
  }
}

// ===================== 处置申请 =====================
async function searchDisposalTech(keyword) {
  disposalTechLoading.value = true
  try {
    const res = await getMedicalTechnologies({ techType: '处置', keyword })
    disposalTechOptions.value = res?.data || []
  } finally {
    disposalTechLoading.value = false
  }
}

function openDisposalDialog() {
  Object.assign(disposalForm, {
    medicalTechnologyId: null,
    disposalInfo: '',
    disposalPosition: '',
    disposalRemark: '',
  })
  disposalDialog.value = true
  searchDisposalTech('')
}

async function handleAddDisposal() {
  disposalSaving.value = true
  try {
    await addDisposalRequest({
      registerId: currentRegister.value.id,
      medicalTechnologyId: disposalForm.medicalTechnologyId,
      disposalInfo: disposalForm.disposalInfo,
      disposalPosition: disposalForm.disposalPosition,
      disposalRemark: disposalForm.disposalRemark,
    })
    ElMessage.success('处置申请单开立成功')
    disposalDialog.value = false
    await loadDetail(currentRegister.value.id)
  } finally {
    disposalSaving.value = false
  }
}

// ===================== 处方 =====================
async function searchDrugs(keyword) {
  drugLoading.value = true
  try {
    const res = await getDrugList({ drugName: keyword })
    drugOptions.value = res?.data || []
  } finally {
    drugLoading.value = false
  }
}

function openPrescriptionDialog() {
  Object.assign(prescriptionForm, { drugId: null, drugUsage: '', drugNumber: 1 })
  prescriptionDialog.value = true
  searchDrugs('')
}

async function handleAddPrescription() {
  prescriptionSaving.value = true
  try {
    await addPrescription({
      registerId: currentRegister.value.id,
      drugId: prescriptionForm.drugId,
      drugUsage: prescriptionForm.drugUsage,
      drugNumber: prescriptionForm.drugNumber,
    })
    ElMessage.success('处方开立成功，药房可查看')
    prescriptionDialog.value = false
    await loadDetail(currentRegister.value.id)
  } catch (e) {
    // 错误提示已由 request.js 统一处理（如：库存不足），保持弹窗打开
  } finally {
    prescriptionSaving.value = false
  }
}

// 病历字段展示（就诊资料 tab 用）
const recordFields = computed(() => {
  const m = detail.value?.medicalRecord
  if (!m) return []
  return [
    { label: '主诉', value: m.readme },
    { label: '现病史', value: m.present },
    { label: '现病治疗情况', value: m.presentTreat },
    { label: '既往史', value: m.history },
    { label: '过敏史', value: m.allergy },
    { label: '体格检查', value: m.physique },
    { label: '检查/检验建议', value: m.proposal },
    { label: '注意事项', value: m.careful },
    { label: '诊断结果', value: m.diagnosis },
    { label: '处理意见', value: m.cure },
  ].filter((f) => f.value)
})

onMounted(async () => {
  await fetchDoctors()
  await fetchDept()
  // 默认选中当前登录医生
  const loggedId = userStore.userInfo?.id
  if (loggedId != null && doctorList.value.some((d) => d.id === loggedId)) {
    doctorId.value = loggedId
  } else if (doctorList.value.length) {
    doctorId.value = doctorList.value[0].id
  }
  await fetchWaitList()
})
</script>

<template>
  <div class="app-container">
    <!-- 顶部：当前医生选择 -->
    <el-card class="doctor-bar" shadow="never">
      <div class="doctor-bar-inner">
        <div class="doctor-select">
          <span class="label">当前医生：</span>
          <el-select
            v-model="doctorId"
            filterable
            placeholder="请选择医生"
            style="width: 220px"
            @change="handleDoctorChange"
          >
            <el-option v-for="e in doctorList" :key="e.id" :label="e.realname" :value="e.id" />
          </el-select>
        </div>
        <el-button type="primary" :icon="Refresh" @click="fetchWaitList">刷新待接诊</el-button>
      </div>
    </el-card>

    <div class="workspace">
      <!-- 左：待接诊患者 -->
      <el-card class="wait-panel" shadow="never">
        <template #header>
          <span class="panel-title">待接诊患者</span>
        </template>
        <el-table v-loading="waitLoading" :data="waitList" border stripe size="small" height="calc(100vh - 260px)">
          <el-table-column label="患者" min-width="110">
            <template #default="{ row }">
              <div class="patient-name">{{ row.realName }}</div>
              <div class="patient-sub">{{ row.gender }} · {{ row.age != null ? row.age + row.ageType : '-' }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="caseNumber" label="病历号" width="90" />
          <el-table-column label="科室" min-width="100">
            <template #default="{ row }">{{ deptNameMap[row.deptmentId] || row.deptmentId }}</template>
          </el-table-column>
          <el-table-column label="看诊" width="130">
            <template #default="{ row }">
              {{ formatDate(row.visitDate, 'MM-DD') }} {{ row.noon }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="70" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" @click="handleReceive(row)">接诊</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 右：接诊工作区 -->
      <el-card class="work-panel" shadow="never" v-loading="detailLoading">
        <template #header>
          <span class="panel-title">接诊工作区</span>
        </template>

        <el-empty v-if="!currentRegister" description="请先在左侧选择待接诊患者并接诊" />

        <template v-else>
          <div class="patient-bar">
            <span class="patient-name">{{ currentRegister.realName }}</span>
            <el-tag size="small" type="info">{{ currentRegister.gender }}</el-tag>
            <el-tag size="small" type="info">{{ currentRegister.age != null ? currentRegister.age + currentRegister.ageType : '-' }}</el-tag>
            <el-tag size="small">病历号 {{ currentRegister.caseNumber }}</el-tag>
            <el-tag size="small">{{ deptNameMap[currentRegister.deptmentId] || '' }}</el-tag>
            <el-tag size="small" :type="visitStateMap[currentRegister.visitState]?.type">
              {{ visitStateMap[currentRegister.visitState]?.label }}
            </el-tag>
          </div>

          <el-tabs v-model="activeTab">
            <!-- 病历 -->
            <el-tab-pane label="病历" name="record">
              <el-alert
                v-if="recordSaved"
                title="该挂号单已保存病历，不可重复新增"
                type="info"
                :closable="false"
                class="mb12"
              />
              <el-form :model="recordForm" label-width="110px">
                <el-row :gutter="16">
                  <el-col :span="12">
                    <el-form-item label="主诉">
                      <el-input v-model="recordForm.readme" type="textarea" :rows="2" placeholder="主诉" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="诊断结果">
                      <el-input v-model="recordForm.diagnosis" type="textarea" :rows="2" placeholder="诊断结果" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="现病史">
                      <el-input v-model="recordForm.present" type="textarea" :rows="2" placeholder="现病史" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="现病治疗情况">
                      <el-input v-model="recordForm.presentTreat" type="textarea" :rows="2" placeholder="现病治疗情况" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="既往史">
                      <el-input v-model="recordForm.history" type="textarea" :rows="2" placeholder="既往史" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="过敏史">
                      <el-input v-model="recordForm.allergy" type="textarea" :rows="2" placeholder="过敏史" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="体格检查">
                      <el-input v-model="recordForm.physique" type="textarea" :rows="2" placeholder="体格检查" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="检查/检验建议">
                      <el-input v-model="recordForm.proposal" type="textarea" :rows="2" placeholder="检查/检验建议" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="注意事项">
                      <el-input v-model="recordForm.careful" type="textarea" :rows="2" placeholder="注意事项" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="处理意见">
                      <el-input v-model="recordForm.cure" type="textarea" :rows="2" placeholder="处理意见" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="诊断疾病">
                      <el-select
                        v-model="selectedDiseaseIds"
                        multiple
                        filterable
                        remote
                        reserve-keyword
                        :remote-method="searchDiseases"
                        :loading="diseaseLoading"
                        placeholder="输入疾病名称/编码/ICD 搜索，可多选"
                        style="width: 100%"
                      >
                        <el-option
                          v-for="d in diseaseOptions"
                          :key="d.id"
                          :label="`${d.diseaseName}（${d.diseaseICD || d.diseaseCode || ''}）`"
                          :value="d.id"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
              <div class="tab-footer">
                <el-button
                  type="primary"
                  :loading="recordSaving"
                  :disabled="recordSaved"
                  @click="handleSaveRecord"
                >
                  保存病历
                </el-button>
              </div>
            </el-tab-pane>

            <!-- 检查申请 -->
            <el-tab-pane label="检查申请" name="check">
              <div class="tab-toolbar">
                <el-button type="primary" :icon="Plus" @click="openCheckDialog">开立检查申请单</el-button>
              </div>
              <el-table :data="detail?.checkList || []" border stripe size="small">
                <el-table-column prop="techName" label="检查项目" min-width="180" />
                <el-table-column prop="checkInfo" label="检查信息" min-width="160" />
                <el-table-column prop="checkPosition" label="检查部位" width="110" />
                <el-table-column label="金额" width="90" align="right">
                  <template #default="{ row }">
                    {{ row.techPrice != null ? formatMoney(row.techPrice) : '-' }}
                  </template>
                </el-table-column>
                <el-table-column prop="checkState" label="状态" width="100" />
                <el-table-column label="开单时间" width="160">
                  <template #default="{ row }">{{ formatDate(row.creationTime) }}</template>
                </el-table-column>
                <el-table-column prop="checkRemark" label="备注" min-width="120" />
              </el-table>
            </el-tab-pane>

            <!-- 处置申请 -->
            <el-tab-pane label="处置申请" name="disposal">
              <div class="tab-toolbar">
                <el-button type="primary" :icon="Plus" @click="openDisposalDialog">开立处置申请单</el-button>
              </div>
              <el-table :data="detail?.disposalList || []" border stripe size="small">
                <el-table-column prop="techName" label="处置项目" min-width="180" />
                <el-table-column prop="disposalInfo" label="处置信息" min-width="160" />
                <el-table-column prop="disposalPosition" label="处置部位" width="110" />
                <el-table-column prop="disposalState" label="状态" width="100" />
                <el-table-column label="开单时间" width="160">
                  <template #default="{ row }">{{ formatDate(row.creationTime) }}</template>
                </el-table-column>
                <el-table-column prop="disposalRemark" label="备注" min-width="120" />
              </el-table>
            </el-tab-pane>

            <!-- 处方 -->
            <el-tab-pane label="处方" name="prescription">
              <div class="tab-toolbar">
                <el-button type="primary" :icon="Plus" @click="openPrescriptionDialog">开立处方</el-button>
              </div>
              <el-table :data="detail?.prescriptionList || []" border stripe size="small">
                <el-table-column prop="drugName" label="药品名称" min-width="180" />
                <el-table-column prop="drugFormat" label="规格" min-width="130" />
                <el-table-column prop="drugUsage" label="用法用量" min-width="150" />
                <el-table-column label="数量" width="80" align="center">
                  <template #default="{ row }">{{ row.drugNumber }}</template>
                </el-table-column>
                <el-table-column label="单价" width="90" align="right">
                  <template #default="{ row }">
                    {{ row.drugPrice != null ? formatMoney(row.drugPrice) : '-' }}
                  </template>
                </el-table-column>
                <el-table-column prop="drugState" label="状态" width="100" />
                <el-table-column label="开立时间" width="160">
                  <template #default="{ row }">{{ formatDate(row.creationTime) }}</template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <!-- 就诊资料 -->
            <el-tab-pane label="就诊资料" name="detail">
              <el-descriptions title="挂号信息" :column="3" border size="small" class="mb12">
                <el-descriptions-item label="姓名">{{ detail?.register?.realName }}</el-descriptions-item>
                <el-descriptions-item label="性别">{{ detail?.register?.gender }}</el-descriptions-item>
                <el-descriptions-item label="年龄">
                  {{ detail?.register?.age != null ? detail?.register?.age + (detail?.register?.ageType || '') : '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="病历号">{{ detail?.register?.caseNumber }}</el-descriptions-item>
                <el-descriptions-item label="身份证号">{{ detail?.register?.cardNumber }}</el-descriptions-item>
                <el-descriptions-item label="家庭住址">{{ detail?.register?.homeAddress }}</el-descriptions-item>
                <el-descriptions-item label="看诊日期">
                  {{ formatDate(detail?.register?.visitDate) }} {{ detail?.register?.noon }}
                </el-descriptions-item>
                <el-descriptions-item label="挂号科室">
                  {{ deptNameMap[detail?.register?.deptmentId] || detail?.register?.deptmentId }}
                </el-descriptions-item>
                <el-descriptions-item label="挂号费">
                  {{ formatMoney(detail?.register?.registMoney) }}
                </el-descriptions-item>
              </el-descriptions>

              <el-descriptions title="病历" :column="1" border size="small" class="mb12">
                <template v-if="recordFields.length">
                  <el-descriptions-item v-for="f in recordFields" :key="f.label" :label="f.label">
                    {{ f.value }}
                  </el-descriptions-item>
                </template>
                <el-descriptions-item v-else label="病历">暂无病历</el-descriptions-item>
              </el-descriptions>

              <div class="block-title">诊断疾病</div>
              <div v-if="(detail?.diseaseList || []).length" class="mb12">
                <el-tag
                  v-for="d in detail.diseaseList"
                  :key="d.id"
                  class="mr6"
                  type="danger"
                >
                  {{ d.diseaseName }}
                </el-tag>
              </div>
              <el-empty v-else description="未绑定疾病" :image-size="40" />

              <div class="block-title">检查申请单</div>
              <el-table :data="detail?.checkList || []" border stripe size="small" class="mb12">
                <el-table-column prop="techName" label="检查项目" min-width="180" />
                <el-table-column prop="checkInfo" label="检查信息" min-width="160" />
                <el-table-column prop="checkPosition" label="部位" width="100" />
                <el-table-column prop="checkState" label="状态" width="100" />
                <el-table-column prop="checkResult" label="结果" min-width="140" />
              </el-table>

              <div class="block-title">处置申请单</div>
              <el-table :data="detail?.disposalList || []" border stripe size="small" class="mb12">
                <el-table-column prop="techName" label="处置项目" min-width="180" />
                <el-table-column prop="disposalInfo" label="处置信息" min-width="160" />
                <el-table-column prop="disposalState" label="状态" width="100" />
              </el-table>

              <div class="block-title">处方</div>
              <el-table :data="detail?.prescriptionList || []" border stripe size="small">
                <el-table-column prop="drugName" label="药品名称" min-width="180" />
                <el-table-column prop="drugFormat" label="规格" min-width="130" />
                <el-table-column prop="drugUsage" label="用法用量" min-width="150" />
                <el-table-column label="数量" width="80" align="center">
                  <template #default="{ row }">{{ row.drugNumber }}</template>
                </el-table-column>
                <el-table-column prop="drugState" label="状态" width="100" />
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </template>
      </el-card>
    </div>

    <!-- 检查申请对话框 -->
    <el-dialog v-model="checkDialog" title="开立检查申请单" width="520px" :close-on-click-modal="false">
      <el-form :model="checkForm" label-width="90px">
        <el-form-item label="项目类型">
          <el-radio-group v-model="checkForm.techType" @change="searchCheckTech('')">
            <el-radio v-for="t in checkTechTypes" :key="t" :value="t">{{ t }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="检查项目">
          <el-select
            v-model="checkForm.medicalTechnologyId"
            filterable
            remote
            reserve-keyword
            :remote-method="searchCheckTech"
            :loading="checkTechLoading"
            placeholder="搜索检查/检验项目"
            style="width: 100%"
          >
            <el-option
              v-for="t in checkTechOptions"
              :key="t.id"
              :label="`${t.techName}（¥${formatMoney(t.techPrice)}）`"
              :value="t.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="检查信息">
          <el-input v-model="checkForm.checkInfo" type="textarea" :rows="2" placeholder="检查信息/目的" />
        </el-form-item>
        <el-form-item label="检查部位">
          <el-input v-model="checkForm.checkPosition" placeholder="检查部位" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="checkForm.checkRemark" type="textarea" :rows="2" placeholder="备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="checkDialog = false">取消</el-button>
        <el-button type="primary" :loading="checkSaving" @click="handleAddCheck">开立</el-button>
      </template>
    </el-dialog>

    <!-- 处置申请对话框 -->
    <el-dialog v-model="disposalDialog" title="开立处置申请单" width="520px" :close-on-click-modal="false">
      <el-form :model="disposalForm" label-width="90px">
        <el-form-item label="处置项目">
          <el-select
            v-model="disposalForm.medicalTechnologyId"
            filterable
            remote
            reserve-keyword
            :remote-method="searchDisposalTech"
            :loading="disposalTechLoading"
            placeholder="搜索处置项目"
            style="width: 100%"
          >
            <el-option
              v-for="t in disposalTechOptions"
              :key="t.id"
              :label="`${t.techName}（¥${formatMoney(t.techPrice)}）`"
              :value="t.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="处置信息">
          <el-input v-model="disposalForm.disposalInfo" type="textarea" :rows="2" placeholder="处置信息" />
        </el-form-item>
        <el-form-item label="处置部位">
          <el-input v-model="disposalForm.disposalPosition" placeholder="处置部位" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="disposalForm.disposalRemark" type="textarea" :rows="2" placeholder="备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="disposalDialog = false">取消</el-button>
        <el-button type="primary" :loading="disposalSaving" @click="handleAddDisposal">开立</el-button>
      </template>
    </el-dialog>

    <!-- 处方对话框 -->
    <el-dialog v-model="prescriptionDialog" title="开立处方" width="520px" :close-on-click-modal="false">
      <el-form :model="prescriptionForm" label-width="90px">
        <el-form-item label="药品">
          <el-select
            v-model="prescriptionForm.drugId"
            filterable
            remote
            reserve-keyword
            :remote-method="searchDrugs"
            :loading="drugLoading"
            placeholder="搜索药品名称"
            style="width: 100%"
          >
            <el-option
              v-for="d in drugOptions"
              :key="d.id"
              :label="`${d.drugName}（${d.drugFormat || ''}）`"
              :value="d.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="用法用量">
          <el-input v-model="prescriptionForm.drugUsage" placeholder="如：一日三次，饭后服用" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="prescriptionForm.drugNumber" :min="1" :max="999" controls-position="right" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="prescriptionDialog = false">取消</el-button>
        <el-button type="primary" :loading="prescriptionSaving" @click="handleAddPrescription">开立</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.doctor-bar {
  margin-bottom: 12px;
}

.doctor-bar-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.doctor-select {
  display: flex;
  align-items: center;
}

.doctor-select .label {
  color: var(--el-text-color-primary);
  font-weight: 600;
}

.workspace {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.wait-panel {
  width: 420px;
  flex-shrink: 0;
}

.work-panel {
  flex: 1;
  min-width: 0;
}

.panel-title {
  font-weight: 600;
}

.patient-name {
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.patient-sub {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.patient-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  margin-bottom: 12px;
  background: var(--el-fill-color-light);
  border-radius: 6px;
}

.patient-bar .patient-name {
  font-size: 16px;
  margin-right: 4px;
}

.tab-toolbar {
  margin-bottom: 12px;
}

.tab-footer {
  margin-top: 12px;
  text-align: right;
}

.block-title {
  margin: 12px 0 8px;
  padding-left: 8px;
  border-left: 3px solid var(--el-color-primary);
  font-weight: 600;
}

.mb12 {
  margin-bottom: 12px;
}

.mr6 {
  margin-right: 6px;
}
</style>
