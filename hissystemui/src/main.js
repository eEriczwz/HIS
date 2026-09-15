import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import 'element-plus/dist/index.css'
import './assets/main.css'
import './styles/index.scss'

import App from './App.vue'
import router from './router'
import store from './store'

const app = createApp(App)

// 全局注册 element-plus 图标组件，方便在菜单等处按名字使用
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus)
app.use(store)
app.use(router)

app.mount('#app')
