# 家庭点餐系统

一个完整的家庭点餐微信小程序系统，包含后端API、管理后台和微信小程序。

## 项目结构

```
family-order/
├── backend/          # Spring Boot 后端服务
├── admin/            # Vue 3 管理后台
├── miniapp/          # 微信小程序
└── README.md
```

## 功能特性

- **菜品管理**：添加/编辑菜品，维护材料和烹饪步骤
- **每日菜单**：厨师发布每日可选菜品
- **在线点餐**：家庭成员选择菜品提交点餐
- **历史记录**：查看过往点餐记录
- **角色管理**：厨师/普通成员角色切换

## 快速开始

### 1. 数据库配置

```sql
-- 执行 backend/src/main/resources/schema.sql 创建数据库和表
-- 默认配置: localhost:3306/family_order，用户 root，密码 root
```

### 2. 启动后端服务

```bash
cd backend
# 需要 Maven 和 Java 17
mvn spring-boot:run
# 后端运行在 http://localhost:8080
```

### 3. 启动管理后台

```bash
cd admin
npm install
npm run dev
# 管理后台运行在 http://localhost:3000
```

### 4. 运行微信小程序

1. 使用微信开发者工具打开 `miniapp` 目录
2. 在 `app.ts` 中修改 `baseUrl` 为你的后端地址
3. 在微信公众平台获取 AppID，替换 `application.yml` 中的配置
4. 点击"预览"或"真机调试"运行小程序

## API 接口

| 接口 | 说明 |
|------|------|
| POST /api/auth/login | 微信登录 |
| GET /api/dishes | 获取菜品列表 |
| POST /api/dishes | 创建菜品 |
| GET /api/menus/today | 获取今日菜单 |
| POST /api/menus | 发布每日菜单 |
| POST /api/orders | 提交点餐 |
| GET /api/orders/my | 获取我的订单 |

## 技术栈

| 组件 | 技术 |
|------|------|
| 后端 | Java 17 + Spring Boot 3 + MyBatis-Plus |
| 数据库 | MySQL 8.0 |
| 管理后台 | Vue 3 + Element Plus + TypeScript |
| 小程序 | 微信原生小程序 |

## 配置说明

### 后端配置 (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/family_order
    username: root
    password: root

wechat:
  appid: your-wechat-appid  # 替换为你的小程序AppID
  secret: your-wechat-secret # 替换为你的小程序Secret
```

### 小程序配置 (miniapp/app.ts)

```typescript
globalData: {
  baseUrl: 'http://localhost:8080/api' // 替换为你的后端地址
}
```

## 默认数据

数据库时会插入示例菜品：
- 红烧肉（含材料和步骤）
- 清炒时蔬
- 番茄炒蛋
- 糖醋排骨
- 麻婆豆腐