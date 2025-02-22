DROP TABLE IF EXISTS `NOTIFICATION`;
DROP TABLE IF EXISTS `ACHIEVEMENT`;
DROP TABLE IF EXISTS `N_SCHEDULE_DETAIL`;
DROP TABLE IF EXISTS `N_SCHEDULE`;
DROP TABLE IF EXISTS `F_SCHEDULE_DETAIL`;
DROP TABLE IF EXISTS `F_SCHEDULE`;
DROP TABLE IF EXISTS `V_SCHEDULE`;
DROP TABLE IF EXISTS `REPLY_HEART`;
DROP TABLE IF EXISTS `POST_SCRAP`;
DROP TABLE IF EXISTS `POST_HEART`;
DROP TABLE IF EXISTS `REPLY`;
DROP TABLE IF EXISTS `POST`;
DROP TABLE IF EXISTS `MEMBER`;

SET MODE MYSQL;

CREATE TABLE `MEMBER`
(
    `member_id`  bigint       NOT NULL AUTO_INCREMENT,
    `username`   varchar(20)  NOT NULL,
    `nickname`   varchar(40)  NOT NULL UNIQUE,
    `email`      varchar(80)  NOT NULL UNIQUE,
    `password`   varchar(100) NOT NULL,
    `phone_number` varchar(11) NOT NULL UNIQUE,
    `gender`     ENUM('MALE', 'FEMALE') NOT NULL,
    `role`       ENUM('ADMIN','NORMAL_USER','FAITHFUL_USER'),
    `mode`       ENUM('MILD', 'SPICY') NOT NULL,
    `profile_image_path` varchar(255) NULL,
    `score` double NOT NULL DEFAULT 0,
    `auth_email` boolean NULL DEFAULT false,
    `auth_phone` boolean NULL DEFAULT false,
    `created_at`  datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    `created_by` varchar(80) NOT NULL,
    `updated_by` varchar(80) NOT NULL,
    PRIMARY KEY (`member_id`)
);

CREATE TABLE `POST`
(
    `post_id`    bigint       NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `contents`     TEXT         NOT NULL,
    `total_like`  bigint       NOT NULL DEFAULT 0,
    `total_scrap` bigint       NOT NULL DEFAULT 0,
    `created_at`  datetime   NOT NULL,
    `updated_at`  datetime   NOT NULL,
    `created_by` varchar(80) NOT NULL,
    `updated_by` varchar(80) NOT NULL,
    PRIMARY KEY (`post_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `REPLY`
(
    `reply_id`   bigint NOT NULL AUTO_INCREMENT,
    `member_id`  bigint NOT NULL,
    `post_id`   bigint NOT NULL,
    `parent_id`  bigint NULL,
    `contents`    TEXT   NOT NULL,
    `total_like` bigint NOT NULL DEFAULT 0,
    `created_at`  datetime   NOT NULL,
    `updated_at` datetime    NOT NULL,
    `created_by` varchar(80) NOT NULL,
    `updated_by` varchar(80) NOT NULL,
    PRIMARY KEY (`reply_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`),
    FOREIGN KEY (`post_id`) REFERENCES `POST` (`post_id`)
);

CREATE TABLE `POST_HEART`
(
    `post_heart_id` bigint NOT NULL AUTO_INCREMENT,
    `member_id`       bigint NOT NULL,
    `post_id`        bigint NOT NULL,
    `created_at`  datetime   NOT NULL,
    `updated_at` datetime    NOT NULL,
    `created_by` varchar(80) NOT NULL,
    `updated_by` varchar(80) NOT NULL,
    PRIMARY KEY (`post_heart_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`),
    FOREIGN KEY (`post_id`) REFERENCES `POST` (`post_id`)
);

CREATE TABLE `POST_SCRAP`
(
    `post_scrap_id` bigint NOT NULL AUTO_INCREMENT,
    `member_id`      bigint NOT NULL,
    `post_id`       bigint NOT NULL,
    `created_at`  datetime   NOT NULL,
    `updated_at` datetime    NOT NULL,
    `created_by` varchar(80) NOT NULL,
    `updated_by` varchar(80) NOT NULL,
    PRIMARY KEY (`post_scrap_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`),
    FOREIGN KEY (`post_id`) REFERENCES `POST` (`post_id`)
);

CREATE TABLE `REPLY_HEART`
(
    `reply_heart_id` bigint NOT NULL AUTO_INCREMENT,
    `member_id`       bigint NOT NULL,
    `reply_id`        bigint NOT NULL,
    `created_at`  datetime   NOT NULL,
    `updated_at` datetime    NOT NULL,
    `created_by` varchar(80) NOT NULL,
    `updated_by` varchar(80) NOT NULL,
    PRIMARY KEY (`reply_heart_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`),
    FOREIGN KEY (`reply_id`) REFERENCES `REPLY` (`reply_id`)
);

CREATE TABLE `N_SCHEDULE`
(
    `n_schedule_id` bigint      NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `common_description` TEXT NOT NULL,
    `start_date_time`  datetime    NOT NULL,
    `end_date_time`    datetime    NOT NULL,
    `buffer_time`   TIME        NOT NULL,
    `total_amount`     int      NOT NULL DEFAULT 0,
    `created_at`  datetime NOT NULL,
    `updated_at`  datetime NOT NULL,
    `created_by`  varchar(80) NOT NULL,
    `updated_by`  varchar(80) NOT NULL,

    PRIMARY KEY (`n_schedule_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `F_SCHEDULE`
(
    `f_schedule_id` bigint      NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `common_description` TEXT NOT NULL,
    `start_date_time`  datetime    NOT NULL,
    `end_date_time`    datetime    NOT NULL,
    `created_at`  datetime NOT NULL,
    `updated_at`  datetime NOT NULL,
    `created_by`  varchar(80) NOT NULL,
    `updated_by`  varchar(80) NOT NULL,

    PRIMARY KEY (`f_schedule_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `N_SCHEDULE_DETAIL`
(
    `n_schedule_detail_id` bigint     NOT NULL AUTO_INCREMENT,
    `n_schedule_id`          bigint     NOT NULL,
    `detail_description`   TEXT NOT NULL,
    `start_date_time`           datetime       NOT NULL,
    `end_date_time`             datetime       NOT NULL,
    `buffer_time`          TIME NOT NULL DEFAULT '00:00:00' ,
    `complete_status`      boolean    NOT NULL DEFAULT false,
    `daily_amount`         int NOT NULL DEFAULT 0,
    `achieved_amount`      int NOT NULL DEFAULT 0,
    `created_at`           datetime NOT NULL,
    `updated_at`           datetime NOT NULL,
    `created_by`           varchar(80) NOT NULL,
    `updated_by`           varchar(80) NOT NULL,
    PRIMARY KEY (`n_schedule_detail_id`),
    FOREIGN KEY (`n_schedule_id`) REFERENCES `N_SCHEDULE` (`n_schedule_id`)
);

CREATE TABLE `V_SCHEDULE`
(
    `v_schedule_id` bigint      NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `common_description` TEXT NOT NULL,
    `start_date_time`  datetime    NOT NULL,
    `end_date_time`    datetime    NOT NULL,
    `created_at`  datetime NOT NULL,
    `updated_at`  datetime NOT NULL,
    `created_by`  varchar(80) NOT NULL,
    `updated_by`  varchar(80) NOT NULL,

    PRIMARY KEY (`v_schedule_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `F_SCHEDULE_DETAIL`
(
    `f_schedule_detail_id` bigint     NOT NULL AUTO_INCREMENT,
    `f_schedule_id`          bigint     NOT NULL,
    `detail_description`   TEXT NOT NULL,
    `start_date_time`           datetime       NOT NULL,
    `end_date_time`             datetime       NOT NULL,
    `created_at`           datetime NOT NULL,
    `updated_at`           datetime NOT NULL,
    `created_by`           varchar(80) NOT NULL,
    `updated_by`           varchar(80) NOT NULL,
    PRIMARY KEY (`f_schedule_detail_id`),
    FOREIGN KEY (`f_schedule_id`) REFERENCES `F_SCHEDULE` (`f_schedule_id`)
);

CREATE TABLE `ACHIEVEMENT`
(
    `achievement_id`   bigint NOT NULL AUTO_INCREMENT,
    `member_id`        bigint NOT NULL,
    `achievement_date` date   NOT NULL,
    `achievement_rate` double NOT NULL DEFAULT 0,
    `created_at`        datetime NOT NULL,
    `updated_at`       datetime NOT NULL,
    `created_by`       varchar(80) NOT NULL,
    `updated_by`       varchar(80) NOT NULL,
    PRIMARY KEY (`achievement_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `NOTIFICATION`
(
    `notification_id` bigint  NOT NULL AUTO_INCREMENT,
    `member_id`       bigint  NOT NULL,
    `notification_type`  ENUM('REPLY_ADD','NESTED_REPLY_ADD', 'TODO_UPDATE','ACHIEVEMENT_FEEDBACK') NOT NULL,
    `message`         TEXT    NOT NULL,
    `target_url`    varchar(255) NULL,
    `related_entity_id`       bigint NULL,
    `is_read`         boolean NOT NULL DEFAULT false,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    `created_by`       varchar(80) NOT NULL,
    `updated_by`       varchar(80) NOT NULL,
    PRIMARY KEY (`notification_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- password = password1
INSERT INTO MEMBER (username, nickname, email, password, phone_number, gender, role, mode, profile_image_path, score,
                    auth_email, auth_phone, created_at, updated_at, created_by, updated_by)
VALUES ('일반테스트회원1', 'nick1', 'normal@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2','01012345678', 'MALE', 'NORMAL_USER', 'MILD', NULL, 10, true, false, NOW(), NOW(), 'system', 'system'),
       ('일반테스트회원2', 'nick2', 'normal2@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2','01012345671', 'FEMALE', 'NORMAL_USER', 'MILD', NULL, 11, true, false, NOW(), NOW(), 'system', 'system'),
       ('일반테스트회원3', 'nick3', 'normal3@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2', '01012345673', 'MALE', 'NORMAL_USER', 'SPICY', NULL, 12, true, false, NOW(), NOW(), 'system', 'system'),
       ('일반테스트회원4', 'nick4', 'normal4@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2','01012345675', 'FEMALE', 'NORMAL_USER', 'SPICY', NULL, 13, true, false, NOW(), NOW(), 'system', 'system'),
       ('관리자테스트회원2', 'nick5', 'admin@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2','01023456789', 'FEMALE', 'ADMIN', 'SPICY', NULL, 20, true, true, NOW(), NOW(), 'system', 'system'),
       ('성실테스트회원3', 'nick6', 'faithful@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2','01034567890', 'MALE', 'FAITHFUL_USER', 'MILD', NULL, 30, false, true, NOW(), NOW(), 'system', 'system');
--

INSERT INTO POST (member_id, title, contents, total_like, total_scrap, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 'First PostEntity Post 1', 'This is the contents of the first postEntity post for memberEntity 1.', 5, 2,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 2', 'This is the contents of the second postEntity post for memberEntity 1.', 3, 1,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 3', 'This is the contents of the third postEntity post for memberEntity 1.', 4, 0,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 4', 'This is the contents of the fourth postEntity post for memberEntity 1.', 2, 3,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 5', 'This is the contents of the fifth postEntity post for memberEntity 1.', 6, 1,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 6', 'This is the contents of the sixth postEntity post for memberEntity 1.', 1, 0,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 7', 'This is the contents of the seventh postEntity post for memberEntity 1.', 0, 0,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 8', 'This is the contents of the eighth postEntity post for memberEntity 1.', 7, 2,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 9', 'This is the contents of the ninth postEntity post for memberEntity 1.', 2, 1,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 10', 'This is the contents of the tenth postEntity post for memberEntity 1.', 5, 3,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 11', 'This is the contents of the eleventh postEntity post for memberEntity 1.', 4, 2,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 12', 'This is the contents of the twelfth postEntity post for memberEntity 1.', 3, 0,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 13', 'This is the contents of the thirteenth postEntity post for memberEntity 1.', 6, 1,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 14', 'This is the contents of the fourteenth postEntity post for memberEntity 1.', 2, 2,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'First PostEntity Post 15', 'This is the contents of the fifteenth postEntity post for memberEntity 1.',1, 0,  NOW(), NOW(), 'normal@example.com', 'normal@example.com'),

    (2, 'Second PostEntity Post 1', 'This is the contents of the first postEntity post for memberEntity 2.', 5, 2,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 2', 'This is the contents of the second postEntity post for memberEntity 2.', 3, 1,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 3', 'This is the contents of the third postEntity post for memberEntity 2.', 4, 0,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 4', 'This is the contents of the fourth postEntity post for memberEntity 2.', 2, 3,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 5', 'This is the contents of the fifth postEntity post for memberEntity 2.', 6, 1,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 6', 'This is the contents of the sixth postEntity post for memberEntity 2.', 1, 0,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 7', 'This is the contents of the seventh postEntity post for memberEntity 2.', 0, 0,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 8', 'This is the contents of the eighth postEntity post for memberEntity 2.', 7, 2,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 9', 'This is the contents of the ninth postEntity post for memberEntity 2.', 2, 1,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 10', 'This is the contents of the tenth postEntity post for memberEntity 2.', 5, 3,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 11', 'This is the contents of the eleventh postEntity post for memberEntity 2.', 4, 2,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 12', 'This is the contents of the twelfth postEntity post for memberEntity 2.', 3, 0,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 13', 'This is the contents of the thirteenth postEntity post for memberEntity 2.', 6, 1,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 14', 'This is the contents of the fourteenth postEntity post for memberEntity 2.', 2, 2,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 'Second PostEntity Post 15', 'This is the contents of the fifteenth postEntity post for memberEntity 2.',1, 0,  NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),

    (3, 'Third PostEntity Post 1', 'This is the contents of the first postEntity post for memberEntity 3.', 5, 2,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 2', 'This is the contents of the second postEntity post for memberEntity 3.', 3, 1,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 3', 'This is the contents of the third postEntity post for memberEntity 3.', 4, 0,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 4', 'This is the contents of the fourth postEntity post for memberEntity 3.', 2, 3,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 5', 'This is the contents of the fifth postEntity post for memberEntity 3.', 6, 1,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 6', 'This is the contents of the sixth postEntity post for memberEntity 3.', 1, 0,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 7', 'This is the contents of the seventh postEntity post for memberEntity 3.', 0, 0,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 8', 'This is the contents of the eighth postEntity post for memberEntity 3.', 7, 2,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 9', 'This is the contents of the ninth postEntity post for memberEntity 3.', 2, 1,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 10', 'This is the contents of the tenth postEntity post for memberEntity 3.', 5, 3,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 11', 'This is the contents of the eleventh postEntity post for memberEntity 3.', 4, 2,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 12', 'This is the contents of the twelfth postEntity post for memberEntity 3.', 3, 0,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 13', 'This is the contents of the thirteenth postEntity post for memberEntity 3.', 6, 1,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 14', 'This is the contents of the fourteenth postEntity post for memberEntity 3.', 2, 2,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 'Third PostEntity Post 15', 'This is the contents of the fifteenth postEntity post for memberEntity 3.', 1, 0,  NOW(), NOW(), 'normal3@example.com', 'normal3@example.com');

INSERT INTO REPLY (member_id, post_id, parent_id, contents, total_like, created_at, updated_at, created_by,updated_by)
VALUES
    (1, 1, NULL, '게시글 1 - 댓글 1', 100,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 1, NULL, '게시글 1 - 댓글 2', 100,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 1, NULL, '게시글 1 - 댓글 3', 100,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 1, NULL, '게시글 1 - 댓글 4', 1, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 2, NULL, '게시글 2 - 댓글 1', 100,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 2, NULL, '게시글 2 - 댓글 2', 20,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 2, NULL, '게시글 2 - 댓글 3', 17,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 1, 1, '게시글 1- 댓글 1 - 대댓글-2', 30, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 1, 2, '게시글 1- 댓글 2 - 대댓글-2', 30, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 2, 5, '게시글 2- 댓글 1 - 대댓글-1', 30, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 2, 5, '게시글 2- 댓글 1 - 대댓글-2', 30, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (2, 1, NULL, '게시글 1 - 댓글 4', 10,NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 1, 1, '게시글 1- 댓글 1 - 대댓글-1', 20, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 1, 3, '게시글 1- 댓글 3 - 대댓글-1', 30, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 2, 7, '게시글 2- 댓글 3 - 대댓글-1', 30, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (2, 2, 7, '게시글 2- 댓글 3 - 대댓글-2', 30, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (3, 1, NULL, '게시글 1 - 댓글 5', 9,NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 1, NULL, '게시글 1 - 댓글 6', 8,NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 1, 2, '게시글 1- 댓글 2 - 대댓글-1', 30, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 1, 3, '게시글 1- 댓글 3 - 대댓글-2', 30, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 2, 7, '게시글 2- 댓글 3 - 대댓글-1', 30, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (3, 2, 7, '게시글 2- 댓글 3 - 대댓글-2', 30, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com');

INSERT INTO POST_HEART (member_id, post_id, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 1,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 2,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (2, 1,NOW(), NOW(), 'normal2@example.com', 'normal2@example.com');

INSERT INTO POST_SCRAP (member_id, post_id, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 1,NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (2, 2,NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (3, 3,NOW(), NOW(), 'normal3@example.com', 'normal3@example.com');

INSERT INTO REPLY_HEART (member_id, reply_id, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 1,NOW(), NOW(), 'normal1@example.com', 'normal1@example.com'),
    (2, 2,NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (3, 3,NOW(), NOW(), 'normal3@example.com', 'normal3@example.com'),
    (1, 4,NOW(), NOW(), 'normal1@example.com', 'normal1@example.com'),
    (1, 10,NOW(), NOW(), 'normal1@example.com', 'normal1@example.com'),
    (2, 10,NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (3, 10,NOW(), NOW(), 'normal3@example.com', 'normal3@example.com');


INSERT INTO N_SCHEDULE (member_id, title, common_description, start_date_time, end_date_time, buffer_time, total_amount, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 'normal schedule1', 'Description of the fourth scheduleEntity.', '2024-05-01 00:00:00', '2024-05-07 00:00:00', '02:00:00', 100, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'normal schedule3', 'Description of the fourth scheduleEntity.', '2024-05-01 00:00:00', '2024-05-07 00:00:00', '02:00:00', 100, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (2, 'normal schedule2', 'Description of the fifth scheduleEntity.', '2024-06-01 00:00:00', '2024-06-14 00:00:00', '01:00:00', 200, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (1, 'normal schedule3', 'Description of the sixth scheduleEntity.', '2024-07-01 00:00:00', '2024-07-22 00:00:00', '00:30:00', 300, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'normal schedule3', 'Description of the sixth scheduleEntity.', '2024-07-01 00:00:00', '2024-07-22 00:00:00', '00:30:00', 300, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'normal schedule3', 'Description of the sixth scheduleEntity.', '2024-07-01 00:00:00', '2024-07-22 00:00:00', '00:30:00', 300, NOW(), NOW(), 'normal@example.com', 'normal@example.com');

INSERT INTO N_SCHEDULE_DETAIL (n_schedule_id, detail_description, start_date_time, end_date_time, buffer_time, complete_status, daily_amount, achieved_amount, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 'Detail of the first N_SCHEDULE.', '2024-05-01 09:00:00', '2024-05-01 10:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00',  '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first N_SCHEDULE.', '2024-05-02 10:00:00', '2024-05-02 11:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first N_SCHEDULE.', '2024-05-03 11:00:00', '2024-05-03 12:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first N_SCHEDULE.', '2024-05-04 12:00:00', '2024-05-04 13:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first N_SCHEDULE.', '2024-05-05 13:00:00', '2024-05-05 14:00:00', '01:00:00', false, 0, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (2, 'Detail of the first N_SCHEDULE.', '2024-05-01 13:00:00', '2024-05-05 14:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (2, 'Detail of the first N_SCHEDULE.', '2024-05-02 13:00:00', '2024-05-05 14:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (2, 'Detail of the first N_SCHEDULE.', '2024-05-03 13:00:00', '2024-05-05 14:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (2, 'Detail of the first N_SCHEDULE.', '2024-05-04 13:00:00', '2024-05-05 14:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (2, 'Detail of the first N_SCHEDULE.', '2024-05-05 13:00:00', '2024-05-05 14:00:00', '01:00:00', false, 20, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (3, 'Detail of the second N_SCHEDULE.', '2024-06-01 09:00:00', '2024-06-01 10:00:00', '01:00:00', false, 40, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (3, 'Detail of the second N_SCHEDULE.', '2024-06-01 12:00:00', '2024-06-01 13:00:00', '01:00:00', false, 40, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (3, 'Detail of the second N_SCHEDULE.', '2024-06-02 10:00:00', '2024-06-02 11:00:00', '01:00:00', false, 40, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (3, 'Detail of the second N_SCHEDULE.', '2024-06-03 11:00:00', '2024-06-03 12:00:00', '01:00:00', false, 40, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (3, 'Detail of the second N_SCHEDULE.', '2024-06-04 12:00:00', '2024-06-04 13:00:00', '01:00:00', false, 40, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (3, 'Detail of the second N_SCHEDULE.', '2024-06-05 13:00:00', '2024-06-05 14:00:00', '01:00:00', false, 40, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (4, 'Detail of the third N_SCHEDULE.', '2024-07-01 09:00:00', '2024-07-01 10:00:00', '01:00:00', false, 60, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (4, 'Detail of the third N_SCHEDULE.', '2024-07-02 10:00:00', '2024-07-02 11:00:00', '01:00:00', false, 60, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (4, 'Detail of the third N_SCHEDULE.', '2024-07-03 11:00:00', '2024-07-03 12:00:00', '01:00:00', false, 60, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (4, 'Detail of the third N_SCHEDULE.', '2024-07-04 12:00:00', '2024-07-04 13:00:00', '01:00:00', false, 60, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com'),
    (4, 'Detail of the third N_SCHEDULE.', '2024-07-05 13:00:00', '2024-07-05 14:00:00', '01:00:00', false, 60, 0, '2024-04-30 09:00:00', '2024-04-30 09:00:00', 'normal@example.com', 'normal@example.com');

INSERT INTO F_SCHEDULE (member_id, title, common_description, start_date_time, end_date_time, created_at, updated_at, created_by, updated_by)
VALUES
    (1,'fixed schedule1', 'Description of the first scheduleEntity.', '2024-01-01 00:00:00', '2024-01-07 00:00:00', NOW(), NOW(), 'normal@example.com','normal@example.com'),
    (2,'fixed schedule2', 'Description of the second scheduleEntity.', '2024-02-01 00:00:00', '2024-02-14 00:00:00', NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
    (1,'fixed schedule3', 'Description of the third scheduleEntity.', '2024-03-01 00:00:00', '2024-03-21 00:00:00', NOW(), NOW(), 'normal@example.com','normal@example.com'),
    (1,'fixed schedule3', 'Description of the third scheduleEntity.', '2024-03-01 00:00:00', '2024-03-21 00:00:00', NOW(), NOW(), 'normal@example.com','normal@example.com'),
    (1,'fixed schedule3', 'Description of the third scheduleEntity.', '2024-03-01 00:00:00', '2024-03-21 00:00:00', NOW(), NOW(), 'normal@example.com','normal@example.com');

INSERT INTO F_SCHEDULE_DETAIL (f_schedule_id, detail_description, start_date_time, end_date_time, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 'Detail of the first F_SCHEDULE.', '2024-01-01 10:00:00', '2024-01-01 14:00:00', '2024-01-01 09:00:00', '2024-01-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first F_SCHEDULE.', '2024-01-01 17:30:00', '2024-01-01 18:00:00', '2024-01-01 09:00:00', '2024-01-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first F_SCHEDULE.', '2024-01-01 21:30:00', '2024-01-01 22:00:00', '2024-01-01 09:00:00', '2024-01-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first F_SCHEDULE.', '2024-01-03 10:00:00', '2024-01-03 14:00:00', '2024-01-01 09:00:00', '2024-01-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first F_SCHEDULE.', '2024-01-04 10:00:00', '2024-01-04 14:00:00', '2024-01-01 09:00:00', '2024-01-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (1, 'Detail of the first F_SCHEDULE.', '2024-01-05 10:00:00', '2024-01-05 14:00:00', '2024-01-01 09:00:00', '2024-01-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (2, 'Detail of the second F_SCHEDULE.', '2024-02-01 10:00:00', '2024-02-01 14:00:00', '2024-02-01 09:00:00', '2024-02-01 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (2, 'Detail of the second F_SCHEDULE.', '2024-02-08 10:00:00', '2024-02-08 14:00:00', '2024-02-01 09:00:00', '2024-02-01 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (2, 'Detail of the second F_SCHEDULE.', '2024-02-14 10:00:00', '2024-02-14 14:00:00', '2024-02-01 09:00:00', '2024-02-01 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (2, 'Detail of the second F_SCHEDULE.', '2024-02-03 10:00:00', '2024-02-03 14:00:00', '2024-02-01 09:00:00', '2024-02-01 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (2, 'Detail of the second F_SCHEDULE.', '2024-02-10 10:00:00', '2024-02-10 14:00:00', '2024-02-01 09:00:00', '2024-02-01 09:00:00', 'normal2@example.com', 'normal2@example.com'),
    (3, 'Detail of the third F_SCHEDULE.', '2024-03-01 10:00:00', '2024-03-01 14:00:00', '2024-03-01 09:00:00', '2024-03-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (3, 'Detail of the third F_SCHEDULE.', '2024-03-08 10:00:00', '2024-03-08 14:00:00', '2024-03-01 09:00:00', '2024-03-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (3, 'Detail of the third F_SCHEDULE.', '2024-03-14 10:00:00', '2024-03-14 14:00:00', '2024-03-01 09:00:00', '2024-03-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (3, 'Detail of the third F_SCHEDULE.', '2024-03-21 10:00:00', '2024-03-21 14:00:00', '2024-03-01 09:00:00', '2024-03-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (4, 'Detail of the third F_SCHEDULE.', '2024-03-21 10:00:00', '2024-03-21 14:00:00', '2024-03-01 09:00:00', '2024-03-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (4, 'Detail of the third F_SCHEDULE.', '2024-03-21 10:00:00', '2024-03-21 14:00:00', '2024-03-01 09:00:00', '2024-03-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (4, 'Detail of the third F_SCHEDULE.', '2024-03-21 10:00:00', '2024-03-21 14:00:00', '2024-03-01 09:00:00', '2024-03-01 09:00:00', 'normal@example.com', 'normal@example.com'),
    (4, 'Detail of the third F_SCHEDULE.', '2024-03-21 10:00:00', '2024-03-21 14:00:00', '2024-03-01 09:00:00', '2024-03-01 09:00:00', 'normal@example.com', 'normal@example.com');

INSERT INTO V_SCHEDULE (member_id, title, common_description, start_date_time, end_date_time, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 'variable Schedule1', 'Description of the seventh scheduleEntity.', '2024-08-01 00:00:00', '2024-08-01 00:00:00', NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, 'variable Schedule2', 'Description of the eighth scheduleEntity.', '2024-09-01 00:00:00', '2024-09-01 00:00:00', NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (2, 'variable Schedule3', 'Description of the ninth scheduleEntity.', '2024-10-01 00:00:00', '2024-10-01 00:00:00', NOW(), NOW(), 'normal2@example.com', 'normal2@example.com');

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
    (1, '2024-05-02', 0, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
    (1, '2024-05-03', 0, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
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

INSERT INTO NOTIFICATION (member_id, notification_type, message, target_url, related_entity_id, is_read, created_at, updated_at, created_by, updated_by)
VALUES (1, 'REPLY_ADD', 'You have a new message!', '/postEntity/1', 2, false, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
       (1, 'REPLY_ADD', 'You have a new message!', '/postEntity/1', 2, false, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
       (1, 'REPLY_ADD', 'You have a new message!', '/postEntity/1', 2, false, NOW(), NOW(), 'normal@example.com', 'normal@example.com'),
       (2, 'TODO_UPDATE', 'Your scheduleEntity is updated.', NULL, NULL, false, NOW(), NOW(), 'normal2@example.com', 'normal2@example.com'),
       (3, 'ACHIEVEMENT_FEEDBACK', 'Your post received a new comment.', NULL, NULL, false, NOW(), NOW(), 'normal3@example.com', 'normal3@example.com');