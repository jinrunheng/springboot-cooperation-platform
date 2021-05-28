package com.github.springbootcooperationplatform.service;

import com.github.springbootcooperationplatform.entity.Blog;
import com.github.springbootcooperationplatform.entity.User;
import com.github.springbootcooperationplatform.repository.BlogMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    BlogMapper blogMapper;

    @InjectMocks
    BlogService blogService;

    @Test
    void testGetBlogById() {
        blogService.getBlogById(1);
        Mockito.verify(blogMapper).findBlogById(1);
    }

    @Test
    void testInsertBlog() {
        Blog testBlog = Blog.builder().id(1).build();
        blogService.insertBlog(testBlog);
        Mockito.verify(blogMapper).findBlogById(1);
    }

    @Test
    void testUpdateBlog() {
        Blog testBlog = Blog.builder().id(1).build();
        blogService.updateBlog(testBlog);
        Mockito.verify(blogMapper).findBlogById(1);
    }

    @Test
    void testDeleteBlog() {
        blogService.deleteBlog(1);
        Mockito.verify(blogMapper).deleteBlog(1);
    }

}