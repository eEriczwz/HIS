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
      // 前端请求 /api/** 会代理到后端 Spring Boot 服务
      '/api': {
        target: 'http://localhost:8092',
        changeOrigin: true,
        // 后端接口本身不带 /api 前缀，这里把 /api 剥掉再转发
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
