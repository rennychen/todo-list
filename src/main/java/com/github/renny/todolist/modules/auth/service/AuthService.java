package com.github.renny.todolist.modules.auth.service;

import com.github.renny.todolist.common.exception.AccountIsExistException;
import com.github.renny.todolist.modules.auth.dto.request.RegisterAccountRequest;
import com.github.renny.todolist.modules.auth.dto.response.RegisterAccountResponse;
import com.github.renny.todolist.modules.user.entity.User;
import com.github.renny.todolist.modules.user.repository.UserRepository;
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

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

}
