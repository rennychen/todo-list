package com.github.renny.todolist.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterAccountRequest {
    @NotBlank( message = "信箱不得為空" )
    @Pattern(regexp = "^(?=[a-zA-Z])[a-zA-Z0-9_.-]+@[a-zA-Z0-9_.-]+\\.[a-zA-Z]{2,}$",
            message = "請輸入正確的電子信箱格式,開頭必須是英文大小寫")
    private String email;
    @NotBlank( message = "密碼不得為空" )
    @Pattern( regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,16}$" , message = "密碼長度至少8~16位數,密碼至少有一個英文大寫字母+一個英文小寫字母+數字組成")
    private String password;
    @NotBlank( message = "使用者名稱不得為空" )
    private String userName;

    public String getEmail(){ return email; }

    public String getPassword(){ return password; }

    public String getUserName(){ return userName; }

    public void setEmail(String email){ this.email = email;}

    public void setPassword(String password){ this.password = password; }

    public void setUserName(String userName){ this.userName = userName; }
}
