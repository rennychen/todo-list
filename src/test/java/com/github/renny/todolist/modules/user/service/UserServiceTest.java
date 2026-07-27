package com.github.renny.todolist.modules.user.service;

import com.github.renny.todolist.common.exception.PasswordNotMatchException;
import com.github.renny.todolist.common.exception.ResourceNotFoundException;
import com.github.renny.todolist.modules.auth.controller.AuthController;
import com.github.renny.todolist.modules.auth.service.AuthService;
import com.github.renny.todolist.modules.user.dto.request.ChangePasswordRequest;
import com.github.renny.todolist.modules.user.dto.request.ChangeUserProfileRequest;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("changePassword Happy-Path")
    void changePassword_success(){
        String mockAccessToken = "accessToken";
        String mockRefreshToken = "refreshToken";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String checkNewPassword = "newPassword";
        Long userId = 6L;
        String oldPasswordHash = "oldPasswordHash";
        String newPasswordHash = "newPasswordHash";
        User mockUser = new User("test@test.com",oldPasswordHash,"testName");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);
        request.setCheckNewPassword(checkNewPassword);
        request.setRefreshToken(mockRefreshToken);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(oldPassword,oldPasswordHash)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPasswordHash);

        userService.changePassword(request,userId,mockAccessToken);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository,times(1)).save(captor.capture());
        assertEquals(newPasswordHash,captor.getValue().getPassword());

        verify(passwordEncoder,times(1)).matches(oldPassword,oldPasswordHash);
        verify(passwordEncoder,times(1)).encode(newPassword);
        verify(authService,times(1)).logoutAccount(mockRefreshToken,userId,mockAccessToken);

    }

    @Test
    @DisplayName("changePassword Sad-Path:當找不到 userId 拋出 ResourceNotFoundException")
    void changePassword_userIdNotExists_throwException(){
        ChangePasswordRequest request = new ChangePasswordRequest();
        Long userId = 3L;
        String accessToken = "accessToken";

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> {userService.changePassword(request,userId,accessToken); });
        verify(passwordEncoder,never()).matches(any(),any());
        verify(userRepository,never()).save(any());
        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("changePassword Sad-Path:舊密碼驗證不符,拋出 PasswordNotMatchException")
    void changePassword_oldPasswordNotMatch_throwException(){
        String oldPassword = "oldPassword";
        String oldPasswordHash = "oldPasswordHash";
        Long userId = 3L;
        String accessToken = "accessToken";
        User mockUser = new User("test@test.com",oldPasswordHash,"userName");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(request.getOldPassword(),mockUser.getPassword())).thenReturn(false);

        assertThrows(PasswordNotMatchException.class,() -> { userService.changePassword(request,userId,accessToken); });

        verify(userRepository,times(1)).findById(userId);
        verify(userRepository,never()).save(mockUser);
        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("changePassword Sad-Path:新密碼與二次輸入密碼驗證不符,拋出 PasswordNotMatchException")
    void changePassword_newPasswordNotMatch_throwException(){
        String oldPassword = "oldPassword";
        String oldPasswordHash = "oldPasswordHash";
        String newPassword = "newPassword";
        String checkNewPassword = "checkNewPasword";
        Long userId = 3L;
        String accessToken = "accessToken";
        User mockUser = new User("test@test.com",oldPasswordHash,"userName");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);
        request.setCheckNewPassword(checkNewPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(request.getOldPassword(),mockUser.getPassword())).thenReturn(true);

        assertThrows(PasswordNotMatchException.class,() -> { userService.changePassword(request,userId,accessToken); });

        verify(userRepository,times(1)).findById(userId);
        verify(passwordEncoder,times(1)).matches(request.getOldPassword(),mockUser.getPassword());
        verify(userRepository,never()).save(mockUser);
        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("changeUserProfile Happy-Path")
    void changeUserProfile_success(){
        String userName = "testName";
        User mockUser = new User("test@test.com","password","userName");
        Long userId = 6L;

        ChangeUserProfileRequest request = new ChangeUserProfileRequest();
        request.setUserName(userName);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.changeUserProfile(request,userId);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository,times(1)).save(captor.capture());

        assertEquals(userName,captor.getValue().getUserName());

    }

    @Test
    @DisplayName("changeUserProfile sad path:當找不到 userId 拋出 ResourceNotFoundException")
    void changeUserProfile_userIdNotExist_theowException(){
        Long userId = 5L;

        ChangeUserProfileRequest request = new ChangeUserProfileRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> { userService.changeUserProfile(request,userId); } );

        verify(userRepository,never()).save(any());
    }

}