package com.family.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("daily_menu")
public class DailyMenu {
    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDate menuDate;
    private Long dishId;
    private Long createdBy;
    private LocalDateTime createdAt;
}