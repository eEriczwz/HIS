<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { constantRoutes } from '@/router/routes'

defineProps({
  collapsed: { type: Boolean, default: false },
})

const route = useRoute()

// 顶层菜单：带 children 的路由（隐藏的登录/404 等排除在外）
const menus = computed(() =>
  constantRoutes.filter((r) => !r.meta?.hidden && r.children?.length),
)

const activeMenu = computed(() => route.path)

function visibleChildren(menu) {
  return (menu.children || []).filter((c) => !c.meta?.hidden)
}

function resolvePath(base, path) {
  if (path.startsWith('/')) return path
  return `${base}/${path}`.replace(/\/+/g, '/')
}
</script>

<template>
  <div class="sidebar">
    <div class="sidebar-logo">
      <span v-if="!collapsed">长沙市中心人民信息管理系统</span>
      <span v-else>HIS</span>
    </div>
    <el-scrollbar class="sidebar-scroll">
      <el-menu
        :default-active="activeMenu"
        :collapse="collapsed"
        router
        unique-opened
        background-color="#001529"
        text-color="#a6adb4"
        active-text-color="#ffffff"
      >
        <template v-for="menu in menus" :key="menu.path">
          <!-- 只有一个子项：直接渲染为一级菜单项 -->
          <el-menu-item
            v-if="menu.meta?.title && visibleChildren(menu).length === 1"
            :index="resolvePath(menu.path, visibleChildren(menu)[0].path)"
          >
            <el-icon v-if="menu.meta?.icon"><component :is="menu.meta.icon" /></el-icon>
            <span>{{ menu.meta.title }}</span>
          </el-menu-item>
          <!-- 有标题且多个子项：渲染为子菜单 -->
          <el-sub-menu v-else-if="menu.meta?.title" :index="menu.path">
            <template #title>
              <el-icon v-if="menu.meta?.icon"><component :is="menu.meta.icon" /></el-icon>
              <span>{{ menu.meta.title }}</span>
            </template>
            <el-menu-item
              v-for="child in visibleChildren(menu)"
              :key="child.path"
              :index="resolvePath(menu.path, child.path)"
            >
              <el-icon v-if="child.meta?.icon"><component :is="child.meta.icon" /></el-icon>
              <span>{{ child.meta?.title }}</span>
            </el-menu-item>
          </el-sub-menu>
          <!-- 无标题的容器（如根路由 /）：直接把子项平铺为菜单项 -->
          <template v-else>
            <el-menu-item
              v-for="child in visibleChildren(menu)"
              :key="child.path"
              :index="resolvePath(menu.path, child.path)"
            >
              <el-icon v-if="child.meta?.icon"><component :is="child.meta.icon" /></el-icon>
              <span>{{ child.meta?.title }}</span>
            </el-menu-item>
          </template>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<style scoped>
.sidebar {
  height: 100%;
  background: #001529;
  display: flex;
  flex-direction: column;
}

.sidebar-logo {
  height: 56px;
  line-height: 56px;
  text-align: center;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  background: #002140;
}

.sidebar-scroll {
  flex: 1;
}

.sidebar :deep(.el-menu) {
  border-right: none;
}
</style>
