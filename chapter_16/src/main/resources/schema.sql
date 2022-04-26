CREATE DATABASE ss_chapter16;
USE ss_chapter16;

CREATE TABLE `ss_chapter16`.`client` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client_id` VARCHAR(45) NULL,
  `secret` VARCHAR(45) NULL,
  `grant_type` VARCHAR(45) NULL,
  `scope` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

INSERT INTO `ss_chapter16`.`client` (`id`, `client_id`, `secret`, `grant_type`, `scope`) VALUES ('1', 'client1', 'secret1', 'password', 'read');
