package com.family.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class DishDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String category;
    private Integer status;
    private List<DishIngredientDTO> ingredients;
    private List<DishStepDTO> steps;
}