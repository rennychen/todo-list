package com.github.renny.todolist.controller;

import com.github.renny.todolist.dto.request.CreateTodoRequest;
import com.github.renny.todolist.dto.response.ApiResponse;
import com.github.renny.todolist.dto.response.CreateTodoResponse;
import com.github.renny.todolist.service.TodoService;
import com.github.renny.todolist.todo.Todo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }

    @PostMapping("/mission")
    public ResponseEntity<ApiResponse<CreateTodoResponse>> createTodo(@RequestBody CreateTodoRequest request){
        CreateTodoResponse successData = todoService.createTodo(request.getMission(),request.getNote());
        return ResponseEntity.ok(ApiResponse.success("建立任務成功!",successData));
    }

}
