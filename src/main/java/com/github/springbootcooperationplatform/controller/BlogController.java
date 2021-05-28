package com.github.springbootcooperationplatform.controller;

import com.alibaba.fastjson.JSON;
import com.github.springbootcooperationplatform.entity.Blog;
import com.github.springbootcooperationplatform.entity.Result;
import com.github.springbootcooperationplatform.entity.User;
import com.github.springbootcooperationplatform.service.BlogService;
import com.github.springbootcooperationplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
public class BlogController {


    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @PostMapping(path = "/blog")
    public String createBlog(@RequestBody Map<String, String> map) {
        String title = map.get("title");
        String content = map.get("content");
        String description = map.get("description");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().toString().contains("anonymous")) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("登录后才能操作").build());
        }
        String username = authentication.getName();
        User loggedUser = userService.getUserByName(username);

        Blog blog = Blog.builder()
                .user(loggedUser)
                .title(title)
                .description(description)
                .content(content)
                .user(loggedUser)
                .build();


        blogService.insertBlog(blog);

        return JSON.toJSONString(Result.builder()
                .status("ok")
                .msg("创建成功")
                .data(
                        Blog.builder()
                                .id(blog.getId())
                                .title(blog.getTitle())
                                .description(blog.getDescription())
                                .content(blog.getContent())
                                .user(
                                        User.builder()
                                                .id(blog.getUser().getId())
                                                .username(blog.getUser().getUsername())
                                                .avatar(blog.getUser().getAvatar())
                                                .build()
                                )
                                .createdAt(blog.getCreatedAt())
                                .updatedAt(blog.getUpdatedAt())
                                .build()
                ).build());
    }

    @GetMapping(path = "/blog/{id}")
    public String getBlogById(@PathVariable int id) {
        Blog blog = blogService.getBlogById(id);
        if (Objects.isNull(blog)) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("系统异常")
                    .build());
        }

        int userId = blog.getUserId();
        User loggedUser = userService.getUserById(userId);

        return JSON.toJSONString(Result.builder()
                .status("ok")
                .msg("获取成功")
                .data(
                        Blog.builder()
                                .id(blog.getId())
                                .title(blog.getTitle())
                                .description(blog.getDescription())
                                .content(blog.getContent())
                                .user(User.builder()
                                        .id(loggedUser.getId())
                                        .username(loggedUser.getUsername())
                                        .avatar(loggedUser.getAvatar())
                                        .build())
                                .createdAt(blog.getCreatedAt())
                                .updatedAt(blog.getUpdatedAt())
                                .build()
                ).build());
    }
}
