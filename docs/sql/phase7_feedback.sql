CREATE TABLE IF NOT EXISTS `feedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int DEFAULT NULL COMMENT '患者ID，可为空',
  `feedback_type` varchar(64) NOT NULL DEFAULT '其他' COMMENT '反馈类型',
  `content` varchar(1000) NOT NULL COMMENT '反馈内容',
  `contact` varchar(128) DEFAULT NULL COMMENT '联系方式',
  `status` varchar(32) NOT NULL DEFAULT '待处理' COMMENT '处理状态',
  `reply` varchar(1000) DEFAULT NULL COMMENT '管理员回复',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_feedback_patient` (`patient_id`),
  KEY `idx_feedback_status` (`status`),
  KEY `idx_feedback_type` (`feedback_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户反馈表';
