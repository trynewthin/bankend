# 智选车 - 系统管理模块API文档

## 目录

- [简介](#简介)
- [身份认证](#身份认证)
- [用户管理API](#用户管理api)
- [经销商管理API](#经销商管理api)
- [内容审核API](#内容审核api)
- [数据统计API](#数据统计api)

## 简介

本文档详细描述了智选车平台系统管理模块的API接口规范。系统管理模块主要提供对用户、经销商、内容的管理功能，以及系统数据统计分析功能。

所有API请求均需要进行身份认证，且仅允许管理员用户访问。

## 身份认证

### 登录获取令牌

**接口**: `POST /api/users/login`

**描述**: 管理员登录系统，获取授权令牌

**请求体**:
```json
{
  "loginIdentity": "管理员邮箱/手机号",
  "password": "密码",
  "loginType": "登录类型(email/phone)"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "user": {
      "userId": 11,
      "username": "admin",
      "email": "admin@zhixuanche.com",
      "phone": "13888888888",
      "userType": "ADMIN",
      "registerTime": "2025-03-08T09:47:47.000+00:00",
      "lastLoginTime": "2025-03-09T03:14:12.052+00:00",
      "status": 1,
      "avatar": "/images/avatars/admin.png"
    },
    "tokenInfo": {
      "tokenName": "Authorization",
      "tokenValue": "2793f07f-ee0d-49d6-b640-486b167aba97",
      "isLogin": true,
      "loginId": "11",
      "loginType": "login",
      "tokenTimeout": 2591999,
      "sessionTimeout": 2591999,
      "tokenSessionTimeout": -2,
      "tokenActiveTimeout": -1,
      "loginDevice": "default-device",
      "tag": null
    }
  }
}
```

**使用方式**: 
- 登录成功后，将获得的`tokenValue`添加到后续请求的Header中
- 格式: `Authorization: {tokenValue}`

## 用户管理API

### 获取用户列表

**接口**: `GET /api/admin/users`

**描述**: 获取系统用户列表，支持分页和多种筛选条件

**请求参数**:
- `keyword` (可选): 关键词，用于搜索用户名、邮箱或手机号
- `status` (可选): 用户状态(1:正常, 0:禁用)
- `userType` (可选): 用户类型(USER:普通用户, DEALER:经销商, ADMIN:管理员)
- `startDate` (可选): 注册起始日期(格式:yyyy-MM-dd)
- `endDate` (可选): 注册结束日期(格式:yyyy-MM-dd)
- `page` (可选, 默认1): 页码
- `size` (可选, 默认20): 每页记录数

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 20,
    "pages": 5,
    "list": [
      {
        "userId": 15,
        "username": "张三",
        "email": "zhangsan@example.com",
        "phone": "13900000000",
        "userType": "USER",
        "registerTime": "2025-03-01T10:00:00.000+00:00",
        "lastLoginTime": "2025-03-08T14:30:00.000+00:00",
        "status": 1,
        "avatar": "/images/avatars/default.png"
      },
      // ...更多用户记录
    ]
  }
}
```

### 获取用户详情

**接口**: `GET /api/admin/users/{userId}`

**描述**: 获取指定用户的详细信息

**路径参数**:
- `userId`: 用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 15,
    "username": "张三",
    "email": "zhangsan@example.com",
    "phone": "13900000000",
    "userType": "USER",
    "registerTime": "2025-03-01T10:00:00.000+00:00",
    "lastLoginTime": "2025-03-08T14:30:00.000+00:00",
    "status": 1,
    "avatar": "/images/avatars/default.png",
    "dealerInfo": null
  }
}
```

### 更新用户状态

**接口**: `PUT /api/admin/users/{userId}/status`

**描述**: 启用或禁用用户账号

**路径参数**:
- `userId`: 用户ID

**请求头**:
- `Content-Type`: application/json
- `Authorization`: {tokenValue}

**请求体**:
```json
{
  "status": 0,  // 0:禁用, 1:启用
  "reason": "禁用原因说明"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "用户状态更新成功"
}
```

**测试命令示例**:
```bash
curl -X PUT "http://localhost:8080/api/admin/users/15/status" \
  -H "Content-Type: application/json; charset=UTF-8" \
  -H "Authorization: 2793f07f-ee0d-49d6-b640-486b167aba97" \
  -d "{\"status\": 0, \"reason\": \"test disable account\"}"
```

### 重置用户密码

**接口**: `POST /api/admin/users/{userId}/reset-password`

**描述**: 重置用户密码为随机密码

**路径参数**:
- `userId`: 用户ID

**查询参数**:
- `notifyUser` (可选, 默认true): 是否通知用户

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "newPassword": "aBc123XyZ",
    "message": "密码重置成功"
  }
}
```

## 经销商管理API

### 获取经销商列表

**接口**: `GET /api/admin/dealers`

**描述**: 获取经销商列表，支持分页和多种筛选条件

**请求参数**:
- `keyword` (可选): 关键词，搜索经销商名称、联系人、联系电话
- `verifyStatus` (可选): 认证状态(0:待审核, 1:已通过, 2:已拒绝)
- `region` (可选): 地区
- `page` (可选, 默认1): 页码
- `size` (可选, 默认20): 每页记录数

**响应格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 50,
    "pageNum": 1,
    "pageSize": 20,
    "pages": 3,
    "list": [
      {
        "dealerId": 5,
        "userId": 20,
        "dealerName": "北京XXX汽车销售有限公司",
        "address": "北京市海淀区XX路XX号",
        "businessLicense": "/images/licenses/license_5.jpg",
        "contactPerson": "李四",
        "contactPhone": "13911111111",
        "status": 1,
        "description": "专业汽车销售服务商"
      },
      // ...更多经销商记录
    ]
  }
}
```

### 审核经销商

**接口**: `POST /api/admin/dealers/{dealerId}/audit`

**描述**: 审核经销商资质

**路径参数**:
- `dealerId`: 经销商ID

**请求体**:
```json
{
  "status": 1,  // 1:通过, 0:拒绝
  "remarks": "审核意见说明"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "经销商审核完成"
}
```

## 内容审核API

### 获取待审核车辆

**接口**: `GET /api/admin/audit/cars`

**描述**: 获取待审核的车辆信息列表

**请求参数**:
- `keyword` (可选): 关键词，搜索车辆品牌、型号等
- `auditStatus` (可选): 审核状态(0:待审核, 1:已通过, 2:已拒绝)
- `dealerId` (可选): 经销商ID
- `startDate` (可选): 提交起始日期
- `endDate` (可选): 提交结束日期
- `page` (可选, 默认1): 页码
- `size` (可选, 默认20): 每页记录数

**响应格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 30,
    "pageNum": 1,
    "pageSize": 20,
    "pages": 2,
    "list": [
      {
        "carId": 25,
        "dealerId": 5,
        "brand": "奔驰",
        "model": "C级",
        "year": 2025,
        "price": 350000.00,
        "mileage": 0,
        "color": "白色",
        "status": 0,
        "createdTime": "2025-03-05T16:30:00.000+00:00",
        "updatedTime": "2025-03-05T16:30:00.000+00:00",
        "coverImage": "/images/cars/car_25_cover.jpg"
      },
      // ...更多车辆记录
    ]
  }
}
```

### 审核车辆信息

**接口**: `POST /api/admin/audit/cars/{carId}`

**描述**: 对车辆信息进行审核

**路径参数**:
- `carId`: 车辆ID

**请求体**:
```json
{
  "status": 1,  // 1:通过, 0:拒绝
  "remarks": "审核意见说明"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "车辆审核完成"
}
```

## 数据统计API

### 获取用户统计

**接口**: `GET /api/admin/statistics/users`

**描述**: 获取用户相关统计数据

**请求参数**:
- `startDate` (可选): 起始日期
- `endDate` (可选): 结束日期
- `userType` (可选, 默认ALL): 用户类型(ALL, USER, DEALER, ADMIN)
- `groupBy` (可选, 默认DAY): 分组方式(DAY, WEEK, MONTH)

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalUsers": 1500,
    "newUsers": 120,
    "userGrowth": 8.7,
    "userTypeDistribution": {
      "USER": 1200,
      "DEALER": 280,
      "ADMIN": 20
    },
    "trendData": [
      {
        "date": "2025-03-01",
        "count": 15
      },
      // ...更多趋势数据
    ]
  }
}
```

### 获取内容统计

**接口**: `GET /api/admin/statistics/content`

**描述**: 获取内容相关统计数据

**请求参数**:
- `startDate` (可选): 起始日期
- `endDate` (可选): 结束日期
- `contentType` (可选, 默认ALL): 内容类型(ALL, CAR, ARTICLE)
- `groupBy` (可选, 默认DAY): 分组方式(DAY, WEEK, MONTH)

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalContent": 2300,
    "newContent": 180,
    "contentGrowth": 7.8,
    "contentTypeDistribution": {
      "CAR": 1800,
      "ARTICLE": 500
    },
    "trendData": [
      {
        "date": "2025-03-01",
        "count": 25
      },
      // ...更多趋势数据
    ]
  }
}
```

### 获取系统统计

**接口**: `GET /api/admin/statistics/system`

**描述**: 获取系统相关统计数据

**请求参数**:
- `startDate` (可选): 起始日期
- `endDate` (可选): 结束日期

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "serverInfo": {
      "cpuUsage": 35.6,
      "memoryUsage": 42.3,
      "diskUsage": 28.7,
      "uptime": "30天5小时"
    },
    "trafficInfo": {
      "totalVisits": 58600,
      "uniqueVisitors": 12500,
      "avgSessionTime": "5分钟23秒",
      "peakTime": "10:00-12:00"
    },
    "errorInfo": {
      "totalErrors": 156,
      "criticalErrors": 12,
      "errorRate": 0.26
    }
  }
}
```

## 注意事项

1. 所有API请求都需要进行身份认证，未认证的请求会返回401状态码
2. Content-Type应为application/json，且确保使用UTF-8编码
3. 错误响应格式统一为：
```json
{
  "code": 错误码,
  "message": "错误信息",
  "data": null
}
```
4. 常见错误码：
   - 400: 请求参数错误
   - 401: 未授权/未登录
   - 403: 权限不足
   - 404: 资源不存在
   - 500: 服务器内部错误
