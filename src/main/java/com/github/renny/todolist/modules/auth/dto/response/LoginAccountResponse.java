package com.github.renny.todolist.modules.auth.dto.response;

public class LoginAccountResponse {
    private String accessToken;
    private String refreshToken;
    private String userName;

    public LoginAccountResponse(String accessToken,String refreshToken , String userName){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userName = userName;
    }

    public String getAccessToken(){ return accessToken; }
    public String getRefreshToken(){ return refreshToken; }
    public String getUserName(){ return userName;}
}
