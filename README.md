# HospitalManager 医院管理系统

这是一个基于 Spring Boot 2.2.4、Java 8、MyBatis-Plus、MySQL、Redis、Vue 2 和 Element UI 的医院管理系统。项目包含管理员、医生、患者、挂号、药品、检查、床位、排班、PDF/Excel、统计和智能导诊等功能。

## 功能列表

- 管理员、医生、患者登录。
- 医生、患者、挂号、药品、检查、床位、排班管理。
- 患者预约挂号、查看挂号记录和住院床位信息。
- 医生处理挂号和复诊相关流程。
- PDF、Excel 导出及统计展示。
- 智能导诊模块：
  - 患者输入症状文本。
  - 后端基于关键词和同义词规则推荐科室。
  - 展示疑似方向、就诊建议、科室位置和路线说明。
  - 展示推荐科室医生。
  - 支持导诊历史、规则管理和导诊统计。
  - 支持从导诊结果跳转到原预约挂号流程。
  - 支持导诊推荐科室到旧系统真实挂号科室的别名映射。

智能导诊模块详细说明见：[docs/SMART_GUIDANCE.md](docs/SMART_GUIDANCE.md)

## 项目结构

```text
HospitalManagerApi - idea/      Spring Boot 后端工程
HospitalManagerApi - eclipse/   Eclipse 版本后端工程
HospitalManagerVue/             Vue 2 前端工程
docs/                           项目说明文档
```

## 数据库初始化

先导入原系统数据库脚本，再导入智能导诊增量 SQL。

智能导诊 SQL 文件：

```text
HospitalManagerApi - idea/docs/sql/phase2_guidance.sql
```

导入示例：

```bash
mysql -u root -p hospital_manager < "HospitalManagerApi - idea/docs/sql/phase2_guidance.sql"
```

该 SQL 新增 `guidance_rule` 和 `guidance_record` 两张表，并插入 12 条常见导诊规则。脚本使用 `CREATE TABLE IF NOT EXISTS`，不会覆盖原系统表。

## 后端启动与构建

进入后端 IDEA 工程：

```bash
cd "HospitalManagerApi - idea"
mvn package
```

启动前请确认：

- MySQL 连接配置正确。
- Redis 服务可用，原预约挂号流程依赖 Redis 号源扣减逻辑。
- 已导入原系统数据库和 `phase2_guidance.sql`。

## 前端启动与构建

进入前端工程：

```bash
cd HospitalManagerVue
npm install
npm run serve
npm run build
```

`npm run serve` 用于本地开发运行，`npm run build` 用于生产构建。

如果使用 Node v25 或较新 Node 版本，旧 Vue CLI 4 / Webpack 4 可能遇到 OpenSSL 兼容问题。可使用：

```powershell
$env:NODE_OPTIONS='--openssl-legacy-provider'; npm run build
```

推荐使用 Node 14 或 Node 16 运行该旧版 Vue CLI 4 项目。

## 智能导诊演示

患者端：

1. 患者登录。
2. 进入“智能导诊”。
3. 输入 `咳嗽 发烧 喉咙痛`。
4. 查看推荐科室、疑似方向、就诊建议和路线提示。
5. 点击“预约该科室”，跳转到旧系统挂号医生列表。
6. 继续选择日期、医生和时间段完成原预约挂号流程。

管理员端：

1. 管理员登录。
2. 进入“导诊规则管理”，查看或维护导诊规则。
3. 进入“导诊统计”，查看导诊总次数、热门推荐科室和最近导诊记录。

## 注意事项

- 智能导诊是规则版实现，结果仅作为挂号参考，不能替代医生诊断。
- 如果导诊跳转后医生列表为空，请检查 `doctor.d_section` 是否存在对应科室医生数据。
- 当前没有引入 Botpress、大模型、真实医疗知识库、真实支付网关或三维地图导航。
"# HospitalManager"  
