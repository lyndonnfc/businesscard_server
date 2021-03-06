CREATE TABLE `nfc_business_card_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) DEFAULT '0' COMMENT '名片所属人的uid',
  `real_name` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `english_real_name` varchar(100) DEFAULT NULL COMMENT '英文姓名',
  `phone` varchar(100) DEFAULT NULL COMMENT '手机号',
  `mobile` varchar(100) DEFAULT NULL COMMENT '座机',
  `position` varchar(100) DEFAULT NULL COMMENT '职位',
  `english_postion` varchar(100) DEFAULT NULL COMMENT '英文职位',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `english_department` varchar(100) DEFAULT NULL COMMENT '英文部门',
  `company_name` varchar(255) DEFAULT NULL COMMENT '公司信息',
  `english_company_name` varchar(255) DEFAULT NULL COMMENT '英文公司信息',
  `address` text COMMENT '地址',
  `english_address` text COMMENT '英文地址',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `postcode` varchar(100) DEFAULT NULL COMMENT '邮编信息',
  `web_url` varchar(255) DEFAULT NULL COMMENT '网址信息',
  `fax` varchar(255) DEFAULT NULL COMMENT '传真',
  `qq_account` varchar(100) DEFAULT NULL COMMENT '邮箱账号',
  `wechat_account` varchar(100) DEFAULT NULL COMMENT '微信账号',
  `msn_account` varchar(255) DEFAULT NULL COMMENT 'MSN账号',
  `weibo_account` varchar(255) DEFAULT NULL COMMENT '微博账号',
  `company_account` varchar(255) DEFAULT NULL COMMENT '公司账号',
  `logo` text COMMENT '名片logo',
  `card_url` text COMMENT '名片URL',
  `other_msg` text COMMENT '其他信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='名片基础信息表';


CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(100) NOT NULL DEFAULT '' COMMENT '手机号',
  `vefify_code` varchar(100) NOT NULL DEFAULT '' COMMENT '验证码',
  `expiry_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';