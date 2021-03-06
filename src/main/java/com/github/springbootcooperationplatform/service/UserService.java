package com.github.springbootcooperationplatform.service;

import com.github.springbootcooperationplatform.entity.User;
import com.github.springbootcooperationplatform.repository.UserMapper;
import com.github.springbootcooperationplatform.utils.AvatarHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AvatarHelper avatarHelper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User getUserByName(String username) {
        return userMapper.findUserByName(username);
    }

    public User getUserById(int id) {
        return userMapper.findUserById(id);
    }

    public void save(String username, String password) {
        userMapper.save(username, bCryptPasswordEncoder.encode(password), avatarHelper.createAvatarUrl(username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByName(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(username + " is not found!");
        }

        return new org.springframework.security.core.userdetails.User(username, user.getEncryptedPassword(), Collections.emptyList());
    }
}
