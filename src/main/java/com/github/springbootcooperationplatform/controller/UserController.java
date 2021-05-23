package com.github.springbootcooperationplatform.controller;

import com.github.springbootcooperationplatform.model.User;
import com.github.springbootcooperationplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/user",method = RequestMethod.GET)
    public User getUserById(@RequestParam Integer id) {
        return userService.findUserById(id);
    }
}
