package com.github.renny.todolist.dto.response;

public class ApiResponse<T> {
    private final boolean status;
    private final String message;
    private final T data;

    public ApiResponse(boolean status,String message,T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public T getData(){
        return data;
    }

    public static <T> ApiResponse<T> success(String message,T data){
        return new ApiResponse<>(true,message,data);
    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(false,message,null);
    }
}
