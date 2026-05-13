package com.family.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.family.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("SELECT o.*, m.nickname as member_name, m.avatar as member_avatar " +
            "FROM `order` o " +
            "LEFT JOIN member m ON o.member_id = m.id " +
            "WHERE o.member_id = #{memberId} " +
            "ORDER BY o.menu_date DESC")
    List<Order> findByMemberId(@Param("memberId") Long memberId);

    @Select("SELECT o.*, m.nickname as member_name, m.avatar as member_avatar " +
            "FROM `order` o " +
            "LEFT JOIN member m ON o.member_id = m.id " +
            "WHERE o.menu_date = #{date} " +
            "ORDER BY o.created_at ASC")
    List<Order> findByDate(@Param("date") LocalDate date);
}