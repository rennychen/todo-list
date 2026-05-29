package com.github.renny.todolist.modules.todo.dto.response;

import java.time.LocalDate;

public class ReadTodoResponse {
    private String mission;
    private String note;
    private boolean complete;
    private LocalDate createDate;

    public ReadTodoResponse(String mission, String note,boolean complete,LocalDate createDate){
        this.mission = mission;
        this.note = note;
        this.complete = complete;
        this.createDate = createDate;
    }

    public String getMission(){
        return mission;
    }

    public String getNote(){
        return note;
    }

    public boolean getComplete(){ return complete; }

    public LocalDate getCreateDate(){return createDate;}

}
