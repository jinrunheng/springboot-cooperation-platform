package com.github.springbootcooperationplatform.repository;

import com.github.springbootcooperationplatform.entity.Blog;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BlogMapper {
    Blog findBlogById(int blogId);

    int insertBlog(Blog newBlog);

    int updateBlog(Blog blog);

    void deleteBlog(int blogId);
}
