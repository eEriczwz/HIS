<script setup>
import { ref } from 'vue'
import Sidebar from './Sidebar.vue'
import Navbar from './Navbar.vue'

const collapsed = ref(false)
</script>

<template>
  <el-container class="layout">
    <el-aside :width="collapsed ? '64px' : '210px'" class="layout-aside">
      <Sidebar :collapsed="collapsed" />
    </el-aside>
    <el-container class="layout-body">
      <el-header class="layout-header" height="56px">
        <Navbar v-model:collapsed="collapsed" />
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  height: 100%;
}

.layout-aside {
  transition: width 0.2s;
  overflow: hidden;
}

.layout-body {
  display: flex;
  flex-direction: column;
}

.layout-header {
  padding: 0;
  border-bottom: 1px solid #e4e7ed;
  background: #fff;
}

.layout-main {
  background: #f0f2f5;
  overflow: auto;
}
</style>
