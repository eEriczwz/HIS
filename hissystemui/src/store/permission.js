import { defineStore } from 'pinia'
import { constantRoutes } from '@/router/routes'

// 权限/菜单 store：当前用静态路由，后续可在此接入后端动态路由
export const usePermissionStore = defineStore('permission', {
  state: () => ({
    routes: [],
    isLoaded: false,
  }),
  actions: {
    generateRoutes() {
      this.routes = constantRoutes
      this.isLoaded = true
      return constantRoutes
    },
  },
})
