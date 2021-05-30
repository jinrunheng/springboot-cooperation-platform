package com.github.springbootcooperationplatform.service;

import com.github.springbootcooperationplatform.entity.Blog;
import com.github.springbootcooperationplatform.repository.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        return blogMapper.findBlogs(page, pageSize, userId);
    }

    public Blog getBlogById(int blogId) {
        return blogMapper.findBlogById(blogId);
    }

    public Blog insertBlog(Blog newBlog) {
        blogMapper.insertBlog(newBlog);
        return getBlogById(newBlog.getId());
    }

    public Blog updateBlog(Blog blog) {
        blogMapper.updateBlog(blog);
        return getBlogById(blog.getId());
    }

    public void deleteBlog(int blogId) {
        blogMapper.deleteBlog(blogId);
    }

    public int count(Integer userId) {
        return blogMapper.count(userId);
    }
}
