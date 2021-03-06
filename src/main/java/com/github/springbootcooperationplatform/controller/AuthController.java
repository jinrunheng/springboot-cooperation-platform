package com.github.springbootcooperationplatform.controller;


import com.alibaba.fastjson.JSON;
import com.github.springbootcooperationplatform.entity.Result;
import com.github.springbootcooperationplatform.entity.User;
import com.github.springbootcooperationplatform.service.UserService;
import com.github.springbootcooperationplatform.utils.AvatarHelper;
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

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AuthController {


    @Autowired
    private AvatarHelper avatarHelper;

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
                        .avatar(loggedUser.getAvatar())
                        .username(loggedUser.getUsername())
                        .updatedAt(loggedUser.getUpdatedAt())
                        .createdAt(loggedUser.getCreatedAt())
                        .build()).build());
    }

    @PostMapping(path = "/auth/register")
    @ResponseBody
    public String register(@RequestBody Map<String, String> usernameAndPassword, HttpServletRequest request) {
        // error msg:
        // 1. ??????????????????????????????
        // 2. ??????????????????
        // 3. ??????????????????1~15????????????????????????????????????????????????????????????????????????
        // 4. ??????????????? 6~16???????????????
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null || username.length() == 0 || password.length() == 0) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("?????????????????????????????????").build());
        }
        if (!Objects.isNull(userService.getUserByName(username))) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("??????????????????").build());
        }
        Pattern pt = Pattern.compile("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
        Matcher mt = pt.matcher(username);
        if (!mt.matches() || username.length() > 15) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("????????????????????????????????????????????????????????????????????????????????????").build());
        }
        if (password.length() > 16 || password.length() < 6) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("????????????????????? 6 ~ 16 ???").build());
        }
        userService.save(username, password);
        User savedUser = userService.getUserByName(username);
        login(usernameAndPassword, request);
        return JSON.toJSONString(Result.builder()
                .status("ok")
                .msg("????????????")
                .data(User.builder()
                        .id(savedUser.getId())
                        .username(savedUser.getUsername())
                        .avatar(savedUser.getAvatar())
                        .updatedAt(savedUser.getUpdatedAt())
                        .createdAt(savedUser.getCreatedAt())
                        .build()).build());
    }

    @PostMapping(path = "/auth/login")
    @ResponseBody
    public String login(@RequestBody Map<String, String> usernameAndPassword, HttpServletRequest request) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("???????????????")
                    .build()
            );
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            User user = userService.getUserByName(username);
            return JSON.toJSONString(Result.builder().status("ok")
                    .msg("????????????")
                    .data(user).build());
        } catch (BadCredentialsException e) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("???????????????")
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
                    .msg("????????????")
                    .build()
            );

        } else {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("??????????????????")
                    .build()
            );
        }
    }
}
