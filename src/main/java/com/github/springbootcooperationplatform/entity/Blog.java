package com.github.springbootcooperationplatform.entity;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Blog {
    private Integer id;
    private User user;
    private String title;
    private String description;
    private String content;
    private Instant updatedAt;
    private Instant createdAt;

    public Integer getUserId() {
        return user == null ? null : user.getId();
    }
}
