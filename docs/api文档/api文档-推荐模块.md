# API文档-推荐模块

## 1. 模块概述

推荐模块提供车辆智能推荐功能，包括基于用户行为的个性化推荐、基于用户偏好的推荐、热门车辆推荐和新车推荐。该模块通过分析用户行为和偏好数据，为用户提供个性化的车辆推荐服务。

## 2. 接口列表

### 2.1 获取首页推荐（公开接口）

- **接口描述**：获取首页的个性化推荐和热门推荐
- **请求方式**：GET
- **接口URL**：`/api/recommendations/home`
- **是否需要登录**：否（公开接口）

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| limit | Integer | 否 | 10 | 推荐数量限制（1-50） |

**响应数据**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "personalRecommendations": [
      {
        "carId": 1,
        "brand": "宝马",
        "model": "X5",
        "year": 2023,
        "price": 529000,
        "category": "SUV",
        "mainImage": "bmw-x5.jpg",
        "status": 1,
        "createTime": "2023-01-15T10:30:00",
        "updateTime": "2023-02-20T16:45:00",
        "viewCount": 1200,
        "favoriteCount": 150,
        "recommendReason": "根据您的浏览历史推荐",
        "relevanceScore": 0.92
      }
      // 更多个性化推荐...
    ],
    "hotRecommendations": [
      // 热门推荐列表...
    ],
    "newRecommendations": [
      // 新车推荐列表...
    ]
  }
}
```

### 2.2 获取行为推荐（需要登录）

- **接口描述**：根据用户浏览历史推荐相似车辆
- **请求方式**：GET
- **接口URL**：`/api/recommendations/behavior`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| limit | Integer | 否 | 10 | 推荐数量限制（1-50） |

**响应数据**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "carId": 1,
      "brand": "宝马",
      "model": "X5",
      "year": 2023,
      "price": 529000,
      "category": "SUV",
      "mainImage": "bmw-x5.jpg",
      "status": 1,
      "createTime": "2023-01-15T10:30:00",
      "updateTime": "2023-02-20T16:45:00",
      "viewCount": 1200,
      "favoriteCount": 150,
      "recommendReason": "与您最近浏览的车型相似",
      "relevanceScore": 0.95
    }
    // 更多推荐列表...
  ]
}
```

### 2.3 获取偏好推荐（需要登录）

- **接口描述**：根据用户设置的购车偏好推荐车辆
- **请求方式**：GET
- **接口URL**：`/api/recommendations/preference`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| limit | Integer | 否 | 10 | 推荐数量限制（1-50） |

**响应数据**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "carId": 2,
      "brand": "奔驰",
      "model": "GLC",
      "year": 2023,
      "price": 479000,
      "category": "SUV",
      "mainImage": "benz-glc.jpg",
      "status": 1,
      "createTime": "2023-01-20T11:15:00",
      "updateTime": "2023-02-18T14:30:00",
      "viewCount": 980,
      "favoriteCount": 120,
      "recommendReason": "符合您的价格和车型偏好",
      "relevanceScore": 0.88
    }
    // 更多推荐列表...
  ]
}
```

### 2.4 获取热门推荐（公开接口）

- **接口描述**：获取系统内热门车辆推荐
- **请求方式**：GET
- **接口URL**：`/api/recommendations/hot`
- **是否需要登录**：否（公开接口）

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| category | String | 否 | null | 车辆类别，如"SUV"、"轿车"等 |
| limit | Integer | 否 | 10 | 推荐数量限制（1-50） |
| days | Integer | 否 | 7 | 统计天数（1-30） |

**响应数据**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "carId": 3,
      "brand": "特斯拉",
      "model": "Model Y",
      "year": 2023,
      "price": 349900,
      "category": "SUV",
      "mainImage": "tesla-model-y.jpg",
      "status": 1,
      "createTime": "2023-01-10T09:20:00",
      "updateTime": "2023-02-15T11:25:00",
      "viewCount": 2200,
      "favoriteCount": 320,
      "recommendReason": "近期热门车型",
      "relevanceScore": 0.0
    }
    // 更多推荐列表...
  ]
}
```

### 2.5 获取新车推荐（公开接口）

- **接口描述**：获取最新上架的车辆推荐
- **请求方式**：GET
- **接口URL**：`/api/recommendations/new`
- **是否需要登录**：否（公开接口）

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| category | String | 否 | null | 车辆类别，如"SUV"、"轿车"等 |
| limit | Integer | 否 | 10 | 推荐数量限制（1-50） |
| days | Integer | 否 | 30 | 统计天数（1-90） |

**响应数据**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "carId": 4,
      "brand": "蔚来",
      "model": "ES6",
      "year": 2023,
      "price": 428000,
      "category": "SUV",
      "mainImage": "nio-es6.jpg",
      "status": 1,
      "createTime": "2023-02-28T14:10:00",
      "updateTime": "2023-02-28T14:10:00",
      "viewCount": 350,
      "favoriteCount": 45,
      "recommendReason": "近期新上架车型",
      "relevanceScore": 0.0
    }
    // 更多推荐列表...
  ]
}
```

## 3. 错误码

| 错误码 | 描述 |
| ------ | ---- |
| 401 | 未登录或登录已过期 |
| 403 | 没有权限访问该接口 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 4. 数据模型

### 4.1 推荐结果模型（RecommendationResult）

| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| personalRecommendations | List<CarRecommendationDTO> | 个性化推荐列表 |
| hotRecommendations | List<CarRecommendationDTO> | 热门推荐列表 |
| newRecommendations | List<CarRecommendationDTO> | 新车推荐列表 |

### 4.2 车辆推荐数据传输对象（CarRecommendationDTO）

| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| carId | Integer | 车辆ID |
| brand | String | 品牌 |
| model | String | 型号 |
| year | Integer | 年份 |
| price | BigDecimal | 价格 |
| category | String | 类别 |
| mainImage | String | 主图片URL |
| status | Integer | 状态 |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 更新时间 |
| viewCount | Integer | 浏览次数 |
| favoriteCount | Integer | 收藏次数 |
| recommendReason | String | 推荐理由 |
| relevanceScore | Double | 相关性得分 |

## 5. 注意事项

1. 需要登录的接口必须在请求头中包含有效的token
2. 推荐结果中的个性化推荐需要用户登录才能获取，未登录用户将返回空列表
3. 统计天数参数(days)影响推荐结果的时间范围，例如热门推荐默认统计近7天的热门车辆
4. limit参数限制返回的推荐数量，有效范围为1-50
5. 所有接口URL都需要添加`/api`前缀
