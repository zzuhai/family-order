package com.family.order.dto;

import lombok.Data;

/**
 * 简化的菜品步骤DTO（不包含图片）
 */
@Data
public class DishStepSimpleDTO {
    private Long id;
    private Long dishId;
    private Integer stepNumber;
    private String description;
    private String imageUrl;
}
