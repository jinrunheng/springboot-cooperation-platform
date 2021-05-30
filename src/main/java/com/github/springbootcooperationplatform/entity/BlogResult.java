package com.github.springbootcooperationplatform.entity;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BlogResult {
    private String msg;
    private String status;
    private Object data;
    private Integer total;
    private Integer page;
    private Integer totalPage;

    public static BlogResult blogResult(String status, String msg, Blog blog, User user) {
        return BlogResult.builder()
                .status(status)
                .msg(msg)
                .data(
                        Blog.builder()
                                .id(blog.getId())
                                .title(blog.getTitle())
                                .description(blog.getDescription())
                                .content(blog.getContent())
                                .user(
                                        User.builder()
                                                .id(user.getId())
                                                .username(user.getUsername())
                                                .avatar(user.getAvatar())
                                                .build()
                                )
                                .createdAt(blog.getCreatedAt())
                                .updatedAt(blog.getUpdatedAt())
                                .build()
                ).build();
    }

    public static BlogResult blogResult(String status, String msg, Blog blog) {
        return blogResult(status, msg, blog, blog.getUser());
    }
}
