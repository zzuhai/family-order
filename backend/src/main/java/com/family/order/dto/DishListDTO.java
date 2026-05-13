package com.family.order.dto;

import lombok.Data;

/**
 * 菜品列表DTO（不包含图片，用于列表查询性能优化）
 */
@Data
public class DishListDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer status;
}
