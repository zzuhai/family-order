package com.family.order.controller;

import com.family.order.dto.DailyMenuDTO;
import com.family.order.service.MenuService;
import com.family.order.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/today")
    public Result<List<DailyMenuDTO>> getTodayMenu() {
        List<DailyMenuDTO> menus = menuService.getTodayMenu();
        return Result.success(menus);
    }

    @GetMapping
    public Result<List<DailyMenuDTO>> getMenuByDate(@RequestParam String date) {
        LocalDate menuDate = LocalDate.parse(date);
        List<DailyMenuDTO> menus = menuService.getMenuByDate(menuDate);
        return Result.success(menus);
    }

    @PostMapping
    public Result<Void> publishMenu(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        Long memberId = (Long) request.getAttribute("memberId");
        LocalDate date = LocalDate.parse((String) body.get("date"));

        // 处理 dishIds 类型转换问题，将可能的 Integer 类型转换为 Long 类型
        List<?> rawDishIds = (List<?>) body.get("dishIds");
        List<Long> dishIds = rawDishIds.stream()
            .map(id -> id instanceof Integer ? ((Integer) id).longValue() : (Long) id)
            .collect(Collectors.toList());

        menuService.publishMenu(date, dishIds, memberId);
        return Result.success();
    }
}