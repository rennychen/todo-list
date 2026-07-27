package com.github.renny.todolist.modules.auth.service;

import com.github.renny.todolist.common.exception.AccountIsExistException;
import com.github.renny.todolist.common.exception.AccountIsNotExistException;
import com.github.renny.todolist.common.exception.PasswordNotMatchException;
import com.github.renny.todolist.common.exception.ResourceNotFoundException;
import com.github.renny.todolist.modules.auth.dto.request.LoginAccountRequest;
import com.github.renny.todolist.modules.auth.dto.request.LogoutAccountRequest;
import com.github.renny.todolist.modules.auth.dto.request.RegisterAccountRequest;
import com.github.renny.todolist.modules.auth.dto.request.TokenRefreshRequest;
import com.github.renny.todolist.modules.auth.dto.response.LoginAccountResponse;
import com.github.renny.todolist.modules.auth.dto.response.RegisterAccountResponse;
import com.github.renny.todolist.modules.auth.dto.response.TokenRefreshResponse;
import com.github.renny.todolist.modules.user.entity.User;
import com.github.renny.todolist.modules.user.repository.UserRepository;
import com.github.renny.todolist.security.JwtUtils;
import com.github.renny.todolist.security.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtUtils jwtUtils,TokenBlacklistService tokenBlacklistService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistService = tokenBlacklistService;
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
                jwtUtils.generateAccessToken(user),
                jwtUtils.generateRefreshToken(user),
                user.getUserName()
        );
    }

    @Transactional
    public TokenRefreshResponse tokenRefresh(TokenRefreshRequest request){
        Claims claims = jwtUtils.validateAndParseToken(request.getRefreshToken());
        String userIdStr = jwtUtils.getUserIdFromClaims(claims);

        long remainingTimeMillis = claims.getExpiration().getTime() - System.currentTimeMillis();
        tokenBlacklistService.blacklistToken(request.getRefreshToken(),remainingTimeMillis);

        Long userId = Long.valueOf(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("使用者id查不到該帳戶,userId: {}",userId);
                    return new ResourceNotFoundException("找不到該用戶");
                });
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        log.info("token更新成功,userId: {}",userId);
        return new TokenRefreshResponse(
                accessToken,
                refreshToken);
    }

    @Transactional
    public void logoutAccount(String refreshToken, Long userId,String accessToken){
        log.info("用戶準備登出,userId: {}",userId);
        try{
            Claims accessTokenClaims = jwtUtils.validateAndParseToken(accessToken);
            long accessTokenRemainingTimeMillis = accessTokenClaims.getExpiration().getTime() - System.currentTimeMillis();
            tokenBlacklistService.blacklistToken(accessToken,accessTokenRemainingTimeMillis);
        }catch (JwtException e){
            log.debug("access token驗證失敗,跳過加入黑名單. {}", e.getMessage());
        }

        try {
            Claims refreshTokenClaims = jwtUtils.validateAndParseToken(refreshToken);
            long refreshTokenRemainingTimeMillis = refreshTokenClaims.getExpiration().getTime() - System.currentTimeMillis();
            tokenBlacklistService.blacklistToken(refreshToken,refreshTokenRemainingTimeMillis);
        }catch (JwtException e){
            log.debug("refresh token驗證失敗,,跳過加入黑名單. {}", e.getMessage());
        }
        log.info("用戶token已加入blacklist");
    }

}
