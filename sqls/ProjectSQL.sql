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


