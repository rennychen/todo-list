package com.github.renny.todolist.service;

import com.github.renny.todolist.modules.todo.dto.response.CreateTodoResponse;
import com.github.renny.todolist.modules.todo.entity.Todo;
import com.github.renny.todolist.modules.todo.service.TodoService;
import com.github.renny.todolist.modules.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private TodoService todoService;

    @Test
    @DisplayName("createTodo Happy-Path")
    void createTodo_Success(){
        String mission = "代辦任務測試";
        String note = "備註測試";

        Todo todo = Todo.builder()
                .id(25L)
                .completed(false)
                .mission(mission)
                .note(note)
                .createDate(LocalDate.now())
                .builder();

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        CreateTodoResponse response = todoService.createTodo(mission,note);

        assertNotNull(response);
        assertEquals(25L,response.getId());
        assertEquals(mission,response.getMission());
        assertEquals(note,response.getNote());
        assertFalse(response.getComplete());

        verify(todoRepository,times(1)).save(any(Todo.class));

    }

}