# API文档-消息交互模块

## 1. 模块概述

消息交互模块提供系统消息、交互消息和用户预约功能，包括消息的发送、接收、查询、标记已读和删除操作，以及预约的创建、查询和状态更新。该模块为用户与系统、用户与用户之间的沟通提供了基础设施支持。

## 2. 接口列表

### 2.1 消息管理接口

#### 2.1.1 获取系统消息（需要登录）

- **接口描述**：获取当前用户的系统消息列表，支持已读/未读过滤和分页
- **请求方式**：GET
- **接口URL**：`/api/message/system`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| read | Boolean | 否 | null | 是否已读：true-已读，false-未读，null-全部 |
| messageType | String | 否 | null | 消息类型：SYSTEM/MARKETING/NOTICE/ACTIVITY等 |
| startDate | String | 否 | null | 开始日期，格式：yyyy-MM-dd |
| endDate | String | 否 | null | 结束日期，格式：yyyy-MM-dd |
| page | Integer | 否 | 1 | 当前页码 |
| size | Integer | 否 | 20 | 每页大小 |

**响应数据**：

```json
{
  "code": 200,
  "message": "获取系统消息成功",
  "data": {
    "total": 24,
    "pages": 2,
    "current": 1,
    "size": 20,
    "records": [
      {
        "id": 1,
        "fromUserId": 0,
        "toUserId": 10,
        "fromUserName": "系统",
        "fromUserAvatar": "/images/system/logo.png",
        "carId": null,
        "carInfo": null,
        "title": "系统更新通知",
        "content": "尊敬的用户，系统将于2023年3月10日凌晨2点进行例行维护更新",
        "messageType": "SYSTEM",
        "interactionType": null,
        "targetType": null,
        "targetId": null,
        "read": false,
        "sendTime": "2023-03-08T09:30:00",
        "expireTime": "2023-04-08T09:30:00",
        "priority": 2,
        "actionType": "NONE",
        "actionValue": null
      }
      // 更多系统消息...
    ]
  }
}
```

#### 2.1.2 获取交互消息（需要登录）

- **接口描述**：获取当前用户的交互消息列表，支持已读/未读过滤、交互类型过滤和分页
- **请求方式**：GET
- **接口URL**：`/api/message/interaction`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| read | Boolean | 否 | null | 是否已读：true-已读，false-未读，null-全部 |
| interactionType | String | 否 | null | 交互类型：COMMENT/REPLY/QUESTION/CONSULTATION等 |
| targetType | String | 否 | null | 目标类型：VEHICLE/COMMENT/POST/DEALER等 |
| startDate | String | 否 | null | 开始日期，格式：yyyy-MM-dd |
| endDate | String | 否 | null | 结束日期，格式：yyyy-MM-dd |
| page | Integer | 否 | 1 | 当前页码 |
| size | Integer | 否 | 20 | 每页大小 |

**响应数据**：

```json
{
  "code": 200,
  "message": "获取交互消息成功",
  "data": {
    "total": 15,
    "pages": 1,
    "current": 1,
    "size": 20,
    "records": [
      {
        "id": 3,
        "fromUserId": 12,
        "toUserId": 10,
        "fromUserName": "王小明",
        "fromUserAvatar": "/images/avatars/user12.jpg",
        "carId": 5,
        "carInfo": "宝马 3系",
        "title": null,
        "content": "请问这辆车还能便宜点吗？",
        "messageType": null,
        "interactionType": "CONSULTATION",
        "targetType": "VEHICLE",
        "targetId": "5",
        "read": false,
        "sendTime": "2023-03-08T14:23:45",
        "expireTime": null,
        "priority": 1,
        "actionType": "VEHICLE",
        "actionValue": "5"
      }
      // 更多交互消息...
    ]
  }
}
```

#### 2.1.3 获取聊天记录（需要登录）

- **接口描述**：获取当前用户与指定联系人的聊天记录，支持关联车辆和分页
- **请求方式**：GET
- **接口URL**：`/api/message/chat/{contactId}`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| contactId | Integer | 是 | - | 联系人ID |
| carId | Integer | 否 | null | 关联车辆ID，可选 |
| page | Integer | 否 | 1 | 当前页码 |
| size | Integer | 否 | 20 | 每页大小 |

**响应数据**：

```json
{
  "code": 200,
  "message": "获取聊天记录成功",
  "data": {
    "total": 8,
    "pages": 1,
    "current": 1,
    "size": 20,
    "records": [
      {
        "id": 15,
        "fromUserId": 10,
        "toUserId": 12,
        "fromUserName": "张三",
        "fromUserAvatar": "/images/avatars/user10.jpg",
        "carId": 5,
        "carInfo": "宝马 3系",
        "title": null,
        "content": "您好，我对这款车很感兴趣，可以详细了解一下吗？",
        "messageType": null,
        "interactionType": "CONSULTATION",
        "targetType": "VEHICLE",
        "targetId": "5",
        "read": true,
        "sendTime": "2023-03-08T09:15:20",
        "expireTime": null,
        "priority": 1,
        "actionType": "VEHICLE",
        "actionValue": "5"
      }
      // 更多聊天记录...
    ]
  }
}
```

#### 2.1.4 发送消息（需要登录）

- **接口描述**：向指定用户发送消息，可选关联车辆
- **请求方式**：POST
- **接口URL**：`/api/message/send`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| receiverId | Integer | 是 | - | 接收者ID |
| carId | Integer | 否 | null | 关联车辆ID |
| content | String | 是 | - | 消息内容 |

**响应数据**：

```json
{
  "code": 200,
  "message": "发送消息成功",
  "data": 23  // 消息ID
}
```

#### 2.1.5 标记消息为已读（需要登录）

- **接口描述**：将指定消息标记为已读状态
- **请求方式**：PUT
- **接口URL**：`/api/message/{messageId}/read`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| messageId | Integer | 是 | - | 消息ID（路径参数） |

**响应数据**：

```json
{
  "code": 200,
  "message": "标记已读成功",
  "data": null
}
```

#### 2.1.6 批量标记消息为已读（需要登录）

- **接口描述**：将多条消息批量标记为已读状态
- **请求方式**：PUT
- **接口URL**：`/api/message/read/batch`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| messageIds | List<Integer> | 是 | - | 消息ID列表（请求体） |

**请求体示例**：

```json
[1, 2, 3, 4, 5]
```

**响应数据**：

```json
{
  "code": 200,
  "message": "批量标记已读成功",
  "data": 5  // 成功标记的消息数量
}
```

### 2.2 预约管理接口

#### 2.2.1 创建预约（需要登录）

- **接口描述**：创建一个新的预约，需要指定车辆ID、经销商ID、预约类型和时间
- **请求方式**：POST
- **接口URL**：`/api/appointment/create`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| carId | Integer | 是 | - | 车辆ID |
| dealerId | Integer | 是 | - | 经销商ID |
| appointmentType | String | 是 | - | 预约类型：看车、试驾 |
| appointmentTime | String | 是 | - | 预约时间，格式：yyyy-MM-dd HH:mm:ss |
| remarks | String | 否 | null | 备注信息 |

**响应数据**：

```json
{
  "code": 200,
  "message": "创建预约成功",
  "data": 12  // 预约ID
}
```

#### 2.2.2 获取用户预约列表（需要登录）

- **接口描述**：获取当前用户的预约列表，可根据状态筛选
- **请求方式**：GET
- **接口URL**：`/api/appointment/user`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| status | String | 否 | null | 预约状态：待确认、已确认、已完成、已取消 |
| page | Integer | 否 | 1 | 当前页码 |
| size | Integer | 否 | 10 | 每页大小 |

**响应数据**：

```json
{
  "code": 200,
  "message": "获取预约列表成功",
  "data": {
    "total": 4,
    "pages": 1,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "userId": 10,
        "userName": "张三",
        "userPhone": "13800138000",
        "carId": 3,
        "carInfo": "奔驰 E级",
        "dealerId": 2,
        "dealerName": "北京星驰汽车销售有限公司",
        "appointmentType": "看车",
        "appointmentTime": "2023-03-15T10:00:00",
        "status": "待确认",
        "remarks": "希望有销售顾问专门介绍",
        "createTime": "2023-03-08T21:33:53"
      }
      // 更多预约记录...
    ]
  }
}
```

#### 2.2.3 获取经销商预约列表（经销商专用）

- **接口描述**：获取当前经销商的预约列表，可根据状态筛选（仅经销商可用）
- **请求方式**：GET
- **接口URL**：`/api/appointment/dealer`
- **是否需要登录**：是（经销商账号）

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| status | String | 否 | null | 预约状态：待确认、已确认、已完成、已取消 |
| page | Integer | 否 | 1 | 当前页码 |
| size | Integer | 否 | 10 | 每页大小 |

**响应数据**：

```json
{
  "code": 200,
  "message": "获取预约列表成功",
  "data": {
    "total": 8,
    "pages": 1,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 2,
        "userId": 14,
        "userName": "李四",
        "userPhone": "13912345678",
        "carId": 5,
        "carInfo": "宝马 3系",
        "dealerId": 3,
        "dealerName": "上海宝信汽车销售服务有限公司",
        "appointmentType": "试驾",
        "appointmentTime": "2023-03-16T14:30:00",
        "status": "待确认",
        "remarks": "",
        "createTime": "2023-03-08T18:22:15"
      }
      // 更多预约记录...
    ]
  }
}
```

#### 2.2.4 更新预约状态（经销商专用）

- **接口描述**：经销商更新预约的状态（仅经销商可用）
- **请求方式**：PUT
- **接口URL**：`/api/appointment/dealer/{appointmentId}/status`
- **是否需要登录**：是（经销商账号）

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| appointmentId | Integer | 是 | - | 预约ID（路径参数） |
| status | String | 是 | - | 新状态：已确认、已完成、已取消 |

**响应数据**：

```json
{
  "code": 200,
  "message": "更新预约状态成功",
  "data": null
}
```

#### 2.2.5 取消预约（用户专用）

- **接口描述**：用户取消自己的预约
- **请求方式**：PUT
- **接口URL**：`/api/appointment/{appointmentId}/cancel`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| appointmentId | Integer | 是 | - | 预约ID（路径参数） |

**响应数据**：

```json
{
  "code": 200,
  "message": "取消预约成功",
  "data": null
}
```

#### 2.2.6 获取即将到来的预约（需要登录）

- **接口描述**：获取当前用户即将到来的预约列表（状态为待确认或已确认）
- **请求方式**：GET
- **接口URL**：`/api/appointment/upcoming`
- **是否需要登录**：是

**请求参数**：

| 参数名 | 类型 | 是否必须 | 默认值 | 说明 |
| ------ | ---- | -------- | ------ | ---- |
| page | Integer | 否 | 1 | 当前页码 |
| size | Integer | 否 | 10 | 每页大小 |

**响应数据**：

```json
{
  "code": 200,
  "message": "获取即将到来的预约成功",
  "data": {
    "total": 2,
    "pages": 1,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 3,
        "userId": 10,
        "userName": "张三",
        "userPhone": "13800138000",
        "carId": 8,
        "carInfo": "丰田 凯美瑞",
        "dealerId": 5,
        "dealerName": "广州丰汽销售服务有限公司",
        "appointmentType": "看车",
        "appointmentTime": "2023-03-18T09:30:00",
        "status": "已确认",
        "remarks": "",
        "createTime": "2023-03-09T10:12:33"
      }
      // 更多预约记录...
    ]
  }
}
```

## 3. 错误码

| 错误码 | 描述 |
| ------ | ---- |
| 5000 | 系统错误 |
| 5001 | 参数错误 |
| 5002 | 未授权 |
| 5003 | 无权限访问 |
| 5100 | 消息系统异常 |
| 5101 | 消息不存在 |
| 5102 | 无权限操作该消息 |
| 5103 | 消息发送失败 |
| 5110 | 预约系统异常 |
| 5111 | 预约不存在 |
| 5112 | 预约状态不允许修改 |
| 5113 | 预约时间无效 |
| 5114 | 预约时间冲突 |
| 5115 | 无权限操作该预约 |

## 4. 数据模型

### 4.1 消息数据传输对象（MessageDTO）

| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| id | Integer | 消息ID |
| fromUserId | Integer | 发送者用户ID |
| toUserId | Integer | 接收者用户ID |
| fromUserName | String | 发送者用户名 |
| fromUserAvatar | String | 发送者头像URL |
| carId | Integer | 关联车辆ID |
| carInfo | String | 关联车辆信息（品牌+型号） |
| title | String | 消息标题 |
| content | String | 消息内容 |
| messageType | String | 消息类型：SYSTEM/MARKETING/NOTICE/ACTIVITY等 |
| interactionType | String | 交互类型：COMMENT/REPLY/QUESTION/CONSULTATION等 |
| targetType | String | 目标类型：VEHICLE/COMMENT/POST/DEALER等 |
| targetId | String | 目标ID |
| read | Boolean | 是否已读 |
| sendTime | LocalDateTime | 发送时间 |
| expireTime | LocalDateTime | 过期时间 |
| priority | Integer | 优先级：1-低，2-中，3-高 |
| actionType | String | 动作类型：URL/ACTIVITY/VEHICLE/NONE等 |
| actionValue | String | 动作值，如URL链接等 |

### 4.2 预约数据传输对象（AppointmentDTO）

| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| id | Integer | 预约ID |
| userId | Integer | 用户ID |
| userName | String | 用户名称 |
| userPhone | String | 用户电话 |
| carId | Integer | 车辆ID |
| carInfo | String | 车辆信息（品牌+型号） |
| dealerId | Integer | 经销商ID |
| dealerName | String | 经销商名称 |
| appointmentType | String | 预约类型：看车、试驾 |
| appointmentTime | LocalDateTime | 预约时间 |
| status | String | 预约状态：待确认、已确认、已完成、已取消 |
| remarks | String | 备注信息 |
| createTime | LocalDateTime | 创建时间 |

## 5. 注意事项

1. 所有需要登录的接口必须在请求头中包含有效的token
2. 所有接口URL都需要添加`/api`前缀
3. 发送消息时，如果指定了carId，系统将自动关联该车辆信息
4. 预约时间必须是未来的时间，且不能与已确认的预约时间冲突
5. 经销商相关接口只有经销商类型的用户才能调用，普通用户调用将返回403错误
6. 标记已读操作只能由消息的接收者执行
7. 取消预约只能由预约的创建者（用户）执行，且只能取消"待确认"和"已确认"状态的预约

curl -X POST "http://localhost:8080/api/appointment/create" \
  -H "Authorization: your-token" \
  -H "Content-Type: application/json" \
  -d '{
    "carId": 1,
    "dealerId": 1,
    "appointmentType": "看车",  // 必须是"看车"或"试驾"
    "appointmentTime": "2025-03-25T16:30:00",  // 未来的时间，且避开已有预约
    "remarks": "希望了解车辆配置"  // 可选
  }'
