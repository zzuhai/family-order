package com.family.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.family.order.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}