package com.family.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dish_step")
public class DishStep {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dishId;
    private Integer stepNumber;
    private String description;
    private String imageUrl;
}