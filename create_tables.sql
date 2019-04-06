CREATE TABLE IF NOT EXISTS `test_db`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `phone` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `is_active` BINARY(1) NULL,
  `is_blocked` BINARY(1) NULL,
  `created_at` DATETIME default current_timestamp,
  `updated_at` DATETIME default current_timestamp,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_db`.`conversation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creator_id` INT NOT NULL,
  `created_at` DATETIME default current_timestamp,
  `updated_at` DATETIME NULL,
  `deleted_at` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_id_idx` (`creator_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_id_conversation`
    FOREIGN KEY (`creator_id`)
    REFERENCES `test_db`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
    
CREATE TABLE IF NOT EXISTS `test_db`.`participants` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `conversation_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_id_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_conversation_id_idx` (`conversation_id` ASC) VISIBLE,
CONSTRAINT `fk_user_id_participants`
    FOREIGN KEY (`user_id`)
    REFERENCES `test_db`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
CONSTRAINT `fk_conversation_id_participants`
    FOREIGN KEY (`conversation_id`)
    REFERENCES `test_db`.`conversation` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);

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