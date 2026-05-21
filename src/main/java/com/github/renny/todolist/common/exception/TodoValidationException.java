package com.github.renny.todolist.common.exception;

public class TodoValidationException extends RuntimeException {
    public TodoValidationException(String message){
        super(message);
    }
}
