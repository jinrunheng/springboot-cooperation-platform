package com.github.springbootcooperationplatform.entity;

import lombok.*;

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
}
