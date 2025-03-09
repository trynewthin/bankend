# 部署文档

## 启动服务

```bash
cd database
docker-compose up -d
```

## 停止服务

```bash
cd database
docker-compose down
```

## 数据库语句

- 清空数据库
database/mysql/init/clear-data.sql

- 初始化数据库
database/mysql/init/schema.sql

- 插入数据
database/mysql/init/seed-data.sql

## 预加载图片位置

- 注：由于图片文件特殊，需要手动解压数据到目录

```bash
database/public/images/
```


