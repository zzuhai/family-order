package com.family.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderItemDTO {
    private Long id;
    private Long dishId;
    private String dishName;
    private String dishImageUrl;
    private String category;
    private List<DishIngredientDTO> ingredients;
    private List<DishStepSimpleDTO> steps; // 使用简化版步骤DTO（不含图片）
}