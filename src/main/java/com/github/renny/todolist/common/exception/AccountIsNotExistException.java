package com.github.renny.todolist.common.exception;

public class AccountIsNotExistException extends RuntimeException{
    public AccountIsNotExistException(String message){
        super(message);
    }
}
