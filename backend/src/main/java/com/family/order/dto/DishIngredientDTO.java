package com.family.order.dto;

import lombok.Data;

@Data
public class DishIngredientDTO {
    private Long id;
    private Long dishId;
    private String name;
    private String amount;
    private String unit;
}