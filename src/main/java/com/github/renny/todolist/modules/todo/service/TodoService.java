package com.github.renny.todolist.modules.todo.service;

import com.github.renny.todolist.common.exception.AccountIsNotExistException;
import com.github.renny.todolist.common.exception.ResourceNotFoundException;
import com.github.renny.todolist.modules.todo.dto.request.CreateTodoRequest;
import com.github.renny.todolist.modules.todo.dto.request.UpdateTodoRequest;
import com.github.renny.todolist.modules.todo.dto.response.CreateTodoResponse;
import com.github.renny.todolist.modules.todo.dto.response.ReadTodoResponse;
import com.github.renny.todolist.modules.todo.dto.response.UpdateTodoResponse;
import com.github.renny.todolist.modules.todo.repository.TodoRepository;
import com.github.renny.todolist.modules.todo.entity.Todo;
import com.github.renny.todolist.modules.user.entity.User;
import com.github.renny.todolist.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.List;

@Service
public class TodoService {
    private static final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository,UserRepository userRepository){
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public CreateTodoResponse createTodo(Long userId,CreateTodoRequest request){
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("找不到該使用者"));
        log.info("開始建立待辦->userId: {}, 待辦事項: {} , 待辦事項備註: {}",userId,request.getMission(),request.getNote());
        Todo todo = new Todo(request.getMission(),request.getNote(),user);
        Todo saveTodo = todoRepository.save(todo);
        log.info("完成建立待辦事項,todoId: {}",saveTodo.getId());
        return new CreateTodoResponse(
                saveTodo.getId(),
                saveTodo.getCompleted(),
                saveTodo.getCreateDate(),
                saveTodo.getMission(),
                saveTodo.getNote(),
                saveTodo.getUser().getId());
    }

    public ReadTodoResponse readTodo(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到該使用者"));
        List<Todo> todos = todoRepository.findByUser_Id(userId);
        List<ReadTodoResponse.TodoItem> todoItems = todos.stream()
                        .map(todo -> new ReadTodoResponse.TodoItem(
                                todo.getMission(),
                                todo.getNote(),
                                todo.getCompleted(),
                                todo.getCreateDate()
                        ))
                        .toList();
        log.info("透過 userId 找到該任務,userId: {},任務數量: {}",userId,todos.size());
        return new ReadTodoResponse(todoItems);
    }

    @Transactional
    public void updateTodoStatus(Long id,Long userId) {
        Todo todo = todoRepository.findByIdAndUser_Id(id,userId)
                .orElseThrow(() -> new ResourceNotFoundException("尚未登入或找不到該任務,請重新嘗試"));
        todo.setCompleted(!todo.getCompleted());
        log.info("任務狀態更新完成,userId: {},todoId: {},目前狀態: {}",userId,id,todo.getCompleted());
        todoRepository.save(todo);
    }

    @Transactional
    public UpdateTodoResponse updateTodo(Long id, UpdateTodoRequest request,Long userId){
        Todo todo = todoRepository.findByIdAndUser_Id(id,userId)
                .orElseThrow(() -> new ResourceNotFoundException("尚未登入或找不到該任務,請重新嘗試"));
        todo.setMission(request.getMission());
        todo.setNote(request.getNote());
        Todo saveTodo = todoRepository.save(todo);
        log.info("修改任務完成,userId: {}, mission: {},note: {}",saveTodo.getUser().getId(),saveTodo.getMission(),saveTodo.getNote());
        return new UpdateTodoResponse(
                saveTodo.getId(),
                saveTodo.getCompleted(),
                saveTodo.getMission(),
                saveTodo.getNote(),
                saveTodo.getCreateDate(),
                saveTodo.getUser().getId());
    }

    @Transactional
    public void deleteTodo(Long id,Long userId){
        Todo todo = todoRepository.findByIdAndUser_Id(id,userId)
                .orElseThrow(() -> new ResourceNotFoundException("尚未登入或找不到該任務,請重新嘗試"));
        String missionName = todo.getMission();
        todoRepository.deleteById(id);
        log.info("成功刪除任務,userId: {},todoId: {},任務內容: {}",userId,id,missionName);
    }
}
