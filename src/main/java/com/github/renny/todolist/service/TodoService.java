package com.github.renny.todolist.service;

import com.github.renny.todolist.dto.response.CreateTodoResponse;
import com.github.renny.todolist.repository.TodoRepository;
import com.github.renny.todolist.todo.Todo;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    public CreateTodoResponse createTodo(String mission, String note){
        if(mission == null || mission.isBlank() ){
            System.out.println(" ?");
        }

        Todo todo = new Todo(mission,note);
        Todo saveTodo = todoRepository.save(todo);
        return new CreateTodoResponse(
                saveTodo.getId(),
                saveTodo.getCompleted(),
                saveTodo.getCreateDate(),
                saveTodo.getMission(),
                saveTodo.getNote());
    }
}
