package com.github.renny.todolist.dto.request;

public class CreateTodoRequest {
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
