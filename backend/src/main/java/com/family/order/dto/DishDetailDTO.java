package com.family.order.dto;

import lombok.Data;
import java.util.List;

/**
 * 菜品详情DTO（不包含图片，用于订单详情页性能优化）
 */
@Data
public class DishDetailDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String category;
    private Integer status;
    private List<DishIngredientDTO> ingredients;
    private List<DishStepSimpleDTO> steps;
}
