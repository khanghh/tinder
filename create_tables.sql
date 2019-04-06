CREATE TABLE IF NOT EXISTS `test_db`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `phone` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(50) NULL,
  `longitude` INT,
  `latitude` INT,
  `max_distance` int,
  `min_age` int,
  `max_age` int,
  `is_active` BINARY(1) NULL,
  `is_blocked` BINARY(1) NULL,
  `created_at` DATETIME default current_timestamp,
  `updated_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

create table if not exists `test_db`.`images` (
	`id` int not null AUTO_INCREMENT,
    `user_id` int,
    `path_image` VARCHAR(50) NULL,
    `created_at` DATETIME default current_timestamp,
	`updated_at` DATETIME NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_user_id_conversation`
		FOREIGN KEY (`user_id`)
		REFERENCES `test_db`.`user` (`id`)
		ON DELETE CASCADE
		ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `test_db`.`conversation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creator_id` INT NOT NULL,
  `member_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  `updated_at` DATETIME NULL,
  `deleted_at` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_id_idx` (`creator_id` ASC) VISIBLE,
  CONSTRAINT `fk_creator_id_conversation`
    FOREIGN KEY (`creator_id`)
    REFERENCES `test_db`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_member_id_conversation`
    FOREIGN KEY (`member_id`)
    REFERENCES `test_db`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `test_db`.`messages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `conversation_id` INT NOT NULL,
  `sender_id` INT NOT NULL,
  `message` VARCHAR(45) NULL,
  `created_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`, `sender_id`),
  INDEX `fk_user_id_idx` (`sender_id` ASC) VISIBLE,
  INDEX `fk_conversation_id_idx` (`conversation_id` ASC) VISIBLE
)
PARTITION BY RANGE(sender_id) PARTITIONS 10
( 
	PARTITION part0 VALUES LESS THAN (100),  
	PARTITION part1 VALUES LESS THAN (200),  
	PARTITION part2 VALUES LESS THAN (300),  
	PARTITION part3 VALUES LESS THAN (400),  
	PARTITION part4 VALUES LESS THAN (500),  
	PARTITION part5 VALUES LESS THAN (600),  
	PARTITION part6 VALUES LESS THAN (700),  
	PARTITION part7 VALUES LESS THAN (800),  
	PARTITION part8 VALUES LESS THAN (900),  
	PARTITION part9 VALUES LESS THAN (1000));
    
CREATE TABLE IF NOT EXISTS `test_db`.`deleted_conversations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `conversation_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  `deleted_conversationscol` VARCHAR(45) NULL,
  PRIMARY KEY (`id`, `conversation_id`, `user_id`),
  INDEX `fk_conversation_id_idx` (`conversation_id` ASC) VISIBLE,
  INDEX `fk_user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_conversation_id_deleted_conversations`
    FOREIGN KEY (`conversation_id`)
    REFERENCES `test_db`.`conversation` (`id`)
    ON DELETE cascade
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_id_deleted_conversations`
    FOREIGN KEY (`user_id`)
    REFERENCES `test_db`.`user` (`id`)
    ON DELETE cascade
    ON UPDATE NO ACTION);
    
CREATE TABLE IF NOT EXISTS `test_db`.`deleted_messages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `message_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`),
  INDEX `fk_message_id_idx` (`message_id` ASC) VISIBLE,
  INDEX `fk_user_id_idx` (`user_id` ASC) VISIBLE);
    
CREATE TABLE IF NOT EXISTS `test_db`.`seen_users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creator_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  `seened_user_id` INT NOT NULL,
  PRIMARY KEY (`id`, `creator_id`),
  INDEX `fk_creator_id_idx` (`creator_id` ASC) VISIBLE,
  INDEX `fk_seened_user_id_idx` (`seened_user_id` ASC) VISIBLE
) 
PARTITION BY RANGE(creator_id) PARTITIONS 10
( 
	PARTITION part0 VALUES LESS THAN (100),  
	PARTITION part1 VALUES LESS THAN (200),  
	PARTITION part2 VALUES LESS THAN (300),  
	PARTITION part3 VALUES LESS THAN (400),  
	PARTITION part4 VALUES LESS THAN (500),  
	PARTITION part5 VALUES LESS THAN (600),  
	PARTITION part6 VALUES LESS THAN (700),  
	PARTITION part7 VALUES LESS THAN (800),  
	PARTITION part8 VALUES LESS THAN (900),  
	PARTITION part9 VALUES LESS THAN (1000)) ;

CREATE TABLE IF NOT EXISTS `test_db`.`user_likes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `liker_user_id` INT NOT NULL,
  `liked_user_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`, `liker_user_id`),
  INDEX `fk_liker_user_id_idx` (`liker_user_id` ASC) VISIBLE,
  INDEX `fk_liked_user_id_idx` (`liked_user_id` ASC) VISIBLE
  )
PARTITION BY RANGE(liker_user_id) PARTITIONS 10
( 
	PARTITION part0 VALUES LESS THAN (100),  
	PARTITION part1 VALUES LESS THAN (200),  
	PARTITION part2 VALUES LESS THAN (300),  
	PARTITION part3 VALUES LESS THAN (400),  
	PARTITION part4 VALUES LESS THAN (500),  
	PARTITION part5 VALUES LESS THAN (600),  
	PARTITION part6 VALUES LESS THAN (700),  
	PARTITION part7 VALUES LESS THAN (800),  
	PARTITION part8 VALUES LESS THAN (900),  
	PARTITION part9 VALUES LESS THAN (1000));
  
-- trigger when insert new row in user_likes,so create new convertion if they are matched
DELIMITER //
CREATE TRIGGER `user_likes_AFTER_INSERT` AFTER INSERT ON `user_likes` 
FOR EACH ROW 
BEGIN
	declare is_like int(11);
    -- check if they are matched or not
    select count(*) 
    from `test_db`.`user_likes`
    where liker_user_id = NEW.liked_user_id and liked_user_id = NEW.liker_user_id
    into is_like;
    -- if they are matched
    if is_like > 0 then
		-- create new row in conversation
		insert into `test_db`.`conversation` (creator_id, member_id) values (NEW.liker_user_id, NEW.liked_user_id);
	end if;
END
DELIMITER ;

-- some test SQL query
select * from user_likes;
delete from user_likes where id > 0;

ALTER TABLE test_db.user_likes AUTO_INCREMENT = 1;

select * from test_db.conversation where creator_id = 5;

insert into test_db.messages (sender_id, conversation_id, message) values (1, 10, "Hello");
insert into test_db.messages (sender_id, conversation_id, message) values (5, 10, "Hi");
insert into test_db.messages (sender_id, conversation_id, message) values (1, 10, "What's your name?");
insert into test_db.messages (sender_id, conversation_id, message) values (5, 10, "Hoang. How about you?");
insert into test_db.messages (sender_id, conversation_id, message) values (1, 10, "Khang");

select * from test_db.messages where conversation_id = 10 order by created_at;