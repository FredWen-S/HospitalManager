-- Minimal demo data for the smart guidance appointment flow.
-- Usage:
--   1. Import hospital_manager.sql first.
--   2. Import HospitalManagerApi - idea/docs/sql/phase2_guidance.sql.
--   3. Import this file.
--
-- This script does not drop or alter existing tables.
-- It only inserts one demo doctor and rolling arrange rows for today + next 6 days.

-- Password is the legacy fixed-salt MD5 value for plain text password: 123456
INSERT IGNORE INTO `doctor` (
  `d_id`,
  `d_password`,
  `d_name`,
  `d_gender`,
  `d_phone`,
  `d_card`,
  `d_email`,
  `d_post`,
  `d_introduction`,
  `d_section`,
  `d_state`,
  `d_price`,
  `d_people`,
  `d_star`,
  `d_avg_star`
) VALUES (
  1100,
  '3d7dd7b26500bd0595573b651d0080fd',
  '李明',
  '男',
  '13800001100',
  '440111199001011100',
  'liming1100@example.com',
  '主任医师',
  '智能导诊演示医生，擅长咳嗽、发热、咽喉痛、胸闷等呼吸系统常见症状处理。',
  '呼吸与危重症医学科',
  1,
  15.00,
  0,
  0.00,
  NULL
);

-- The frontend date selector shows today and the following days.
-- Keep arrange rows rolling by computing dates from CURDATE().
INSERT IGNORE INTO `arrange` (`ar_id`, `ar_time`, `d_id`)
SELECT CONCAT('1100', DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 0 DAY), '%Y-%m-%d')),
       DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 0 DAY), '%Y-%m-%d'),
       1100;

INSERT IGNORE INTO `arrange` (`ar_id`, `ar_time`, `d_id`)
SELECT CONCAT('1100', DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '%Y-%m-%d')),
       DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '%Y-%m-%d'),
       1100;

INSERT IGNORE INTO `arrange` (`ar_id`, `ar_time`, `d_id`)
SELECT CONCAT('1100', DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 2 DAY), '%Y-%m-%d')),
       DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 2 DAY), '%Y-%m-%d'),
       1100;

INSERT IGNORE INTO `arrange` (`ar_id`, `ar_time`, `d_id`)
SELECT CONCAT('1100', DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 3 DAY), '%Y-%m-%d')),
       DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 3 DAY), '%Y-%m-%d'),
       1100;

INSERT IGNORE INTO `arrange` (`ar_id`, `ar_time`, `d_id`)
SELECT CONCAT('1100', DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 4 DAY), '%Y-%m-%d')),
       DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 4 DAY), '%Y-%m-%d'),
       1100;

INSERT IGNORE INTO `arrange` (`ar_id`, `ar_time`, `d_id`)
SELECT CONCAT('1100', DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 5 DAY), '%Y-%m-%d')),
       DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 5 DAY), '%Y-%m-%d'),
       1100;

INSERT IGNORE INTO `arrange` (`ar_id`, `ar_time`, `d_id`)
SELECT CONCAT('1100', DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 6 DAY), '%Y-%m-%d')),
       DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 6 DAY), '%Y-%m-%d'),
       1100;
