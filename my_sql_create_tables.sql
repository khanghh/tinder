-- MySQL Script generated by MySQL Workbench
-- Sat Apr  6 12:34:20 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `phone` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `is_active` BINARY(1) NULL,
  `is_blocked` BINARY(1) NULL,
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`conversation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`conversation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creator_id` INT NOT NULL,
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  `deleted_at` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_id_idx` (`creator_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`creator_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `mydb`.`messages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`messages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `conversation_id` INT NOT NULL,
  `sender_id` INT NOT NULL,
  `message` VARCHAR(45) NULL,
  `created_at` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_id_idx` (`sender_id` ASC) VISIBLE,
  INDEX `fk_conversation_id_idx` (`conversation_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`sender_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_conversation_id`
    FOREIGN KEY (`conversation_id`)
    REFERENCES `mydb`.`conversation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `mydb`.`deleted_conversations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`deleted_conversations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `conversation_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `created_at` DATETIME NULL,
  `deleted_conversationscol` VARCHAR(45) NULL,
  PRIMARY KEY (`id`, `conversation_id`, `user_id`),
  INDEX `fk_conversation_id_idx` (`conversation_id` ASC) VISIBLE,
  INDEX `fk_user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_conversation_id`
    FOREIGN KEY (`conversation_id`)
    REFERENCES `mydb`.`conversation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `mydb`.`deleted_messages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`deleted_messages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `message_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `created_at` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_message_id_idx` (`message_id` ASC) VISIBLE,
  INDEX `fk_user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_message_id`
    FOREIGN KEY (`message_id`)
    REFERENCES `mydb`.`messages` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `mydb`.`user_likes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user_likes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `liker_user_id` INT NOT NULL,
  `liked_user_id` INT NOT NULL,
  `created_at` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_liker_user_id_idx` (`liker_user_id` ASC) VISIBLE,
  INDEX `fk_liked_user_id_idx` (`liked_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_liker_user_id`
    FOREIGN KEY (`liker_user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_liked_user_id`
    FOREIGN KEY (`liked_user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION) PARTITION BY RANGE(liker_user_id) PARTITIONS 10( PARTITION part0 VALUES LESS THAN (100),  PARTITION part1 VALUES LESS THAN (200),  PARTITION part2 VALUES LESS THAN (300),  PARTITION part3 VALUES LESS THAN (400),  PARTITION part4 VALUES LESS THAN (500),  PARTITION part5 VALUES LESS THAN (600),  PARTITION part6 VALUES LESS THAN (700),  PARTITION part7 VALUES LESS THAN (800),  PARTITION part8 VALUES LESS THAN (900),  PARTITION part9 VALUES LESS THAN (1000)) ;


-- -----------------------------------------------------
-- Table `mydb`.`seen_users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`seen_users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creator_id` INT NOT NULL,
  `created_at` DATETIME NULL,
  `seened_user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_creator_id_idx` (`creator_id` ASC) VISIBLE,
  INDEX `fk_seened_user_id_idx` (`seened_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_creator_id`
    FOREIGN KEY (`creator_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_seened_user_id`
    FOREIGN KEY (`seened_user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) PARTITION BY RANGE(creator_id) PARTITIONS 10( PARTITION part0 VALUES LESS THAN (100),  PARTITION part1 VALUES LESS THAN (200),  PARTITION part2 VALUES LESS THAN (300),  PARTITION part3 VALUES LESS THAN (400),  PARTITION part4 VALUES LESS THAN (500),  PARTITION part5 VALUES LESS THAN (600),  PARTITION part6 VALUES LESS THAN (700),  PARTITION part7 VALUES LESS THAN (800),  PARTITION part8 VALUES LESS THAN (900),  PARTITION part9 VALUES LESS THAN (1000)) ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
