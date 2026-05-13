-- 修改图片字段为LONGTEXT类型以支持base64存储
-- base64图片数据可能很大，使用LONGTEXT类型（最大4GB）
USE family_order;

-- 修改dish表的image_url字段
ALTER TABLE dish MODIFY COLUMN image_url LONGTEXT COMMENT '菜品图片(base64)';

-- 修改dish_step表的image_url字段
ALTER TABLE dish_step MODIFY COLUMN image_url LONGTEXT COMMENT '步骤图片(base64)';

-- 清空现有的图片URL数据（因为要改为base64）
UPDATE dish SET image_url = NULL WHERE image_url IS NOT NULL;
UPDATE dish_step SET image_url = NULL WHERE image_url IS NOT NULL;

SELECT 'Database schema updated successfully!' as message;
