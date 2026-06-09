package com.github.renny.todolist.modules.auth.controller;

import com.github.renny.todolist.common.response.ApiResponse;
import com.github.renny.todolist.modules.auth.dto.request.RegisterAccountRequest;
import com.github.renny.todolist.modules.auth.dto.response.RegisterAccountResponse;
import com.github.renny.todolist.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
