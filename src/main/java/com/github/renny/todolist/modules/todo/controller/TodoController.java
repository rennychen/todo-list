package com.github.renny.todolist.modules.todo.controller;

import com.github.renny.todolist.modules.todo.dto.request.CreateTodoRequest;
import com.github.renny.todolist.common.response.ApiResponse;
import com.github.renny.todolist.modules.todo.dto.response.CreateTodoResponse;
import com.github.renny.todolist.modules.todo.service.TodoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<CreateTodoResponse>> createTodo(@RequestBody @Valid CreateTodoRequest request){
        CreateTodoResponse successData = todoService.createTodo(request.getMission(),request.getNote());
        return ResponseEntity.ok(ApiResponse.success("建立任務成功!",successData));
    }

}
