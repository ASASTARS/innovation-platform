# 单元测试计划

## 1. 测试目标

为天津科技大学创新创业信息平台项目编写全面的单元测试，确保每个函数都有对应的测试覆盖。

## 2. 测试范围

### 2.1 优先测试层级（按优先级排序）

| 优先级 | 层级 | 说明 |
|-------|------|------|
| P0 | Util 工具类 | 无外部依赖，测试简单且收益高 |
| P1 | Service 服务层 | 核心业务逻辑，需要 Mock Mapper |
| P2 | Controller 控制层 | API 接口测试，需要 Mock Service |
| P3 | Common 通用类 | 结果封装、分页、异常处理等 |
| P4 | Config 配置类 | 配置加载和 Bean 创建 |
| P5 | Filter/Interceptor | 过滤器、拦截器、监听器 |

### 2.2 待测试类清单

#### Util 工具类 (4个)
- [x] `JwtUtil` - JWT Token 生成与解析
- [x] `RedisUtil` - Redis 操作封装
- [x] `MinioUtils` - MinIO 对象存储操作
- [x] `PasswordUtil` - 密码加密工具

#### Service 服务层 (15个)
- [x] `UserService` - 用户登录、注册、密码修改
- [x] `UserDetailsServiceImpl` - Spring Security 用户详情加载
- [x] `ActivityService` - 活动 CRUD、审批、报名、总结
- [x] `TeamService` - 团队管理、成员管理、导入导出
- [x] `NewsService` - 新闻管理
- [x] `ProjectService` - 项目管理
- [x] `ProjectApplicationService` - 项目申请管理
- [x] `ProjectApplicationFormService` - 项目申请表单管理
- [x] `SpaceReservationService` - 空间预约管理
- [x] `EntryApplicationService` - 入驻申请管理
- [x] `FundApplicationService` - 基金申请管理
- [x] `InnovationTeamApplicationService` - 创新团队申请管理
- [x] `InformationLinkService` - 资讯链接管理
- [x] `UserDetailsServiceImpl` - 用户详情服务

#### Controller 控制层 (13个)
- [x] `AuthController` - 认证接口
- [x] `UserController` - 用户接口
- [x] `ActivityController` - 活动接口
- [x] `TeamController` - 团队接口
- [x] `NewsController` - 新闻接口
- [x] `ProjectController` - 项目接口
- [x] `SpaceController` - 空间接口
- [x] `UploadController` - 文件上传接口
- [x] `CommonController` - 通用接口
- [x] `PersonController` - 人才库接口
- [x] `ProjectApplicationFormController` - 项目申请表单接口
- [x] `EntryApplicationController` - 入驻申请接口
- [x] `FundApplicationController` - 基金申请接口

#### Common 通用类 (4个)
- [x] `Result` - 统一响应结果
- [x] `PageResult` - 分页结果
- [x] `PageRequest` - 分页请求
- [x] `GlobalExceptionHandler` - 全局异常处理

#### Entity/DTO/VO (模型类)
- 使用 Lombok，主要测试 getter/setter 和构建器模式

## 3. 测试策略

### 3.1 测试框架

- **JUnit 5** (Jupiter) - 测试框架
- **Mockito** - Mock 框架
- **Spring Boot Test** - Spring 测试支持
- **AssertJ** - 流式断言（可选）

### 3.2 Mock 策略

| 被测层 | Mock 对象 |
|--------|----------|
| Service | Mapper、其他 Service、Redis、MinIO |
| Controller | Service、JwtUtil |
| Util | 外部依赖（如 RedisTemplate） |

### 3.3 测试覆盖率目标

- 行覆盖率：≥ 80%
- 分支覆盖率：≥ 70%
- 函数覆盖率：≥ 90%

## 4. 测试文件组织

```
src/test/java/com/abajin/innovation/
├── util/                          # 工具类测试
│   ├── JwtUtilTest.java
│   ├── RedisUtilTest.java
│   ├── MinioUtilsTest.java
│   └── PasswordUtilTest.java
├── service/                       # 服务层测试
│   ├── UserServiceTest.java
│   ├── UserDetailsServiceImplTest.java
│   ├── ActivityServiceTest.java
│   ├── TeamServiceTest.java
│   ├── NewsServiceTest.java
│   ├── ProjectServiceTest.java
│   ├── ProjectApplicationServiceTest.java
│   ├── ProjectApplicationFormServiceTest.java
│   ├── SpaceReservationServiceTest.java
│   ├── EntryApplicationServiceTest.java
│   ├── FundApplicationServiceTest.java
│   ├── InnovationTeamApplicationServiceTest.java
│   └── InformationLinkServiceTest.java
├── controller/                    # 控制层测试
│   ├── AuthControllerTest.java
│   ├── UserControllerTest.java
│   ├── ActivityControllerTest.java
│   ├── TeamControllerTest.java
│   ├── NewsControllerTest.java
│   ├── ProjectControllerTest.java
│   ├── SpaceControllerTest.java
│   ├── UploadControllerTest.java
│   ├── CommonControllerTest.java
│   ├── PersonControllerTest.java
│   ├── ProjectApplicationFormControllerTest.java
│   ├── EntryApplicationControllerTest.java
│   └── FundApplicationControllerTest.java
├── common/                        # 通用类测试
│   ├── ResultTest.java
│   ├── PageResultTest.java
│   └── PageRequestTest.java
├── config/                        # 配置类测试
│   └── (视情况而定)
└── IntegrationTests.java          # 集成测试
```

## 5. 测试用例设计规范

### 5.1 命名规范

- 测试类：`{被测类名}Test`
- 测试方法：`{被测方法名}_{条件}_{预期结果}`

示例：
```java
@Test
void login_validCredentials_returnsToken() {}

@Test
void login_invalidPassword_throwsException() {}
```

### 5.2 测试结构（AAA 模式）

```java
@Test
void method_condition_result() {
    // Arrange - 准备数据
    
    // Act - 执行操作
    
    // Assert - 验证结果
}
```

### 5.3 测试数据管理

- 使用 `@BeforeEach` 准备通用测试数据
- 使用工厂方法创建测试对象
- 避免测试间数据依赖

## 6. 详细测试计划

### Phase 1: 工具类测试 (预计 1 天)

#### 6.1.1 JwtUtilTest
| 方法 | 测试场景 |
|------|----------|
| `generateToken` | 正常生成Token |
| `parseToken` | 解析有效Token、解析无效Token |
| `validateToken` | 验证有效Token、验证过期Token、验证无效Token |
| `getUserIdFromToken` | 从Token提取用户ID |
| `getRoleFromToken` | 从Token提取角色 |
| `getUsernameFromToken` | 从Token提取用户名 |

#### 6.1.2 RedisUtilTest
| 方法 | 测试场景 |
|------|----------|
| `buildKey` | 构建单层key、多层key |
| `exist` | key存在、key不存在 |
| `del` | 删除存在的key、删除不存在的key |
| `set/get` | 存储和读取字符串 |
| `setNx` | 设置成功、设置失败（key已存在）|
| `zAdd/countZset/rangeZset` | ZSet操作 |

#### 6.1.3 MinioUtilsTest
| 方法 | 测试场景 |
|------|----------|
| `uploadFile` | 上传成功、上传失败 |
| `downloadFile` | 下载成功、文件不存在 |
| `deleteFile` | 删除成功、删除不存在文件 |
| `getFileUrl` | 获取URL |

### Phase 2: Service 层测试 (预计 3 天)

#### 6.2.1 UserServiceTest
| 方法 | 测试场景 |
|------|----------|
| `login` | 正常登录、用户不存在、密码错误、账户禁用 |
| `register` | 正常注册、用户名已存在、设置学院名称 |
| `getUserById` | 获取存在的用户、获取不存在的用户 |
| `getUserByUsername` | 根据用户名查询 |
| `changePassword` | 正常修改、原密码错误、新密码太短 |

#### 6.2.2 UserDetailsServiceImplTest
| 方法 | 测试场景 |
|------|----------|
| `loadUserByUsername` | 正常加载、用户不存在、账户禁用 |

#### 6.2.3 ActivityServiceTest
| 方法 | 测试场景 |
|------|----------|
| `createActivity` | 正常创建、组织者不存在、时间冲突 |
| `updateActivity` | 正常更新、无权修改 |
| `updateActivityPoster` | 管理员上传、非管理员上传 |
| `submitActivity` | 正常提交、非草稿状态 |
| `collegeReview` | 通过、驳回、无效状态 |
| `schoolReviewAndPublish` | 发布、拒绝 |
| `registerActivity` | 正常报名、已报名、人数已满 |
| `submitSummary` | 正常提交、重复提交 |
| `reviewSummary` | 通过、驳回 |

#### 6.2.4 TeamServiceTest
| 方法 | 测试场景 |
|------|----------|
| `createTeam` | 正常创建、用户不存在、招募条件为空 |
| `updateTeam` | 正常更新、无权修改 |
| `addMember` | 正常添加、已是成员 |
| `removeMember` | 正常移除、移除队长 |
| `isMember` | 是成员、不是成员 |
| `reviewMemberApplication` | 通过、驳回 |
| `exportTeamsToExcel` | 导出成功 |
| `importTeamsFromExcel` | 导入成功、姓名不存在、姓名不唯一 |

### Phase 3: Controller 层测试 (预计 2 天)

每个 Controller 测试包括：
- 正常请求场景
- 参数校验失败场景
- 权限不足场景
- 资源不存在场景

### Phase 4: Common 层测试 (预计 0.5 天)

- `Result` 的各种静态工厂方法
- `PageResult` 的构建和分页计算
- `PageRequest` 的默认值处理

## 7. 执行计划

| 阶段 | 内容 | 预计时间 | 优先级 |
|------|------|----------|--------|
| Phase 1 | 工具类测试 | 1 天 | P0 |
| Phase 2 | Service 层测试 | 3 天 | P1 |
| Phase 3 | Controller 层测试 | 2 天 | P2 |
| Phase 4 | Common 层测试 | 0.5 天 | P3 |
| Phase 5 | 补充测试和修复 | 1 天 | - |
| **总计** | | **7.5 天** | |

## 8. 测试命令

```bash
# 运行所有测试
./mvnw test

# 运行单个测试类
./mvnw test -Dtest=UserServiceTest

# 运行指定包下的测试
./mvnw test -Dtest="com.abajin.innovation.service.*"

# 生成测试报告
./mvnw test jacoco:report
```

## 9. 注意事项

1. **数据库隔离**：使用 `@Transactional` 或内存数据库确保测试隔离
2. **时间敏感测试**：使用 `Clock` 或固定时间进行测试
3. **文件操作**：使用临时文件或内存文件系统
4. **异步操作**：适当使用 `Awaitility` 等待异步完成
5. **代码覆盖**：定期运行 `mvn jacoco:report` 检查覆盖率

## 10. 验收标准

- [ ] 所有 P0/P1 测试用例通过
- [ ] 行覆盖率达到 80% 以上
- [ ] 无严重 Bug 遗留
- [ ] CI/CD 流程中集成测试步骤
