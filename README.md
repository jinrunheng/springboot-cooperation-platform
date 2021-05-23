## Spring Boot 多人协作平台项目

#### 如何在本地启动程序

使用 Docker 启动数据库

```
docker run --name my-mysql -e MYSQL_ROOT_PASSWORD=123 -e MYSQL_DATABASE=test -p 3306:3306 -d mysql
```