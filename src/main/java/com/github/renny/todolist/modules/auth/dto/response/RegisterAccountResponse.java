package com.github.renny.todolist.modules.auth.dto.response;

public class RegisterAccountResponse {
    private String email;
    private String userName;

    public RegisterAccountResponse(String email,String userName){
        this.email = email;
        this.userName = userName;
    }

    public String getEmail(){ return email; }

    public String getUserName(){ return userName; }
}
