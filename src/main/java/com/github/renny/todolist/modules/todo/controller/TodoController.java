package com.github.renny.todolist.modules.todo.controller;

import com.github.renny.todolist.modules.todo.dto.request.CreateTodoRequest;
import com.github.renny.todolist.common.response.ApiResponse;
import com.github.renny.todolist.modules.todo.dto.response.CreateTodoResponse;
import com.github.renny.todolist.modules.todo.dto.response.ReadTodoResponse;
import com.github.renny.todolist.modules.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateTodoResponse>> createTodo(@RequestBody @Valid CreateTodoRequest request){
        CreateTodoResponse successData = todoService.createTodo(request.getMission(),request.getNote());
        return ResponseEntity.ok(ApiResponse.success("建立任務成功!",successData));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ReadTodoResponse>> readTodo(@PathVariable Long id){
        ReadTodoResponse successData = todoService.readTodo(id);
        return ResponseEntity.ok(ApiResponse.success("查詢成功!",successData));
    }
}
