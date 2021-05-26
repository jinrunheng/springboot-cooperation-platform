package com.github.springbootcooperationplatform.service;

import com.github.springbootcooperationplatform.entity.User;
import com.github.springbootcooperationplatform.repository.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;

    @Mock
    UserMapper mockMapper;

    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {

        when(mockEncoder.encode("test")).thenReturn("encodedTest");
        userService.save("test", "test");
        verify(mockMapper).save("test", "encodedTest");
    }

    @Test
    public void testGetUserByName() {
        userService.getUserByName("user");
        verify(mockMapper).findUserByName("user");
    }

    @Test
    public void testLoadUserByUserNameThrowExceptionWhenUserNotFound() {
        when(mockMapper.findUserByName("user")).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("user"));
    }

    @Test
    public void testLoadUserByUserNameReturnUserDetailsWhenUserFound() {
        when(mockMapper.findUserByName("user")).thenReturn(User.builder()
                .username("user")
                .encryptedPassword("encodedPassword")
                .build());
        UserDetails userDetails = userService.loadUserByUsername("user");
        Assertions.assertEquals(userDetails.getUsername(), "user");
        Assertions.assertEquals(userDetails.getPassword(), "encodedPassword");
    }
}