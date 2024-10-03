-- Drop tables if they exist
DROP TABLE IF EXISTS `NOTIFICATION`;
DROP TABLE IF EXISTS `ACHIEVEMENT`;
DROP TABLE IF EXISTS `N_SCHEDULE_DETAIL`;
DROP TABLE IF EXISTS `N_SCHEDULE`;
DROP TABLE IF EXISTS `F_SCHEDULE_DETAIL`;
DROP TABLE IF EXISTS `F_SCHEDULE`;
DROP TABLE IF EXISTS `V_SCHEDULE`;
DROP TABLE IF EXISTS `SCHEDULE`;
DROP TABLE IF EXISTS `BOARD_ATTACHMENT`;
DROP TABLE IF EXISTS `REPLY_HEART`;
DROP TABLE IF EXISTS `BOARD_SCRAP`;
DROP TABLE IF EXISTS `BOARD_HEART`;
DROP TABLE IF EXISTS `REPLY`;
DROP TABLE IF EXISTS `BOARD`;
DROP TABLE IF EXISTS `MEMBER_ROLE`;
DROP TABLE IF EXISTS `MEMBER`;

-- 회원
CREATE TABLE `MEMBER`
(
    `member_id`  bigint       NOT NULL AUTO_INCREMENT,
    `username`   varchar(20)  NOT NULL,
    `nickname`   varchar(40)  NOT NULL,
    `email`      varchar(80)  NOT NULL UNIQUE,
    `password`   varchar(100) NOT NULL,
    `gender`     ENUM('MALE', 'FEMALE') NOT NULL,
    `mode`       ENUM('MILD', 'SPICY') NOT NULL,
    `image_file` varchar(255) NULL,
    `created_at`  datetime NULL,
    `created_by` varchar(80) NULL,
    `updated_at` datetime NULL,
    `updated_by` varchar(80) NULL,
    `score` DOUBLE NULL DEFAULT 0,
    `phone_number` varchar(11) NOT NULL UNIQUE,
    `auth_email` boolean NULL DEFAULT false,
    `auth_phone` boolean NULL DEFAULT false,
    PRIMARY KEY (`member_id`)
);

CREATE TABLE `MEMBER_ROLE`
(
    `member_role_id` bigint NOT NULL AUTO_INCREMENT,
    `member_id`      bigint NOT NULL,
    `role`           ENUM('ADMIN', 'NORMAL_USER', 'PUNCTUAL_USER') NOT NULL DEFAULT 'NORMAL_USER',
    `created_at`     datetime NULL,
    `created_by`     varchar(80) NULL,
    `updated_at`     datetime NULL,
    `updated_by`     varchar(80) NULL,
    PRIMARY KEY (`member_role_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- 게시판, 댓글
CREATE TABLE `BOARD`
(
    `board_id`    bigint       NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `content`     TEXT         NOT NULL,
    `created_at`  datetime   NULL,
    `updated_at`  datetime   NULL,
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
    `total_like` bigint NOT NULL,
    `created_at`  datetime   NULL,
    `updated_at` datetime    NULL,
    PRIMARY KEY (`reply_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`),
    FOREIGN KEY (`parent_id`) REFERENCES `REPLY` (`reply_id`)
);

CREATE TABLE `BOARD_HEART`
(
    `board_heart_id` bigint NOT NULL AUTO_INCREMENT,
    `board_id`        bigint NOT NULL,
    `member_id`       bigint NOT NULL,
    PRIMARY KEY (`board_heart_id`),
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

CREATE TABLE `REPLY_HEART`
(
    `reply_heart_id` bigint NOT NULL AUTO_INCREMENT,
    `reply_id`        bigint NOT NULL,
    `member_id`       bigint NOT NULL,
    PRIMARY KEY (`reply_heart_id`),
    FOREIGN KEY (`reply_id`) REFERENCES `REPLY` (`reply_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- 일정
CREATE TABLE `SCHEDULE`
(
    `schedule_id` bigint       NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `common_description` TEXT NOT NULL,
    `start_date`  datetime    NOT NULL,
    `end_date`    datetime    NOT NULL,
    `created_at`  datetime NULL,
    `created_by`  varchar(80) NULL,
    `updated_at`  datetime NULL,
    `updated_by`  varchar(80) NULL,
    `dtype` varchar(40) NOT NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `N_SCHEDULE`
(
    `schedule_id`   bigint      NOT NULL,
    `total_amount`     int      NOT NULL,
    `buffer_time`   TIME        NOT NULL DEFAULT '00:00:00',
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `F_SCHEDULE`
(
    `schedule_id` bigint      NOT NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `V_SCHEDULE`
(
    `schedule_id`   bigint     NOT NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `N_SCHEDULE_DETAIL`
(
    `n_schedule_detail_id` bigint     NOT NULL AUTO_INCREMENT,
    `schedule_id`          bigint     NOT NULL,
    `start_date`           datetime       NOT NULL,
    `end_date`             datetime       NOT NULL,
    `complete_status`      boolean    NOT NULL DEFAULT false,
    `detail_description`   TEXT NULL,
    `daily_amount`         int NOT NULL,
    `achieved_amount`      int NOT NULL ,
    `created_by`           varchar(80) NOT NULL,
    `created_at`           datetime NOT NULL,
    `updated_by`           varchar(80) NULL,
    `updated_at`           datetime NOT NULL,
    PRIMARY KEY (`n_schedule_detail_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `N_SCHEDULE` (`schedule_id`)
);

CREATE TABLE `F_SCHEDULE_DETAIL`
(
    `f_schedule_detail_id` bigint     NOT NULL AUTO_INCREMENT,
    `schedule_id`          bigint     NOT NULL,
    `detail_description`   TEXT NULL,
    `start_date`           datetime       NOT NULL,
    `end_date`             datetime       NOT NULL,
    `created_by`           varchar(80) NOT NULL,
    `created_at`           datetime NOT NULL,
    `updated_by`           varchar(80) NOT NULL,
    `updated_at`           datetime NOT NULL,
    PRIMARY KEY (`f_schedule_detail_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `F_SCHEDULE` (`schedule_id`)
);

-- 성취도
CREATE TABLE `ACHIEVEMENT`
(
    `achievement_id`   bigint NOT NULL AUTO_INCREMENT,
    `member_id`        bigint NOT NULL,
    `achievement_date` date   NOT NULL,
    `achievement_rate` double NOT NULL,
    `created_at`        datetime NULL,
    `updated_at`       datetime NULL,
    `created_by`       varchar(80) NULL,
    `updated_by`       varchar(80) NULL,
    PRIMARY KEY (`achievement_id`),
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
    `created_at`    datetime NULL,
    `type`            varchar(255) NULL COMMENT 'null 이면 일반 메시지',
    PRIMARY KEY (`notification_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);
-- -- MEMBER 테이블에 데이터 삽입
-- password = password1
INSERT INTO MEMBER (username, nickname, email, password, gender, mode, image_file, created_at, created_by, updated_at, updated_by, score, phone_number, auth_email, auth_phone)
VALUES
    ('일반테스트회원1', 'nick1', 'normal@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2', 'MALE', 'MILD', NULL, NOW(), 'system', NOW(), 'system', 10, '01012345678', true, false),
    ('일반테스트회원2', 'nick2', 'normal2@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2', 'FEMALE', 'MILD', NULL, NOW(), 'system', NOW(), 'system', 11, '01012345671', true, false),
    ('일반테스트회원3', 'nick3', 'normal3@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2', 'MALE', 'SPICY', NULL, NOW(), 'system', NOW(), 'system', 12, '01012345673', true, false),
    ('일반테스트회원4', 'nick4', 'normal4@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2', 'FEMALE', 'SPICY', NULL, NOW(), 'system', NOW(), 'system', 13, '01012345675', true, false),
    ('관리자테스트회원2', 'nick2', 'admin@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2', 'FEMALE', 'SPICY', NULL, NOW(), 'system', NOW(), 'system', 20, '01023456789', true, true),
    ('성실테스트회원3', 'nick3', 'faithful@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2', 'MALE', 'MILD', NULL, NOW(), 'system', NOW(), 'system', 30, '01034567890', false, true);
--
-- -- MEMBER_ROLE 테이블에 데이터 삽입
INSERT INTO MEMBER_ROLE (member_id, role, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 'NORMAL_USER', NOW(), 'system', NOW(), 'system'),
    (2, 'ADMIN', NOW(), 'system', NOW(), 'system'),
    (3, 'PUNCTUAL_USER', NOW(), 'system', NOW(), 'system');
--
-- BOARD 테이블에 데이터 삽입
INSERT INTO BOARD (member_id, title, content, created_at, updated_at, total_like, total_scrap)
VALUES
    (1, 'First Board Post 1', 'This is the content of the first board post for member 1.', NOW(), NOW(), 5, 2),
    (1, 'First Board Post 2', 'This is the content of the second board post for member 1.', NOW(), NOW(), 3, 1),
    (1, 'First Board Post 3', 'This is the content of the third board post for member 1.', NOW(), NOW(), 4, 0),
    (1, 'First Board Post 4', 'This is the content of the fourth board post for member 1.', NOW(), NOW(), 2, 3),
    (1, 'First Board Post 5', 'This is the content of the fifth board post for member 1.', NOW(), NOW(), 6, 1),
    (1, 'First Board Post 6', 'This is the content of the sixth board post for member 1.', NOW(), NOW(), 1, 0),
    (1, 'First Board Post 7', 'This is the content of the seventh board post for member 1.', NOW(), NOW(), 0, 0),
    (1, 'First Board Post 8', 'This is the content of the eighth board post for member 1.', NOW(), NOW(), 7, 2),
    (1, 'First Board Post 9', 'This is the content of the ninth board post for member 1.', NOW(), NOW(), 2, 1),
    (1, 'First Board Post 10', 'This is the content of the tenth board post for member 1.', NOW(), NOW(), 5, 3),
    (1, 'First Board Post 11', 'This is the content of the eleventh board post for member 1.', NOW(), NOW(), 4, 2),
    (1, 'First Board Post 12', 'This is the content of the twelfth board post for member 1.', NOW(), NOW(), 3, 0),
    (1, 'First Board Post 13', 'This is the content of the thirteenth board post for member 1.', NOW(), NOW(), 6, 1),
    (1, 'First Board Post 14', 'This is the content of the fourteenth board post for member 1.', NOW(), NOW(), 2, 2),
    (1, 'First Board Post 15', 'This is the content of the fifteenth board post for member 1.', NOW(), NOW(), 1, 0),

    (2, 'Second Board Post 1', 'This is the content of the first board post for member 2.', NOW(), NOW(), 5, 2),
    (2, 'Second Board Post 2', 'This is the content of the second board post for member 2.', NOW(), NOW(), 3, 1),
    (2, 'Second Board Post 3', 'This is the content of the third board post for member 2.', NOW(), NOW(), 4, 0),
    (2, 'Second Board Post 4', 'This is the content of the fourth board post for member 2.', NOW(), NOW(), 2, 3),
    (2, 'Second Board Post 5', 'This is the content of the fifth board post for member 2.', NOW(), NOW(), 6, 1),
    (2, 'Second Board Post 6', 'This is the content of the sixth board post for member 2.', NOW(), NOW(), 1, 0),
    (2, 'Second Board Post 7', 'This is the content of the seventh board post for member 2.', NOW(), NOW(), 0, 0),
    (2, 'Second Board Post 8', 'This is the content of the eighth board post for member 2.', NOW(), NOW(), 7, 2),
    (2, 'Second Board Post 9', 'This is the content of the ninth board post for member 2.', NOW(), NOW(), 2, 1),
    (2, 'Second Board Post 10', 'This is the content of the tenth board post for member 2.', NOW(), NOW(), 5, 3),
    (2, 'Second Board Post 11', 'This is the content of the eleventh board post for member 2.', NOW(), NOW(), 4, 2),
    (2, 'Second Board Post 12', 'This is the content of the twelfth board post for member 2.', NOW(), NOW(), 3, 0),
    (2, 'Second Board Post 13', 'This is the content of the thirteenth board post for member 2.', NOW(), NOW(), 6, 1),
    (2, 'Second Board Post 14', 'This is the content of the fourteenth board post for member 2.', NOW(), NOW(), 2, 2),
    (2, 'Second Board Post 15', 'This is the content of the fifteenth board post for member 2.', NOW(), NOW(), 1, 0),

    (3, 'Third Board Post 1', 'This is the content of the first board post for member 3.', NOW(), NOW(), 5, 2),
    (3, 'Third Board Post 2', 'This is the content of the second board post for member 3.', NOW(), NOW(), 3, 1),
    (3, 'Third Board Post 3', 'This is the content of the third board post for member 3.', NOW(), NOW(), 4, 0),
    (3, 'Third Board Post 4', 'This is the content of the fourth board post for member 3.', NOW(), NOW(), 2, 3),
    (3, 'Third Board Post 5', 'This is the content of the fifth board post for member 3.', NOW(), NOW(), 6, 1),
    (3, 'Third Board Post 6', 'This is the content of the sixth board post for member 3.', NOW(), NOW(), 1, 0),
    (3, 'Third Board Post 7', 'This is the content of the seventh board post for member 3.', NOW(), NOW(), 0, 0),
    (3, 'Third Board Post 8', 'This is the content of the eighth board post for member 3.', NOW(), NOW(), 7, 2),
    (3, 'Third Board Post 9', 'This is the content of the ninth board post for member 3.', NOW(), NOW(), 2, 1),
    (3, 'Third Board Post 10', 'This is the content of the tenth board post for member 3.', NOW(), NOW(), 5, 3),
    (3, 'Third Board Post 11', 'This is the content of the eleventh board post for member 3.', NOW(), NOW(), 4, 2),
    (3, 'Third Board Post 12', 'This is the content of the twelfth board post for member 3.', NOW(), NOW(), 3, 0),
    (3, 'Third Board Post 13', 'This is the content of the thirteenth board post for member 3.', NOW(), NOW(), 6, 1),
    (3, 'Third Board Post 14', 'This is the content of the fourteenth board post for member 3.', NOW(), NOW(), 2, 2),
    (3, 'Third Board Post 15', 'This is the content of the fifteenth board post for member 3.', NOW(), NOW(), 1, 0);
--
-- REPLY 테이블에 데이터 삽입
INSERT INTO REPLY (member_id, board_id, parent_id, content, total_like, created_at, updated_at)
VALUES
    (1, 1, NULL, '게시글 1 - 댓글 1', 100,NOW(), NOW()),
    (1, 1, NULL, '게시글 1 - 댓글 2', 100,NOW(), NOW()),
    (1, 1, NULL, '게시글 1 - 댓글 3', 100,NOW(), NOW()),
    (2, 1, NULL, '게시글 1 - 댓글 4', 10,NOW(), NOW()),
    (3, 1, NULL, '게시글 1 - 댓글 5', 9,NOW(), NOW()),
    (3, 1, NULL, '게시글 1 - 댓글 6', 8,NOW(), NOW()),
    (1, 2, NULL, '게시글 2 - 댓글 1', 100,NOW(), NOW()),
    (1, 2, NULL, '게시글 2 - 댓글 2', 20,NOW(), NOW()),
    (1, 2, NULL, '게시글 2 - 댓글 3', 17,NOW(), NOW()),
    (2, 2, NULL, '게시글 2 - 댓글 4', 14,NOW(), NOW()),
    (3, 2, NULL, '게시글 2 - 댓글 5', 13,NOW(), NOW()),
    (3, 2, NULL, '게시글 2 - 댓글 6', 11,NOW(), NOW()),
    (2, 1, 1, '게시글 1- 댓글 1 - 대댓글-1', 20, NOW(), NOW()),
    (1, 1, 1, '게시글 1- 댓글 1 - 대댓글-2', 30, NOW(), NOW()),
    (3, 1, 2, '게시글 1- 댓글 2 - 대댓글-1', 30, NOW(), NOW()),
    (1, 1, 2, '게시글 1- 댓글 2 - 대댓글-2', 30, NOW(), NOW()),
    (2, 1, 3, '게시글 1- 댓글 3 - 대댓글-1', 30, NOW(), NOW()),
    (3, 1, 3, '게시글 1- 댓글 3 - 대댓글-2', 30, NOW(), NOW()),
    (1, 2, 7, '게시글 2- 댓글 1 - 대댓글-1', 30, NOW(), NOW()),
    (2, 2, 9, '게시글 2- 댓글 3 - 대댓글-1', 30, NOW(), NOW()),
    (3, 2, 8, '게시글 2- 댓글 2 - 대댓글-1', 30, NOW(), NOW()),
    (1, 2, 7, '게시글 2- 댓글 1 - 대댓글-2', 30, NOW(), NOW()),
    (2, 2, 9, '게시글 2- 댓글 3 - 대댓글-2', 30, NOW(), NOW()),
    (3, 2, 8, '게시글 2- 댓글 2 - 대댓글-2', 30, NOW(), NOW());

-- BOARD_HEART 테이블에 데이터 삽입
INSERT INTO BOARD_HEART (board_id, member_id)
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

-- REPLY_HEART 테이블에 데이터 삽입
INSERT INTO REPLY_HEART (reply_id, member_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);

-- SCHEDULE 테이블에 데이터 삽입
INSERT INTO SCHEDULE (member_id, title, common_description, start_date, end_date, created_at, created_by, dtype, updated_at, updated_by)
VALUES
    (1, 'fixed schedule1', 'Description of the first schedule.', '2024-01-01 00:00:00', '2024-01-07 00:00:00', NOW(), 'normal@example.com', 'F', NOW(), 'normal@example.com'),
    (2, 'fixed schedule2', 'Description of the second schedule.', '2024-02-01 00:00:00', '2024-02-14 00:00:00', NOW(), 'normal2@example.com', 'F', NOW(), 'normal2@example.com'),
    (1, 'fixed schedule3', 'Description of the third schedule.', '2024-03-01 00:00:00', '2024-03-21 00:00:00', NOW(), 'normal@example.com', 'F', NOW(), 'normal@example.com'),
    (1, 'normal schedule1', 'Description of the fourth schedule.', '2024-05-01 00:00:00', '2024-05-07 00:00:00', NOW(), 'normal@example.com', 'N', NOW(), 'normal@example.com'),
    (2, 'normal schedule2', 'Description of the fifth schedule.', '2024-06-01 00:00:00', '2024-06-14 00:00:00', NOW(), 'normal2@example.com', 'N', NOW(), 'normal2@example.com'),
    (1, 'normal schedule3', 'Description of the sixth schedule.', '2024-07-01 00:00:00', '2024-07-22 00:00:00', NOW(), 'normal@example.com', 'N', NOW(), 'normal@example.com'),
    (1, 'variable Schedule1', 'Description of the seventh schedule.', '2024-08-01 00:00:00', '2024-08-01 00:00:00', NOW(), 'normal@example.com', 'V', NOW(), 'normal@example.com'),
    (1, 'variable Schedule2', 'Description of the eighth schedule.', '2024-09-01 00:00:00', '2024-09-01 00:00:00', NOW(), 'normal@example.com', 'V', NOW(), 'normal@example.com'),
    (2, 'variable Schedule3', 'Description of the ninth schedule.', '2024-10-01 00:00:00', '2024-10-01 00:00:00', NOW(), 'normal2@example.com', 'V', NOW(), 'normal2@example.com');

INSERT INTO N_SCHEDULE (schedule_id,buffer_time,total_amount)
VALUES
    (4,'02:00:00',100),
    (5,'01:00:00',200),
    (6,'00:30:00',300);
-- N_SCHEDULE_DETAIL 테이블에 데이터 삽입
INSERT INTO N_SCHEDULE_DETAIL (schedule_id, start_date, end_date, complete_status, detail_description, created_by,created_at,updated_by, updated_at, daily_amount , achieved_amount)
VALUES
    (4, '2024-05-01 09:00:00', '2024-05-01 10:00:00', false, 'Detail of the first N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com', '2024-04-30 09:00:00',20,0),
    (4, '2024-05-02 10:00:00', '2024-05-02 11:00:00', false, 'Detail of the first N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',20,0),
    (4, '2024-05-03 11:00:00', '2024-05-03 12:00:00', false, 'Detail of the first N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',20,0),
    (4, '2024-05-04 12:00:00', '2024-05-04 13:00:00', false, 'Detail of the first N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',20,0),
    (4, '2024-05-05 13:00:00', '2024-05-05 14:00:00', false, 'Detail of the first N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',20,0),
    (5, '2024-06-01 09:00:00', '2024-06-01 10:00:00', false, 'Detail of the second N_SCHEDULE.','normal2@example.com', '2024-04-30 09:00:00', 'normal2@example.com','2024-04-30 09:00:00',40,0),
    (5, '2024-06-02 10:00:00', '2024-06-02 11:00:00', false, 'Detail of the second N_SCHEDULE.','normal2@example.com', '2024-04-30 09:00:00', 'normal2@example.com','2024-04-30 09:00:00',40,0),
    (5, '2024-06-03 11:00:00', '2024-06-03 12:00:00', false, 'Detail of the second N_SCHEDULE.','normal2@example.com', '2024-04-30 09:00:00', 'normal2@example.com','2024-04-30 09:00:00',40,0),
    (5, '2024-06-04 12:00:00', '2024-06-04 13:00:00', false, 'Detail of the second N_SCHEDULE.','normal2@example.com', '2024-04-30 09:00:00', 'normal2@example.com','2024-04-30 09:00:00',40,0),
    (5, '2024-06-05 13:00:00', '2024-06-05 14:00:00', false, 'Detail of the second N_SCHEDULE.','normal2@example.com', '2024-04-30 09:00:00', 'normal2@example.com','2024-04-30 09:00:00',40,0),
    (6, '2024-07-01 09:00:00', '2024-07-01 10:00:00', false, 'Detail of the third N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',60,0),
    (6, '2024-07-02 10:00:00', '2024-07-02 11:00:00', false, 'Detail of the third N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',60,0),
    (6, '2024-07-03 11:00:00', '2024-07-03 12:00:00', false, 'Detail of the third N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',60,0),
    (6, '2024-07-04 12:00:00', '2024-07-04 13:00:00', false, 'Detail of the third N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',60,0),
    (6, '2024-07-05 13:00:00', '2024-07-05 14:00:00', false, 'Detail of the third N_SCHEDULE.','normal@example.com', '2024-04-30 09:00:00', 'normal@example.com','2024-04-30 09:00:00',60,0);

-- F_SCHEDULE 테이블에 데이터 삽입
INSERT INTO F_SCHEDULE (schedule_id)
VALUES
    (1),
    (2),
    (3);

-- F_SCHEDULE_DETAIL 테이블에 데이터 삽입
INSERT INTO F_SCHEDULE_DETAIL (schedule_id, detail_description, created_by,created_at,updated_by, updated_at,start_date,end_date)
VALUES
    (1, 'Detail of the first F_SCHEDULE.','normal@example.com', '2024-01-01 09:00:00', 'normal@example.com', '2024-01-01 09:00:00','2024-01-01 10:00:00','2024-01-01 14:00:00'),
    (1, 'Detail of the first F_SCHEDULE.','normal@example.com','2024-01-01 09:00:00', 'normal@example.com', '2024-01-01 09:00:00','2024-01-01 17:30:00','2024-01-01 18:00:00'),
    (1, 'Detail of the first F_SCHEDULE.','normal@example.com','2024-01-01 09:00:00', 'normal@example.com', '2024-01-01 09:00:00','2024-01-01 21:30:00','2024-01-01 22:00:00'),
    (1, 'Detail of the first F_SCHEDULE.','normal@example.com','2024-01-01 09:00:00', 'normal@example.com', '2024-01-01 09:00:00','2024-01-03 10:00:00','2024-01-03 14:00:00'),
    (1, 'Detail of the first F_SCHEDULE.','normal@example.com','2024-01-01 09:00:00', 'normal@example.com', '2024-01-01 09:00:00','2024-01-04 10:00:00','2024-01-04 14:00:00'),
    (1, 'Detail of the first F_SCHEDULE.','normal@example.com','2024-01-01 09:00:00', 'normal@example.com', '2024-01-01 09:00:00','2024-01-05 10:00:00','2024-01-05 14:00:00'),
    (2, 'Detail of the second F_SCHEDULE.','normal2@example.com', '2024-02-01 09:00:00', 'normal2@example.com', '2024-02-01 09:00:00','2024-02-01 10:00:00','2024-02-01 14:00:00'),
    (2, 'Detail of the second F_SCHEDULE.','normal2@example.com', '2024-02-01 09:00:00', 'normal2@example.com', '2024-02-01 09:00:00','2024-02-08 10:00:00','2024-02-08 14:00:00'),
    (2, 'Detail of the second F_SCHEDULE.','normal2@example.com', '2024-02-01 09:00:00', 'normal2@example.com', '2024-02-01 09:00:00','2024-02-14 10:00:00','2024-02-14 14:00:00'),
    (2, 'Detail of the second F_SCHEDULE.','normal2@example.com', '2024-02-01 09:00:00', 'normal2@example.com', '2024-02-01 09:00:00','2024-02-03 10:00:00','2024-02-03 14:00:00'),
    (2, 'Detail of the second F_SCHEDULE.','normal2@example.com', '2024-02-01 09:00:00', 'normal2@example.com', '2024-02-01 09:00:00','2024-02-10 10:00:00','2024-02-10 14:00:00'),
    (3, 'Detail of the third F_SCHEDULE.','normal@example.com','2024-03-01 09:00:00', 'normal@example.com','2024-03-01 09:00:00','2024-03-01 10:00:00','2024-03-01 14:00:00'),
    (3, 'Detail of the third F_SCHEDULE.','normal@example.com','2024-03-01 09:00:00', 'normal@example.com','2024-03-01 09:00:00','2024-03-08 10:00:00','2024-03-08 14:00:00'),
    (3, 'Detail of the third F_SCHEDULE.','normal@example.com','2024-03-01 09:00:00', 'normal@example.com','2024-03-01 09:00:00','2024-03-14 10:00:00','2024-03-14 14:00:00'),
    (3, 'Detail of the third F_SCHEDULE.','normal@example.com','2024-03-01 09:00:00', 'normal@example.com','2024-03-01 09:00:00','2024-03-21 10:00:00','2024-03-21 14:00:00');


-- V_SCHEDULE 테이블에 데이터 삽입
INSERT INTO V_SCHEDULE (schedule_id)
VALUES
    (7),
    (8),
    (9);

-- ACHIEVEMENT 테이블에 데이터 삽입
INSERT INTO ACHIEVEMENT (member_id, achievement_date, achievement_rate, created_at, updated_at, created_by, updated_by)
VALUES
    (1, '2024-01-01', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-02', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-03', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-04', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-05', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-06', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-07', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-08', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-09', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-01-10', 90, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (2, '2024-01-02', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-03', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-04', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-05', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-06', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-07', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-08', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-09', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-10', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, '2024-01-11', 80, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (3, '2024-01-03', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-04', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-05', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-06', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-07', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-08', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-09', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-10', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-11', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, '2024-01-12', 70, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com');

-- NOTIFICATION 테이블에 데이터 삽입
INSERT INTO NOTIFICATION (member_id, message, is_read, entity_id, created_at, type)
VALUES
    (1, 'You have a new message!', false, NULL, NOW(), NULL),
    (2, 'Your schedule is updated.', false, 1, NOW(), 'schedule'),
    (3, 'Your post received a new comment.', false, 2, NOW(), 'comment');