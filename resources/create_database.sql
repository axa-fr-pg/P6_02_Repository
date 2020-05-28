/* Setting up PROD DB */
drop database if exists pay_my_buddy_prod;
create database pay_my_buddy_prod;
use pay_my_buddy_prod;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(30) NOT NULL,
  `password` varchar(60) NOT NULL,
  `type` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_index` (`email`)
);
INSERT INTO `user` VALUES 
	(7,'EMAIL_FOR_COMMISSION_USER','',0),
	(8,'philippe.gey@axa.fr','$2y$10$MNyoW.RghhyEhWEV4Ic61OX6unMMjs2L.PsUQ8MGSmRfUPCLAaZ4W',0),
	(9,'my.wife@home.fr','$2y$10$3oN0znyPYd3ulsyL5r6PCuKsDCjuUl1Y.cnZExjesp2PAnVS5uPrC',0);

CREATE TABLE `account` (
  `type` tinyint NOT NULL,
  `user_id` int NOT NULL,
  `balance` decimal(9,3) NOT NULL,
  `bic` varchar(11) DEFAULT  NULL,
  `iban` varchar(34) DEFAULT NULL,
  PRIMARY KEY (`type`,`user_id`),
  KEY `FK_user_id` (`user_id`),
  CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
INSERT INTO `account` VALUES 
	(0,7,1000.000,NULL,NULL),
	(0,8,100000.000,NULL,NULL),
	(0,9,50000.000,NULL,NULL),
	(1,8,900000.000,'my_bic','this_is_my_iban');

CREATE TABLE `persistent_logins` (
  `series` varchar(64) NOT NULL,
  `last_used` datetime(6) DEFAULT NULL,
  `token` varchar(64) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`series`),
  KEY `FK_username` (`username`),
  CONSTRAINT `FK_email` FOREIGN KEY (`username`) REFERENCES `user` (`email`)
);

CREATE TABLE `relation` (
  `user_credit_id` int NOT NULL,
  `user_debit_id` int NOT NULL,
  PRIMARY KEY (`user_credit_id`,`user_debit_id`),
  KEY `IFK_user_credit_id` (`user_credit_id`),
  KEY `IFK_user_debit_id` (`user_debit_id`),
  CONSTRAINT `FK_user_credit_id` FOREIGN KEY (`user_credit_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_user_debit_id` FOREIGN KEY (`user_debit_id`) REFERENCES `user` (`id`)
);
INSERT INTO `relation` VALUES 
	(8,9),
	(9,8);

CREATE TABLE `transfer` (
  `transfer_id` int NOT NULL,
  `amount` decimal(9,3) NOT NULL,
  `description` varchar(100) NOT NULL,
  `account_credit_type` tinyint NOT NULL,
  `account_credit_user_id` int NOT NULL,
  `account_debit_type` tinyint NOT NULL,
  `account_debit_user_id` int NOT NULL,
  PRIMARY KEY (`account_credit_type`,`account_credit_user_id`,`account_debit_type`,`account_debit_user_id`,`transfer_id`),
  KEY `FK_account_credit` (`account_credit_user_id`,`account_credit_type`),
  KEY `FK_relation` (`account_credit_user_id`,`account_debit_user_id`),
  KEY `FK_account_debit` (`account_debit_type`,`account_debit_user_id`),
  CONSTRAINT `FK_account_credit` FOREIGN KEY (`account_credit_type`, `account_credit_user_id`) REFERENCES `account` (`type`, `user_id`),
  CONSTRAINT `FK_account_debit` FOREIGN KEY (`account_debit_type`, `account_debit_user_id`) REFERENCES `account` (`type`, `user_id`)
);
INSERT INTO `transfer` VALUES 
	(103,750.000,'Loading my money',0,7,0,8),
	(105,250.000,'Sharing with my wife',0,7,0,8),
	(102,150000.000,'Loading my money',0,8,1,8),
	(104,50000.000,'Sharing with my wife',0,9,0,8);

CREATE TABLE `transfer_sequence` (
  `next_val` bigint DEFAULT NULL
);
INSERT INTO `transfer_sequence` VALUES (201);

/* Setting up TEST DB */
drop database if exists pay_my_buddy_test;
create database pay_my_buddy_test;
use pay_my_buddy_test;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(30) NOT NULL,
  `password` varchar(60) NOT NULL,
  `type` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_index` (`email`)
);

CREATE TABLE `account` (
  `type` tinyint NOT NULL,
  `user_id` int NOT NULL,
  `balance` decimal(9,3) NOT NULL,
  `bic` varchar(11) DEFAULT  NULL,
  `iban` varchar(34) DEFAULT NULL,
  PRIMARY KEY (`type`,`user_id`),
  KEY `FK_user_id` (`user_id`),
  CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `persistent_logins` (
  `series` varchar(64) NOT NULL,
  `last_used` datetime(6) DEFAULT NULL,
  `token` varchar(64) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`series`),
  KEY `FK_username` (`username`),
  CONSTRAINT `FK_email` FOREIGN KEY (`username`) REFERENCES `user` (`email`)
);

CREATE TABLE `relation` (
  `user_credit_id` int NOT NULL,
  `user_debit_id` int NOT NULL,
  PRIMARY KEY (`user_credit_id`,`user_debit_id`),
  KEY `IFK_user_credit_id` (`user_credit_id`),
  KEY `IFK_user_debit_id` (`user_debit_id`),
  CONSTRAINT `FK_user_credit_id` FOREIGN KEY (`user_credit_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_user_debit_id` FOREIGN KEY (`user_debit_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `transfer` (
  `transfer_id` int NOT NULL,
  `amount` decimal(9,3) NOT NULL,
  `description` varchar(100) NOT NULL,
  `account_credit_type` tinyint NOT NULL,
  `account_credit_user_id` int NOT NULL,
  `account_debit_type` tinyint NOT NULL,
  `account_debit_user_id` int NOT NULL,
  PRIMARY KEY (`account_credit_type`,`account_credit_user_id`,`account_debit_type`,`account_debit_user_id`,`transfer_id`),
  KEY `FK_account_credit` (`account_credit_user_id`,`account_credit_type`),
  KEY `FK_relation` (`account_credit_user_id`,`account_debit_user_id`),
  KEY `FK_account_debit` (`account_debit_type`,`account_debit_user_id`),
  CONSTRAINT `FK_account_credit` FOREIGN KEY (`account_credit_type`, `account_credit_user_id`) REFERENCES `account` (`type`, `user_id`),
  CONSTRAINT `FK_account_debit` FOREIGN KEY (`account_debit_type`, `account_debit_user_id`) REFERENCES `account` (`type`, `user_id`)
);

CREATE TABLE `transfer_sequence` (
  `next_val` bigint DEFAULT NULL
);
insert into transfer_sequence values (1);

commit;