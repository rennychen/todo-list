package com.github.renny.todolist.modules.user.service;

import com.github.renny.todolist.common.exception.PasswordNotMatchException;
import com.github.renny.todolist.common.exception.ResourceNotFoundException;
import com.github.renny.todolist.modules.auth.service.AuthService;
import com.github.renny.todolist.modules.user.dto.request.ChangePasswordRequest;
import com.github.renny.todolist.modules.user.dto.request.ChangeUserProfileRequest;
import com.github.renny.todolist.modules.user.dto.response.ChangeUserProfileResponse;
import com.github.renny.todolist.modules.user.entity.User;
import com.github.renny.todolist.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,AuthService authService,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request, Long userId,String accessToken){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到該用戶"));
        log.info("使用者準備變更密碼,userId: {}",userId);
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new PasswordNotMatchException("舊密碼不符,請重新嘗試");
        }
        if(!request.getNewPassword().matches(request.getCheckNewPassword())){
            throw new PasswordNotMatchException("新密碼與二次確認密碼不符,請重新嘗試");
        }
        String hashNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(hashNewPassword);
        userRepository.save(user);
        authService.logoutAccount(request.getRefreshToken(),userId,accessToken);
        log.info("使用者密碼更改完成,userId: {}",userId);
    }

    @Transactional
    public ChangeUserProfileResponse changeUserProfile(ChangeUserProfileRequest request,Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到該用戶"));
        log.info("使用者準備變更使用者名稱,userId: {},舊userName: {},新userName: {}",userId,user.getUserName(),request.getUserName());
        user.setUserName(request.getUserName());
        User saveUser = userRepository.save(user);
        log.info("使用者成功更改名稱,更改後的userName: {}",saveUser.getUserName());
        return new ChangeUserProfileResponse(
                saveUser.getEmail(),
                saveUser.getUserName());
    }
}
