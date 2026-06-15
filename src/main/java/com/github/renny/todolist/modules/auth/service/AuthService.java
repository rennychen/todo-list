package com.github.renny.todolist.modules.auth.service;

import com.github.renny.todolist.common.exception.AccountIsExistException;
import com.github.renny.todolist.common.exception.AccountIsNotExistException;
import com.github.renny.todolist.common.exception.PasswordNotMatchException;
import com.github.renny.todolist.modules.auth.dto.request.LoginAccountRequest;
import com.github.renny.todolist.modules.auth.dto.request.RegisterAccountRequest;
import com.github.renny.todolist.modules.auth.dto.response.LoginAccountResponse;
import com.github.renny.todolist.modules.auth.dto.response.RegisterAccountResponse;
import com.github.renny.todolist.modules.user.entity.User;
import com.github.renny.todolist.modules.user.repository.UserRepository;
import com.github.renny.todolist.security.JwtUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final static Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtUtils jwtUtils){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public RegisterAccountResponse registerAccount(RegisterAccountRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AccountIsExistException("該帳號已註冊過");
        }
        String hashPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(),hashPassword,request.getUserName());
        User saveUser = userRepository.save(user);
        log.info("註冊成功, email: {}, userName: {}",saveUser.getEmail(),saveUser.getUserName());
        return new RegisterAccountResponse(
                saveUser.getEmail(),
                saveUser.getUserName());
    }

    public LoginAccountResponse loginAccount(LoginAccountRequest request){
        log.info("嘗試登入,帳號: {}",request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("登入失敗,帳號: {} 不存在",request.getEmail());
                    return new AccountIsNotExistException("帳號或密碼錯誤"); });

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            log.warn("登入失敗,密碼錯誤. 帳號: {}",request.getEmail());
            throw new PasswordNotMatchException("帳號或密碼錯誤");
        }
        log.info("登入成功,帳號: {},使用者名稱: {}",user.getEmail(),user.getUserName());
        return new LoginAccountResponse(
                jwtUtils.generateToken(user),
                user.getUserName()
        );
    }

}
