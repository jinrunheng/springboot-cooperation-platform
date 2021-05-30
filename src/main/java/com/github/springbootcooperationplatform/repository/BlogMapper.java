package com.github.springbootcooperationplatform.repository;

import com.github.springbootcooperationplatform.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Mapper
public interface BlogMapper {

    Blog findBlogById(int blogId);

    int insertBlog(Blog newBlog);

    int updateBlog(Blog blog);

    void deleteBlog(int blogId);

    List<Blog> findBlogs(Integer page, Integer pageSize, Integer userId);

    int count(@Param("userId") Integer userId);
}
