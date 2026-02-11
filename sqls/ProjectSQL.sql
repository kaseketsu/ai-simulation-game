CREATE DATABASE IF NOT EXISTS ai_simulation_game;

USE ai_simulation_game;

CREATE TABLE IF NOT EXISTS user
(
    id             BIGINT      NOT NULL AUTO_INCREMENT COMMENT '用户 id',
    user_detail_id BIGINT      NOT NULL COMMENT '用户详情表 id',
    user_name      VARCHAR(32) NOT NULL COMMENT '用户名',
    user_account   VARCHAR(32) NOT NULL COMMENT '用户账号',
    user_password  VARCHAR(32) NOT NULL COMMENT '密码',
    user_role      VARCHAR(3)  NOT NULL COMMENT '0 - 普通用户; 1 - vip; 9 - 管理员',
    create_time    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator        VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier       VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    UNIQUE KEY (user_account),
    INDEX (user_role)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '用户基础表';

CREATE TABLE IF NOT EXISTS user_detail
(
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '用户 id',
    user_name   VARCHAR(32) NOT NULL COMMENT '用户名',
    user_role   VARCHAR(3)  NOT NULL COMMENT '0 - 普通用户; 1 - vip; 9 - 管理员',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator     VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier    VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    INDEX (user_role)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '用户详情表';

ALTER TABLE user
    DROP COLUMN user_detail_id;
ALTER TABLE user_detail
    ADD COLUMN user_id BIGINT NOT NULL COMMENT '用户 id';
ALTER TABLE play_progress
    ADD COLUMN user_id BIGINT NOT NULL COMMENT '用户 id';
ALTER TABLE user_properties
    ADD COLUMN user_id BIGINT NOT NULL COMMENT '用户 id';


CREATE TABLE IF NOT EXISTS play_progress
(
    id           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '用户 id',
    user_name    VARCHAR(32) NOT NULL COMMENT '用户名',
    user_role    VARCHAR(3)  NOT NULL COMMENT '0 - 普通用户; 1 - vip; 9 - 管理员',
    open_days    INT         NOT NULL DEFAULT 0 COMMENT '经营天数',
    time_period  TIME        NOT NULL COMMENT '营业时段',
    earned_money BIGINT      NOT NULL DEFAULT 0 COMMENT '营业额',
    store_level  INT         NOT NULL DEFAULT 0 COMMENT '店铺等级',
    create_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator      VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier     VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    INDEX (user_role)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '用户游玩进度表';

CREATE TABLE IF NOT EXISTS user_properties
(
    id             BIGINT      NOT NULL AUTO_INCREMENT COMMENT '用户 id',
    user_name      VARCHAR(32) NOT NULL COMMENT '用户名',
    user_role      VARCHAR(3)  NOT NULL COMMENT '0 - 普通用户; 1 - vip; 9 - 管理员',
    store_type     VARCHAR(3)  NOT NULL COMMENT '0 - 饮品, 1 - 主食',
    sense          INT         NOT NULL DEFAULT 0 COMMENT '眼光',
    speaking_skill INT         NOT NULL DEFAULT 0 COMMENT '口才',
    cooking_skill  INT         NOT NULL DEFAULT 0 COMMENT '厨艺',
    create_time    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator        VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier       VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    INDEX (user_role)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '用户相关的属性';

USE ai_simulation_game;

ALTER TABLE user
    DROP COLUMN user_role;

-- 创建角色表

CREATE TABLE IF NOT EXISTS user_role
(
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '用户 id',
    user_id     BIGINT      NOT NULL COMMENT '用户 id',
    user_name   VARCHAR(32) NOT NULL COMMENT '用户名',
    user_role   VARCHAR(3)  NOT NULL COMMENT '0 - 普通用户; 1 - vip; 9 - 管理员',
    role_name   VARCHAR(16) NOT NULL COMMENT '角色名，普通用户, vip, 管理员',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator     VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier    VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    INDEX (user_id),
    INDEX (user_role)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '用户相关的属性';

ALTER TABLE user_role RENAME role;

ALTER TABLE role
    DROP COLUMN user_id;

ALTER TABLE role
    DROP COLUMN user_name;

ALTER TABLE role RENAME COLUMN user_role TO role_code;

-- 创建权限表

CREATE TABLE IF NOT EXISTS permission
(
    id              BIGINT      NOT NULL AUTO_INCREMENT COMMENT '用户 id',
    permission_name VARCHAR(32) NOT NULL COMMENT '权限详情',
    permission_code VARCHAR(3)  NOT NULL COMMENT '0 - 查看, 1 - 修改, 2 - 发布, 9 - 全部',
    create_time     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator         VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier        VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    INDEX (permission_code)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '权限表';

-- 创建权限角色关联表

CREATE TABLE IF NOT EXISTS role_permission
(
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '关联表 id',
    role_id       BIGINT      NOT NULL COMMENT '角色 id',
    permission_id BIGINT      NOT NULL COMMENT '权限 id',
    create_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator       VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier      VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    INDEX idx_role_id (role_id),
    INDEX idx_role_id_permission_id (role_id, permission_id)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '权限表';

-- 创建用户角色关联表

CREATE TABLE IF NOT EXISTS user_role
(
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '关联表 id',
    user_id     BIGINT      NOT NULL COMMENT '用户 id',
    role_id     BIGINT      NOT NULL COMMENT '角色 id',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator     VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier    VARCHAR(16) NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_user_id_role_id (user_id, role_id)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '用户角色关联表';

alter table permission
    add is_deleted int default 0 not null comment '是否删除';

alter table play_progress
    add is_deleted int default 0 not null comment '是否删除';

alter table role
    add is_deleted int default 0 not null comment '是否删除';

alter table role_permission
    add is_deleted int default 0 not null comment '是否删除';

alter table user_detail
    add is_deleted int default 0 not null comment '是否删除';

alter table user_properties
    add is_deleted int default 0 not null comment '是否删除';

alter table user_role
    add is_deleted int default 0 not null comment '是否删除';


-- 创建码表

CREATE TABLE IF NOT EXISTS sys_code
(
    id            BIGINT             NOT NULL AUTO_INCREMENT COMMENT '码表 id',
    code_category VARCHAR(32) UNIQUE NOT NULL COMMENT '码值类型',
    code_key      VARCHAR(32)        NOT NULL COMMENT '码值 key',
    code_value    VARCHAR(32)        NOT NULL COMMENT '码值 value',
    code_desc     VARCHAR(128)       NOT NULL COMMENT '码值描述',
    create_time   DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator       VARCHAR(16)        NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    modifier      VARCHAR(16)        NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    PRIMARY KEY (id),
    INDEX idx_code_key (code_key),
    INDEX idx_code_cat_code_key (code_category, code_key)
) ENGINE = InnoDB
  COLLATE = utf8mb4_0900_as_cs COMMENT = '码值表';

-- 灵材基础库表
CREATE TABLE `spiritual_materials_base`
(
    `id`             INT                                    NOT NULL AUTO_INCREMENT COMMENT '灵材唯一ID',
    `name`           VARCHAR(64) COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '灵材名称',
    `type`           INT                                    NOT NULL COMMENT '灵材类型 (0 - 灵谷, 1 - 灵蔬, 2 - 灵肉, 3 - 灵茶, 4 - 灵果, 5 - 灵酿)',
    `rarity`         INT                                    NOT NULL COMMENT '稀有度 (1 - 普通, 2 - 稀有, 3 - 传世, 4 - 至尊)',
    `url`            VARCHAR(128)                           NOT NULL COMMENT '灵材图片 url',
    `price`          DECIMAL(10, 2)                         NOT NULL COMMENT '灵材价格',
    `store_type`     INT                                    NOT NULL COMMENT '店铺类型 (0 - 固本类, 1 - 淬灵类)',
    `material_grade` INT                                    NOT NULL COMMENT '灵材品阶 (1 - 低阶, 2 - 中阶, 3 - 高阶)',
    `created_at`     DATETIME                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     DATETIME                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by`     VARCHAR(16) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    `updated_by`     VARCHAR(16) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    `is_deleted`     INT                                    NOT NULL DEFAULT 0 COMMENT '是否删除 (0 - 否, 1 - 是)',
    PRIMARY KEY (`id`),
    KEY idx_name (`name`),
    KEY idx_rarity(`rarity`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='灵材基础信息表';


-- 调味料基础库表
CREATE TABLE `spiritual_seasoning_base`
(
    `id`             INT                                    NOT NULL AUTO_INCREMENT COMMENT '调味料唯一ID',
    `name`           VARCHAR(64) COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '调味料名称',
    `url`            VARCHAR(128)                           NOT NULL COMMENT '调味料图片 url',
    `create_time`     DATETIME                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `creator`     VARCHAR(16) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT 'SYS' COMMENT '创建人',
    `modifier`     VARCHAR(16) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT 'SYS' COMMENT '修改人',
    `is_deleted`     INT                                    NOT NULL DEFAULT 0 COMMENT '是否删除 (0 - 否, 1 - 是)',
    PRIMARY KEY (`id`),
    KEY idx_name (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='调味料基础信息表';


USE ai_simulation_game;

-- 批量插入灵材数据：灵谷(类型0) 10条
INSERT INTO spiritual_materials_base (name, type, rarity, url, price, store_type, material_grade, created_by, updated_by, is_deleted)
VALUES
    ('星纹灵谷', 0, 1, '', 68.00, 0, 1, 'SYS', 'SYS', 0),
    ('云穗仙谷', 0, 2, '', 298.00, 0, 1, 'SYS', 'SYS', 0),
    ('翠影灵谷', 0, 2, '', 358.00, 1, 2, 'SYS', 'SYS', 0),
    ('金脉灵谷', 0, 3, '', 998.00, 1, 2, 'SYS', 'SYS', 0),
    ('月华灵谷', 0, 3, '', 1298.00, 0, 3, 'SYS', 'SYS', 0),
    ('玄霜灵谷', 0, 3, '', 1498.00, 1, 3, 'SYS', 'SYS', 0),
    ('九阳灵谷', 0, 4, '', 5898.00, 0, 2, 'SYS', 'SYS', 0),
    ('星河仙谷', 0, 4, '', 8998.00, 1, 3, 'SYS', 'SYS', 0),
    ('青纹灵谷', 0, 1, '', 88.00, 1, 1, 'SYS', 'SYS', 0),
    ('紫府灵谷', 0, 2, '', 328.00, 0, 2, 'SYS', 'SYS', 0);

-- 批量插入灵材数据：灵蔬(类型1) 10条
INSERT INTO spiritual_materials_base (name, type, rarity, url, price, store_type, material_grade, created_by, updated_by, is_deleted)
VALUES
    ('冰蕊灵蔬', 1, 1, '', 78.00, 1, 1, 'SYS', 'SYS', 0),
    ('翠纹仙蔬', 1, 2, '', 318.00, 0, 1, 'SYS', 'SYS', 0),
    ('玄玉灵蔬', 1, 2, '', 368.00, 1, 2, 'SYS', 'SYS', 0),
    ('炎心灵蔬', 1, 3, '', 1098.00, 0, 2, 'SYS', 'SYS', 0),
    ('清露灵蔬', 1, 3, '', 1398.00, 1, 3, 'SYS', 'SYS', 0),
    ('雷纹灵蔬', 1, 3, '', 1598.00, 0, 3, 'SYS', 'SYS', 0),
    ('瑶池仙蔬', 1, 4, '', 6298.00, 1, 2, 'SYS', 'SYS', 0),
    ('鸿蒙灵蔬', 1, 4, '', 9598.00, 0, 3, 'SYS', 'SYS', 0),
    ('青雾灵蔬', 1, 1, '', 98.00, 0, 1, 'SYS', 'SYS', 0),
    ('幻彩灵蔬', 1, 2, '', 348.00, 1, 2, 'SYS', 'SYS', 0);

-- 批量插入灵材数据：灵肉(类型2) 10条
INSERT INTO spiritual_materials_base (name, type, rarity, url, price, store_type, material_grade, created_by, updated_by, is_deleted)
VALUES
    ('玄兽灵肉', 2, 1, '', 128.00, 0, 1, 'SYS', 'SYS', 0),
    ('焰虎灵肉', 2, 2, '', 498.00, 1, 1, 'SYS', 'SYS', 0),
    ('冰熊灵肉', 2, 2, '', 558.00, 0, 2, 'SYS', 'SYS', 0),
    ('雷蛟灵肉', 2, 3, '', 1898.00, 1, 2, 'SYS', 'SYS', 0),
    ('金麟灵肉', 2, 3, '', 2298.00, 0, 3, 'SYS', 'SYS', 0),
    ('风凰灵肉', 2, 3, '', 2598.00, 1, 3, 'SYS', 'SYS', 0),
    ('麒麟灵肉', 2, 4, '', 8998.00, 0, 2, 'SYS', 'SYS', 0),
    ('混沌兽肉', 2, 4, '', 15998.00, 1, 3, 'SYS', 'SYS', 0),
    ('黑狼灵肉', 2, 1, '', 148.00, 1, 1, 'SYS', 'SYS', 0),
    ('碧蟒灵肉', 2, 2, '', 528.00, 0, 2, 'SYS', 'SYS', 0);

-- 批量插入灵材数据：灵茶(类型3) 10条
INSERT INTO spiritual_materials_base (name, type, rarity, url, price, store_type, material_grade, created_by, updated_by, is_deleted)
VALUES
    ('青雾灵茶', 3, 1, '', 98.00, 1, 1, 'SYS', 'SYS', 0),
    ('白露仙茶', 3, 2, '', 398.00, 0, 1, 'SYS', 'SYS', 0),
    ('紫韵灵茶', 3, 2, '', 458.00, 1, 2, 'SYS', 'SYS', 0),
    ('金芽灵茶', 3, 3, '', 1298.00, 0, 2, 'SYS', 'SYS', 0),
    ('月华仙茶', 3, 3, '', 1698.00, 1, 3, 'SYS', 'SYS', 0),
    ('鸿蒙灵茶', 3, 3, '', 1898.00, 0, 3, 'SYS', 'SYS', 0),
    ('瑶池仙茶', 3, 4, '', 6998.00, 1, 2, 'SYS', 'SYS', 0),
    ('天道灵茶', 3, 4, '', 12998.00, 0, 3, 'SYS', 'SYS', 0),
    ('绿萼灵茶', 3, 1, '', 118.00, 0, 1, 'SYS', 'SYS', 0),
    ('丹枫灵茶', 3, 2, '', 428.00, 1, 2, 'SYS', 'SYS', 0);

-- 批量插入灵材数据：灵果(类型4) 10条
INSERT INTO spiritual_materials_base (name, type, rarity, url, price, store_type, material_grade, created_by, updated_by, is_deleted)
VALUES
    ('青纹灵果', 4, 1, '', 118.00, 0, 1, 'SYS', 'SYS', 0),
    ('红珠灵果', 4, 2, '', 458.00, 1, 1, 'SYS', 'SYS', 0),
    ('冰魄灵果', 4, 2, '', 518.00, 0, 2, 'SYS', 'SYS', 0),
    ('火麟灵果', 4, 3, '', 1698.00, 1, 2, 'SYS', 'SYS', 0),
    ('紫府仙果', 4, 3, '', 2098.00, 0, 3, 'SYS', 'SYS', 0),
    ('星纹灵果', 4, 3, '', 2398.00, 1, 3, 'SYS', 'SYS', 0),
    ('九转仙果', 4, 4, '', 7998.00, 0, 2, 'SYS', 'SYS', 0),
    ('混沌圣果', 4, 4, '', 14998.00, 1, 3, 'SYS', 'SYS', 0),
    ('黄玉灵果', 4, 1, '', 138.00, 1, 1, 'SYS', 'SYS', 0),
    ('碧霞灵果', 4, 2, '', 488.00, 0, 2, 'SYS', 'SYS', 0);

-- 批量插入灵材数据：灵酿(类型5) 10条
INSERT INTO spiritual_materials_base (name, type, rarity, url, price, store_type, material_grade, created_by, updated_by, is_deleted)
VALUES
    ('清露灵酿', 5, 1, '', 158.00, 1, 1, 'SYS', 'SYS', 0),
    ('桂花仙酿', 5, 2, '', 598.00, 0, 1, 'SYS', 'SYS', 0),
    ('青梅灵酿', 5, 2, '', 658.00, 1, 2, 'SYS', 'SYS', 0),
    ('赤霞仙酿', 5, 3, '', 2198.00, 0, 2, 'SYS', 'SYS', 0),
    ('玉液灵酿', 5, 3, '', 2598.00, 1, 3, 'SYS', 'SYS', 0),
    ('琼浆仙酿', 5, 3, '', 2898.00, 0, 3, 'SYS', 'SYS', 0),
    ('龙涎灵酿', 5, 4, '', 9998.00, 1, 2, 'SYS', 'SYS', 0),
    ('鸿蒙圣酿', 5, 4, '', 18998.00, 0, 3, 'SYS', 'SYS', 0),
    ('竹叶灵酿', 5, 1, '', 178.00, 0, 1, 'SYS', 'SYS', 0),
    ('玫瑰灵酿', 5, 2, '', 628.00, 1, 2, 'SYS', 'SYS', 0);










USE ai_simulation_game;

-- 批量插入灵材基础数据（总计96条，6个type各16条）
INSERT INTO spiritual_materials_base (name, type, url, store_type, group_id, price, rarity, is_deleted)
VALUES
-- ======================================
-- type0：灵谷（4组：1-4，16条）| 固本(0)：1/3组，淬灵(1)：2/4组
-- ======================================
-- 1组：青禾灵谷系列（group1，固本0）
('青禾灵谷', 0, '/spiritual/type0/group1/rarity1.png', 0, 1, 1000, 1, 0),
('碧纹青禾谷', 0, '/spiritual/type0/group1/rarity2.png', 0, 1, 2000, 2, 0),
('紫金灵谷穗', 0, '/spiritual/type0/group1/rarity3.png', 0, 1, 5000, 3, 0),
('鸿蒙灵谷种', 0, '/spiritual/type0/group1/rarity4.png', 0, 1, 15000, 4, 0),
-- 2组：黄云粟米系列（group2，淬灵1）
('黄云粟米', 0, '/spiritual/type0/group2/rarity1.png', 1, 2, 900, 1, 0),
('玄光粟米粒', 0, '/spiritual/type0/group2/rarity2.png', 1, 2, 1800, 2, 0),
('九转粟米芯', 0, '/spiritual/type0/group2/rarity3.png', 1, 2, 4500, 3, 0),
('混沌粟米魂', 0, '/spiritual/type0/group2/rarity4.png', 1, 2, 13500, 4, 0),
-- 3组：赤焰麦种系列（group3，固本0）
('赤焰麦种', 0, '/spiritual/type0/group3/rarity1.png', 0, 3, 1100, 1, 0),
('炎纹赤焰麦', 0, '/spiritual/type0/group3/rarity2.png', 0, 3, 2200, 2, 0),
('焚天麦芒穗', 0, '/spiritual/type0/group3/rarity3.png', 0, 3, 5500, 3, 0),
('洪荒麦源种', 0, '/spiritual/type0/group3/rarity4.png', 0, 3, 16500, 4, 0),
-- 4组：寒水稻禾系列（group4，淬灵1）
('寒水稻禾', 0, '/spiritual/type0/group4/rarity1.png', 1, 4, 950, 1, 0),
('冰魄稻禾茎', 0, '/spiritual/type0/group4/rarity2.png', 1, 4, 1900, 2, 0),
('凝霜稻禾穗', 0, '/spiritual/type0/group4/rarity3.png', 1, 4, 4750, 3, 0),
('极寒稻禾根', 0, '/spiritual/type0/group4/rarity4.png', 1, 4, 14250, 4, 0),

-- ======================================
-- type1：灵蔬（4组：5-8，16条）| 固本(0)：5/7组，淬灵(1)：6/8组
-- ======================================
-- 5组：清芯灵菜系列（group5，固本0）
('清芯灵菜', 1, '/spiritual/type1/group5/rarity1.png', 0, 5, 1000, 1, 0),
('翠纹清芯菜', 1, '/spiritual/type1/group5/rarity2.png', 0, 5, 2000, 2, 0),
('紫韵清芯茎', 1, '/spiritual/type1/group5/rarity3.png', 0, 5, 5000, 3, 0),
('先天清芯芯', 1, '/spiritual/type1/group5/rarity4.png', 0, 5, 15000, 4, 0),
-- 6组：赤焰灵芹系列（group6，淬灵1）
('赤焰灵芹', 1, '/spiritual/type1/group6/rarity1.png', 1, 6, 900, 1, 0),
('火纹灵芹段', 1, '/spiritual/type1/group6/rarity2.png', 1, 6, 1800, 2, 0),
('焚天灵芹芯', 1, '/spiritual/type1/group6/rarity3.png', 1, 6, 4500, 3, 0),
('混沌灵芹魂', 1, '/spiritual/type1/group6/rarity4.png', 1, 6, 13500, 4, 0),
-- 7组：碧叶灵苔系列（group7，固本0）
('碧叶灵苔', 1, '/spiritual/type1/group7/rarity1.png', 0, 7, 1100, 1, 0),
('玉纹碧叶苔', 1, '/spiritual/type1/group7/rarity2.png', 0, 7, 2200, 2, 0),
('仙韵碧叶芯', 1, '/spiritual/type1/group7/rarity3.png', 0, 7, 5500, 3, 0),
('鸿蒙碧叶种', 1, '/spiritual/type1/group7/rarity4.png', 0, 7, 16500, 4, 0),
-- 8组：墨影灵菇系列（group8，淬灵1）
('墨影灵菇', 1, '/spiritual/type1/group8/rarity1.png', 1, 8, 950, 1, 0),
('玄纹墨影菇', 1, '/spiritual/type1/group8/rarity2.png', 1, 8, 1900, 2, 0),
('暗影墨影伞', 1, '/spiritual/type1/group8/rarity3.png', 1, 8, 4750, 3, 0),
('幽冥墨影菌', 1, '/spiritual/type1/group8/rarity4.png', 1, 8, 14250, 4, 0),

-- ======================================
-- type2：灵肉（4组：9-12，16条）| 固本(0)：9/11组，淬灵(1)：10/12组
-- ======================================
-- 9组：青纹兽脯系列（group9，固本0）
('青纹兽脯', 2, '/spiritual/type2/group9/rarity1.png', 0, 9, 1000, 1, 0),
('碧鳞兽腱肉', 2, '/spiritual/type2/group9/rarity2.png', 0, 9, 2000, 2, 0),
('紫金兽心肉', 2, '/spiritual/type2/group9/rarity3.png', 0, 9, 5000, 3, 0),
('鸿蒙兽本源肉',2,'/spiritual/type2/group9/rarity4.png',0,9,15000,4,0),
-- 10组：赤火狐肉系列（group10，淬灵1）
('赤火狐肉', 2, '/spiritual/type2/group10/rarity1.png',1,10,900,1,0),
('炎焰狐腿肉', 2, '/spiritual/type2/group10/rarity2.png',1,10,1800,2,0),
('焚天狐心髓', 2, '/spiritual/type2/group10/rarity3.png',1,10,4500,3,0),
('混沌狐灵肉', 2, '/spiritual/type2/group10/rarity4.png',1,10,13500,4,0),
-- 11组：玄水蛟肉系列（group11，固本0）
('玄水蛟肉', 2, '/spiritual/type2/group11/rarity1.png',0,11,1100,1,0),
('冰魄蛟腱肉', 2, '/spiritual/type2/group11/rarity2.png',0,11,2200,2,0),
('凝霜蛟丹肉', 2, '/spiritual/type2/group11/rarity3.png',0,11,5500,3,0),
('洪荒蛟本源肉',2,'/spiritual/type2/group11/rarity4.png',0,11,16500,4,0),
-- 12组：墨影豹肉系列（group12，淬灵1）
('墨影豹肉', 2, '/spiritual/type2/group12/rarity1.png',1,12,950,1,0),
('暗影豹里脊', 2, '/spiritual/type2/group12/rarity2.png',1,12,1900,2,0),
('幽冥豹骨血', 2, '/spiritual/type2/group12/rarity3.png',1,12,4750,3,0),
('炼狱豹魂肉', 2, '/spiritual/type2/group12/rarity4.png',1,12,14250,4,0),

-- ======================================
-- type3：灵茶（4组：13-16，16条）| 固本(0)：13/15组，淬灵(1)：14/16组
-- ======================================
-- 13组：清露灵茶系列（group13，固本0）
('清露灵茶', 3, '/spiritual/type3/group13/rarity1.png',0,13,1000,1,0),
('碧芽灵茶尖', 3, '/spiritual/type3/group13/rarity2.png',0,13,2000,2,0),
('紫金灵茶膏', 3, '/spiritual/type3/group13/rarity3.png',0,13,5000,3,0),
('先天灵茶蕊', 3, '/spiritual/type3/group13/rarity4.png',0,13,15000,4,0),
-- 14组：赤焰灵茶系列（group14，淬灵1）
('赤焰灵茶', 3, '/spiritual/type3/group14/rarity1.png',1,14,900,1,0),
('炎叶灵茶片', 3, '/spiritual/type3/group14/rarity2.png',1,14,1800,2,0),
('焚天灵茶粉', 3, '/spiritual/type3/group14/rarity3.png',1,14,4500,3,0),
('混沌灵茶魂', 3, '/spiritual/type3/group14/rarity4.png',1,14,13500,4,0),
-- 15组：玄霜灵茶系列（group15，固本0）
('玄霜灵茶', 3, '/spiritual/type3/group15/rarity1.png',0,15,1100,1,0),
('冰芽灵茶芯', 3, '/spiritual/type3/group15/rarity2.png',0,15,2200,2,0),
('仙韵灵茶液', 3, '/spiritual/type3/group15/rarity3.png',0,15,5500,3,0),
('鸿蒙灵茶种', 3, '/spiritual/type3/group15/rarity4.png',0,15,16500,4,0),
-- 16组：墨韵灵茶系列（group16，淬灵1）
('墨韵灵茶', 3, '/spiritual/type3/group16/rarity1.png',1,16,950,1,0),
('玄纹灵茶叶', 3, '/spiritual/type3/group16/rarity2.png',1,16,1900,2,0),
('暗影灵茶膏', 3, '/spiritual/type3/group16/rarity3.png',1,16,4750,3,0),
('幽冥灵茶精', 3, '/spiritual/type3/group16/rarity4.png',1,16,14250,4,0),

-- ======================================
-- type4：灵果（4组：17-20，16条）| 固本(0)：17/19组，淬灵(1)：18/20组
-- ======================================
-- 17组：青韵灵果系列（group17，固本0）
('青韵灵果', 4, '/spiritual/type4/group17/rarity1.png',0,17,1000,1,0),
('碧纹灵果实', 4, '/spiritual/type4/group17/rarity2.png',0,17,2000,2,0),
('紫金灵果核', 4, '/spiritual/type4/group17/rarity3.png',0,17,5000,3,0),
('先天灵果魂', 4, '/spiritual/type4/group17/rarity4.png',0,17,15000,4,0),
-- 18组：赤火灵桃系列（group18，淬灵1）
('赤火灵桃', 4, '/spiritual/type4/group18/rarity1.png',1,18,900,1,0),
('炎纹灵桃肉', 4, '/spiritual/type4/group18/rarity2.png',1,18,1800,2,0),
('焚天灵桃核', 4, '/spiritual/type4/group18/rarity3.png',1,18,4500,3,0),
('混沌灵桃源', 4, '/spiritual/type4/group18/rarity4.png',1,18,13500,4,0),
-- 19组：冰魄灵梨系列（group19，固本0）
('冰魄灵梨', 4, '/spiritual/type4/group19/rarity1.png',0,19,1100,1,0),
('凝霜灵梨肉', 4, '/spiritual/type4/group19/rarity2.png',0,19,2200,2,0),
('极寒灵梨核', 4, '/spiritual/type4/group19/rarity3.png',0,19,5500,3,0),
('洪荒灵梨种', 4, '/spiritual/type4/group19/rarity4.png',0,19,16500,4,0),
-- 20组：墨影灵柿系列（group20，淬灵1）
('墨影灵柿', 4, '/spiritual/type4/group20/rarity1.png',1,20,950,1,0),
('玄纹灵柿肉', 4, '/spiritual/type4/group20/rarity2.png',1,20,1900,2,0),
('暗影灵柿核', 4, '/spiritual/type4/group20/rarity3.png',1,20,4750,3,0),
('幽冥灵柿精', 4, '/spiritual/type4/group20/rarity4.png',1,20,14250,4,0),

-- ======================================
-- type5：灵酿（4组：21-24，16条）| 固本(0)：21/23组，淬灵(1)：22/24组
-- ======================================
-- 21组：清韵灵酿系列（group21，固本0）
('清韵灵酿', 5, '/spiritual/type5/group21/rarity1.png',0,21,1000,1,0),
('碧纹灵酿液', 5, '/spiritual/type5/group21/rarity2.png',0,21,2000,2,0),
('紫金灵酿浆', 5, '/spiritual/type5/group21/rarity3.png',0,21,5000,3,0),
('先天灵酿髓', 5, '/spiritual/type5/group21/rarity4.png',0,21,15000,4,0),
-- 22组：赤火灵酒系列（group22，淬灵1）
('赤火灵酒', 5, '/spiritual/type5/group22/rarity1.png',1,22,900,1,0),
('炎焰灵酒浆', 5, '/spiritual/type5/group22/rarity2.png',1,22,1800,2,0),
('焚天灵酒髓', 5, '/spiritual/type5/group22/rarity3.png',1,22,4500,3,0),
('混沌灵酒魂', 5, '/spiritual/type5/group22/rarity4.png',1,22,13500,4,0),
-- 23组：玄霜灵醪系列（group23，固本0）
('玄霜灵醪', 5, '/spiritual/type5/group23/rarity1.png',0,23,1100,1,0),
('冰魄灵醪液', 5, '/spiritual/type5/group23/rarity2.png',0,23,2200,2,0),
('仙韵灵醪浆', 5, '/spiritual/type5/group23/rarity3.png',0,23,5500,3,0),
('鸿蒙灵醪源', 5, '/spiritual/type5/group23/rarity4.png',0,23,16500,4,0),
-- 24组：墨韵灵醴系列（group24，淬灵1）
('墨韵灵醴', 5, '/spiritual/type5/group24/rarity1.png',1,24,950,1,0),
('玄纹灵醴液', 5, '/spiritual/type5/group24/rarity2.png',1,24,1900,2,0),
('暗影灵醴浆', 5, '/spiritual/type5/group24/rarity3.png',1,24,4750,3,0),
('幽冥灵醴精', 5, '/spiritual/type5/group24/rarity4.png',1,24,14250,4,0);