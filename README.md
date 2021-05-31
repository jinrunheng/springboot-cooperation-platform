## Spring Boot 多人协作平台项目
[![Build Status](https://travis-ci.com/jinrunheng/springboot-cooperation-platform.svg?branch=master)](https://travis-ci.com/jinrunheng/springboot-cooperation-platform)
![GitHub last commit](https://img.shields.io/github/last-commit/jinrunheng/springboot-cooperation-platform?color=blue)
![GitHub pull requests](https://img.shields.io/github/issues-pr/jinrunheng/springboot-cooperation-platform?color=pink)
![GitHub issues](https://img.shields.io/github/issues/jinrunheng/springboot-cooperation-platform?color=yellow)

English | 中文文档

### Introductions

<为什么做这个项目，动机和背景是什么>

这是一个简单的，基于 Spring Boot 框架开发的博客项目。

本项目是我刚刚接触到 Spring Boot 框架该项目适合刚刚接触 Spring Boot 的开发人员进行学习

### Features

### Usage

- 使用 `Docker` 启动数据库

    ```
    docker run --name my-mysql -e MYSQL_ROOT_PASSWORD=123 -e MYSQL_DATABASE=test -p 3306:3306 -d mysql
    ```
- 使用`flyway` 开启数据库持久化，完成`user`表的创建
    ```
    mvn flyway:clean flyway:migrate
    ``` 
- 启动`jenkins` 
    ```
    docker run -p 8080:8080 -v `pwd`/jenkins-data:/var/jenkins_home jenkins/jenkins
    ``` 
### Authors

### License  