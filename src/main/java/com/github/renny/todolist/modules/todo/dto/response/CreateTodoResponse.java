package com.github.renny.todolist.modules.todo.dto.response;

import java.time.LocalDate;

public class CreateTodoResponse {
    private Long todoId;
    private boolean complete;
    private LocalDate createDate;
    private String mission;
    private String note;
    private Long userId;

    public CreateTodoResponse(Long todoId, boolean complete, LocalDate createDate, String mission, String note,Long userId){
        this.todoId = todoId;
        this.complete = complete;
        this.createDate = createDate;
        this.mission = mission;
        this.note = note;
        this.userId = userId;
    }

    public Long getTodoId(){
        return todoId;
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

    public Long getUserId(){ return userId; }
}
