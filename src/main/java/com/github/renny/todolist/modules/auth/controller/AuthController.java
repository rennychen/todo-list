package com.github.renny.todolist.modules.auth.controller;

import com.github.renny.todolist.common.response.ApiResponse;
import com.github.renny.todolist.modules.auth.dto.request.LoginAccountRequest;
import com.github.renny.todolist.modules.auth.dto.request.LogoutAccountRequest;
import com.github.renny.todolist.modules.auth.dto.request.RegisterAccountRequest;
import com.github.renny.todolist.modules.auth.dto.request.TokenRefreshRequest;
import com.github.renny.todolist.modules.auth.dto.response.LoginAccountResponse;
import com.github.renny.todolist.modules.auth.dto.response.RegisterAccountResponse;
import com.github.renny.todolist.modules.auth.dto.response.TokenRefreshResponse;
import com.github.renny.todolist.modules.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterAccountResponse>> registerAccount(@RequestBody @Valid RegisterAccountRequest request){
        RegisterAccountResponse successData = authService.registerAccount(request);
        return ResponseEntity.ok(ApiResponse.success("帳號註冊成功!",successData));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginAccountResponse>> loginAccount(@RequestBody @Valid LoginAccountRequest request){
        LoginAccountResponse successData = authService.loginAccount(request);
        return ResponseEntity.ok(ApiResponse.success("登入成功!",successData));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> tokenRefresh(@RequestBody @Valid TokenRefreshRequest request){
        TokenRefreshResponse successData = authService.tokenRefresh(request);
        return ResponseEntity.ok(ApiResponse.success("token更新成功",successData));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logoutAccount(@RequestBody @Valid LogoutAccountRequest resquest,
                                                           @RequestAttribute("currentUserId") Long userId,
                                                           HttpServletRequest httpRequest){
        String accessToken = httpRequest.getHeader("Authorization").substring(7);
        authService.logoutAccount(resquest.getRefreshToken(),userId,accessToken);
        return ResponseEntity.ok(ApiResponse.success("登出成功",null));
    }
}
