package com.family.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.family.order.dto.DailyMenuDTO;
import com.family.order.entity.DailyMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailyMenuMapper extends BaseMapper<DailyMenu> {

    @Select("SELECT dm.id, dm.menu_date as menuDate, dm.dish_id as dishId, " +
            "d.name as dishName, d.description as dishDescription, d.image_url as dishImageUrl " +
            "FROM daily_menu dm " +
            "LEFT JOIN dish d ON dm.dish_id = d.id " +
            "WHERE dm.menu_date = #{date} AND d.deleted = 0")
    List<DailyMenuDTO> findByDateWithDish(@Param("date") LocalDate date);
}