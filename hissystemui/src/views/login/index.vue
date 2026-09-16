<script setup>
import { ref, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  realname: '',
  password: '',
})

const rules = {
  realname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  await formRef.value.validate()
  loading.value = true
  try {
    await userStore.login({ ...form })
    ElMessage.success('登录成功')
    // 医生登录后直接进入医生工作站
    const redirect = route.query.redirect
    router.push(redirect ? String(redirect) : '/outpatient/doctor')
  } catch (e) {
    // 网络/接口错误已在 request.js 统一提示；这里兜底 store 层抛出的异常
    if (e?.message && e.message !== 'Error') {
      ElMessage.error(e.message)
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-title">长沙市中心人民医院信息管理系统</div>
      <div class="login-subtitle">Hospital Information System</div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        size="large"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="realname">
          <el-input v-model="form.realname" placeholder="姓名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            show-password
            :prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-tip">请输入医生姓名及密码登录</div>
    </el-card>
  </div>
</template>

<style scoped>
.login-container {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: url('/csszxrmyy.png') no-repeat center center / cover;
}

.login-card {
  width: 380px;
  padding: 8px 12px;
  border-radius: 8px;
}

.login-title {
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.login-subtitle {
  text-align: center;
  font-size: 12px;
  color: #909399;
  margin: 4px 0 24px;
}

.login-btn {
  width: 100%;
}

.login-tip {
  text-align: center;
  font-size: 12px;
  color: #c0c4cc;
}
</style>
