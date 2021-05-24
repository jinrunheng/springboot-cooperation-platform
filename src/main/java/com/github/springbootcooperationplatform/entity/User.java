package com.github.springbootcooperationplatform.entity;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    private Integer id;
    private String username;
    private String avatar;
    private Instant createdAt;
    private Instant updatedAt;
}
