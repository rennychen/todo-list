package com.github.renny.todolist.modules.todo.service;

import com.github.renny.todolist.common.exception.ResourceNotFoundException;
import com.github.renny.todolist.modules.todo.dto.response.CreateTodoResponse;
import com.github.renny.todolist.modules.todo.dto.response.ReadTodoResponse;
import com.github.renny.todolist.modules.todo.repository.TodoRepository;
import com.github.renny.todolist.modules.todo.entity.Todo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private static final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    public CreateTodoResponse createTodo(String mission, String note){
        log.info("開始建立待辦事項: {} , 待辦事項備註: {}",mission,note);
        Todo todo = new Todo(mission,note);
        Todo saveTodo = todoRepository.save(todo);
        log.info("完成建立待辦事項,id: {}",saveTodo.getId());
        return new CreateTodoResponse(
                saveTodo.getId(),
                saveTodo.getCompleted(),
                saveTodo.getCreateDate(),
                saveTodo.getMission(),
                saveTodo.getNote());
    }

    public ReadTodoResponse readTodo(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("找不到該任務"));
        log.info("透過id找到該任務,任務: {},備註: {}",todo.getMission(),todo.getNote());
        return new ReadTodoResponse(
                todo.getMission(),
                todo.getNote(),
                todo.getCompleted()
                ,todo.getCreateDate());
    }

    @Transactional
    public void updateTodoStatus(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("找不到該任務"));
        todo.setCompleted(!todo.getCompleted());
        log.info("任務狀態更新完成,id= {},目前狀態= {}",id,todo.getCompleted());
        todoRepository.save(todo);
    }
}
