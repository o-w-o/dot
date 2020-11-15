# `烛火录`服务端

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8b32af3afdc44f8fa75c71ccd16cd335)](https://app.codacy.com/manual/o-w-o/dot?utm_source=github.com&utm_medium=referral&utm_content=o-w-o/dot&utm_campaign=Badge_Grade_Dashboard)

## 项目关键词
- `Spring Boot`、`Java`
- `JWT`、`RBAC`
- `Rest API`、`OpenAPIv3`
- `PostgreSQL`、`Redis`、`Flyway`
- `Junit`、`Jacoco`
- `Lombok`、`Mapstruct`、`Javapoet`
- `PMD`、`PMD-P3C`
- `OSS`
- `CI`、`CD`
- `Prometheus`

## 项目结构
```
src
├─main
│  ├─java
│  │  └─o
│  │      └─w
│  │          └─o
│  │              ├─api
│  │              ├─resource
│  │              │  ├─example
│  │              │  │   └─modulex
│  │              │  │      ├─configuration
│  │              │  │      ├─domain
│  │              │  │      │  └─property
│  │              │  │      ├─repository
│  │              │  │      ├─service
│  │              │  │      │  ├─dto
│  │              │  │      │  ├─dtomapper(DO DTO 转换配置)
│  │              │  │      │  ├─dtovalidator(DTO 验证器)
│  │              │  │      │  └─impl
│  │              │  │      └─util
│  │              │  └─system
│  │              │      ├─authentication(认证)
│  │              │      ├─authorization(授权)
│  │              │      ├─notification(通知)
│  │              │      ├─permission(权限)
│  │              │      ├─role(角色)
│  │              │      └─user(用户)
│  │              ├─server
│  │              │  ├─aop
│  │              │  ├─configuration
│  │              │  │  ├─handler
│  │              │  │  │  └─exceptions(分类全局异常拦截器)
│  │              │  │  └─properties
│  │              │  ├─definition(数据定义)
│  │              │  ├─helper(与 util 区别 需要 @Resource 注入使用)
│  │              │  ├─runner
│  │              │  ├─schedule
│  │              │  ├─util(静态工具类)
│  │              │  └─validator(验证器)
│  │              ├─util(全局静态工具类，尽量使用完善的三方库基于接口封装)
│  │              └─websocket
│  │                  └─endpoint
│  └─resources
│      ├─META-INF
│      ├─config(配置文件)
│      ├─db
│      │  └─migration(flyway 脚本)
│      ├─i18n(国际化)
│      ├─resource
│      ├─static
│      └─templates
└─test
```

## 项目约定

### 定义 Definition

- 模型
  - [x] 服务泛型：ServiceResult
  - [x] 服务异常：ServiceException
  - [x] 接口泛型：ApiResult
  - [x] 接口异常：ApiException
  - [x] 系统异常：SystemException
  
- 全局处理
  - [x] 接口全局异常处理：ApiExceptionsHandler
  - [x] 接口全局包装处理：ApiResponseHandler


### 安全 Security

- 权限配置
  - [ ] 访问控制形式 RBAC
  
- 认证 - 授权 [ `JWT` ]
  - 认证：
    - [x] 生成、注册、校验、刷新、注销
  - 授权
    - [x] 解析、注入

- 安全策略
  - 参考：[Github [ security-guide-for-developers ]](https://github.com/FallibleInc/security-guide-for-developers)

### 数据 Data

- PostgreSQL
  - 系统资源：
    - [x] 用户
    - [x] 角色
    - [ ] 权限
    
- Redis
  - [x] 认证
  - [x] 服务访问限制（基于 IP 和 User ID）
  - [x] 接口访问限制（基于 IP 或 User ID）
  - [x] 缓存 Service 层操作结果
  
- Flyway
  - 数据库管理
    - [x] 数据库 Baseline 配置
    - [x] 数据库 核心表 SQL 脚本

### 测试 Test

- 单元测试
  - [x] Junit 单元测试
  - [ ] Jacoco 代码覆盖率

### 持续集成 / 部署 CI/CD

- [x] Jenkins
  - [ ] 单元测试
  - [ ] 代码检查
- [x] Aliyun Docker Registry
- [x] Github App Integration
  - [x] 依赖检测

### 代码简化 Simplification

- OpenApi
  - [x] 接口 SDK 自动生成
- MapStruct
  - [x] 对象 DTO DO 自动转化
- Lombok
  - [x] 对象代码简化

## 贡献

感谢 JetBrains 提供的 开源许可证（open source licence）。

![Jetbrains logo](resource/jetbrains-idea.svg)
