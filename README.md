---

# 海上捞火锅店线上点餐管理系统

## 项目简介

该系统是一个面向火锅店的线上点餐管理系统，旨在提高餐饮服务效率。系统分为 **客户端** 和 **管理端** 两部分，分别供消费者和餐饮企业管理员使用。

- **客户端**：提供给消费者浏览菜品、下单和查看订单状态的功能。
- **管理端**：供餐饮企业管理员工、菜品、套餐和订单等信息。

## 主要功能

### 客户端功能

- 用户登录
- 菜品浏览与搜索
- 购物车添加与结算
- 订单管理（历史订单查看、订单状态查询）

### 管理端功能

- **员工管理**：员工信息的增删查改、状态管理（启用/禁用）。
- **分类管理**：菜品与套餐分类的增删查改。
- **菜品管理**：菜品的增删查改及上下架管理，批量操作支持。
- **套餐管理**：套餐的增删查改及批量管理功能。
- **订单管理**：支持订单详情查看、订单状态查询。

## 系统架构

### 前端

- 使用 H5 和 Vue.js 技术开发，实现了移动端和管理端界面。
- 前端页面部署在 Nginx 服务器上。

### 后端

- 基于 Spring Boot 框架构建，集成了 MySQL 数据库、MyBatis Plus 进行数据持久化。
- 使用 SSM 技术（Spring、Spring MVC、MyBatis）管理后台服务。
- 后端服务通过 Nginx 反向代理对外提供 API 接口。

### 数据库设计

系统共包含 9 张核心数据表，分别用于存储分类、菜品、套餐、订单、员工等信息。

- **category（分类表）**
- **dish（菜品表）**
- **setmeal（套餐表）**
- **order（订单表）**
- **employee（员工表）**
- **shopping_cart（购物车表）**

#### 示例：`dish` 表结构
| 字段        | 类型           | 描述        |
|-------------|----------------|-------------|
| id          | bigint          | 主键        |
| name        | varchar(64)     | 菜品名称    |
| category_id | bigint          | 分类 ID     |
| price       | decimal(10,2)   | 菜品价格    |
| image       | varchar(200)    | 菜品图片    |
| status      | int             | 状态 (0 停售, 1 起售) |

## 环境依赖

- **Java 8** 或以上版本
- **Maven 3.6** 或以上版本
- **MySQL 5.7** 或以上版本
- **Nginx 1.18** 或以上版本

## 本地开发与部署

### 数据库配置

在 `src/main/resources/application.yml` 中配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hotpot_order?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 启动步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/RuiLing-Su/hotpot_order
   cd hotpot_order
   ```

2. **安装依赖**
   ```bash
   mvn clean install
   ```

3. **启动项目**
   ```bash
   mvn spring-boot:run
   ```

4. **前端打包**
   ```bash
   cd front-end
   npm install
   npm run build
   ```

5. **Nginx 配置**
   将打包后的前端项目部署到 Nginx，并配置反向代理：

   ```nginx
   server {
       listen       80;
       server_name  your_domain;

       location / {
           root   /var/www/hotpot_order_front;
           index  index.html index.htm;
       }

       location /api/ {
           proxy_pass http://localhost:8999;
       }
   }
   ```

### 接口文档

项目接口文档可以访问 [ShowDoc 在线文档](https://www.showdoc.com.cn/haishanglao/9708582704740747)。

## 贡献指南

如果你对项目有任何建议或贡献，欢迎提交 Pull Request。

---