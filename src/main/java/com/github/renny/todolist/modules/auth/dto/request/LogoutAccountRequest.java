package com.github.renny.todolist.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LogoutAccountRequest {
    @NotBlank(message = "access token不得為空")
    private String accessToken;
    @NotBlank(message = "refresh token不得為空")
    private String refreshToken;

    public String getAccessToken(){return accessToken;}

    public String getRefreshToken(){return refreshToken;}

    public void setAccessToken(String accessToken){ this.accessToken = accessToken;}

    public void setRefreshToken(String refreshToken){this.refreshToken = refreshToken;}
}
