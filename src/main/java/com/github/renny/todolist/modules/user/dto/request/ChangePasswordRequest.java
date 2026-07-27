package com.github.renny.todolist.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ChangePasswordRequest {
    @NotBlank(message = "密碼不得為空")
    private String oldPassword;
    @NotBlank(message = "新密碼不得為空")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,16}$",
            message = "密碼格式錯誤，密碼須有一個英文大寫、一個英文小寫及數字組成，寫長度在8~16位數")
    private String newPassword;
    @NotBlank(message = "再次確認新密碼欄位不得為空")
    private String checkNewPassword;
    @NotBlank(message = "Refresh Token 不得為空")
    String refreshToken;

    public String getOldPassword(){return oldPassword;}

    public String getNewPassword(){ return newPassword; }

    public String getCheckNewPassword(){ return checkNewPassword; }

    public String getRefreshToken(){ return refreshToken; }

    public void setOldPassword(String oldPassword){ this.oldPassword = oldPassword; }

    public void setNewPassword(String newPassword){ this.newPassword = newPassword; }

    public void setCheckNewPassword(String checkNewPassword){ this.checkNewPassword = checkNewPassword; }

    public void setRefreshToken(String refreshToken){ this.refreshToken = refreshToken; }

}
