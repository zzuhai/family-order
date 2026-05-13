package com.family.order.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long memberId;
    private String nickname;
    private String avatar;
    private String role;
}