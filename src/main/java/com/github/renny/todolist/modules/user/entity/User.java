package com.github.renny.todolist.modules.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name = "users")
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    @Column( nullable = false, unique = true, length = 150 )
    private String email;
    @Column( nullable = false)
    private String password;
    @Column( nullable = false, length = 50)
    private String userName;

    protected User(){}

    public User(String email, String password, String userName){
        this.email = email;
        this.password = password;
        this.userName = userName;
    }

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
}
