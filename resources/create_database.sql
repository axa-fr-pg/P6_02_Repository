/* Setting up PROD DB */
drop database pay_my_buddy_prod;
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

CREATE TABLE `account` (
  `type` tinyint NOT NULL,
  `user_id` int NOT NULL,
  `balance` decimal(9,3) NOT NULL,
  `bic` varchar(11) NOT NULL,
  `iban` varchar(34) NOT NULL,
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

/* Setting up TEST DB */
drop database pay_my_buddy_test;
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
  `bic` varchar(11) NOT NULL,
  `iban` varchar(34) NOT NULL,
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