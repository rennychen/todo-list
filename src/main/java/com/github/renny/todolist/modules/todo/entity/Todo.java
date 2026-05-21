package com.github.renny.todolist.modules.todo.entity;

import com.github.renny.todolist.common.exception.TodoValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.LocalDate;

@Entity
public class Todo {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean completed;
    @Column( nullable = false )
    private String mission;
    private String note;
    private LocalDate createDate;

    private Todo(){}  // for JPA
    public Todo(String mission, String note){
        if (mission == null || mission.isBlank()) {
            throw new TodoValidationException("建立待辦事項時，任務名稱不可為空");
        }
        this.mission = mission;
        this.note = note;
    }
    private Todo(Long id,boolean completed,String mission,String note,LocalDate createDate){  //for JUnit
        this.id = id;
        this.completed = completed;
        this.mission = mission;
        this.note = note;
        this.createDate=createDate;
    }

    @PrePersist
    private void onCreate(){
        this.createDate = LocalDate.now();
    }

    public Long getId(){
        return id;
    }

    public boolean getCompleted() {
        return completed;
    }

    public String getMission() {
        return mission;
    }

    public String getNote() {
        return note;
    }

    public LocalDate getCreateDate(){
        return createDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setMission(String mission){
        this.mission = mission;
    }

    public void setNote(String note){
        this.note = note;
    }

    public static TodoBuilder builder(){
        return new TodoBuilder();
    }

    public static class TodoBuilder{
        private Long id;
        private boolean completed;
        private String mission;
        private String note;
        private LocalDate createDate;

        public TodoBuilder id(Long id){
            this.id = id;
            return this;
        }

        public TodoBuilder completed(boolean completed){
            this.completed = completed;
            return this;
        }

        public TodoBuilder mission(String mission){
            this.mission = mission;
            return this;
        }

        public TodoBuilder note(String note){
            this.note = note;
            return this;
        }

        public TodoBuilder createDate(LocalDate createDate){
            this.createDate = createDate;
            return this;
        }

        public Todo builder(){
            return new Todo(this.id,this.completed,this.mission,this.note,this.createDate);
        }
    }

}
