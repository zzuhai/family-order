-- 家庭点餐系统数据库
-- 创建数据库
CREATE DATABASE IF NOT EXISTS family_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE family_order;

-- 成员表
CREATE TABLE IF NOT EXISTS member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    openid VARCHAR(100) NOT NULL UNIQUE COMMENT '微信openid',
    nickname VARCHAR(50) DEFAULT '家庭成员' COMMENT '昵称',
    avatar VARCHAR(500) COMMENT '头像URL',
    role VARCHAR(20) DEFAULT 'user' COMMENT '角色: chef(厨师)/user(普通成员)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭成员表';

-- 菜品表
CREATE TABLE IF NOT EXISTS dish (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    description VARCHAR(500) COMMENT '菜品描述',
    image_url LONGTEXT COMMENT '菜品图片(base64)',
    category VARCHAR(50) DEFAULT '家常菜' COMMENT '分类',
    status INT DEFAULT 1 COMMENT '状态: 1-上架, 0-下架',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- 菜品材料表
CREATE TABLE IF NOT EXISTS dish_ingredient (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    name VARCHAR(100) NOT NULL COMMENT '材料名称',
    amount VARCHAR(50) COMMENT '用量',
    unit VARCHAR(20) COMMENT '单位',
    INDEX idx_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品材料表';

-- 菜品步骤表
CREATE TABLE IF NOT EXISTS dish_step (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    step_number INT NOT NULL COMMENT '步骤序号',
    description VARCHAR(500) NOT NULL COMMENT '步骤描述',
    image_url LONGTEXT COMMENT '步骤图片(base64)',
    INDEX idx_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品步骤表';

-- 每日菜单表
CREATE TABLE IF NOT EXISTS daily_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    menu_date DATE NOT NULL COMMENT '菜单日期',
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    created_by BIGINT COMMENT '创建人ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_menu_date (menu_date),
    INDEX idx_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日菜单表';

-- 订单表
CREATE TABLE IF NOT EXISTS order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL COMMENT '成员ID',
    menu_date DATE NOT NULL COMMENT '点餐日期',
    status VARCHAR(20) DEFAULT 'submitted' COMMENT '状态: submitted-已提交, preparing-准备中, completed-已完成',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_member_id (member_id),
    INDEX idx_menu_date (menu_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    daily_menu_id BIGINT COMMENT '每日菜单ID（已废弃，可为空）',
    INDEX idx_order_id (order_id),
    INDEX idx_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 清空旧数据
TRUNCATE TABLE dish_step;
TRUNCATE TABLE dish_ingredient;
TRUNCATE TABLE order_item;
TRUNCATE TABLE `order`;
TRUNCATE TABLE daily_menu;
TRUNCATE TABLE dish;

-- 插入示例数据

-- 荤菜
INSERT INTO dish (name, description, category, status, image_url) VALUES
('红烧肉', '经典家常菜，肥而不腻，入口即化', '荤菜', 1, 'https://picsum.photos/seed/hongshaorou/400/300'),
('糖醋排骨', '酸甜适中，肉质酥软', '荤菜', 1, 'https://picsum.photos/seed/tangcupaigu/400/300'),
('宫保鸡丁', '麻辣鲜香，鸡肉嫩滑', '荤菜', 1, 'https://picsum.photos/seed/gongbaojiding/400/300'),
('鱼香肉丝', '鱼香味浓，肉丝滑嫩', '荤菜', 1, 'https://picsum.photos/seed/yuxiangrousi/400/300'),
('红烧鱼', '鱼肉鲜嫩，汁浓味美', '荤菜', 1, 'https://picsum.photos/seed/hongshaoyu/400/300'),
('回锅肉', '色泽红亮，肥而不腻', '荤菜', 1, 'https://picsum.photos/seed/huiguorou/400/300');

-- 素菜
INSERT INTO dish (name, description, category, status, image_url) VALUES
('清炒时蔬', '清淡爽口，营养丰富', '素菜', 1, 'https://picsum.photos/seed/qingchaoshishu/400/300'),
('蒜蓉西兰花', '清脆爽口，蒜香浓郁', '素菜', 1, 'https://picsum.photos/seed/suanrongxilanhua/400/300'),
('地三鲜', '茄子土豆青椒的完美结合', '素菜', 1, 'https://picsum.photos/seed/disanxian/400/300'),
('干煸豆角', '外焦里嫩，香辣可口', '素菜', 1, 'https://picsum.photos/seed/ganbiandoujiao/400/300'),
('酸辣土豆丝', '酸辣开胃，爽脆可口', '素菜', 1, 'https://picsum.photos/seed/suanlatudousi/400/300');

-- 汤羹
INSERT INTO dish (name, description, category, status, image_url) VALUES
('番茄蛋汤', '酸甜开胃，营养丰富', '汤羹', 1, 'https://picsum.photos/seed/fanqiedantang/400/300'),
('紫菜蛋花汤', '清淡鲜美，简单易做', '汤羹', 1, 'https://picsum.photos/seed/zicaidanhuatang/400/300'),
('冬瓜排骨汤', '清热解暑，营养滋补', '汤羹', 1, 'https://picsum.photos/seed/dongguapaigutang/400/300'),
('玉米排骨汤', '香甜可口，营养丰富', '汤羹', 1, 'https://picsum.photos/seed/yumipaigutang/400/300');

-- 家常菜
INSERT INTO dish (name, description, category, status, image_url) VALUES
('番茄炒蛋', '酸甜可口，简单易做', '家常菜', 1, 'https://picsum.photos/seed/fanqiechaodan/400/300'),
('青椒肉丝', '清脆爽口，肉丝滑嫩', '家常菜', 1, 'https://picsum.photos/seed/qingjiaorousi/400/300'),
('麻婆豆腐', '麻辣鲜香，下饭必备', '家常菜', 1, 'https://picsum.photos/seed/mapodoufu/400/300'),
('木须肉', '色香味俱全，营养均衡', '家常菜', 1, 'https://picsum.photos/seed/muxurou/400/300');

-- 凉菜
INSERT INTO dish (name, description, category, status, image_url) VALUES
('拍黄瓜', '清爽解腻，简单快手', '凉菜', 1, 'https://picsum.photos/seed/paihuanggua/400/300'),
('凉拌木耳', '爽脆可口，营养健康', '凉菜', 1, 'https://picsum.photos/seed/liangbanmuer/400/300'),
('糖拌西红柿', '酸甜可口，清凉解暑', '凉菜', 1, 'https://picsum.photos/seed/tangbanxihongshi/400/300'),
('凉拌海带丝', '清爽开胃，低脂健康', '凉菜', 1, 'https://picsum.photos/seed/liangbanhaidaisi/400/300');

-- 主食
INSERT INTO dish (name, description, category, status, image_url) VALUES
('蛋炒饭', '粒粒分明，香气扑鼻', '主食', 1, 'https://picsum.photos/seed/danchaofan/400/300'),
('炸酱面', '酱香浓郁，劲道爽滑', '主食', 1, 'https://picsum.photos/seed/zhajiangmian/400/300'),
('手擀面', '筋道爽滑，传统美味', '主食', 1, 'https://picsum.photos/seed/shouganmian/400/300'),
('米饭', '软糯香甜，营养主食', '主食', 1, 'https://picsum.photos/seed/mifan/400/300');

-- 示例材料
INSERT INTO dish_ingredient (dish_id, name, amount, unit) VALUES
-- 红烧肉
(1, '五花肉', '500', 'g'),
(1, '冰糖', '30', 'g'),
(1, '生抽', '20', 'ml'),
(1, '老抽', '10', 'ml'),
(1, '料酒', '15', 'ml'),
-- 清炒时蔬
(7, '青菜', '300', 'g'),
(7, '蒜末', '10', 'g'),
(7, '盐', '3', 'g'),
-- 番茄炒蛋
(15, '番茄', '2', '个'),
(15, '鸡蛋', '3', '个'),
(15, '白糖', '5', 'g');

-- 示例步骤
INSERT INTO dish_step (dish_id, step_number, description) VALUES
-- 红烧肉
(1, 1, '五花肉切块，冷水下锅焯水去血水'),
(1, 2, '锅中放少许油，加入冰糖炒糖色'),
(1, 3, '放入五花肉翻炒上色'),
(1, 4, '加入生抽、老抽、料酒调味'),
(1, 5, '加水没过肉块，大火烧开转小火炖1小时'),
-- 清炒时蔬
(7, 1, '青菜洗净切段'),
(7, 2, '锅中热油爆香蒜末'),
(7, 3, '放入青菜大火快炒'),
(7, 4, '加盐调味，出锅装盘'),
-- 番茄炒蛋
(15, 1, '番茄切块，鸡蛋打散'),
(15, 2, '热锅倒油，倒入蛋液炒至凝固盛出'),
(15, 3, '锅中再倒油，放入番茄翻炒出汁'),
(15, 4, '加入少许白糖，倒入炒好的鸡蛋'),
(15, 5, '翻炒均匀即可出锅');