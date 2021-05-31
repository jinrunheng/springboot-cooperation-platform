## Spring Boot Blog Project
[![Build Status](https://travis-ci.com/jinrunheng/springboot-cooperation-platform.svg?branch=master)](https://travis-ci.com/jinrunheng/springboot-cooperation-platform)
![GitHub last commit](https://img.shields.io/github/last-commit/jinrunheng/springboot-cooperation-platform?color=blue)
![GitHub pull requests](https://img.shields.io/github/issues-pr/jinrunheng/springboot-cooperation-platform?color=pink)
![GitHub issues](https://img.shields.io/github/issues/jinrunheng/springboot-cooperation-platform?color=yellow)

English | [中文文档](https://github.com/jinrunheng/springboot-cooperation-platform/blob/master/README_zh.md)

### Introductions

This is a simple blog project based on spring boot framework :)

- [preview]()
- [document](https://github.com/jinrunheng/springboot-cooperation-platform/blob/master/interface-convention.md)
- [font-end code](https://github.com/jinrunheng/vue-blog-preview)

Development environment and tools
- JDK 1.8
- Spring Boot 2.1.3.RELEASE
- Maven
- Docker
- MySQL
- MyBatis
- Flyway
- Jacoco

### Features

1. Standard RESTful code style,strictly through the four HTTP verbs(GET,POST,PATCH,DELETE) to send requests to server.
2. Using **docker** and **flyway** to initialize schema table and realize database persistence 
3. test code coverage: *87%* by **Jacoco**
4. Use **okhttp** to send requests to each interface that realize complete automatic integration testing
5. **Travis Ci** to maintain the robustness of the project
### Usage
- Use `git` clone project
    ```
    git clone https://github.com/jinrunheng/springboot-cooperation-platform.git
    ```

- Use `docker` to start database

    ```
    docker run --name my-mysql -e MYSQL_ROOT_PASSWORD=123 -e MYSQL_DATABASE=test -p 3306:3306 -d mysql
    ```
- Use `flyway` initialize table USER and BLOG
    ```
    mvn flyway:clean flyway:migrate
    ``` 
- use `jenkins` 
    ```
    docker run -p 8080:8080 -v `pwd`/jenkins-data:/var/jenkins_home jenkins/jenkins
    ``` 
### Authors

Welcome to follow my Yuque document library : https://www.yuque.com/dobbykim , you can learn more about Java technology through my article



If you have any problem，Please [submit](https://github.com/jinrunheng/springboot-cooperation-platform/issues/new) issue to me，or send email to : jinrunheng@foxmail.com

Thank you

### License 
 