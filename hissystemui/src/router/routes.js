import Layout from '@/layout/index.vue'

// 静态路由表。meta.title 用于菜单/面包屑，meta.icon 用于菜单图标，
// meta.hidden 为 true 的路由不出现在侧边栏。
export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { hidden: true },
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台', icon: 'HomeFilled', affix: true },
      },
    ],
  },
  {
    path: '/outpatient',
    component: Layout,
    redirect: '/outpatient/registration',
    meta: { title: '门诊管理', icon: 'FirstAidKit' },
    children: [
      {
        path: 'registration',
        name: 'OutpatientRegistration',
        component: () => import('@/views/outpatient/registration.vue'),
        meta: { title: '挂号登记' },
      },
      {
        path: 'doctor',
        name: 'OutpatientDoctor',
        component: () => import('@/views/outpatient/doctor.vue'),
        meta: { title: '医生工作站' },
      },
    ],
  },
  {
    path: '/charge',
    component: Layout,
    redirect: '/charge/billing',
    meta: { title: '收费管理', icon: 'Coin' },
    children: [
      {
        path: 'billing',
        name: 'ChargeBilling',
        component: () => import('@/views/charge/billing.vue'),
        meta: { title: '收费' },
      },
      {
        path: 'refund',
        name: 'ChargeRefund',
        component: () => import('@/views/charge/refund.vue'),
        meta: { title: '退费' },
      },
    ],
  },
  {
    path: '/pharmacy',
    component: Layout,
    redirect: '/pharmacy/drug',
    meta: { title: '药房管理', icon: 'Goods' },
    children: [
      {
        path: 'drug',
        name: 'PharmacyDrug',
        component: () => import('@/views/pharmacy/drug.vue'),
        meta: { title: '药品管理' },
      },
      {
        path: 'stock',
        name: 'PharmacyStock',
        component: () => import('@/views/pharmacy/stock.vue'),
        meta: { title: '库存管理' },
      },
      {
        path: 'dispense',
        name: 'PharmacyDispense',
        component: () => import('@/views/pharmacy/dispense.vue'),
        meta: { title: '发药' },
      },
    ],
  },
  {
    path: '/department',
    component: Layout,
    redirect: '/department/index',
    meta: { title: '部门管理', icon: 'OfficeBuilding' },
    children: [
      {
        path: 'index',
        name: 'Department',
        component: () => import('@/views/department/index.vue'),
        meta: { title: '部门管理' },
      },
    ],
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/error/403.vue'),
    meta: { hidden: true },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { hidden: true },
  },
]
