CREATE TABLE IF NOT EXISTS `guidance_rule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department` varchar(64) NOT NULL COMMENT '推荐科室',
  `disease_name` varchar(128) DEFAULT NULL COMMENT '疑似疾病',
  `keywords` varchar(512) NOT NULL COMMENT '关键词，逗号分隔',
  `synonyms` varchar(512) DEFAULT NULL COMMENT '同义词，逗号分隔',
  `advice` varchar(1000) DEFAULT NULL COMMENT '就诊建议',
  `location` varchar(255) DEFAULT NULL COMMENT '科室位置',
  `route_tip` varchar(500) DEFAULT NULL COMMENT '路线说明',
  `priority` int DEFAULT 0 COMMENT '优先级，数字越大越优先',
  `enabled` tinyint DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_guidance_rule_department` (`department`),
  KEY `idx_guidance_rule_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能导诊规则表';

CREATE TABLE IF NOT EXISTS `guidance_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int DEFAULT NULL COMMENT '患者ID，可为空',
  `symptom_text` varchar(1000) NOT NULL COMMENT '患者输入症状',
  `recommended_department` varchar(64) DEFAULT NULL COMMENT '推荐科室',
  `disease_name` varchar(128) DEFAULT NULL COMMENT '疑似疾病',
  `matched_keywords` varchar(512) DEFAULT NULL COMMENT '命中的关键词',
  `score` int DEFAULT 0 COMMENT '匹配分数',
  `advice` varchar(1000) DEFAULT NULL COMMENT '建议',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_guidance_record_patient` (`patient_id`),
  KEY `idx_guidance_record_department` (`recommended_department`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能导诊记录表';

INSERT INTO `guidance_rule` (`department`, `disease_name`, `keywords`, `synonyms`, `advice`, `location`, `route_tip`, `priority`, `enabled`) VALUES
('呼吸内科', '上呼吸道感染', '咳嗽,发烧,发热,胸闷,气短,喉咙痛', '低烧,高烧,嗓子疼,咽痛,咳痰', '建议前往呼吸内科就诊，完善血常规、胸片或肺功能等检查。若出现明显呼吸困难，请优先前往急诊。', '门诊楼 2 层 A 区', '从一楼大厅左侧扶梯上二楼，右转至 A 区。', 9, 1),
('消化内科', '胃炎/肠炎', '胃痛,腹痛,腹泻,恶心,呕吐', '反酸,烧心,拉肚子,肚子疼,胃胀', '建议前往消化内科就诊，注意清淡饮食并补充水分。如有黑便、持续剧痛或脱水表现，请及时急诊。', '门诊楼 2 层 B 区', '从一楼大厅扶梯上二楼，沿指示牌前往 B 区。', 9, 1),
('心内科', '心律失常/高血压', '心慌,胸痛,心悸,血压高', '胸闷,心跳快,高血压,心口疼', '建议前往心内科就诊，完善心电图、血压监测等检查。胸痛持续不缓解时请立即前往急诊。', '门诊楼 3 层 A 区', '乘坐门诊电梯至三楼，出电梯左转至 A 区。', 10, 1),
('神经内科', '头痛/眩晕', '头痛,头晕,失眠,手脚麻木', '偏头痛,眩晕,睡不着,麻木,肢体麻木', '建议前往神经内科就诊，必要时完善头颅影像、神经系统查体等检查。突发口角歪斜或肢体无力请立即急诊。', '门诊楼 3 层 B 区', '乘坐门诊电梯至三楼，沿走廊右侧前往 B 区。', 8, 1),
('皮肤科', '皮炎/湿疹', '皮疹,瘙痒,过敏,红斑,红疹', '发痒,起疹子,荨麻疹,皮肤痒', '建议前往皮肤科就诊，避免抓挠和自行混用药膏，携带近期用药或过敏史信息。', '门诊楼 2 层 C 区', '从二楼大厅向右侧走廊前行至 C 区。', 8, 1),
('骨科', '腰肌劳损/骨折', '腰痛,关节痛,扭伤,骨折', '腰疼,膝盖疼,崴脚,摔伤,手臂疼', '建议前往骨科就诊，近期外伤或活动受限者可先行 X 线检查评估。', '门诊楼 1 层 C 区', '从一楼大厅直行至收费处旁，左转进入 C 区。', 8, 1),
('眼科', '结膜炎/视疲劳', '眼痛,视力模糊,眼红', '眼睛疼,看不清,红眼,流泪', '建议前往眼科就诊，避免揉眼和自行使用含激素眼药水。视力骤降请尽快急诊。', '门诊楼 3 层 C 区', '乘坐电梯至三楼，按眼科标识前往 C 区。', 7, 1),
('耳鼻喉科', '鼻炎/咽喉炎', '鼻塞,流鼻涕,耳鸣,咽喉痛', '流涕,嗓子疼,咽痛,耳朵响,喉咙疼', '建议前往耳鼻喉科就诊，医生会结合鼻咽喉检查判断病因。', '门诊楼 3 层 D 区', '乘坐电梯至三楼，出电梯右转至 D 区。', 7, 1),
('口腔科', '牙髓炎/口腔溃疡', '牙痛,牙龈出血,口腔溃疡', '牙疼,牙龈肿,嘴里溃疡,口疮', '建议前往口腔科就诊，保持口腔清洁，避免冷热刺激。', '门诊楼 1 层 D 区', '从一楼大厅右侧通道前往 D 区。', 7, 1),
('妇科', '妇科炎症/月经不调', '月经不调,腹痛,白带异常', '痛经,经期紊乱,白带多,下腹痛', '建议前往妇科就诊，必要时完善妇科检查和超声检查。', '门诊楼 4 层 A 区', '乘坐电梯至四楼，左转至妇科候诊区。', 8, 1),
('儿科', '儿童呼吸道/消化道感染', '儿童发热,儿童咳嗽,儿童腹泻', '小孩发烧,孩子咳嗽,宝宝腹泻,儿童发烧', '建议前往儿科就诊，记录体温、精神状态和饮食情况，便于医生判断病情。', '门诊楼 1 层 E 区', '从一楼大厅沿儿童候诊区指示牌前往 E 区。', 9, 1),
('全科门诊', '症状待明确', '不舒服,乏力,难受,咨询', '不知道挂什么科,全身不适,没精神', '症状暂不明确，建议先到全科门诊进行初步评估，再由医生判断是否转专科。', '门诊楼 1 层 咨询台旁', '从一楼大厅进入后，前往导诊台右侧全科门诊候诊区。', 1, 1);
