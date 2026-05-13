package com.family.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.family.order.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    @Select("SELECT oi.*, d.name as dish_name, d.image_url as dish_image_url " +
            "FROM order_item oi " +
            "LEFT JOIN dish d ON oi.dish_id = d.id " +
            "WHERE oi.order_id = #{orderId}")
    List<OrderItem> findByOrderIdWithDish(@Param("orderId") Long orderId);
}