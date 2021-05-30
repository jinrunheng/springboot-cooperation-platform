package com.github.springbootcooperationplatform.integration;

import com.alibaba.fastjson.JSON;
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

    @Test
    public void notLoggedInByDefault() {
        String port = environment.getProperty("local.server.port");
        String url = "http://localhost:" + port + "/auth";
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
        String port = environment.getProperty("local.server.port");
        String url = "http://localhost:" + port + "/auth/register";
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

}
