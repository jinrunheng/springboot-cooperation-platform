package com.github.springbootcooperationplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.springbootcooperationplatform.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    private MockMvc mvc;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Mock
    AuthenticationManager mockAuthenticationManager;

    @Mock
    UserService userService;

    @InjectMocks
    AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(authController).build();
    }


    // 未登陆状态，访问 auth 接口返回正确信息
    @Test
    void testUserNotLoginAuth() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk()).andExpect(mvcResult -> {
            Map<String, Object> map = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
            Assertions.assertFalse((Boolean) map.get("isLogin"));
            Assertions.assertEquals("ok", map.get("status"));
        });
    }

    // 登陆成功，然后访问 auth 接口返回正确信息
    @Test
    void testUserLoginAuth() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", "testUser");
        map.put("password", "testPassword");

        Mockito.when(userService.loadUserByUsername("testUser")).thenReturn(new User("testUser", encoder.encode("testPassword"), Collections.emptyList()));
        Mockito.when(userService.getUserByName("testUser")).thenReturn(
                com.github.springbootcooperationplatform.entity.User
                        .builder()
                        .username("testUser")
                        .encryptedPassword(encoder.encode("testPassword"))
                        .build());

        MvcResult response = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(map))).andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    Map<String, Object> jsonMap = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
                    Assertions.assertEquals(jsonMap.get("status"), "ok");
                }).andReturn();


        HttpSession session = response.getRequest().getSession();

        mvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk()).andExpect(mvcResult -> {
            Map<String, Object> jsonMap = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
            Assertions.assertTrue((Boolean) jsonMap.get("isLogin"));
        });
    }

    // 测试登陆是否成功
    @Test
    void testUserLogin() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", "testUser");
        map.put("password", "testPassword");

        Mockito.when(userService.loadUserByUsername("testUser")).thenReturn(new User("testUser", encoder.encode("testPassword"), Collections.emptyList()));

        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(map))).andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    Map<String, Object> jsonMap = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
                    Assertions.assertEquals(jsonMap.get("status"), "ok");
                });

    }

}