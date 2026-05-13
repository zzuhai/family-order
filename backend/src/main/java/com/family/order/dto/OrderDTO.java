package com.family.order.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private String memberAvatar;
    private LocalDate menuDate;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}