-- 收费管理模块 - 收费记录表
-- 数据库：his01（与其它模块共用）
-- 执行方式：mysql -h127.0.0.1 -uroot -p123456 his01 < charge_record.sql

CREATE TABLE IF NOT EXISTS charge_record (
  id INT AUTO_INCREMENT PRIMARY KEY COMMENT '收费单号',
  register_id INT NOT NULL COMMENT '挂号id',
  case_number VARCHAR(50) DEFAULT NULL COMMENT '病历号',
  real_name VARCHAR(64) DEFAULT NULL COMMENT '姓名',
  regist_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '挂号费',
  drug_total_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '药品总费用',
  check_total_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '检验项目总费用',
  inspection_total_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '检查项目总费用',
  disposal_total_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '处置治疗总费用',
  total_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '合计总金额',
  charge_state VARCHAR(64) DEFAULT '已收费' COMMENT '状态(已收费/已退费)',
  charge_time DATETIME DEFAULT NULL COMMENT '收费时间',
  refund_time DATETIME DEFAULT NULL COMMENT '退费时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费记录';
