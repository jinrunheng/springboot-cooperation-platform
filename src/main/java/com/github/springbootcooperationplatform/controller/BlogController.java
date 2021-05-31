package com.github.springbootcooperationplatform.controller;

import com.alibaba.fastjson.JSON;
import com.github.springbootcooperationplatform.entity.Blog;
import com.github.springbootcooperationplatform.entity.BlogResult;
import com.github.springbootcooperationplatform.entity.Result;
import com.github.springbootcooperationplatform.entity.User;
import com.github.springbootcooperationplatform.service.AuthService;
import com.github.springbootcooperationplatform.service.BlogService;
import com.github.springbootcooperationplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class BlogController {


    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @GetMapping(path = "/blog")
    public String getBlogs(@RequestParam("page") Integer page, @RequestParam(value = "userId", required = false) Integer userId) {
        if (page == null || page < 0) {
            page = 1;
        }
        // 默认每一页有 10 个列表，即：pageSize = 10
        List<Blog> blogs = blogService.getBlogs(page,10,userId);
        int count = blogService.count(userId);
        int pageCount = count % 10 == 0 ? count / 10 : count / 10 + 1;
        return JSON.toJSONString(
                BlogResult.builder()
                        .status("ok")
                        .msg("获取成功")
                        .total(count)
                        .page(page)
                        .totalPage(pageCount)
                        .data(blogs)
                        .build()
        );
    }

    @PostMapping(path = "/blog")
    public String createBlog(@RequestBody Map<String, String> map) {
        String title = map.get("title");
        String content = map.get("content");
        String description = map.get("description");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().toString().contains("anonymous")) {
            return JSON.toJSONString(Result.notLoggedIn());
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

        return JSON.toJSONString(BlogResult.blogResult("ok", "创建成功", blog));
    }

    @GetMapping(path = "/blog/{id}")
    public String getBlogById(@PathVariable int id) {
        Blog blog = blogService.getBlogById(id);
        if (Objects.isNull(blog)) {
            return JSON.toJSONString(Result.systemError());
        }

        int userId = blog.getUserId();
        User user = userService.getUserById(userId);

        return JSON.toJSONString(BlogResult.blogResult("ok", "获取成功", blog, user));
    }

    @PatchMapping(path = "/blog/{id}")
    public String updateBlog(@PathVariable int id, @RequestBody Map<String, Object> map) {
        // 判断用户是否登陆
        if (!authService.isLoggedIn()) {
            return JSON.toJSONString(Result.notLoggedIn());
        }

        // 如果用户登陆，判断blogId是否存在
        Blog blog = blogService.getBlogById(id);
        if (Objects.isNull(blog)) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("博客不存在").build());
        }
        // 判断当前登陆用户和博客的作者是否为同一人
        if (!Objects.equals(userService.getUserById(blog.getUser().getId()), authService.getCurrentUser())) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("无法修改别人的博客").build());
        }
        String title = (String) map.get("title");
        String content = (String) map.get("content");
        String description = (String) map.get("description");


        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);
        blog.setUpdatedAt(new Date().toInstant());
        blogService.updateBlog(blog);
        return JSON.toJSONString(BlogResult.blogResult("ok", "修改成功", blog));
    }

    @DeleteMapping(path = "/blog/{id}")
    public String deleteBlog(@PathVariable int id) {
        // 判断用户是否登陆
        if (!authService.isLoggedIn()) {
            return JSON.toJSONString(Result.notLoggedIn());
        }

        // 如果用户登陆，判断blogId是否存在
        Blog blog = blogService.getBlogById(id);
        if (Objects.isNull(blog)) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("博客不存在").build());
        }
        // 判断当前登陆用户和博客的作者是否为同一人
        if (!Objects.equals(userService.getUserById(blog.getUser().getId()), authService.getCurrentUser())) {
            return JSON.toJSONString(Result.builder()
                    .status("fail")
                    .msg("无法删除别人的博客").build());
        }

        blogService.deleteBlog(id);
        return JSON.toJSONString(Result.builder().status("ok").msg("删除成功").build());
    }

}
