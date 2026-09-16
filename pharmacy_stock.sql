-- 药房管理模块：为药品表 drug_info 追加库存列
-- 已导入过 his数据库.sql 的库，只需在本库执行下面这条即可，无需重新导入全量数据。
USE his01;

ALTER TABLE `drug_info` ADD COLUMN `drug_stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量';
