package com.family.order.controller;

import com.family.order.dto.DishDTO;
import com.family.order.dto.DishListDTO;
import com.family.order.service.DishService;
import com.family.order.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public Result<List<DishDTO>> getAllDishes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status) {
        List<DishDTO> dishes = dishService.getAllDishes(name, category, status);
        return Result.success(dishes);
    }

    /**
     * 获取菜品列表（不包含图片，用于小程序性能优化）
     */
    @GetMapping("/list")
    public Result<List<DishListDTO>> getDishList() {
        List<DishListDTO> dishes = dishService.getDishList();
        return Result.success(dishes);
    }

    @GetMapping("/{id}")
    public Result<DishDTO> getDishById(@PathVariable Long id) {
        DishDTO dish = dishService.getDishById(id);
        if (dish == null) {
            return Result.error(404, "菜品不存在");
        }
        return Result.success(dish);
    }

    @PostMapping
    public Result<DishDTO> createDish(@RequestBody DishDTO dto) {
        DishDTO dish = dishService.createDish(dto);
        return Result.success(dish);
    }

    @PutMapping("/{id}")
    public Result<DishDTO> updateDish(@PathVariable Long id, @RequestBody DishDTO dto) {
        DishDTO dish = dishService.updateDish(id, dto);
        if (dish == null) {
            return Result.error(404, "菜品不存在");
        }
        return Result.success(dish);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDish(@PathVariable Long id) {
        boolean success = dishService.deleteDish(id);
        if (!success) {
            return Result.error(404, "菜品不存在");
        }
        return Result.success();
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteDishes(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error(400, "请选择要删除的菜品");
        }
        dishService.batchDeleteDishes(ids);
        return Result.success();
    }
}