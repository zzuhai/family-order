package com.family.order.controller;

import com.family.order.dto.LoginRequest;
import com.family.order.dto.LoginResponse;
import com.family.order.entity.Member;
import com.family.order.service.AuthService;
import com.family.order.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request.getCode());
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public Result<Member> getProfile(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        Member member = authService.getProfile(memberId);
        return Result.success(member);
    }

    @PutMapping("/role")
    public Result<Void> updateRole(HttpServletRequest request, @RequestParam String role) {
        Long memberId = (Long) request.getAttribute("memberId");
        authService.updateRole(memberId, role);
        return Result.success();
    }
}