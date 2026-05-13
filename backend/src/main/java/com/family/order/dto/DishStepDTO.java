package com.family.order.dto;

import lombok.Data;

@Data
public class DishStepDTO {
    private Long id;
    private Long dishId;
    private Integer stepNumber;
    private String description;
    private String imageUrl;
}