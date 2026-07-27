package com.github.renny.todolist.modules.user.dto.response;

public class ChangeUserProfileResponse {
    private String email;
    private String userName;

    public ChangeUserProfileResponse(String email,String userName){
        this.email = email;
        this.userName = userName;
    }

    public String getEmail(){ return email; }

    public String getUserName(){ return userName; }
}
