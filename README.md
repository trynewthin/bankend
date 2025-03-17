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

## Docker部署说明

本项目支持使用Docker进行容器化部署，包括应用本身、MySQL数据库和Nginx静态资源服务器。

### 部署步骤

1. 确保已安装Docker和Docker Compose
   ```
   docker --version
   docker-compose --version
   ```

2. 在项目根目录下运行以下命令启动所有服务
   ```
   docker-compose up -d
   ```

3. 服务说明
   - Spring Boot应用：http://localhost:8080/api
   - Nginx静态资源：http://localhost:8090
   - MySQL数据库：localhost:3307 (用户名: zhixuanche_user, 密码: zhixuanche_pass)

4. 停止所有服务
   ```
   docker-compose down
   ```

5. 查看日志
   ```
   docker logs zhixuanche_app    # 查看应用日志
   docker logs zhixuanche_mysql  # 查看数据库日志
   docker logs zhixuanche_nginx  # 查看Nginx日志
   ```

### 单独使用原有数据库

如果您希望仅启动应用而使用已有的Docker数据库：

1. 修改src/main/resources/application.yml中的数据库连接参数
2. 使用以下命令单独构建和启动应用
   ```
   docker build -t zhixuanche-app .
   docker run -d --name zhixuanche_app --network database_zhixuanche_net -p 8080:8080 zhixuanche-app
   ```

注意：确保应用与数据库容器处于同一网络中 