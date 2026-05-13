package com.github.renny.todolist.dto.response;

import java.time.LocalDate;

public class CreateTodoResponse {
    private Long id;
    private boolean complete;
    private LocalDate createDate;
    private String mission;
    private String note;

    public CreateTodoResponse(Long id,boolean complete,LocalDate createDate,String mission,String note){
        this.id = id;
        this.complete = complete;
        this.createDate = createDate;
        this.mission = mission;
        this.note = note;
    }

    public Long getId(){
        return id;
    }

    public boolean getComplete(){
        return complete;
    }

    public LocalDate getCreateDate(){
        return createDate;
    }

    public String getMission(){
        return mission;
    }

    public String getNote(){
        return note;
    }
}
