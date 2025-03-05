USE zhixuanche;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 清空预约表
TRUNCATE TABLE Appointments;

-- 清空消息表
TRUNCATE TABLE Messages;

-- 清空收藏表
TRUNCATE TABLE Favorites;

-- 清空用户行为记录表
TRUNCATE TABLE UserBehaviors;

-- 清空用户偏好表
TRUNCATE TABLE UserPreferences;

-- 清空车辆图片表
TRUNCATE TABLE CarImages;

-- 清空车辆详情表
TRUNCATE TABLE CarDetails;

-- 清空车辆表
TRUNCATE TABLE Cars;

-- 清空经销商表
TRUNCATE TABLE Dealers;

-- 清空用户表
TRUNCATE TABLE Users;

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 重置自增ID
ALTER TABLE Users AUTO_INCREMENT = 1;
ALTER TABLE Dealers AUTO_INCREMENT = 1;
ALTER TABLE Cars AUTO_INCREMENT = 1;
ALTER TABLE CarDetails AUTO_INCREMENT = 1;
ALTER TABLE CarImages AUTO_INCREMENT = 1;
ALTER TABLE UserPreferences AUTO_INCREMENT = 1;
ALTER TABLE UserBehaviors AUTO_INCREMENT = 1;
ALTER TABLE Favorites AUTO_INCREMENT = 1;
ALTER TABLE Messages AUTO_INCREMENT = 1;
ALTER TABLE Appointments AUTO_INCREMENT = 1; 