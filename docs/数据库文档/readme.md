# 智选车数据库系统

本项目是智选车应用的数据库系统，使用Docker容器化部署MySQL数据库和Nginx静态资源服务。

## 系统架构

系统包含两个主要组件：

1. MySQL数据库 (3307端口)
   - 存储用户、车辆、经销商等业务数据
   - 使用UTF-8编码确保中文支持
   - 实现完整的数据关系模型

2. Nginx静态资源服务器 (8090端口)
   - 处理图片存储和访问
   - 支持用户头像和车辆图片
   - 实现缓存和访问控制

## 目录结构

```
zhixuanche/database/
├── docs/                    # 文档目录
│   └── 数据库设计.md        # 数据库设计文档
├── mysql/                   # MySQL相关文件
│   ├── data/               # 数据库数据文件
│   └── init/               # 数据库初始化脚本
│       ├── schema.sql      # 数据库表结构
│       └── seed-data.sql   # 测试数据
├── nginx/                   # Nginx相关文件
│   └── conf.d/             # Nginx配置文件
├── public/                  # 静态资源目录
│   └── images/             # 图片存储目录
│       ├── avatars/        # 用户头像
│       └── cars/           # 车辆图片
├── scripts/                 # 管理脚本
│   ├── init-db.ps1        # 初始化数据库
│   ├── reset-db.ps1       # 重置数据库
│   ├── start.ps1          # 启动服务
│   ├── stop.ps1           # 停止服务
│   └── test-image-upload.ps1  # 测试图片上传
├── docker-compose.yml      # Docker配置文件
└── readme.md              # 项目说明文档
```

## 环境要求

- Windows 10或更高版本
- Docker Desktop
- PowerShell 5.0或更高版本

## 快速开始

1. 克隆项目到本地：
   ```powershell
   git clone <repository_url>
   cd zhixuanche/database
   ```

2. 初始化系统：
   ```powershell
   ./scripts/init-db.ps1
   ```
   这将：
   - 创建必要的目录结构
   - 初始化数据库表
   - 创建图片存储目录

3. 启动服务：
   ```powershell
   ./scripts/start.ps1
   ```

4. 测试图片上传：
   ```powershell
   ./scripts/test-image-upload.ps1
   ```

## 服务访问

1. MySQL数据库：
   - 主机：localhost
   - 端口：3307
   - 数据库：zhixuanche
   - 用户名：zhixuanche_user
   - 密码：zhixuanche_pass

2. 图片服务：
   - 基础URL：http://localhost:8090/images/
   - 用户头像：http://localhost:8090/images/avatars/
   - 车辆图片：http://localhost:8090/images/cars/

## 管理命令

1. 启动服务：
   ```powershell
   ./scripts/start.ps1
   ```

2. 停止服务：
   ```powershell
   ./scripts/stop.ps1
   ```

3. 重置数据：
   ```powershell
   ./scripts/reset-db.ps1
   ```
   注意：这将清除所有数据和图片！

## 数据库设计

详细的数据库设计文档请参考 `docs/数据库设计.md`，其中包含：
- 完整的ER图
- 表结构设计
- 字段说明
- 关系模型

## 图片存储规范

1. 文件格式：
   - 支持：jpg, jpeg, png, gif, ico, webp
   - 推荐：jpeg用于照片，png用于图标和界面元素

2. 文件路径：
   - 用户头像：/images/avatars/{user_id}.{ext}
   - 车辆图片：/images/cars/{car_id}-{type}.{ext}

3. 图片类型：
   - 缩略图
   - 完整图（最多5张）

## 注意事项

1. 数据安全：
   - 定期备份数据库
   - 不要将敏感信息提交到版本控制系统

2. 性能优化：
   - Nginx已配置图片缓存
   - 建议定期清理未使用的图片

3. 故障排除：
   - 检查Docker服务是否运行
   - 确保所需端口未被占用
   - 查看Docker日志获取详细错误信息

