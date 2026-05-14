package com.family.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.family.order.dto.DishDTO;
import com.family.order.dto.DishIngredientDTO;
import com.family.order.dto.DishListDTO;
import com.family.order.dto.DishStepDTO;
import com.family.order.entity.Dish;
import com.family.order.entity.DishIngredient;
import com.family.order.entity.DishStep;
import com.family.order.mapper.DishIngredientMapper;
import com.family.order.mapper.DishMapper;
import com.family.order.mapper.DishStepMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishMapper dishMapper;
    private final DishIngredientMapper ingredientMapper;
    private final DishStepMapper stepMapper;

    public List<DishDTO> getAllDishes() {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getDeleted, 0).orderByDesc(Dish::getCreatedAt);
        List<Dish> dishes = dishMapper.selectList(wrapper);

        return dishes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 获取菜品列表（不包含图片和详细信息，用于性能优化）
     */
    public List<DishListDTO> getDishList() {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getDeleted, 0)
               .eq(Dish::getStatus, 1) // 只返回上架的菜品
               .orderByDesc(Dish::getCreatedAt);
        List<Dish> dishes = dishMapper.selectList(wrapper);

        return dishes.stream().map(dish -> {
            DishListDTO dto = new DishListDTO();
            dto.setId(dish.getId());
            dto.setName(dish.getName());
            dto.setDescription(dish.getDescription());
            dto.setCategory(dish.getCategory());
            dto.setStatus(dish.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    public DishDTO getDishById(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null || dish.getDeleted() == 1) {
            return null;
        }
        return convertToDTO(dish);
    }

    /**
     * 获取菜品详情（不包含图片，用于订单详情页性能优化）
     */
    public com.family.order.dto.DishDetailDTO getDishDetailWithoutImages(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null || dish.getDeleted() == 1) {
            return null;
        }
        return convertToDetailDTOWithoutImages(dish);
    }

    @Transactional
    public DishDTO createDish(DishDTO dto) {
        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setImageUrl(dto.getImageUrl());
        dish.setCategory(dto.getCategory());
        dish.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        dish.setCreatedAt(LocalDateTime.now());
        dish.setUpdatedAt(LocalDateTime.now());
        dish.setDeleted(0);
        dishMapper.insert(dish);

        Long dishId = dish.getId();

        if (dto.getIngredients() != null) {
            for (DishIngredientDTO ingredientDTO : dto.getIngredients()) {
                DishIngredient ingredient = new DishIngredient();
                ingredient.setDishId(dishId);
                ingredient.setName(ingredientDTO.getName());
                ingredient.setAmount(ingredientDTO.getAmount());
                ingredient.setUnit(ingredientDTO.getUnit());
                ingredientMapper.insert(ingredient);
            }
        }

        if (dto.getSteps() != null) {
            for (DishStepDTO stepDTO : dto.getSteps()) {
                DishStep step = new DishStep();
                step.setDishId(dishId);
                step.setStepNumber(stepDTO.getStepNumber());
                step.setDescription(stepDTO.getDescription());
                step.setImageUrl(stepDTO.getImageUrl());
                stepMapper.insert(step);
            }
        }

        return convertToDTO(dish);
    }

    @Transactional
    public DishDTO updateDish(Long id, DishDTO dto) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null || dish.getDeleted() == 1) {
            return null;
        }

        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setImageUrl(dto.getImageUrl());
        dish.setCategory(dto.getCategory());
        dish.setStatus(dto.getStatus());
        dish.setUpdatedAt(LocalDateTime.now());
        dishMapper.updateById(dish);

        LambdaQueryWrapper<DishIngredient> ingredientWrapper = new LambdaQueryWrapper<>();
        ingredientWrapper.eq(DishIngredient::getDishId, id);
        ingredientMapper.delete(ingredientWrapper);

        LambdaQueryWrapper<DishStep> stepWrapper = new LambdaQueryWrapper<>();
        stepWrapper.eq(DishStep::getDishId, id);
        stepMapper.delete(stepWrapper);

        if (dto.getIngredients() != null) {
            for (DishIngredientDTO ingredientDTO : dto.getIngredients()) {
                DishIngredient ingredient = new DishIngredient();
                ingredient.setDishId(id);
                ingredient.setName(ingredientDTO.getName());
                ingredient.setAmount(ingredientDTO.getAmount());
                ingredient.setUnit(ingredientDTO.getUnit());
                ingredientMapper.insert(ingredient);
            }
        }

        if (dto.getSteps() != null) {
            for (DishStepDTO stepDTO : dto.getSteps()) {
                DishStep step = new DishStep();
                step.setDishId(id);
                step.setStepNumber(stepDTO.getStepNumber());
                step.setDescription(stepDTO.getDescription());
                step.setImageUrl(stepDTO.getImageUrl());
                stepMapper.insert(step);
            }
        }

        return convertToDTO(dish);
    }

    @Transactional
    public boolean deleteDish(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null) {
            return false;
        }

        dish.setDeleted(1);
        dish.setUpdatedAt(LocalDateTime.now());
        dishMapper.updateById(dish);
        return true;
    }

    @Transactional
    public void batchDeleteDishes(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.selectById(id);
            if (dish != null) {
                dish.setDeleted(1);
                dish.setUpdatedAt(LocalDateTime.now());
                dishMapper.updateById(dish);
            }
        }
    }

    private DishDTO convertToDTO(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setImageUrl(dish.getImageUrl());
        dto.setCategory(dish.getCategory());
        dto.setStatus(dish.getStatus());

        LambdaQueryWrapper<DishIngredient> ingredientWrapper = new LambdaQueryWrapper<>();
        ingredientWrapper.eq(DishIngredient::getDishId, dish.getId());
        List<DishIngredient> ingredients = ingredientMapper.selectList(ingredientWrapper);
        dto.setIngredients(ingredients.stream().map(i -> {
            DishIngredientDTO iDTO = new DishIngredientDTO();
            iDTO.setId(i.getId());
            iDTO.setDishId(i.getDishId());
            iDTO.setName(i.getName());
            iDTO.setAmount(i.getAmount());
            iDTO.setUnit(i.getUnit());
            return iDTO;
        }).collect(Collectors.toList()));

        LambdaQueryWrapper<DishStep> stepWrapper = new LambdaQueryWrapper<>();
        stepWrapper.eq(DishStep::getDishId, dish.getId()).orderByAsc(DishStep::getStepNumber);
        List<DishStep> steps = stepMapper.selectList(stepWrapper);
        dto.setSteps(steps.stream().map(s -> {
            DishStepDTO sDTO = new DishStepDTO();
            sDTO.setId(s.getId());
            sDTO.setDishId(s.getDishId());
            sDTO.setStepNumber(s.getStepNumber());
            sDTO.setDescription(s.getDescription());
            sDTO.setImageUrl(s.getImageUrl());
            return sDTO;
        }).collect(Collectors.toList()));

        return dto;
    }

    private com.family.order.dto.DishDetailDTO convertToDetailDTOWithoutImages(Dish dish) {
        com.family.order.dto.DishDetailDTO dto = new com.family.order.dto.DishDetailDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setImageUrl(dish.getImageUrl());
        dto.setCategory(dish.getCategory());
        dto.setStatus(dish.getStatus());

        // 加载材料（材料本身不包含图片）
        LambdaQueryWrapper<DishIngredient> ingredientWrapper = new LambdaQueryWrapper<>();
        ingredientWrapper.eq(DishIngredient::getDishId, dish.getId());
        List<DishIngredient> ingredients = ingredientMapper.selectList(ingredientWrapper);
        dto.setIngredients(ingredients.stream().map(i -> {
            DishIngredientDTO iDTO = new DishIngredientDTO();
            iDTO.setId(i.getId());
            iDTO.setDishId(i.getDishId());
            iDTO.setName(i.getName());
            iDTO.setAmount(i.getAmount());
            iDTO.setUnit(i.getUnit());
            return iDTO;
        }).collect(Collectors.toList()));

        // 加载步骤（包含步骤图片）
        LambdaQueryWrapper<DishStep> stepWrapper = new LambdaQueryWrapper<>();
        stepWrapper.eq(DishStep::getDishId, dish.getId()).orderByAsc(DishStep::getStepNumber);
        List<DishStep> steps = stepMapper.selectList(stepWrapper);
        dto.setSteps(steps.stream().map(s -> {
            com.family.order.dto.DishStepSimpleDTO sDTO = new com.family.order.dto.DishStepSimpleDTO();
            sDTO.setId(s.getId());
            sDTO.setDishId(s.getDishId());
            sDTO.setStepNumber(s.getStepNumber());
            sDTO.setDescription(s.getDescription());
            sDTO.setImageUrl(s.getImageUrl());
            return sDTO;
        }).collect(Collectors.toList()));

        return dto;
    }
}