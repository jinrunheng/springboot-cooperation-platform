package com.github.springbootcooperationplatform.controller;


import com.alibaba.fastjson.JSON;
import com.github.springbootcooperationplatform.entity.Result;
import com.github.springbootcooperationplatform.entity.User;
import com.github.springbootcooperationplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(path = "/auth")
    @ResponseBody
    public String auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return JSON.toJSONString(Result.builder().status("ok").isLogin(false).build());
        }
        String username = authentication.getName();
        User loggedUser = userService.getUserByName(username);
        if (Objects.isNull(loggedUser)) {
            return JSON.toJSONString(Result.builder().status("ok").isLogin(false).build());
        }
        return JSON.toJSONString(Result.builder()
                .status("ok")
                .isLogin(true)
                .data(User.builder()
                        .id(loggedUser.getId())
                        .avatar("_")
                        .username(loggedUser.getUsername())
                        .updatedAt(loggedUser.getUpdatedAt())
                        .createdAt(loggedUser.getCreatedAt())
                        .build()).build());
    }

    @PostMapping(path = "/auth/register")
    @ResponseBody
    public String register(@RequestBody Map<String, String> usernameAndPassword) {
        // error msg:
        // 1. 用户名或密码不能为空
        // 2. 该用户已注册
        // 3. 用户名长度为1~15个字符，只能是字母数字下划线中文，下划线位置不限
        // 4. 密码长度为 6~16个任意字符
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null || username.length() == 0 || password.length() == 0) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("用户名或密码不能为空！").build());
        }
        if (!Objects.isNull(userService.getUserByName(username))) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("该用户已注册").build());
        }
        Pattern pt = Pattern.compile("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
        Matcher mt = pt.matcher(username);
        if (!mt.matches() || username.length() > 15) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("用户名请以字母数字下划线或中文命名，且长度要求小于十六位").build());
        }
        if (password.length() > 16 || password.length() < 6) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("密码长度要求为 6 ~ 16 位").build());
        }
        userService.save(username, password);
        User savedUser = userService.getUserByName(username);
        return JSON.toJSONString(Result.builder()
                .status("ok")
                .msg("注册成功")
                .data(User.builder()
                        .id(savedUser.getId())
                        .username(savedUser.getUsername())
                        .avatar("_")
                        .updatedAt(savedUser.getUpdatedAt())
                        .createdAt(savedUser.getCreatedAt())
                        .build()).build());
    }

    @PostMapping(path = "/auth/login")
    @ResponseBody
    public String login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("用户不存在")
                    .build()
            );
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return JSON.toJSONString(Result.builder().status("ok")
                    .msg("登录成功")
                    .data(User.builder()
                            .id(1)
                            .username(username)
                            .avatar("_")
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build()
                    ).build());
        } catch (BadCredentialsException e) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("密码不正确")
                    .build()
            );
        }
    }

    @GetMapping(path = "/auth/logout")
    @ResponseBody
    public String logout() {
        String authJson = auth();
        Result result = JSON.parseObject(authJson, Result.class);

        if (result.getIsLogin()) {
            SecurityContextHolder.clearContext();
            return JSON.toJSONString(Result.builder()
                    .status("ok")
                    .msg("注销成功")
                    .build()
            );

        } else {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("用户尚未登录")
                    .build()
            );
        }
    }
}
