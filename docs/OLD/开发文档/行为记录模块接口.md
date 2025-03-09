# 行为记录模块接口文档

本文档描述了行为记录模块提供的API接口，包括浏览记录、搜索记录管理和用户兴趣分析等功能。

## 通用说明

- 所有接口的基础路径为 `/api`
- 所有接口需要通过 Authorization 头提供认证 Token
- 响应格式统一为 JSON，包含以下字段：
  - `code`: 状态码，200表示成功
  - `message`: 状态描述
  - `data`: 响应数据，具体结构根据接口不同而变化

## 浏览记录管理

### 获取浏览历史

获取当前用户的浏览历史记录列表。

- **URL**: `/behavior/browse`
- **方法**: `GET`
- **参数**:
  - `page`: 页码，默认1
  - `size`: 每页条数，默认10

**请求示例**:
```bash
curl -X GET 'http://localhost:8080/api/behavior/browse?page=1&size=10' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "total": 2,
        "current": 1,
        "pages": 1,
        "records": [
            {
                "carId": 1,
                "brand": "BMW",
                "model": "宝马5系 530Li",
                "price": 479800.00,
                "browseTime": "2025-03-06T07:43:57",
                "duration": 300,
                "thumbnailUrl": "/images/cars/bmw/5-series-thumb.jpg"
            },
            {
                "carId": 4,
                "brand": "Mercedes-Benz",
                "model": "奔驰E级 E300L",
                "price": 498800.00,
                "browseTime": "2025-03-02T08:12:15",
                "duration": 220,
                "thumbnailUrl": "/images/cars/mercedes/e300l-thumb.jpg"
            }
        ]
    }
}
```

### 记录浏览行为

记录用户对特定车辆的浏览行为。

- **URL**: `/behavior/browse`
- **方法**: `POST`
- **请求体**:
  - `car_id`: 车辆ID（必填）

**请求示例**:
```bash
curl -X POST 'http://localhost:8080/api/behavior/browse' \
    -H 'Authorization: {your-token}' \
    -H 'Content-Type: application/json' \
    -d '{"car_id": 1}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "记录成功"
}
```

### 删除浏览记录

删除特定车辆的浏览记录。

- **URL**: `/behavior/browse/{carId}`
- **方法**: `DELETE`
- **路径参数**:
  - `carId`: 车辆ID

**请求示例**:
```bash
curl -X DELETE 'http://localhost:8080/api/behavior/browse/1' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "删除成功"
}
```

### 清空浏览历史

清空当前用户的所有浏览记录。

- **URL**: `/behavior/browse`
- **方法**: `DELETE`

**请求示例**:
```bash
curl -X DELETE 'http://localhost:8080/api/behavior/browse' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "清空成功"
}
```

## 搜索记录管理

### 获取搜索历史

获取当前用户的搜索历史记录列表。

- **URL**: `/behavior/search`
- **方法**: `GET`
- **参数**:
  - `page`: 页码，默认1
  - `size`: 每页条数，默认10

**请求示例**:
```bash
curl -X GET 'http://localhost:8080/api/behavior/search?page=1&size=10' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "total": 1,
        "current": 1,
        "pages": 1,
        "records": [
            {
                "behaviorId": 10,
                "searchKeywords": "BMW 5系 北京",
                "searchTime": "2025-03-04T08:12:15"
            }
        ]
    }
}
```

### 记录搜索行为

记录用户的搜索关键词。

- **URL**: `/behavior/search`
- **方法**: `POST`
- **请求体**:
  - `keyword`: 搜索关键词（必填）

**请求示例**:
```bash
curl -X POST 'http://localhost:8080/api/behavior/search' \
    -H 'Authorization: {your-token}' \
    -H 'Content-Type: application/json' \
    -d '{"keyword": "宝马 SUV"}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "记录成功"
}
```

### 删除搜索记录

删除特定的搜索记录。

- **URL**: `/behavior/search/{searchId}`
- **方法**: `DELETE`
- **路径参数**:
  - `searchId`: 搜索记录ID

**请求示例**:
```bash
curl -X DELETE 'http://localhost:8080/api/behavior/search/10' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "删除成功"
}
```

### 清空搜索历史

清空当前用户的所有搜索记录。

- **URL**: `/behavior/search`
- **方法**: `DELETE`

**请求示例**:
```bash
curl -X DELETE 'http://localhost:8080/api/behavior/search' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "清空成功"
}
```

## 用户兴趣分析

### 获取用户兴趣分析

获取基于用户行为的兴趣分析数据。

- **URL**: `/behavior/interests`
- **方法**: `GET`

**请求示例**:
```bash
curl -X GET 'http://localhost:8080/api/behavior/interests' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "topBrands": [
            {
                "brand": "BMW",
                "count": 1
            },
            {
                "brand": "Mercedes-Benz",
                "count": 1
            }
        ],
        "topKeywords": [
            {
                "search_keywords": "BMW 5系 北京",
                "count": 1
            }
        ],
        "browseCount": 2,
        "searchCount": 1,
        "consultCount": 1
    }
}
```

## 收藏管理

### 获取收藏列表

获取当前用户的收藏列表。

- **URL**: `/favorites`
- **方法**: `GET`
- **参数**:
  - `page`: 页码，默认1
  - `size`: 每页条数，默认10

**请求示例**:
```bash
curl -X GET 'http://localhost:8080/api/favorites?page=1&size=10' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "total": 2,
        "current": 1,
        "pages": 1,
        "records": [
            {
                "carId": 1,
                "brand": "BMW",
                "model": "宝马5系 530Li",
                "price": 479800.00,
                "createTime": "2025-03-02T08:12:16",
                "thumbnailUrl": "/images/cars/bmw/5-series-thumb.jpg"
            },
            {
                "carId": 4,
                "brand": "Mercedes-Benz",
                "model": "奔驰E级 E300L",
                "price": 498800.00,
                "createTime": "2025-03-03T08:12:16",
                "thumbnailUrl": "/images/cars/mercedes/e300l-thumb.jpg"
            }
        ]
    }
}
```

### 添加收藏

添加车辆到收藏列表。

- **URL**: `/favorites`
- **方法**: `POST`
- **请求体**:
  - `car_id`: 车辆ID（必填）

**请求示例**:
```bash
curl -X POST 'http://localhost:8080/api/favorites' \
    -H 'Authorization: {your-token}' \
    -H 'Content-Type: application/json' \
    -d '{"car_id": 2}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "favorite_id": 6,
        "car_id": 2,
        "create_time": 1709714289123
    }
}
```

**错误响应**:
```json
{
    "code": 409,
    "message": "已收藏该车辆",
    "data": null
}
```

### 取消收藏

从收藏列表中移除特定车辆。

- **URL**: `/favorites/{carId}`
- **方法**: `DELETE`
- **路径参数**:
  - `carId`: 车辆ID

**请求示例**:
```bash
curl -X DELETE 'http://localhost:8080/api/favorites/2' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": "取消收藏成功"
}
```

### 检查是否收藏

检查特定车辆是否已被当前用户收藏。

- **URL**: `/favorites/check/{carId}`
- **方法**: `GET`
- **路径参数**:
  - `carId`: 车辆ID

**请求示例**:
```bash
curl -X GET 'http://localhost:8080/api/favorites/check/1' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "isFavorite": true
    }
}
```

### 获取收藏数量

获取当前用户的收藏数量。

- **URL**: `/favorites/count`
- **方法**: `GET`

**请求示例**:
```bash
curl -X GET 'http://localhost:8080/api/favorites/count' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": 2
}
```

### 获取车辆被收藏数量

获取特定车辆被收藏的次数。

- **URL**: `/favorites/count/{carId}`
- **方法**: `GET`
- **路径参数**:
  - `carId`: 车辆ID

**请求示例**:
```bash
curl -X GET 'http://localhost:8080/api/favorites/count/1' \
    -H 'Authorization: {your-token}'
```

**响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": 1
}
```

## 注意事项

1. 所有API调用前都必须先获取认证token，通过登录API：
   ```bash
   curl -X POST 'http://localhost:8080/api/users/login' \
       -H 'Content-Type: application/json' \
       -d '{"loginIdentity": "user1@example.com", "password": "user123", "loginType": "email"}'
   ```

2. 获取到token后，在所有请求的Header中添加`Authorization`字段。

3. 记录浏览行为和添加收藏时，确保请求的Content-Type为`application/json`。

4. 浏览和收藏的API支持分页，可以通过page和size参数控制。
