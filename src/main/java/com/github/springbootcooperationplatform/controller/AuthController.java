package com.github.springbootcooperationplatform.controller;

import com.alibaba.fastjson.JSON;
import com.github.springbootcooperationplatform.entity.Result;
import com.github.springbootcooperationplatform.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.Map;

@Controller
public class AuthController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping(path = "/auth")
    @ResponseBody
    public String auth() {
        return JSON.toJSONString(Result.builder().status("ok").isLogin(false).build());
    }

    @PostMapping(path = "/auth/login")
    @ResponseBody
    public String login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return JSON.toJSONString(Result.builder()
                    .status("ok")
                    .msg("登陆成功")
                    .data(User.builder().id(1).username(username).avatar("").createdAt(Instant.now()).updatedAt(Instant.now()).build())
                    .build());
        } catch (BadCredentialsException e) {
            return JSON.toJSONString(Result.builder().msg("密码不正确").status("fail").build());
        }
    }
}
