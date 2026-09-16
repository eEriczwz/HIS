# 长沙市中心人民医院信息管理系统（HIS）

一套前后端分离的医院信息管理系统，覆盖「挂号登记 → 门诊就诊 → 开检查/检验/处置/处方 → 发药扣库存 → 收费/退费」的门诊主诊疗流程。

---

## 一、技术栈

### 后端（HISHIS）
| 技术 | 版本 |
| --- | --- |
| Java | 17 |
| Spring Boot | 2.7.18 |
| MyBatis / MyBatis-Spring-Boot-Starter | 2.3.0 |
| MySQL 驱动 | mysql-connector-j（8.0.33） |
| 构建工具 | Maven（多模块） |

### 前端（hissystemui）
| 技术 | 版本 |
| --- | --- |
| Vue | 3.5.x（`<script setup>`） |
| Element Plus | 2.14.x |
| Vite | 8.x |
| Vue Router | 4.x |
| Pinia | 4.x |
| Axios | 1.20.x |
| Node | ^22.18.0 或 >=24.12.0 |

---

## 二、目录结构

```
HISsystem
├── HISHIS/                 # 后端（Maven 多模块）
│   ├── pom.xml             # 父工程 com.neuedu:hismodules
│   ├── his-common/         # 公共模块（Result 统一响应等）
│   ├── his-outpatient/     # 科室管理（8092）
│   ├── his-drugstore/      # 药房/库存/发药（8091）
│   ├── his-registration/   # 挂号登记（8093）
│   ├── his-employee/       # 员工登录（8094）
│   ├── his-menzhen/        # 门诊医生工作站（8095）
│   └── his-charge/         # 收费管理（8096）
├── hissystemui/            # 前端（Vue 3 + Element Plus）
├── his数据库.sql           # 全量数据库脚本
└── pharmacy_stock.sql      # 药房库存补充脚本
```

---

## 三、后端服务与接口

所有服务共用同一数据库 `his01`（`root` / `123456`）。

### 1. 挂号登记 his-registration（8093）

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/register/add` | 新增挂号，自动生成连续病历号（最大病历号 + 1） |
| GET | `/register/list` | 挂号列表（可按姓名/科室/状态/日期过滤） |
| GET | `/register/{id}` | 按 id 查询挂号单 |
| PUT | `/register/state/{id}/{state}` | 修改就诊状态 |

### 2. 科室管理 his-outpatient（8092）

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/department/list` | 科室列表 |
| POST | `/department/add` | 新增科室 |
| DELETE | `/department/del/{id}` | 删除科室 |
| GET | `/test/hello` | 服务健康检查 |

### 3. 员工登录 his-employee（8094）

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/employee/login` | 员工登录 |
| GET | `/employee/list` | 员工列表 |

### 4. 药房/库存/发药 his-drugstore（8091）

药品管理 `/drugstore/drug`：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/drugstore/drug/list` | 药品列表 |
| POST | `/drugstore/drug/add` | 新增药品 |
| PUT | `/drugstore/drug/update` | 修改药品 |
| DELETE | `/drugstore/drug/del/{id}` | 删除药品 |
| PUT | `/drugstore/drug/stockIn/{id}/{qty}` | 入库 |
| PUT | `/drugstore/drug/stockOut/{id}/{qty}` | 出库 |

发药 `/drugstore/dispense`：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/drugstore/dispense/list` | 待发药处方列表 |
| PUT | `/drugstore/dispense/issue/{id}` | 发药（自动扣库存，库存不足返回错误） |
| PUT | `/drugstore/dispense/return/{id}` | 退药（自动回补库存） |

### 5. 门诊医生工作站 his-menzhen（8095）

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/outpatient/doctor/waitPatient/{doctorId}` | 候诊患者 |
| PUT | `/outpatient/doctor/receive/{registerId}` | 接诊 |
| POST | `/outpatient/doctor/saveMedicalRecord` | 保存病历 |
| POST | `/outpatient/doctor/addCheckRequest` | 开检查申请 |
| GET | `/outpatient/doctor/listCheck/{registerId}` | 检查申请列表 |
| POST | `/outpatient/doctor/addDisposalRequest` | 开处置申请 |
| GET | `/outpatient/doctor/listDisposal/{registerId}` | 处置申请列表 |
| POST | `/outpatient/doctor/addPrescription` | 开处方（库存不足返回错误） |
| GET | `/outpatient/doctor/listPrescription/{registerId}` | 处方列表 |
| GET | `/outpatient/doctor/detail/{registerId}` | 就诊详情 |
| GET | `/outpatient/doctor/diseases` | 诊断/疾病字典 |
| GET | `/outpatient/doctor/medicalTechnologies` | 检查/检验/处置项目 |

### 6. 收费管理 his-charge（8096）

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/charge/fee/{key}` | 按病历号或挂号 ID 计算费用明细 |
| POST | `/charge/settle/{key}` | 收费结算 |
| GET | `/charge/charged/list` | 已收费记录列表 |
| POST | `/charge/refund/{chargeId}` | 退费 |

费用明细字段：`registFee`（挂号费）、`drugTotalFee`（药品总费用）、`checkTotalFee`（检验总费用）、`inspectionTotalFee`（检查总费用）、`disposalTotalFee`（处置总费用）、`totalAmount`（合计）。

---

## 四、前端功能模块

侧边栏菜单由 `src/router/routes.js` 动态生成，当前包含：

| 菜单 | 页面 | 对应后端 |
| --- | --- | --- |
| 工作台 | dashboard | — |
| 门诊管理 → 挂号登记 | outpatient/registration | his-registration |
| 门诊管理 → 医生工作站 | outpatient/doctor | his-menzhen |
| 收费管理 → 收费 | charge/billing | his-charge |
| 收费管理 → 退费 | charge/refund | his-charge |
| 药房管理 → 药品管理 | pharmacy/drug | his-drugstore |
| 药房管理 → 库存管理 | pharmacy/stock | his-drugstore |
| 药房管理 → 发药 | pharmacy/dispense | his-drugstore |
| 部门管理 | department | his-outpatient |

---

## 五、前端代理配置（vite.config.js）

前端 axios `baseURL` 为 `/api`，由 Vite 代理转发到各后端服务：

| 前缀 | 转发目标 |
| --- | --- |
| `/api/register` | http://localhost:8093 |
| `/api/drugstore` | http://localhost:8091 |
| `/api/employee` | http://localhost:8094 |
| `/api/outpatient` | http://localhost:8095 |
| `/api/charge` | http://localhost:8096 |
| `/api`（默认） | 兜底转发（部门管理等） |

> 说明：更具体的路径（如 `/api/register`）必须写在通用 `/api` 之前，Vite 按顺序取首个匹配。

---

## 六、接口响应规范

后端统一返回 `Result<T>`：

```json
{
  "code": 200,        // 200 成功，500 失败
  "msg": "操作成功",
  "data": { }
}
```

前端 `src/api/request.js` 拦截器会解包 `response.data`，当 `code !== 200` 时自动弹出错误提示。

---

## 七、数据库

- 数据库名：`his01`
- 用户名 / 密码：`root` / `123456`
- 初始化脚本：根目录 `his数据库.sql`（全量）、`pharmacy_stock.sql`（药房库存字段补充）
- 所有服务连接同一库，表名与字段采用 snake_case，后端开启 `map-underscore-to-camel-case` 自动映射。

---

## 八、启动与部署

### 后端（IDEA 或命令行）

1. 导入 `HISHIS` 为 Maven 多模块工程。
2. 执行根目录 `his数据库.sql` 初始化数据库（如需库存字段，再执行 `pharmacy_stock.sql`）。
3. 依次启动各服务的 `*Application` 主类（端口互不冲突，共 6 个服务 + 1 个公共模块）：
   - his-outpatient（8092）、his-drugstore（8091）、his-registration（8093）、his-employee（8094）、his-menzhen（8095）、his-charge（8096）。

> 修改后端代码后需重新编译/重启，避免 `target/classes` 里的旧 `.class` 生效。

### 前端

```bash
cd hissystemui
npm install
npm run dev          # 开发环境，默认 http://localhost:5173
npm run build        # 生产构建
```

---

## 九、已实现 / 未实现

**已实现（前后端闭环）：** 挂号登记、门诊医生工作站、药房（药品/库存/发药）、科室管理、员工登录、收费与退费。

**未实现（前端占位页已移除）：** 患者管理、住院管理、系统管理（用户/角色/菜单/字典）、分诊台。后续如需实现，参照 `his-charge` 新建对应后端微服务模块，再补前端页面与 API 文件即可。
