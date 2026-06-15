package com.github.renny.todolist.modules.auth.dto.response;

public class LoginAccountResponse {
    private String token;
    private String userName;

    public LoginAccountResponse(String token,String userName){
        this.token = token;
        this.userName = userName;
    }

    public String getToken(){ return token; }
    public String getUserName(){ return userName;}
}
