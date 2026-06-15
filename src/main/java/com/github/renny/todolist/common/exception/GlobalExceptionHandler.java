package com.github.renny.todolist.common.exception;

import com.github.renny.todolist.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> resourceNotFound(ResourceNotFoundException e){
        log.warn("搜尋失敗, {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("搜尋失敗," + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(TodoValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleTodoValidation(TodoValidationException e){
        log.warn("格式錯誤, {}",e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("格式錯誤," + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleFormatValidationError(MethodArgumentNotValidException e){
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("驗證格式錯誤, {}" , errorMessage);
        ApiResponse<Void> response = ApiResponse.error("格式錯誤," + errorMessage);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccountIsExistException.class)
    public ResponseEntity<ApiResponse<Void>> handlerAccountIsExist(AccountIsExistException e){
        log.warn("帳號重複錯誤: {}",e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("錯誤,帳號重複," + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccountIsNotExistException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccountIsNotExist(AccountIsNotExistException e){
        log.warn("帳號不存在錯誤: {}",e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("登入失敗" + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<ApiResponse<Void>> handlePasswordNotMatch(PasswordNotMatchException e){
        log.warn("密碼不符錯誤: {}",e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("登入失敗," + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception e){
        log.error("發生非預期例外: {} | 詳細如下" ,e.toString(), e);
        ApiResponse<Void> response = ApiResponse.error("伺服器發生錯誤,稍後再試");
        return ResponseEntity.internalServerError().body(response);
    }
}
