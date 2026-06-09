package com.github.renny.todolist.modules.auth.service;

import com.github.renny.todolist.common.exception.AccountIsExistException;
import com.github.renny.todolist.modules.auth.dto.request.RegisterAccountRequest;
import com.github.renny.todolist.modules.user.entity.User;
import com.github.renny.todolist.modules.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register Happy-path")
    void registerUser_success(){
        String email = "test1245@gmail.com";
        String password = "Test1245";
        String userName = "test";
        String mockHashPassword = "Fake-Password-Encoder";

        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUserName(userName);
        request.setEmail(email);
        request.setPassword(password);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(mockHashPassword);
        User user = new User(email,mockHashPassword,userName);
        when(userRepository.save(any(User.class))).thenReturn(user);

        authService.registerAccount(request);

        verify(userRepository,times(1)).existsByEmail(email);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository,times(1)).save(captor.capture());

        assertEquals(email,captor.getValue().getEmail());
        assertEquals(userName,captor.getValue().getUserName());
        assertEquals(mockHashPassword,captor.getValue().getPassword());
    }

    @Test
    @DisplayName("register Sad-Path:當 email 存在時應拋出 AccountIsExistException")
    void register_ExistsEmail_throwException(){
        String email = "sadPathTest@test.com";
        String password = "test1255";
        String userName = "Test1";

        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setPassword(password);
        request.setEmail(email);
        request.setUserName(userName);

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(AccountIsExistException.class, () -> {
            authService.registerAccount(request);
        });
    }

}