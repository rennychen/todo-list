package com.github.renny.todolist.common.exception;

public class AccountIsExistException extends RuntimeException{
    public AccountIsExistException(String message){
        super(message);
    }
}
