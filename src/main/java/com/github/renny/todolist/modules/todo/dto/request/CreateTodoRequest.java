package com.github.renny.todolist.modules.todo.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateTodoRequest {
    @NotBlank(message = " 待辦任務不得為空 ")
    private String mission;
    private String note;

    public String getMission(){
        return mission;
    }

    public String getNote(){
        return note;
    }

    public void setMission(String mission){
        this.mission = mission;
    }

    public void setNote(String note){
        this.note= note;
    }
}
