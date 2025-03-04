# 智选车 - 汽车推荐购买平台

## 项目介绍
这是一个基于Spring Boot开发的汽车推荐购买平台后端系统。系统提供用户管理、车辆管理、智能推荐、在线交互等功能。

## 技术栈
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- MySQL 8.0
- Maven
- JWT

## 开发环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

## 快速开始

### 1. 数据库配置
1. 创建MySQL数据库：
```sql
CREATE DATABASE car_recommendation DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改`application.yml`中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/car_recommendation
    username: your-username
    password: your-password
```

### 2. 运行应用
```bash
mvn spring-boot:run
```

## 项目结构
```
src/main/java/com/zhixuanche/
├── config/          # 配置类
├── controller/      # 控制器
├── model/          # 实体类
├── repository/     # 数据访问层
├── service/        # 业务逻辑层
├── security/       # 安全相关
└── util/           # 工具类
```

## API文档
启动应用后，访问：http://localhost:8080/api/swagger-ui.html

## 主要功能模块
1. 用户管理模块
2. 车辆管理模块
3. 行为记录模块
4. 推荐引擎模块
5. 消息交互模块
6. 系统管理模块 