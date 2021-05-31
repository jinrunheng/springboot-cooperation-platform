## Spring Boot 多人协作平台项目
[![Build Status](https://travis-ci.com/jinrunheng/springboot-cooperation-platform.svg?branch=master)](https://travis-ci.com/jinrunheng/springboot-cooperation-platform)
![GitHub last commit](https://img.shields.io/github/last-commit/jinrunheng/springboot-cooperation-platform?color=blue)
![GitHub pull requests](https://img.shields.io/github/issues-pr/jinrunheng/springboot-cooperation-platform?color=pink)
![GitHub issues](https://img.shields.io/github/issues/jinrunheng/springboot-cooperation-platform?color=yellow)

[English](https://github.com/jinrunheng/springboot-cooperation-platform/blob/master/README.md) | 中文文档

### 项目介绍

这是一个简单的，基于 Spring Boot 框架开发的博客项目。

- [在线预览]()
- [接口文档](https://github.com/jinrunheng/springboot-cooperation-platform/blob/master/interface-convention.md)
- [前端代码](https://github.com/jinrunheng/vue-blog-preview)

项目使用的技术栈与工具
- JDK 1.8
- Spring Boot 2.1.3.RELEASE
- Maven
- Docker
- MySQL
- MyBatis
- Flyway
- Jacoco

### 项目特点

1. 标准的 RESTful 代码风格，严格通过四个 HTTP 动词(GET,POST,PATCH,DELETE)对服务端资源操作
2. 使用 docker + flyway 实现数据表自动创建与数据库持久化
3. jacoco 测试代码覆盖率:87%
4. 使用 okhttp 对每一个前后端交互接口发送请求，实现完整的自动化集成测试
5. travis ci 持续集成，维护项目的健壮性

### 如何使用
1. 使用 `git`将项目`clone` 到本地
    ```shell script
    git clone https://github.com/jinrunheng/springboot-cooperation-platform.git
    ```
2.  推荐使用 `docker` 启动数据库
    ```
    docker run --name my-mysql -e MYSQL_ROOT_PASSWORD=123 -e MYSQL_DATABASE=test -p 3306:3306 -d mysql
    ```
3. 使用`flyway` 开启数据库持久化，完成`user`表，`blog`表的创建
    ```
    mvn flyway:clean flyway:migrate
    ``` 
4. 启动`jenkins` 
    ```
    docker run -p 8080:8080 -v `pwd`/jenkins-data:/var/jenkins_home jenkins/jenkins
    ``` 
### 关于我

欢迎关注我的语雀文档库：https://www.yuque.com/dobbykim

如果您有任何问题，请提交 issue，或发送邮件至 jinrunheng@foxmail.com

谢谢
### 版权信息

  