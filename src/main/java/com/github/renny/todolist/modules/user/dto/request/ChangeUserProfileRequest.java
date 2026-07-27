package com.github.renny.todolist.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangeUserProfileRequest {
    @NotBlank(message = "使用者名稱不得為空")
    @Size(min = 2,max = 10,message = "使用者名稱長度為2~10字元")
    private String userName;

    public String getUserName(){ return userName; }

    public void setUserName(String userName){ this.userName = userName;}
}
