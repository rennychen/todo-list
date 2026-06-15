package com.github.renny.todolist.modules.auth.service;

import com.github.renny.todolist.common.exception.AccountIsExistException;
import com.github.renny.todolist.common.exception.AccountIsNotExistException;
import com.github.renny.todolist.common.exception.PasswordNotMatchException;
import com.github.renny.todolist.modules.auth.dto.request.LoginAccountRequest;
import com.github.renny.todolist.modules.auth.dto.request.RegisterAccountRequest;
import com.github.renny.todolist.modules.auth.dto.response.LoginAccountResponse;
import com.github.renny.todolist.modules.user.entity.User;
import com.github.renny.todolist.modules.user.repository.UserRepository;
import com.github.renny.todolist.security.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register Happy-path")
    void registerAccount_success(){
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
    void registerAccount_ExistsEmail_throwException(){
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

    @Test
    @DisplayName("login Happy-Path")
    void loginAccount_success(){
        String email = "testhappypath@test.com";
        String password = "Test1234";
        String userName = "Amy";
        String mockHashPassword = "test_hash_password";
        String mockToken = "mock.Token.test";
        LoginAccountRequest request = new LoginAccountRequest();
        request.setEmail(email);
        request.setPassword(password);
        User mockUser = new User(email,mockHashPassword,userName);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password,mockHashPassword)).thenReturn(true);
        when(jwtUtils.generateToken(any(User.class))).thenReturn(mockToken);

        LoginAccountResponse response = authService.loginAccount(request);

        verify(userRepository,times(1)).findByEmail(email);
        verify(passwordEncoder,times(1)).matches(password,mockHashPassword);
        assertEquals(mockToken,response.getToken());
        assertEquals(userName,response.getUserName());
    }

    @Test
    @DisplayName("loginAccount Sad-Path:找不到 email 拋出 AccountNotExistException")
    void loginAccount_accountNotExist_throwException(){
        String email = "test@test.com";
        String password = "Test1243";
        LoginAccountRequest request = new LoginAccountRequest();
        request.setEmail(email);
        request.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(AccountIsNotExistException.class,() -> authService.loginAccount(request));
    }

    @Test
    @DisplayName("loginAccount Sad-Path:當密碼錯誤,拋出PasswordNotMatchException")
    void loginAccount_passwordNotMatch_throwException(){
        String email = "test@test.com";
        String password = "Test1243";
        String userName = "TestUser";
        String mockHashPassword = "mock_hash_password";
        User mockUser = new User(email,mockHashPassword,userName);
        LoginAccountRequest request = new LoginAccountRequest();
        request.setEmail(email);
        request.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password,mockHashPassword)).thenReturn(false);

        assertThrows(PasswordNotMatchException.class,() -> authService.loginAccount(request));
    }
}