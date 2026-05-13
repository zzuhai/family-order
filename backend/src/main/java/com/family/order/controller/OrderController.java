package com.family.order.controller;

import com.family.order.dto.OrderDTO;
import com.family.order.service.OrderService;
import com.family.order.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/my")
    public Result<List<OrderDTO>> getMyOrders(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        List<OrderDTO> orders = orderService.getMyOrders(memberId);
        return Result.success(orders);
    }

    @GetMapping("/{id}")
    public Result<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        if (order == null) {
            return Result.error(404, "订单不存在");
        }
        return Result.success(order);
    }

    @GetMapping
    public Result<List<OrderDTO>> getOrdersByDate(@RequestParam String date) {
        LocalDate menuDate = LocalDate.parse(date);
        List<OrderDTO> orders = orderService.getOrdersByDate(menuDate);
        return Result.success(orders);
    }

    @PostMapping
    public Result<OrderDTO> createOrder(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        Long memberId = (Long) request.getAttribute("memberId");
        LocalDate date = LocalDate.parse((String) body.get("date"));

        // 支持dishIds参数（新方式，直接传菜品ID）
        List<?> rawIds = (List<?>) body.get("dishIds");
        if (rawIds == null) {
            // 兼容旧的menuIds参数
            rawIds = (List<?>) body.get("menuIds");
        }

        List<Long> dishIds = rawIds.stream().map(id -> ((Number) id).longValue()).collect(Collectors.toList());
        OrderDTO order = orderService.createOrder(memberId, date, dishIds);
        return Result.success(order);
    }
}