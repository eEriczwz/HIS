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
        path: 'triage',
        name: 'OutpatientTriage',
        component: () => import('@/views/outpatient/triage.vue'),
        meta: { title: '分诊台' },
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
    path: '/inpatient',
    component: Layout,
    redirect: '/inpatient/admission',
    meta: { title: '住院管理', icon: 'Suitcase' },
    children: [
      {
        path: 'admission',
        name: 'InpatientAdmission',
        component: () => import('@/views/inpatient/admission.vue'),
        meta: { title: '入院登记' },
      },
      {
        path: 'orders',
        name: 'InpatientOrders',
        component: () => import('@/views/inpatient/orders.vue'),
        meta: { title: '医嘱管理' },
      },
      {
        path: 'ward',
        name: 'InpatientWard',
        component: () => import('@/views/inpatient/wrad.vue'),
        meta: { title: '病房管理' },
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
    path: '/patient',
    component: Layout,
    redirect: '/patient/list',
    meta: { title: '患者管理', icon: 'User' },
    children: [
      {
        path: 'list',
        name: 'PatientList',
        component: () => import('@/views/patient/list.vue'),
        meta: { title: '患者列表' },
      },
      {
        path: 'register',
        name: 'PatientRegister',
        component: () => import('@/views/patient/register.vue'),
        meta: { title: '患者登记' },
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
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        name: 'SystemUser',
        component: () => import('@/views/system/user.vue'),
        meta: { title: '用户管理' },
      },
      {
        path: 'role',
        name: 'SystemRole',
        component: () => import('@/views/system/role.vue'),
        meta: { title: '角色管理' },
      },
      {
        path: 'menu',
        name: 'SystemMenu',
        component: () => import('@/views/system/menu.vue'),
        meta: { title: '菜单管理' },
      },
      {
        path: 'dict',
        name: 'SystemDict',
        component: () => import('@/views/system/dict.vue'),
        meta: { title: '字典管理' },
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
