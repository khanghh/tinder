USE `test_db`;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+07:00";

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(256) NULL,
  `gender` tinyint(1) NULL,
  `age` int(11) NULL,
  `phone` VARCHAR(45) NULL,
  `description` VARCHAR(256) NULL,
  `longitude` INT,
  `latitude` INT,
  `max_distance` int,
  `min_age` int,
  `max_age` int,
  `is_activate` tinyint(1) NULL,
  `is_banned` tinyint(1) NULL,
  `ban_reason` varchar(256) null,
  `exprired_ban` DATETIME,
  `created_at` DATETIME default current_timestamp,
  `updated_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

create table if not exists `images` (
	`id` int not null AUTO_INCREMENT,
    `user_id` int,
    `path_image` VARCHAR(50) NULL,
    `created_at` DATETIME default current_timestamp,
	`updated_at` DATETIME NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_user_id_conversation`
		FOREIGN KEY (`user_id`)
		REFERENCES `user` (`id`)
		ON DELETE CASCADE
		ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `conversation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creator_id` INT NOT NULL,
  `member_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  `is_deleted` tinyint(1) NULL,
  `deleted_at` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY(`creator_id`,`member_id`),
  INDEX `fk_user_id_idx` (`creator_id` ASC),
  CONSTRAINT `fk_creator_id_conversation`
    FOREIGN KEY (`creator_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_member_id_conversation`
    FOREIGN KEY (`member_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `messages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `conversation_id` INT NOT NULL,
  `sender_id` INT NOT NULL,
  `message` VARCHAR(45) CHARACTER SET utf8mb4 NULL,
  `seen` tinyint(1) NULL,
  `seen_at` DATETIME NULL,
  `created_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`, `sender_id`),
  INDEX `fk_user_id_idx` (`sender_id` ASC),
  INDEX `fk_conversation_id_idx` (`conversation_id` ASC)
)
PARTITION BY RANGE(sender_id) PARTITIONS 10
(
	PARTITION part0 VALUES LESS THAN (1000),
	PARTITION part1 VALUES LESS THAN (2000),
	PARTITION part2 VALUES LESS THAN (3000),
	PARTITION part3 VALUES LESS THAN (4000),
	PARTITION part4 VALUES LESS THAN (5000),
	PARTITION part5 VALUES LESS THAN (6000),
	PARTITION part6 VALUES LESS THAN (7000),
	PARTITION part7 VALUES LESS THAN (8000),
	PARTITION part8 VALUES LESS THAN (9000),
	PARTITION part9 VALUES LESS THAN (10000));

CREATE TABLE IF NOT EXISTS `passed_users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `passer_id` INT NOT NULL,
  `passed_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`, `passer_id`),
  UNIQUE KEY(`passer_id`,`passed_id`),
  INDEX `fk_passer_id_idx` (`passer_id` ASC),
  INDEX `fk_passed_user_id_idx` (`passed_id` ASC)
)
PARTITION BY RANGE(passer_id) PARTITIONS 10
(
	PARTITION part0 VALUES LESS THAN (1000),
	PARTITION part1 VALUES LESS THAN (2000),
	PARTITION part2 VALUES LESS THAN (3000),
	PARTITION part3 VALUES LESS THAN (4000),
	PARTITION part4 VALUES LESS THAN (5000),
	PARTITION part5 VALUES LESS THAN (6000),
	PARTITION part6 VALUES LESS THAN (7000),
	PARTITION part7 VALUES LESS THAN (8000),
	PARTITION part8 VALUES LESS THAN (9000),
	PARTITION part9 VALUES LESS THAN (10000));

CREATE TABLE IF NOT EXISTS `user_likes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `liker_id` INT NOT NULL,
  `liked_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`, `liker_id`),
  UNIQUE KEY(`liker_id`,`liked_id`),
  INDEX `fk_liker_id_idx` (`liker_id` ASC),
  INDEX `fk_liked_id_idx` (`liked_id` ASC)
  )
PARTITION BY RANGE(liker_id) PARTITIONS 10
(
	PARTITION part0 VALUES LESS THAN (1000),
	PARTITION part1 VALUES LESS THAN (2000),
	PARTITION part2 VALUES LESS THAN (3000),
	PARTITION part3 VALUES LESS THAN (4000),
	PARTITION part4 VALUES LESS THAN (5000),
	PARTITION part5 VALUES LESS THAN (6000),
	PARTITION part6 VALUES LESS THAN (7000),
	PARTITION part7 VALUES LESS THAN (8000),
	PARTITION part8 VALUES LESS THAN (9000),
	PARTITION part9 VALUES LESS THAN (10000));

-- some test SQL query
-- select * from user_likes;
-- delete from user_likes where id > 0;

-- ALTER TABLE test_db.user_likes AUTO_INCREMENT = 1;

-- select * from test_db.conversation where creator_id = 5;

-- insert into test_db.messages (sender_id, conversation_id, message) values (1, 10, "Hello");
-- insert into test_db.messages (sender_id, conversation_id, message) values (5, 10, "Hi");
-- insert into test_db.messages (sender_id, conversation_id, message) values (1, 10, "What's your name?");
-- insert into test_db.messages (sender_id, conversation_id, message) values (5, 10, "Hoang. How about you?");
-- insert into test_db.messages (sender_id, conversation_id, message) values (1, 10, "Khang");

-- select * from test_db.messages where conversation_id = 10 order by created_at;

-- select * from test_db.conversation where creator_id = 5 or member_id = 5;
