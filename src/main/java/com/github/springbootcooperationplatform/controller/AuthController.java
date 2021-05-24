package com.github.springbootcooperationplatform.controller;

import com.alibaba.fastjson.JSON;
import com.github.springbootcooperationplatform.entity.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AuthController {

    @PostMapping(path = "/auth/login")
    @ResponseBody
    public String login(@RequestBody Map<String,String> usernameAndPassword){
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        System.out.println("username : " + username);
        System.out.println("password : " + password);
        // 查询不到该用户，返回格式
        // {"status": "fail", "msg": "用户不存在"}
        // 用户密码不正确，返回格式
        // {"status": "fail", "msg": "用户不存在"}
        return JSON.toJSONString(Result.builder().msg("用户不存在").status("fail").build());
    }
}
