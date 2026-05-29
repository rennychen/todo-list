package com.github.renny.todolist.modules.todo.controller;

import com.github.renny.todolist.modules.todo.dto.request.CreateTodoRequest;
import com.github.renny.todolist.common.response.ApiResponse;
import com.github.renny.todolist.modules.todo.dto.request.UpdateTodoRequest;
import com.github.renny.todolist.modules.todo.dto.response.CreateTodoResponse;
import com.github.renny.todolist.modules.todo.dto.response.ReadTodoResponse;
import com.github.renny.todolist.modules.todo.dto.response.UpdateTodoResponse;
import com.github.renny.todolist.modules.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
        CreateTodoResponse successData = todoService.createTodo(request);
        return ResponseEntity.ok(ApiResponse.success("建立任務成功!",successData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReadTodoResponse>> readTodo(@PathVariable Long id){
        ReadTodoResponse successData = todoService.readTodo(id);
        return ResponseEntity.ok(ApiResponse.success("查詢成功!",successData));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateTodoStatus(@PathVariable Long id){
        todoService.updateTodoStatus(id);
        return ResponseEntity.ok(ApiResponse.success("更改待辦任務狀態成功",null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateTodoResponse>> updateTodo(@PathVariable Long id,
                                                                      @RequestBody @Valid UpdateTodoRequest request){
        UpdateTodoResponse successData = todoService.updateTodo(id,request);
        return ResponseEntity.ok(ApiResponse.success("更改任務成功!",successData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(@PathVariable Long id){
        todoService.deleteTodo(id);
        return ResponseEntity.ok(ApiResponse.success("成功刪除任務",null));
    }
}
