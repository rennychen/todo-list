package com.github.renny.todolist.modules.todo.dto.response;

import java.time.LocalDate;

public class UpdateTodoResponse {
    private Long id;
    private boolean complete;
    private LocalDate createDate;
    private String mission;
    private String note;
    private Long userId;

    public UpdateTodoResponse(Long id,boolean complete,String mission, String note,LocalDate createDate,Long userId){
        this.id =id;
        this.complete = complete;
        this.mission = mission;
        this.note = note;
        this.createDate = createDate;
        this.userId = userId;
    }

    public Long getId(){
        return id;
    }

    public boolean getComplete(){
        return complete;
    }

    public String getMission(){
        return mission;
    }

    public String getNote(){
        return note;
    }

    public LocalDate getCreateDate(){
        return createDate;
    }

    public Long getUserId(){ return userId; }

}
