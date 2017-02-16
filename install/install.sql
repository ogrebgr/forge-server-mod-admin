CREATE TABLE `admin_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_disabled` tinyint(3) NOT NULL DEFAULT '0',
  `is_super_admin` tinyint(3) NOT NULL DEFAULT '0',
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `admin_user_scram` (
  `user` int(11) NOT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `salt` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `server_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `stored_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `iterations` int(11) NOT NULL,
  `username_lc` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`user`),
  UNIQUE KEY `i_username` (`username`),
  UNIQUE KEY `i_username_lc` (`username_lc`),
  CONSTRAINT `fk_admin_user_scram_1` FOREIGN KEY (`user`) REFERENCES `admin_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

