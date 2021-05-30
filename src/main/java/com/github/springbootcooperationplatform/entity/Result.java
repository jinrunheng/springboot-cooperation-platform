package com.github.springbootcooperationplatform.entity;

import com.alibaba.fastjson.JSON;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Result {
    private String msg;
    private String status;
    private Boolean isLogin;
    private Object data;

    // 用户未登陆
    public static Result notLoggedIn() {
        return Result.builder().status("fail").msg("登陆后才能操作").build();
    }

    // 系统异常
    public static Result systemError(){
        return Result.builder().status("fail").msg("系统异常").build();
    }
}
