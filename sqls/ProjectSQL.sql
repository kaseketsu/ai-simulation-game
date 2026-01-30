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


