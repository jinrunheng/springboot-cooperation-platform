package com.github.springbootcooperationplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.springbootcooperationplatform.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    private MockMvc mvc;

    @Mock
    AuthenticationManager mockAuthenticationManager;

    @Mock
    UserService mockUserService;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new AuthController()).build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk()).andExpect(mvcResult -> {
            Map<String, Object> map = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
            Assertions.assertFalse((Boolean) map.get("isLogin"));
            Assertions.assertEquals("ok", map.get("status"));
        });
    }
    
}