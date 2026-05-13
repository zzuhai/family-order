package com.family.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.family.order.dto.DailyMenuDTO;
import com.family.order.entity.DailyMenu;
import com.family.order.entity.Dish;
import com.family.order.mapper.DailyMenuMapper;
import com.family.order.mapper.DishMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final DailyMenuMapper dailyMenuMapper;
    private final DishMapper dishMapper;

    public List<DailyMenuDTO> getTodayMenu() {
        return getMenuByDate(LocalDate.now());
    }

    public List<DailyMenuDTO> getMenuByDate(LocalDate date) {
        return dailyMenuMapper.findByDateWithDish(date);
    }

    @Transactional
    public void publishMenu(LocalDate date, List<Long> dishIds, Long createdBy) {
        LambdaQueryWrapper<DailyMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyMenu::getMenuDate, date);
        dailyMenuMapper.delete(wrapper);

        for (Long dishId : dishIds) {
            Dish dish = dishMapper.selectById(dishId);
            if (dish != null && dish.getDeleted() == 0) {
                DailyMenu menu = new DailyMenu();
                menu.setMenuDate(date);
                menu.setDishId(dishId);
                menu.setCreatedBy(createdBy);
                menu.setCreatedAt(LocalDateTime.now());
                dailyMenuMapper.insert(menu);
            }
        }
    }
}