# 用户模块API文档

## 1. 用户认证相关 API

### 1.1 用户注册
- **接口**: `POST /api/users/register`
- **描述**: 新用户注册
- **请求体**:
```json
{
    "username": "string",     // 用户名，3-20字符
    "password": "string",     // 密码，6-20字符
    "email": "string",       // 邮箱地址
    "phone": "string",       // 手机号（中国大陆11位）
    "userType": "string"     // 用户类型：NORMAL_USER/DEALER/ADMIN
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "注册成功",
    "data": {
        "userId": 1,
        "username": "string",
        "email": "string",
        "phone": "string",
        "userType": "string",
        "registerTime": "timestamp",
        "lastLoginTime": null,
        "status": 1,
        "avatar": null
    }
}
```

### 1.2 用户登录
- **接口**: `POST /api/users/login`
- **描述**: 用户登录，支持邮箱和手机号登录
- **请求体**:
```json
{
    "loginIdentity": "string",  // 登录标识（邮箱或手机号）
    "password": "string",       // 密码
    "loginType": "string"       // 登录类型：email/phone
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "user": {
            "userId": 1,
            "username": "string",
            "email": "string",
            "phone": "string",
            "userType": "string",
            "registerTime": "timestamp",
            "lastLoginTime": "timestamp",
            "status": 1,
            "avatar": "string"
        },
        "tokenInfo": {
            "tokenName": "Authorization",
            "tokenValue": "string",
            "isLogin": true,
            "loginId": "string",
            "loginType": "string",
            "tokenTimeout": 2591999,
            "sessionTimeout": 2591999
        }
    }
}
```

### 1.3 用户登出
- **接口**: `POST /api/users/logout`
- **描述**: 用户登出
- **请求头**: `Authorization: token值`
- **响应**:
```json
{
    "code": 200,
    "message": "登出成功",
    "data": null
}
```

## 2. 用户资料管理 API

### 2.1 获取用户资料
- **接口**: `GET /api/users/profile`
- **描述**: 获取当前登录用户的资料信息
- **请求头**: `Authorization: token值`
- **响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "userId": 1,
        "username": "string",
        "email": "string",
        "phone": "string",
        "userType": "string",
        "registerTime": "timestamp",
        "lastLoginTime": "timestamp",
        "status": 1,
        "avatar": "string"
    }
}
```

### 2.2 更新用户资料
- **接口**: `PUT /api/users/profile`
- **描述**: 更新当前登录用户的资料信息
- **请求头**: `Authorization: token值`
- **请求体**:
```json
{
    "email": "string",    // 新邮箱地址
    "phone": "string"     // 新手机号
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "更新成功",
    "data": {
        "userId": 1,
        "username": "string",
        "email": "string",
        "phone": "string",
        "userType": "string",
        "registerTime": "timestamp",
        "lastLoginTime": "timestamp",
        "status": 1,
        "avatar": "string"
    }
}
```

### 2.3 上传头像
- **接口**: `POST /api/users/avatar`
- **描述**: 上传用户头像
- **请求头**: 
  - `Authorization: token值`
  - `Content-Type: multipart/form-data`
- **请求参数**:
  - `file`: 图片文件（支持JPG、PNG、GIF，最大2MB）
- **响应**:
```json
{
    "code": 200,
    "message": "上传成功",
    "data": "http://localhost:8090/images/avatars/avatar_1679012345678.jpg"
}
```

## 3. 用户偏好设置 API

### 3.1 获取用户偏好设置
- **接口**: `GET /api/users/preference`
- **描述**: 获取当前登录用户的偏好设置
- **请求头**: `Authorization: token值`
- **响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "preferenceId": 1,
        "userId": 1,
        "priceMin": 100000.00,
        "priceMax": 500000.00,
        "preferredBrands": "string",      // 逗号分隔的品牌列表
        "preferredCategories": "string",   // 逗号分隔的车型列表
        "otherPreferences": "string",      // 逗号分隔的其他偏好
        "updateTime": "timestamp"
    }
}
```

### 3.2 更新用户偏好设置
- **接口**: `PUT /api/users/preference`
- **描述**: 更新当前登录用户的偏好设置
- **请求头**: `Authorization: token值`
- **请求体**:
```json
{
    "priceMin": 100000.00,              // 最低价格
    "priceMax": 500000.00,              // 最高价格
    "preferredBrands": "string",        // 逗号分隔的品牌列表
    "preferredCategories": "string",     // 逗号分隔的车型列表
    "otherPreferences": "string"        // 逗号分隔的其他偏好
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "更新成功",
    "data": {
        "preferenceId": 1,
        "userId": 1,
        "priceMin": 100000.00,
        "priceMax": 500000.00,
        "preferredBrands": "string",
        "preferredCategories": "string",
        "otherPreferences": "string",
        "updateTime": "timestamp"
    }
}
```

## 4. 权限说明

根据用户类型，系统自动分配以下权限：

### 4.1 所有用户基础权限
- `user:profile:view` - 查看个人资料
- `user:profile:edit` - 编辑个人资料

### 4.2 普通用户权限
- `car:view` - 查看车辆
- `car:favorite` - 收藏车辆
- `order:create` - 创建订单
- `order:cancel` - 取消订单
- `review:add` - 添加评价

### 4.3 经销商权限
- `dealer:info:edit` - 编辑经销商信息
- `car:add` - 添加车辆
- `car:edit` - 编辑车辆
- `car:delete` - 删除车辆
- `order:view` - 查看订单
- `order:process` - 处理订单

### 4.4 管理员权限
- `user:*` - 用户管理所有权限
- `dealer:*` - 经销商管理所有权限
- `car:*` - 车辆管理所有权限
- `system:*` - 系统管理所有权限

## 5. 注意事项

1. **认证相关**
   - 除了注册和登录接口外，其他所有接口都需要在请求头中携带`Authorization`令牌
   - 令牌有效期默认为30天
   - 密码传输目前为明文，后续会增加加密机制

2. **文件上传**
   - 头像上传大小限制为2MB
   - 仅支持JPG、PNG、GIF格式
   - 上传成功后会返回可访问的URL地址

3. **错误处理**
   - 所有接口统一使用`ApiResponse`格式返回
   - 成功响应code为200
   - 客户端错误code为400系列
   - 服务端错误code为500系列

4. **数据验证**
   - 用户名长度：3-20字符
   - 密码长度：6-20字符
   - 手机号格式：中国大陆11位手机号
   - 邮箱格式：标准邮箱格式验证
