package com.family.order.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.family.order.config.WechatConfig;
import com.family.order.dto.LoginResponse;
import com.family.order.entity.Member;
import com.family.order.mapper.MemberMapper;
import com.family.order.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WechatConfig wechatConfig;
    private final MemberMapper memberMapper;
    private final JwtUtil jwtUtil;

    public LoginResponse login(String code) {
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wechatConfig.getAppid(), wechatConfig.getSecret(), code
        );

        String response = HttpUtil.get(url);
        JSONObject json = JSONUtil.parseObj(response);

        if (json.containsKey("errcode") && json.getInt("errcode") != 0) {
            throw new RuntimeException("微信登录失败: " + json.getStr("errmsg"));
        }

        String openid = json.getStr("openid");

        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getOpenid, openid);
        Member member = memberMapper.selectOne(wrapper);

        if (member == null) {
            member = new Member();
            member.setOpenid(openid);
            member.setNickname("家庭成员");
            member.setRole("user");
            member.setCreatedAt(LocalDateTime.now());
            member.setDeleted(0);
            memberMapper.insert(member);
        }

        String token = jwtUtil.generateToken(member.getId());

        LoginResponse result = new LoginResponse();
        result.setToken(token);
        result.setMemberId(member.getId());
        result.setNickname(member.getNickname());
        result.setAvatar(member.getAvatar());
        result.setRole(member.getRole());
        return result;
    }

    public Member getProfile(Long memberId) {
        return memberMapper.selectById(memberId);
    }

    public void updateRole(Long memberId, String role) {
        Member member = memberMapper.selectById(memberId);
        if (member != null) {
            member.setRole(role);
            memberMapper.updateById(member);
        }
    }
}