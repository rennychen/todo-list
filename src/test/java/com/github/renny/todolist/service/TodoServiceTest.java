package com.github.renny.todolist.service;

import com.github.renny.todolist.common.exception.ResourceNotFoundException;
import com.github.renny.todolist.modules.todo.dto.response.CreateTodoResponse;
import com.github.renny.todolist.modules.todo.dto.response.ReadTodoResponse;
import com.github.renny.todolist.modules.todo.entity.Todo;
import com.github.renny.todolist.modules.todo.service.TodoService;
import com.github.renny.todolist.modules.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                .build();

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        CreateTodoResponse response = todoService.createTodo(mission,note);

        assertNotNull(response);
        assertEquals(25L,response.getId());
        assertEquals(mission,response.getMission());
        assertEquals(note,response.getNote());
        assertFalse(response.getComplete());

        verify(todoRepository,times(1)).save(any(Todo.class));

    }

    @Test
    @DisplayName("readTodo Happy-Path")
    void readTodo_success(){
        Long id = 2L;

        Todo mockedTodo = Todo.builder()
                .id(2L)
                .createDate(LocalDate.of(2026,5,21))
                .mission("buy milk")
                .note("2026/5/25")
                .completed(false)
                .build();

        when(todoRepository.findById(2L)).thenReturn(Optional.of(mockedTodo));
        ReadTodoResponse response = todoService.readTodo(id);

        assertNotNull(response);
        assertFalse(response.getComplete());
        assertEquals("buy milk",response.getMission());
        assertEquals("2026/5/25",response.getNote());
        assertEquals(LocalDate.of(2026,5,21),response.getCreateDate());

        verify(todoRepository,times(1)).findById(2L);

    }

    @Test
    @DisplayName("readTodo Sad-Path:當 ID 不存在時應拋出 ResourceNotFoundException")
    void readTodo_EmptyId_ThrowException(){
        Long id = 99L;

        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> { todoService.readTodo(id); } );
    }

    @Test
    @DisplayName("updateTodoStatus Happy-Path")
    void updateTodoStatus_success(){
        Long id = 5L;

        Todo mockedTodo = Todo.builder()
                .createDate(LocalDate.of(2026,5,28))
                .note(null)
                .mission("happy path test")
                .id(5L)
                .completed(false)
                .build();

        when(todoRepository.findById(5L)).thenReturn(Optional.of(mockedTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(mockedTodo);


        todoService.updateTodoStatus(id);

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
        verify(todoRepository).save(captor.capture());
        assertTrue(captor.getValue().getCompleted());

        verify(todoRepository,times(1)).findById(5L);
    }

    @Test
    @DisplayName("updateTodo Sad-Path:當 ID 不存在時應拋出 ResourceNotFoundException")
    void updateTodoStatus_EmptyId_ThrowException(){
        Long id = 5L;

        when(todoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> { todoService.updateTodoStatus(id); });
    }

}