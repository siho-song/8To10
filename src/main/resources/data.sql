-- Drop tables if they exist
DROP TABLE IF EXISTS `NOTIFICATION`;
DROP TABLE IF EXISTS `CAT_UNIT_ACH`;
DROP TABLE IF EXISTS `ACHIEVEMENT`;
DROP TABLE IF EXISTS `N_VALUE`;
DROP TABLE IF EXISTS `N_RANGE`;
DROP TABLE IF EXISTS `N_D_VALUE`;
DROP TABLE IF EXISTS `N_D_RANGE`;
DROP TABLE IF EXISTS `F_SCHEDULE_DETAIL`;
DROP TABLE IF EXISTS `N_SCHEDULE_DETAIL`;
DROP TABLE IF EXISTS `V_SCHEDULE`;
DROP TABLE IF EXISTS `F_SCHEDULE`;
DROP TABLE IF EXISTS `N_SCHEDULE`;
DROP TABLE IF EXISTS `SCHEDULE`;
DROP TABLE IF EXISTS `BOARD_ATTACHMENT`;
DROP TABLE IF EXISTS `REPLY_HEARTS`;
DROP TABLE IF EXISTS `BOARD_SCRAP`;
DROP TABLE IF EXISTS `BOARD_HEARTS`;
DROP TABLE IF EXISTS `REPLY`;
DROP TABLE IF EXISTS `BOARD`;
DROP TABLE IF EXISTS `AUTHENTICATION`;
DROP TABLE IF EXISTS `MEMBER`;

-- 회원
CREATE TABLE `MEMBER`
(
    `member_id`  bigint       NOT NULL AUTO_INCREMENT,
    `username`   varchar(20)  NOT NULL,
    `nickname`   varchar(20)  NOT NULL,
    `email`      varchar(80)  NOT NULL UNIQUE,
    `password`   varchar(100) NOT NULL,
    `gender`     ENUM('MALE', 'FEMALE') NOT NULL,
    `mode`       ENUM('MILD', 'SPICY') NOT NULL,
    `image_file` varchar(255) NULL,
    `role`       ENUM('ADMIN', 'NORMAL_USER', 'PUNCTUAL_USER') NOT NULL DEFAULT 'NORMAL_USER',
    `created_at` datetime(6) NOT NULL,
    `created_by` varchar(80) NULL,
    `updated_at` datetime(6) NOT NULL,
    `updated_by` varchar(80) NULL,
    `score` DOUBLE NULL DEFAULT 0,
    PRIMARY KEY (`member_id`)
);

-- 인증
CREATE TABLE `AUTHENTICATION`
(
    `authentication_id` bigint  NOT NULL AUTO_INCREMENT,
    `member_id`         bigint  NOT NULL,
    `auth_email`        boolean NOT NULL DEFAULT false,
    `auth_phone`        boolean NOT NULL DEFAULT false,
    `created_at`        datetime(6) NOT NULL,
    `created_by`        varchar(80) NULL,
    `updated_at`        datetime(6) NOT NULL,
    `updated_by`        varchar(80) NULL,
    PRIMARY KEY (`authentication_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- 게시판, 댓글
CREATE TABLE `BOARD`
(
    `board_id`    bigint       NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `content`     TEXT         NOT NULL,
    `created_at`  datetime(6) NOT NULL,
    `updated_at`  datetime(6) NOT NULL,
    `total_like`  bigint       NOT NULL DEFAULT 0,
    `total_scrap` bigint       NOT NULL DEFAULT 0,
    PRIMARY KEY (`board_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `REPLY`
(
    `reply_id`   bigint NOT NULL AUTO_INCREMENT,
    `member_id`  bigint NOT NULL,
    `board_id`   bigint NOT NULL,
    `parent_id`  bigint NULL,
    `content`    TEXT   NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    PRIMARY KEY (`reply_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`),
    FOREIGN KEY (`parent_id`) REFERENCES `REPLY` (`reply_id`)
);

CREATE TABLE `BOARD_HEARTS`
(
    `board_hearts_id` bigint NOT NULL AUTO_INCREMENT,
    `board_id`        bigint NOT NULL,
    `member_id`       bigint NOT NULL,
    PRIMARY KEY (`board_hearts_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `BOARD_SCRAP`
(
    `board_scrap_id` bigint NOT NULL AUTO_INCREMENT,
    `board_id`       bigint NOT NULL,
    `member_id`      bigint NOT NULL,
    PRIMARY KEY (`board_scrap_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `REPLY_HEARTS`
(
    `reply_hearts_id` bigint NOT NULL AUTO_INCREMENT,
    `reply_id`        bigint NOT NULL,
    `member_id`       bigint NOT NULL,
    PRIMARY KEY (`reply_hearts_id`),
    FOREIGN KEY (`reply_id`) REFERENCES `REPLY` (`reply_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `BOARD_ATTACHMENT`
(
    `board_attachment_id` bigint       NOT NULL AUTO_INCREMENT,
    `board_id`            bigint       NOT NULL,
    `file_name`           varchar(255) NOT NULL,
    PRIMARY KEY (`board_attachment_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`)
);

-- 일정
CREATE TABLE `SCHEDULE`
(
    `schedule_id` bigint       NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `description` TEXT NULL,
    `start_date`  date         NOT NULL,
    `end_date`    date         NOT NULL,
    `created_at`  datetime(6) NOT NULL,
    `created_by`  varchar(80) NULL,
    `updated_at`  datetime(6) NOT NULL,
    `updated_by`  varchar(80) NULL,
    `dtype` varchar(40) NOT NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `N_SCHEDULE`
(
    `schedule_id`   bigint      NOT NULL,
    `category_unit` ENUM('PAGE', 'CHAPTER','LECTURE','PROJECT','WORKOUT','NONE') NOT NULL DEFAULT 'NONE',
    `buffer_time`   TIME        NOT NULL DEFAULT '00:00:00',
    `frequency`     varchar(10) NOT NULL,
    `created_at`    datetime(6) NOT NULL,
    `created_by`    varchar(80) NULL,
    `updated_at`    datetime(6) NOT NULL,
    `updated_by`    varchar(80) NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `F_SCHEDULE`
(
    `schedule_id` bigint      NOT NULL,
    `start_time`    time        NOT NULL,
    `duration`      int         NOT NULL,
    `frequency`     varchar(10) NOT NULL,
    `created_at`    datetime(6) NOT NULL,
    `created_by`    varchar(80) NULL,
    `updated_at`    datetime(6) NOT NULL,
    `updated_by`    varchar(80) NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `V_SCHEDULE`
(
    `schedule_id`   bigint     NOT NULL,
    `start_time`      time       NOT NULL,
    `end_time`        time       NOT NULL,
    `day`             varchar(3) NOT NULL,
    `complete_status` boolean    NOT NULL DEFAULT false,
    `created_at`      datetime(6) NOT NULL,
    `created_by`      varchar(80) NULL,
    `updated_at`      datetime(6) NOT NULL,
    `updated_by`      varchar(80) NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `N_SCHEDULE_DETAIL`
(
    `n_schedule_detail_id` bigint     NOT NULL AUTO_INCREMENT,
    `schedule_id`          bigint     NOT NULL,
    `start_time`           time       NOT NULL,
    `end_time`             time       NOT NULL,
    `complete_status`      boolean    NOT NULL DEFAULT false,
    `day`                  varchar(3) NOT NULL,
    `detail_description`   TEXT NULL,
    `updated_by`           varchar(80) NULL,
    `updated_at`           datetime(6) NOT NULL,
    PRIMARY KEY (`n_schedule_detail_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `N_SCHEDULE` (`schedule_id`)
);

CREATE TABLE `F_SCHEDULE_DETAIL`
(
    `f_schedule_detail_id` bigint     NOT NULL AUTO_INCREMENT,
    `schedule_id`          bigint     NOT NULL,
    `day`                  varchar(3) NOT NULL,
    `complete_status`      boolean    NOT NULL DEFAULT false,
    `detail_description`   TEXT NULL,
    `updated_by`           varchar(80) NULL,
    `updated_at`           datetime(6) NOT NULL,
    PRIMARY KEY (`f_schedule_detail_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `F_SCHEDULE` (`schedule_id`)
);

CREATE TABLE `N_D_RANGE`
(
    `n_d_range_id`         bigint NOT NULL AUTO_INCREMENT,
    `start_range`          int    NOT NULL,
    `end_range`            int    NOT NULL,
    `n_schedule_detail_id` bigint NOT NULL,
    PRIMARY KEY (`n_d_range_id`),
    FOREIGN KEY (`n_schedule_detail_id`) REFERENCES `N_SCHEDULE_DETAIL` (`n_schedule_detail_id`)
);

CREATE TABLE `N_D_VALUE`
(
    `n_d_value_id`         bigint NOT NULL AUTO_INCREMENT,
    `n_schedule_detail_id` bigint NOT NULL,
    `value`                int    NOT NULL,
    PRIMARY KEY (`n_d_value_id`),
    FOREIGN KEY (`n_schedule_detail_id`) REFERENCES `N_SCHEDULE_DETAIL` (`n_schedule_detail_id`)
);

CREATE TABLE `N_RANGE`
(
    `n_range_id`  bigint NOT NULL AUTO_INCREMENT,
    `schedule_id` bigint NOT NULL,
    `start_range` int    NOT NULL,
    `end_range`   int    NOT NULL,
    PRIMARY KEY (`n_range_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `N_SCHEDULE` (`schedule_id`)
);

CREATE TABLE `N_VALUE`
(
    `n_value_id`  bigint NOT NULL AUTO_INCREMENT,
    `schedule_id` bigint NOT NULL,
    `value`       int    NOT NULL,
    PRIMARY KEY (`n_value_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `N_SCHEDULE` (`schedule_id`)
);

-- 성취도
CREATE TABLE `ACHIEVEMENT`
(
    `achievement_id`   bigint NOT NULL AUTO_INCREMENT,
    `member_id`        bigint NOT NULL,
    `achievement_date` date   NOT NULL,
    `achievement_rate` double NOT NULL,
    `created_at`       datetime(6) NOT NULL,
    `updated_at`       datetime(6) NOT NULL,
    `created_by`       varchar(80) NULL,
    `updated_by`       varchar(80) NULL,
    PRIMARY KEY (`achievement_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `CAT_UNIT_ACH`
(
    `cat_unit_ach_Id` bigint NOT NULL AUTO_INCREMENT,
    `member_id`       bigint NOT NULL,
    `category_unit`   ENUM('PAGE', 'CHAPTER','LECTURE','PROJECT','WORKOUT','NONE') NOT NULL DEFAULT 'NONE',
    `achievement_rate` double NOT NULL,
    `created_at`      datetime(6) NOT NULL,
    `updated_at`      datetime(6) NOT NULL,
    `created_by`      varchar(80) NULL,
    `updated_by`      varchar(80) NULL,
    PRIMARY KEY (`cat_unit_ach_Id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- 알림
CREATE TABLE `NOTIFICATION`
(
    `notification_id` bigint  NOT NULL AUTO_INCREMENT,
    `member_id`       bigint  NOT NULL,
    `message`         TEXT    NOT NULL,
    `is_read`         boolean NOT NULL DEFAULT false,
    `entity_id`       bigint NULL,
    `created_at`      datetime(6) NOT NULL,
    `type`            varchar(255) NULL COMMENT 'null 이면 일반 메시지',
    PRIMARY KEY (`notification_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- 데이터 삽입
-- MEMBER 테이블에 데이터 삽입
INSERT INTO MEMBER (username, nickname, email, password, gender, mode, image_file, role, created_at, created_by, updated_at, updated_by, score)
VALUES
    ('user1', 'nick1', 'user1@example.com', 'password1', 'MALE', 'MILD', NULL, 'NORMAL_USER', NOW(), 'system', NOW(), 'system', 10),
    ('user2', 'nick2', 'user2@example.com', 'password2', 'FEMALE', 'SPICY', NULL, 'ADMIN', NOW(), 'system', NOW(), 'system', 20),
    ('user3', 'nick3', 'user3@example.com', 'password3', 'MALE', 'MILD', NULL, 'PUNCTUAL_USER', NOW(), 'system', NOW(), 'system', 30);

-- authentication 테이블에 데이터 삽입
INSERT INTO authentication (member_id, auth_email, auth_phone, created_at, created_by, updated_at, updated_by)
VALUES
    (1, true, false, NOW(), 'system', NOW(), 'system'),
    (2, true, true, NOW(), 'system', NOW(), 'system'),
    (3, false, true, NOW(), 'system', NOW(), 'system');

-- BOARD 테이블에 데이터 삽입
INSERT INTO BOARD (member_id, title, content, created_at, updated_at, total_like, total_scrap)
VALUES
    (1, 'First Board Post', 'This is the content of the first board post.', NOW(), NOW(), 5, 2),
    (2, 'Second Board Post', 'This is the content of the second board post.', NOW(), NOW(), 3, 1),
    (3, 'Third Board Post', 'This is the content of the third board post.', NOW(), NOW(), 4, 0);

-- REPLY 테이블에 데이터 삽입
INSERT INTO REPLY (member_id, board_id, parent_id, content, created_at, updated_at)
VALUES
    (1, 1, NULL, 'This is a comment on the first board post.', NOW(), NOW()),
    (2, 1, 1, 'This is a reply to the first comment.', NOW(), NOW()),
    (3, 2, NULL, 'This is a comment on the second board post.', NOW(), NOW());

-- BOARD_HEARTS 테이블에 데이터 삽입
INSERT INTO BOARD_HEARTS (board_id, member_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 1);

-- BOARD_SCRAP 테이블에 데이터 삽입
INSERT INTO BOARD_SCRAP (board_id, member_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);

-- REPLY_HEARTS 테이블에 데이터 삽입
INSERT INTO REPLY_HEARTS (reply_id, member_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);

-- BOARD_ATTACHMENT 테이블에 데이터 삽입
INSERT INTO BOARD_ATTACHMENT (board_id, file_name)
VALUES
    (1, 'attachment1.jpg'),
    (2, 'attachment2.jpg'),
    (3, 'attachment3.jpg');

-- SCHEDULE 테이블에 데이터 삽입
INSERT INTO SCHEDULE (member_id, title, description, start_date, end_date, created_at, created_by, dtype , updated_at , updated_by)
VALUES
    (1, 'fixed schedule1', 'Description of the first schedule.', '2024-01-01', '2024-01-07', NOW(), 'system', 'F',NOW(), 'system'),
    (1, 'fixed schedule2', 'Description of the second schedule.', '2024-02-01', '2024-02-14', NOW(), 'system', 'F',NOW(), 'system'),
    (1, 'fixed schedule3', 'Description of the third schedule.', '2024-03-01', '2024-03-21', NOW(), 'system', 'F',NOW(), 'system'),
    (2, 'normal schedule1', 'Description of the fourth schedule.', '2024-05-01', '2024-05-07', NOW(), 'system', 'N',NOW(), 'system'),
    (2, 'normal schedule2', 'Description of the fifth schedule.', '2024-06-01', '2024-06-14', NOW(), 'system', 'N',NOW(), 'system'),
    (2, 'normal schedule3', 'Description of the sixth schedule.', '2024-07-01', '2024-07-22', NOW(), 'system', 'N',NOW(), 'system'),
    (3, 'variable Schedule1', 'Description of the seventh schedule.', '2024-08-01', '2024-08-01', NOW(), 'system', 'V',NOW(), 'system'),
    (3, 'variable Schedule2', 'Description of the eighth schedule.', '2024-09-01', '2024-09-01', NOW(), 'system', 'V',NOW(), 'system'),
    (3, 'variable Schedule3', 'Description of the ninth schedule.', '2024-10-01', '2024-10-01', NOW(), 'system', 'V',NOW(), 'system');
-- N_SCHEDULE 테이블에 데이터 삽입
INSERT INTO N_SCHEDULE (category_unit, buffer_time, frequency, schedule_id,created_at, created_by,updated_at,updated_by)
VALUES
    ('PAGE', '01:00:00', 'weekly', 4,NOW(), 'system',NOW(), 'system'),
    ('CHAPTER', '00:30:00', 'monthly', 5,NOW(), 'system',NOW(), 'system'),
    ('LECTURE', '02:00:00', 'weekly', 6,NOW(), 'system',NOW(), 'system');

-- N_SCHEDULE_DETAIL 테이블에 데이터 삽입
INSERT INTO N_SCHEDULE_DETAIL (schedule_id, start_time, end_time, complete_status, day, detail_description, updated_by, updated_at)
VALUES
    (4, '09:00:00', '10:00:00', false, 'Mon', 'Detail of the first N_SCHEDULE.', 'system', NOW()),
    (4, '10:00:00', '11:00:00', false, 'Tue', 'Detail of the first N_SCHEDULE.', 'system', NOW()),
    (4, '11:00:00', '12:00:00', false, 'Wed', 'Detail of the first N_SCHEDULE.', 'system', NOW()),
    (4, '12:00:00', '13:00:00', false, 'Thu', 'Detail of the first N_SCHEDULE.', 'system', NOW()),
    (4, '13:00:00', '14:00:00', false, 'Fri', 'Detail of the first N_SCHEDULE.', 'system', NOW()),
    (5, '09:00:00', '10:00:00', false, 'Mon', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (5, '10:00:00', '11:00:00', false, 'Tue', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (5, '11:00:00', '12:00:00', false, 'Wed', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (5, '12:00:00', '13:00:00', false, 'Thu', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (5, '13:00:00', '14:00:00', false, 'Fri', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (6, '09:00:00', '10:00:00', false, 'Mon', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (6, '10:00:00', '11:00:00', false, 'Tue', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (6, '11:00:00', '12:00:00', false, 'Wed', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (6, '12:00:00', '13:00:00', false, 'Thu', 'Detail of the second N_SCHEDULE.', 'system', NOW()),
    (6, '13:00:00', '14:00:00', false, 'Fri', 'Detail of the second N_SCHEDULE.', 'system', NOW());


-- ND_RANGE 테이블에 데이터 삽입
INSERT INTO N_D_RANGE (start_range, end_range, n_schedule_detail_id)
VALUES
    (1, 10, 1),
    (11, 20, 2),
    (21, 30, 3),
    (31, 40, 4),
    (41, 50, 5);

-- ND_VALUE 테이블에 데이터 삽입
INSERT INTO N_D_VALUE (n_schedule_detail_id, value)
VALUES
    (6, 15),
    (7, 15),
    (8, 15),
    (9, 15),
    (10, 15),
    (11, 5),
    (12, 5),
    (13, 5),
    (14, 5),
    (15, 5);

-- N_RANGE 테이블에 데이터 삽입
INSERT INTO N_RANGE (schedule_id, start_range, end_range)
VALUES
    (4, 1, 50);

-- N_VALUE 테이블에 데이터 삽입
INSERT INTO N_VALUE (schedule_id, value)
VALUES
    (5, 75),
    (6, 25);

-- F_SCHEDULE 테이블에 데이터 삽입
INSERT INTO F_SCHEDULE (start_time, duration, frequency, schedule_id,created_at,created_by,updated_at,updated_by)
VALUES
    ('09:00:00', 60, 'daily', 1,NOW(), 'system',NOW(), 'system'),
    ('10:00:00', 90, 'weekly', 2,NOW(), 'system',NOW(), 'system'),
    ('11:00:00', 120, 'monthly', 3,NOW(), 'system',NOW(), 'system');

INSERT INTO F_SCHEDULE_DETAIL (schedule_id, day, complete_status, detail_description, updated_by, updated_at)
VALUES
    (1, 'Mon', false, 'Detail of the first F_SCHEDULE.', 'system', NOW()),
    (1, 'Tue', false, 'Detail of the first F_SCHEDULE.', 'system', NOW()),
    (1, 'Wed', false, 'Detail of the first F_SCHEDULE.', 'system', NOW()),
    (1, 'Thu', false, 'Detail of the first F_SCHEDULE.', 'system', NOW()),
    (1, 'Fri', false, 'Detail of the first F_SCHEDULE.', 'system', NOW()),
    (2, 'Mon', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (2, 'Tue', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (2, 'Wed', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (2, 'Thu', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (2, 'Fri', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (3, 'Mon', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (3, 'Tue', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (3, 'Wed', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (3, 'Thu', false, 'Detail of the second F_SCHEDULE.', 'system', NOW()),
    (3, 'Fri', false, 'Detail of the second F_SCHEDULE.', 'system', NOW());

-- V_SCHEDULE 테이블에 데이터 삽입
INSERT INTO V_SCHEDULE (start_time, end_time, schedule_id, day, complete_status, created_at,created_by,updated_at, updated_by)
VALUES
    ('09:00:00', '10:00:00', 7, 'Mon', false, NOW(), 'system',NOW(), 'system'),
    ('10:00:00', '11:00:00', 8, 'Tue', false, NOW(), 'system',NOW(), 'system'),
    ('11:00:00', '12:00:00', 9, 'Wed', false, NOW(), 'system',NOW(), 'system');


-- ACHIEVEMENT 테이블에 데이터 삽입
# INSERT INTO ACHIEVEMENT (member_id, achievement_date, achievement_rate, created_at, updated_at, created_by, updated_by)
# VALUES
#     (1, '2024-01-01', 90, NOW(), NOW(), 'system', 'system'),
#     (2, '2024-01-02', 80, NOW(), NOW(), 'system', 'system'),
#     (3, '2024-01-03', 70, NOW(), NOW(), 'system', 'system');

-- CAT_UNIT_ACH 테이블에 데이터 삽입
INSERT INTO CAT_UNIT_ACH (member_id, category_unit, achievement_rate, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 'PAGE', 90, NOW(), NOW(), 'system', 'system'),
    (1, 'CHAPTER', 80, NOW(), NOW(), 'system', 'system'),
    (1, 'LECTURE', 70, NOW(), NOW(), 'system', 'system');

-- NOTIFICATION 테이블에 데이터 삽입
INSERT INTO NOTIFICATION (member_id, message, is_read, entity_id, created_at, type)
VALUES
    (1, 'You have a new message!', false, NULL, NOW(), NULL),
    (2, 'Your schedule is updated.', false, 1, NOW(), 'schedule'),
    (3, 'Your post received a new comment.', false, 2, NOW(), 'comment');