<script setup>
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

// 演示用的统计卡片数据，后续对接后端接口
const stats = [
  { label: '今日挂号', value: 128, icon: 'FirstAidKit', color: '#409eff' },
  { label: '在院患者', value: 86, icon: 'Suitcase', color: '#67c23a' },
  { label: '待发药品', value: 42, icon: 'Goods', color: '#e6a23c' },
  { label: '今日收费(元)', value: 25680, icon: 'Coin', color: '#f56c6c' },
]
</script>

<template>
  <div class="dashboard">
    <el-card class="welcome-card" shadow="never">
      <h2>你好，{{ userStore.userInfo?.name || '管理员' }} 👋</h2>
      <p>欢迎使用长沙市中心人民医院信息管理系统</p>
    </el-card>

    <el-row :gutter="16" class="stat-row">
      <el-col v-for="s in stats" :key="s.label" :xs="12" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-body">
            <div class="stat-icon" :style="{ backgroundColor: s.color }">
              <el-icon :size="24" color="#fff"><component :is="s.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ s.value }}</div>
              <div class="stat-label">{{ s.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.welcome-card {
  margin-bottom: 16px;
}

.welcome-card h2 {
  margin-bottom: 8px;
}

.welcome-card p {
  color: #909399;
}

.stat-body {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.stat-label {
  font-size: 13px;
  color: #909399;
}
</style>
