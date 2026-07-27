package com.github.renny.todolist.modules.user.controller;

import com.github.renny.todolist.common.response.ApiResponse;
import com.github.renny.todolist.modules.user.dto.request.ChangePasswordRequest;
import com.github.renny.todolist.modules.user.dto.request.ChangeUserProfileRequest;
import com.github.renny.todolist.modules.user.dto.response.ChangeUserProfileResponse;
import com.github.renny.todolist.modules.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordRequest request,
                                                            @RequestAttribute("currentUserId") Long userId,
                                                            HttpServletRequest httpRequrst){
        String accessToken = httpRequrst.getHeader("Authorization").substring(7);
        userService.changePassword(request,userId,accessToken);
        return ResponseEntity.ok(ApiResponse.success("密碼更改成功,請重新登入",null));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<ChangeUserProfileResponse>> changeUserProfile(@RequestBody @Valid ChangeUserProfileRequest request,
                                                                                    @RequestAttribute("currentUserId") Long userId){
        ChangeUserProfileResponse response = userService.changeUserProfile(request,userId);
        return ResponseEntity.ok(ApiResponse.success("使用者資料更改成功",response));
    }
}
