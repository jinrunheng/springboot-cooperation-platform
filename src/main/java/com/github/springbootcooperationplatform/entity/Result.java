package com.github.springbootcooperationplatform.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result {
    private String msg;
    private String status;
    private Object data;
}
