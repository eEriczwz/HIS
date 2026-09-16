import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5173,
    proxy: {
      // 按后端服务分流（各服务端口已错开，避免四服务同起撞端口）：
      //   挂号 his-registration :8093  药房 his-drugstore :8091
      //   员工 his-employee      :8094  门诊 his-outpatient  :8092
      // 注意：更具体的路径必须写在通用 '/api' 之前（vite 按顺序取首个匹配）
      '/api/register': {
        target: 'http://localhost:8093',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/api/drugstore': {
        target: 'http://localhost:8091',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/api/employee': {
        target: 'http://localhost:8094',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      // 门诊医生工作站 his-menzhen :8095（含 /outpatient/doctor/*）
      '/api/outpatient': {
        target: 'http://localhost:8095',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      // 收费管理 his-charge :8096（含 /charge/*）
      '/api/charge': {
        target: 'http://localhost:8096',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      // 兜底：门诊服务（/department、/test 等）
      '/api': {
        target: 'http://localhost:8092',
        changeOrigin: true,
        // 后端接口本身不带 /api 前缀，这里把 /api 剥掉再转发
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
