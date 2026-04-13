# CAS 统一身份认证测试指南

本文档说明如何运行和调试 CAS 登录流程的测试。

## 测试架构

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  前端页面    │────▶│  后端API     │────▶│  CAS服务器   │
└─────────────┘     └─────────────┘     └─────────────┘
       │                    │                    │
       ▼                    ▼                    ▼
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ Vitest测试  │     │  JUnit测试   │     │ Mock Server │
└─────────────┘     └─────────────┘     └─────────────┘
```

## 测试类型

### 1. 前端单元测试

**文件位置**: `frontend/src/test/`

**运行方式**:
```bash
cd frontend
npm install
npm run test
```

**测试覆盖**:
- CAS 状态获取
- 自动跳转逻辑
- URL 参数解析
- Token 处理
- 页面渲染

### 2. 后端集成测试

**文件位置**: `src/test/java/com/abajin/innovation/controller/`

**主要测试类**:
- `CasAuthIntegrationTest`: 基础 CAS 认证测试
- `CasFrontendIntegrationTest`: 前后端联合流程测试
- `CasServiceTest`: 服务层业务逻辑测试

**运行方式**:
```bash
# 运行所有 CAS 相关测试
./mvnw test -Dtest=CasAuthIntegrationTest,CasFrontendIntegrationTest,CasServiceTest

# 运行单个测试
./mvnw test -Dtest=CasFrontendIntegrationTest#testNewUserCompleteFlow
```

### 3. Mock CAS 服务器

**文件位置**: `frontend/src/test/mocks/server.js`

使用 MSW (Mock Service Worker) 模拟:
- CAS 状态接口
- 登录重定向
- Ticket 验证
- 用户数据返回

## 完整测试流程

### 场景1: 新用户首次登录

```
1. 访问登录页
   GET /auth/cas/status
   返回: { enabled: true, mockMode: true }

2. 自动跳转到 CAS
   GET /auth/cas/login
   返回: 302 → /cas-callback?ticket=MOCK-xxx

3. 验证 Ticket（后端）
   GET /auth/cas/validate?ticket=MOCK-xxx
   返回: 302 → /complete-profile?token=xxx

4. 获取用户信息（前端）
   GET /users/me (with token)
   返回: { username, realName, role, isProfileComplete: 0 }

5. 完善资料（前端）
   POST /auth/cas/complete-profile
   返回: { profileComplete: true }

6. 进入系统
   跳转: /dashboard
```

### 场景2: 同名账号合并

```
1-2. 同新用户流程

3. 验证 Ticket 发现同名账号
   GET /auth/cas/validate?ticket=MOCK-xxx
   返回: 302 → /cas-merge?data=xxx

4. 显示合并选项（前端）
   解析 data 参数显示同名账号

5. 用户选择合并，输入密码
   POST /auth/cas/merge
   返回: { token, user }

6. 进入系统
   跳转: /dashboard
```

## 调试技巧

### 前端调试

1. **查看 Mock Server 日志**:
```javascript
// 在测试文件中
import { server } from './mocks/server'

// 打印所有请求
server.events.on('request:start', ({ request }) => {
  console.log('Outgoing:', request.method, request.url)
})
```

2. **检查 Store 状态**:
```javascript
const userStore = useUserStore()
console.log('Store state:', userStore.$state)
```

### 后端调试

1. **启用详细日志**:
```properties
logging.level.com.abajin.innovation=DEBUG
logging.level.org.springframework.security=DEBUG
```

2. **打印重定向 URL**:
```java
log.info("[CAS] Redirect URL: {}", redirectUrl);
```

### 常见问题

#### Q: 前端测试报错 "Cannot read property of undefined"
**A**: 确保在测试前正确初始化了 Pinia 和 Vue Router

#### Q: 后端测试报错 "Table doesn't exist"
**A**: 确保使用了 `@Transactional` 注解，或使用内存数据库

#### Q: Mock Server 没有拦截请求
**A**: 检查 baseURL 配置，确保请求路径匹配 mock 处理器

## GitHub Actions

测试会在以下情况自动运行:
- 推送到 `main` 或 `develop` 分支
- 创建 Pull Request
- 修改相关文件路径:
  - `src/**`
  - `frontend/src/**`
  - `.github/workflows/cas-integration-test.yml`

## 本地开发测试

### 快速验证命令

```bash
# 1. 启动后端（测试模式）
./mvnw spring-boot:run -Dspring.profiles.active=test -DCAS_MOCK_MODE=true

# 2. 启动前端（开发模式）
cd frontend && npm run dev

# 3. 访问并测试
open http://localhost:5173

# 4. 使用 Mock 用户登录
# 用户名: MOCK-2021001-张三
# 或任何以 MOCK- 开头的票据
```

### 测试数据

预置的 Mock 用户:

| 票据 | 场景 | 结果 |
|------|------|------|
| `MOCK-2021001-张三` | 新用户 | 需要完善资料 |
| `MOCK-MERGE-2021001-张三` | 同名账号 | 需要合并 |
| `MOCK-EXISTING-2021001-张三` | 已存在 | 直接登录 |

## 参考文档

- [CAS Protocol Specification](https://apereo.github.io/cas/6.6.x/protocol/CAS-Protocol.html)
- [Vitest Documentation](https://vitest.dev/)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
