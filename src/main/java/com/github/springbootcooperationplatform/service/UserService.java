package com.github.springbootcooperationplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Map<String, String> memory = new ConcurrentHashMap<>();

    public UserService() {
        memory.put("user", "password");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!memory.containsKey(username)) {
            throw new UsernameNotFoundException(username + " is not found!");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(memory.get(username));
        return new org.springframework.security.core.userdetails.User(username, encodedPassword, Collections.emptyList());
    }
}
