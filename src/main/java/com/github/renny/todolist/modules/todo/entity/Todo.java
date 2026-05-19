package com.github.renny.todolist.modules.todo.entity;

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

    private Todo(){}
    public Todo(String mission, String note){
        this.mission = mission;
        this.note = note;
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



}
