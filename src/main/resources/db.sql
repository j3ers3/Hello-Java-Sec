CREATE DATABASE IF NOT EXISTS test default charset utf8 COLLATE utf8_general_ci;
use test;

CREATE TABLE `users`
(
    `id`       int(11) unsigned NOT NULL AUTO_INCREMENT,
    `user` varchar(50)      NOT NULL,
    `pass`  varchar(128)     NOT NULL,
    PRIMARY KEY (`id`)
);