package com.family.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.family.order.dto.DishDTO;
import com.family.order.dto.OrderDTO;
import com.family.order.dto.OrderItemDTO;
import com.family.order.entity.DailyMenu;
import com.family.order.entity.Order;
import com.family.order.entity.OrderItem;
import com.family.order.mapper.DailyMenuMapper;
import com.family.order.mapper.OrderItemMapper;
import com.family.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final DailyMenuMapper dailyMenuMapper;
    private final DishService dishService;

    public List<OrderDTO> getMyOrders(Long memberId) {
        List<Order> orders = orderMapper.findByMemberId(memberId);
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return null;
        }
        return convertToDTOWithDetails(order);
    }

    public List<OrderDTO> getOrdersByDate(LocalDate date) {
        List<Order> orders = orderMapper.findByDate(date);
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO createOrder(Long memberId, LocalDate date, List<Long> dishIds) {
        LambdaQueryWrapper<Order> existWrapper = new LambdaQueryWrapper<>();
        existWrapper.eq(Order::getMemberId, memberId).eq(Order::getMenuDate, date);
        Order existOrder = orderMapper.selectOne(existWrapper);

        if (existOrder != null) {
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItem::getOrderId, existOrder.getId());
            orderItemMapper.delete(itemWrapper);

            for (Long dishId : dishIds) {
                OrderItem item = new OrderItem();
                item.setOrderId(existOrder.getId());
                item.setDishId(dishId);
                item.setDailyMenuId(null); // 不再使用每日菜单
                orderItemMapper.insert(item);
            }

            existOrder.setUpdatedAt(LocalDateTime.now());
            orderMapper.updateById(existOrder);
            return convertToDTO(existOrder);
        }

        Order order = new Order();
        order.setMemberId(memberId);
        order.setMenuDate(date);
        order.setStatus("submitted");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        Long orderId = order.getId();
        for (Long dishId : dishIds) {
            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setDishId(dishId);
            item.setDailyMenuId(null); // 不再使用每日菜单
            orderItemMapper.insert(item);
        }

        return convertToDTO(order);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setMemberId(order.getMemberId());
        dto.setMenuDate(order.getMenuDate());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        List<OrderItem> items = orderItemMapper.findByOrderIdWithDish(order.getId());
        dto.setItems(items.stream().map(i -> {
            OrderItemDTO iDTO = new OrderItemDTO();
            iDTO.setId(i.getId());
            iDTO.setDishId(i.getDishId());
            iDTO.setDishName(i.getDishName());
            iDTO.setDishImageUrl(i.getDishImageUrl());
            return iDTO;
        }).collect(Collectors.toList()));

        return dto;
    }

    private OrderDTO convertToDTOWithDetails(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setMemberId(order.getMemberId());
        dto.setMenuDate(order.getMenuDate());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        List<OrderItem> items = orderItemMapper.findByOrderIdWithDish(order.getId());
        dto.setItems(items.stream().map(i -> {
            OrderItemDTO iDTO = new OrderItemDTO();
            iDTO.setId(i.getId());
            iDTO.setDishId(i.getDishId());
            iDTO.setDishName(i.getDishName());

            // 获取菜品的详细信息（包含图片）
            com.family.order.dto.DishDetailDTO dish = dishService.getDishDetailWithoutImages(i.getDishId());
            if (dish != null) {
                iDTO.setDishImageUrl(dish.getImageUrl());
                iDTO.setCategory(dish.getCategory());
                iDTO.setIngredients(dish.getIngredients());
                iDTO.setSteps(dish.getSteps());
            }

            return iDTO;
        }).collect(Collectors.toList()));

        return dto;
    }
}