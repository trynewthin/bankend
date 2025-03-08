-- 设置数据库字符集
ALTER DATABASE zhixuanche CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 设置连接字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE zhixuanche;

-- 用户表
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    user_type ENUM('NORMAL_USER', 'DEALER', 'ADMIN') NOT NULL,
    register_time DATETIME NOT NULL,
    last_login_time DATETIME,
    status TINYINT NOT NULL DEFAULT 1,
    avatar VARCHAR(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 经销商表
CREATE TABLE Dealers (
    dealer_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    dealer_name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    business_license VARCHAR(100),
    contact_person VARCHAR(50),
    contact_phone VARCHAR(20),
    status TINYINT NOT NULL DEFAULT 0,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 车辆表
CREATE TABLE Cars (
    car_id INT PRIMARY KEY AUTO_INCREMENT,
    dealer_id INT NOT NULL,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year YEAR NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME,
    FOREIGN KEY (dealer_id) REFERENCES Dealers(dealer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 车辆详情表
CREATE TABLE CarDetails (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    car_id INT NOT NULL,
    engine VARCHAR(100),
    transmission VARCHAR(50),
    fuel_type VARCHAR(50),
    fuel_consumption DECIMAL(5,2),
    seats INT,
    color VARCHAR(200),
    body_size VARCHAR(100),
    wheelbase DECIMAL(6,1),
    features TEXT,
    warranty VARCHAR(100),
    FOREIGN KEY (car_id) REFERENCES Cars(car_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 车辆图片表
CREATE TABLE CarImages (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    car_id INT NOT NULL,
    image_type ENUM('缩略图', '完整图1', '完整图2', '完整图3', '完整图4', '完整图5') NOT NULL,
    image_url VARCHAR(200) NOT NULL,
    upload_time DATETIME NOT NULL,
    FOREIGN KEY (car_id) REFERENCES Cars(car_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户偏好表
CREATE TABLE UserPreferences (
    preference_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    price_min DECIMAL(12,2),
    price_max DECIMAL(12,2),
    preferred_brands VARCHAR(200),
    preferred_categories VARCHAR(200),
    other_preferences TEXT,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户行为记录表
CREATE TABLE UserBehaviors (
    behavior_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    behavior_type ENUM('浏览', '搜索', '咨询') NOT NULL,
    behavior_time DATETIME NOT NULL,
    duration INT,
    search_keywords VARCHAR(200),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (car_id) REFERENCES Cars(car_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 收藏表
CREATE TABLE Favorites (
    favorite_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (car_id) REFERENCES Cars(car_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 消息表（完整版）
CREATE TABLE Messages (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    car_id INT,
    content TEXT NOT NULL,
    title VARCHAR(200),
    message_type VARCHAR(50),
    interaction_type VARCHAR(50),
    target_type VARCHAR(50),
    target_id VARCHAR(50),
    send_time DATETIME NOT NULL,
    read_status TINYINT NOT NULL DEFAULT 0,
    expire_time DATETIME,
    priority TINYINT DEFAULT 3,
    action_type VARCHAR(50),
    action_value VARCHAR(500),
    FOREIGN KEY (sender_id) REFERENCES Users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES Users(user_id),
    FOREIGN KEY (car_id) REFERENCES Cars(car_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 预约表
CREATE TABLE Appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    dealer_id INT NOT NULL,
    appointment_type ENUM('看车', '试驾') NOT NULL,
    appointment_time DATETIME NOT NULL,
    status ENUM('待确认', '已确认', '已完成', '已取消') NOT NULL DEFAULT '待确认',
    remarks TEXT,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (car_id) REFERENCES Cars(car_id),
    FOREIGN KEY (dealer_id) REFERENCES Dealers(dealer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 