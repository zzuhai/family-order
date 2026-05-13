package com.family.order.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DailyMenuDTO {
    private Long id;
    private LocalDate menuDate;
    private Long dishId;
    private String dishName;
    private String dishDescription;
    private String dishImageUrl;
}