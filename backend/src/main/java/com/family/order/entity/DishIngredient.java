package com.family.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dish_ingredient")
public class DishIngredient {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dishId;
    private String name;
    private String amount;
    private String unit;
}