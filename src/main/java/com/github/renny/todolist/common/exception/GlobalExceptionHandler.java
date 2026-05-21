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
    public ResponseEntity<ApiResponse<Void>> HandleTodoValidation(TodoValidationException e){
        log.warn("格式錯誤, {}",e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("格式錯誤," + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> HandleFormatValidationError(MethodArgumentNotValidException e){
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("驗證格式錯誤, {}" , errorMessage);
        ApiResponse<Void> response = ApiResponse.error("格式錯誤," + errorMessage);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception e){
        log.error("發生非預期例外:" , e);
        ApiResponse<Void> response = ApiResponse.error("伺服器發生錯誤,稍後再試");
        return ResponseEntity.internalServerError().body(response);
    }
}
