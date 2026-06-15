package com.github.renny.todolist.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginAccountRequest {
    @NotBlank(message = "信箱不得為空")
    private String email;
    @NotBlank(message = "密碼不得為空")
    private String password;

    public String getEmail(){ return email; }

    public String getPassword(){ return password; }

    public void setEmail(String email){ this.email = email; }

    public void setPassword(String password){ this.password = password; }
}
