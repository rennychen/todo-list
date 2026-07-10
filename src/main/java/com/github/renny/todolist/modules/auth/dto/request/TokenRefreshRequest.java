package com.github.renny.todolist.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    @NotBlank(message = "token不得為空")
    private String refreshToken;

    public String getRefreshToken(){ return refreshToken; }

    public void setRefreshToken(String refreshToken){ this.refreshToken = refreshToken; }
}
