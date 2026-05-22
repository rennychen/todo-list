package com.github.renny.todolist.modules.todo.dto.response;

public class ReadTodoResponse {
    private String mission;
    private String note;

    public ReadTodoResponse(String mission, String note){
        this.mission = mission;
        this.note = note;
    }

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
        this.note = note;
    }
}
