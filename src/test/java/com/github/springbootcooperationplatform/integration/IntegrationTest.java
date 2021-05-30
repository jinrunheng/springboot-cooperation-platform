package com.github.springbootcooperationplatform.integration;

import com.alibaba.fastjson.JSONObject;
import com.github.springbootcooperationplatform.SpringbootCooperationPlatformApplication;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringbootCooperationPlatformApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class IntegrationTest {

    @Autowired
    Environment environment;

    private String getUrl(String path) {
        String port = environment.getProperty("local.server.port");
        return "http://localhost:" + port + path;
    }

    @Test
    public void notLoggedInByDefault() {
        String url = getUrl("/auth");
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            Map<String, Object> map = JSONObject.parseObject(response.body().string());
            Assertions.assertFalse((boolean) map.get("isLogin"));
            Assertions.assertEquals(map.get("status"), "ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void registerSucceed() {
        final MediaType JSON = MediaType.get("application/json;charset=utf-8");
        String url = getUrl("/auth/register");
        OkHttpClient httpClient = new OkHttpClient();
        JSONObject testJson = new JSONObject();
        testJson.put("username", "testUser");
        testJson.put("password", "testPassword");
        RequestBody requestBody = RequestBody.create(JSON, testJson.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            Map<String, Object> map = JSONObject.parseObject(response.body().string());
            Assertions.assertEquals(map.get("status"), "ok");
            Assertions.assertFalse(Objects.isNull(map.get("data")));
        } catch (IOException e) {
        }
    }

    @Test
    public void testUserRegisterSucceedAndLogin() {
        final MediaType JSON = MediaType.get("application/json;charset=utf-8");
        OkHttpClient httpClient = new OkHttpClient();
        JSONObject testJson = new JSONObject();
        testJson.put("username", "test");
        testJson.put("password", "123456");
        RequestBody requestBody = RequestBody.create(JSON, testJson.toString());
        Request registerRequest = new Request.Builder()
                .url(getUrl("/auth/register"))
                .post(requestBody)
                .build();

        Request loginRequest = new Request.Builder()
                .url(getUrl("/auth/login"))
                .post(requestBody)
                .build();


        try {
            httpClient.newCall(registerRequest).execute();
            Response loginResponse = httpClient.newCall(loginRequest).execute();
            Map<String, Object> loginMap = JSONObject.parseObject(loginResponse.body().string());
            Assertions.assertEquals(loginMap.get("status"), "ok");
            Assertions.assertFalse(Objects.isNull(loginMap.get("data")));

        } catch (IOException e) {
        }

    }

    @Test
    public void testEmptyBlogReturnSystemError() {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getUrl("/blog/1"))
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            Map<String, Object> map = JSONObject.parseObject(response.body().string());
            Assertions.assertEquals(map.get("status"), "fail");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
