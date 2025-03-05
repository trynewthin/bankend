USE zhixuanche;

-- 1. 插入用户数据
INSERT INTO Users (username, password, email, phone, user_type, register_time, last_login_time, status, avatar) VALUES
('admin', 'admin123', 'admin@zhixuanche.com', '13888888888', 'ADMIN', NOW(), NOW(), 1, '/images/avatars/admin.png'),
('dealer1', 'dealer123', 'dealer1@example.com', '13800138000', 'DEALER', NOW(), NOW(), 1, '/images/avatars/dealer1.png'),
('dealer2', 'dealer456', 'dealer2@example.com', '13700137000', 'DEALER', NOW(), NOW(), 1, '/images/avatars/dealer2.png'),
('user1', 'user123', 'user1@example.com', '13900139000', 'NORMAL_USER', NOW(), NOW(), 1, '/images/avatars/user1.png'),
('user2', 'user456', 'user2@example.com', '13600136000', 'NORMAL_USER', NOW(), NOW(), 1, '/images/avatars/user2.png'),
('user3', 'user789', 'user3@example.com', '13500135000', 'NORMAL_USER', NOW(), NOW(), 1, '/images/avatars/user3.png');

-- 2. 插入经销商数据
INSERT INTO Dealers (user_id, dealer_name, address, business_license, contact_person, contact_phone, status, description) VALUES
((SELECT user_id FROM Users WHERE username = 'dealer1'), '北京豪车4S店', '北京市朝阳区建国路88号', 'BJ123456789', '张经理', '13800138000', 1, '北京地区豪华车专业销售服务商，提供全方位的购车及售后服务'),
((SELECT user_id FROM Users WHERE username = 'dealer2'), '上海名车汇', '上海市浦东新区世纪大道100号', 'SH987654321', '李总监', '13700137000', 1, '上海最大的进口豪华车销售中心，拥有完善的展厅和试驾场地');

-- 3. 插入车辆基本信息
INSERT INTO Cars (dealer_id, brand, model, year, price, category, status, create_time, update_time) VALUES
((SELECT dealer_id FROM Dealers WHERE dealer_name = '北京豪车4S店'), 'BMW', '宝马5系 530Li', 2023, 479800.00, '中大型轿车', 1, NOW(), NOW()),
((SELECT dealer_id FROM Dealers WHERE dealer_name = '北京豪车4S店'), 'BMW', '宝马X5 xDrive40i', 2023, 829900.00, '中大型SUV', 1, NOW(), NOW()),
((SELECT dealer_id FROM Dealers WHERE dealer_name = '北京豪车4S店'), 'Audi', '奥迪A6L 45TFSI', 2023, 459800.00, '中大型轿车', 1, NOW(), NOW()),
((SELECT dealer_id FROM Dealers WHERE dealer_name = '上海名车汇'), 'Mercedes-Benz', '奔驰E级 E300L', 2023, 498800.00, '中大型轿车', 1, NOW(), NOW()),
((SELECT dealer_id FROM Dealers WHERE dealer_name = '上海名车汇'), 'Mercedes-Benz', '奔驰GLC 300L 4MATIC', 2023, 489800.00, '中型SUV', 1, NOW(), NOW()),
((SELECT dealer_id FROM Dealers WHERE dealer_name = '上海名车汇'), 'Porsche', '保时捷Macan', 2023, 618000.00, '中型SUV', 1, NOW(), NOW());

-- 4. 插入车辆详情信息
INSERT INTO CarDetails (car_id, engine, transmission, fuel_type, fuel_consumption, seats, color, body_size, wheelbase, features, warranty) VALUES
(1, '2.0T 涡轮增压直列四缸', '8速手自一体', '汽油', 6.8, 5, '墨尔本红,雪山白,碳黑色,矿石灰', '5087×1868×1498mm', 3108.0, '语音控制,自动泊车,自适应巡航,无钥匙进入,全景天窗,电动后备厢', '三年不限公里'),
(2, '3.0T 涡轮增压直列六缸', '8速手自一体', '汽油', 8.2, 5, '阿尔卑斯白,黑色,矿石银,青铜棕', '4935×2004×1765mm', 2975.0, '四区空调,空气悬架,全景天窗,自动泊车,电加热座椅,车道保持', '三年不限公里'),
(3, '2.0T 涡轮增压直列四缸', '7速双离合', '汽油', 7.0, 5, '幻影黑,月光白,至尊银', '5050×1886×1487mm', 3024.0, '虚拟仪表盘,矩阵式LED大灯,四区空调,前后排座椅加热,车道保持辅助', '三年不限公里'),
(4, '2.0T 涡轮增压直列四缸', '9速手自一体', '汽油', 7.2, 5, '曜岩黑,北极白,铱银,绿松石蓝', '5056×1860×1490mm', 3079.0, '10.25英寸中控屏,环绕音响,64色氛围灯,无线充电,前排座椅记忆', '三年不限公里'),
(5, '2.0T 涡轮增压直列四缸', '9速手自一体', '汽油', 7.8, 5, '曜岩黑,北极白,铂耀灰,高山蓝', '4792×1893×1648mm', 2973.0, '全地形调节系统,LED智能灯光系统,64色氛围灯,全景天窗,后排娱乐系统', '三年不限公里'),
(6, '2.0T 涡轮增压直列四缸', '7速双离合', '汽油', 8.0, 5, '卡拉拉白,墨胄黑,火山灰,迈阿密蓝', '4708×1927×1624mm', 2807.0, 'Sport Chrono运动计时组件,保时捷动态照明系统(PDLS),BOSE音响系统', '四年不限公里');

-- 5. 插入车辆图片信息（使用实际存在的本地图片路径）
INSERT INTO CarImages (car_id, image_type, image_url, upload_time) VALUES
(1, '缩略图', '/images/cars/bmw/5-series-thumb.jpg', NOW()),
(1, '完整图1', '/images/cars/bmw/5-series-1.jpg', NOW()),
(2, '缩略图', '/images/cars/bmw/x5-thumb.jpg', NOW()),
(2, '完整图1', '/images/cars/bmw/x5-1.jpg', NOW()),
(3, '缩略图', '/images/cars/audi/a6l-thumb.jpg', NOW()),
(4, '缩略图', '/images/cars/mercedes/e300l-thumb.jpg', NOW()),
(6, '缩略图', '/images/cars/porsche/macan-thumb.jpg', NOW());

-- 6. 插入用户偏好数据
INSERT INTO UserPreferences (user_id, price_min, price_max, preferred_brands, preferred_categories, other_preferences, update_time) VALUES
((SELECT user_id FROM Users WHERE username = 'user1'), 450000.00, 600000.00, 'BMW,Mercedes-Benz', '中大型轿车', '希望车辆配置高，颜色为黑色或白色', NOW()),
((SELECT user_id FROM Users WHERE username = 'user2'), 600000.00, 900000.00, 'Porsche,BMW', '中型SUV,中大型SUV', '需要全景天窗和四驱系统', NOW()),
((SELECT user_id FROM Users WHERE username = 'user3'), 400000.00, 550000.00, 'Audi,Mercedes-Benz', '中大型轿车', '希望油耗低，配置高', NOW());

-- 7. 插入用户行为记录 (修正版)
INSERT INTO UserBehaviors (user_id, car_id, behavior_type, behavior_time, duration, search_keywords) VALUES
((SELECT user_id FROM Users WHERE username = 'user1'), 1, '浏览', DATE_SUB(NOW(), INTERVAL 5 DAY), 300, NULL),
((SELECT user_id FROM Users WHERE username = 'user1'), 4, '浏览', DATE_SUB(NOW(), INTERVAL 4 DAY), 220, NULL),
((SELECT user_id FROM Users WHERE username = 'user1'), 1, '咨询', DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, NULL),
((SELECT user_id FROM Users WHERE username = 'user2'), 2, '浏览', DATE_SUB(NOW(), INTERVAL 6 DAY), 180, NULL),
((SELECT user_id FROM Users WHERE username = 'user2'), 5, '浏览', DATE_SUB(NOW(), INTERVAL 5 DAY), 250, NULL),
((SELECT user_id FROM Users WHERE username = 'user2'), 6, '浏览', DATE_SUB(NOW(), INTERVAL 3 DAY), 320, NULL),
((SELECT user_id FROM Users WHERE username = 'user2'), 6, '咨询', DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, NULL),
((SELECT user_id FROM Users WHERE username = 'user3'), 3, '浏览', DATE_SUB(NOW(), INTERVAL 7 DAY), 200, NULL),
((SELECT user_id FROM Users WHERE username = 'user3'), 4, '浏览', DATE_SUB(NOW(), INTERVAL 5 DAY), 270, NULL),
-- 对于搜索行为，我们关联到具体的车辆
((SELECT user_id FROM Users WHERE username = 'user1'), 1, '搜索', DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, 'BMW 5系 北京'),
((SELECT user_id FROM Users WHERE username = 'user2'), 6, '搜索', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, '保时捷 SUV'),
((SELECT user_id FROM Users WHERE username = 'user3'), 3, '搜索', DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, '奥迪A6L 价格');

-- 8. 插入收藏数据
INSERT INTO Favorites (user_id, car_id, create_time) VALUES
((SELECT user_id FROM Users WHERE username = 'user1'), 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
((SELECT user_id FROM Users WHERE username = 'user1'), 4, DATE_SUB(NOW(), INTERVAL 3 DAY)),
((SELECT user_id FROM Users WHERE username = 'user2'), 2, DATE_SUB(NOW(), INTERVAL 5 DAY)),
((SELECT user_id FROM Users WHERE username = 'user2'), 6, DATE_SUB(NOW(), INTERVAL 2 DAY)),
((SELECT user_id FROM Users WHERE username = 'user3'), 3, DATE_SUB(NOW(), INTERVAL 6 DAY));

-- 9. 插入消息数据
INSERT INTO Messages (sender_id, receiver_id, car_id, content, send_time, read_status) VALUES
((SELECT user_id FROM Users WHERE username = 'user1'), (SELECT user_id FROM Users WHERE username = 'dealer1'), 1, '您好，我对这款宝马5系很感兴趣，请问有现车可以看吗？', DATE_SUB(NOW(), INTERVAL 3 DAY), 1),
((SELECT user_id FROM Users WHERE username = 'dealer1'), (SELECT user_id FROM Users WHERE username = 'user1'), 1, '您好，我们店里有现车，欢迎您来店里看车试驾，可以先预约一下时间。', DATE_SUB(NOW(), INTERVAL 3 DAY), 1),
((SELECT user_id FROM Users WHERE username = 'user1'), (SELECT user_id FROM Users WHERE username = 'dealer1'), 1, '好的，请问周末有时间吗？', DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
((SELECT user_id FROM Users WHERE username = 'dealer1'), (SELECT user_id FROM Users WHERE username = 'user1'), 1, '周末的话建议您提前预约，可以通过我们的预约系统选择合适的时间。', DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
((SELECT user_id FROM Users WHERE username = 'user2'), (SELECT user_id FROM Users WHERE username = 'dealer2'), 6, '这款保时捷Macan最低优惠多少？有什么贷款方案？', DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
((SELECT user_id FROM Users WHERE username = 'dealer2'), (SELECT user_id FROM Users WHERE username = 'user2'), 6, '您好，目前这款车型有金融贷款方案，可以享受首付30%，24-36期0利率的活动。详情可以来店里详谈。', DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
((SELECT user_id FROM Users WHERE username = 'user3'), (SELECT user_id FROM Users WHERE username = 'dealer1'), 3, '奥迪A6L有哪些颜色可选？', DATE_SUB(NOW(), INTERVAL 1 DAY), 0),
((SELECT user_id FROM Users WHERE username = 'user2'), (SELECT user_id FROM Users WHERE username = 'dealer2'), 6, '周末我可以来看车吗？', DATE_SUB(NOW(), INTERVAL 1 DAY), 0);

-- 10. 插入预约数据
INSERT INTO Appointments (user_id, car_id, dealer_id, appointment_type, appointment_time, status, remarks, create_time) VALUES
((SELECT user_id FROM Users WHERE username = 'user1'), 1, (SELECT dealer_id FROM Dealers WHERE dealer_name = '北京豪车4S店'), '看车', DATE_ADD(NOW(), INTERVAL 2 DAY), '已确认', '希望有销售顾问专门介绍', DATE_SUB(NOW(), INTERVAL 1 DAY)),
((SELECT user_id FROM Users WHERE username = 'user1'), 1, (SELECT dealer_id FROM Dealers WHERE dealer_name = '北京豪车4S店'), '试驾', DATE_ADD(NOW(), INTERVAL 3 DAY), '待确认', '想体验车辆的动力性能', NOW()),
((SELECT user_id FROM Users WHERE username = 'user2'), 6, (SELECT dealer_id FROM Dealers WHERE dealer_name = '上海名车汇'), '看车', DATE_ADD(NOW(), INTERVAL 1 DAY), '已确认', '顺便了解一下贷款方案', DATE_SUB(NOW(), INTERVAL 2 DAY)),
((SELECT user_id FROM Users WHERE username = 'user2'), 6, (SELECT dealer_id FROM Dealers WHERE dealer_name = '上海名车汇'), '试驾', DATE_ADD(NOW(), INTERVAL 1 DAY), '已确认', NULL, DATE_SUB(NOW(), INTERVAL 2 DAY)),
((SELECT user_id FROM Users WHERE username = 'user3'), 3, (SELECT dealer_id FROM Dealers WHERE dealer_name = '北京豪车4S店'), '看车', DATE_ADD(NOW(), INTERVAL 4 DAY), '待确认', '想了解不同配置的价格差异', DATE_SUB(NOW(), INTERVAL 1 DAY));