package com.github.renny.todolist.modules.todo.repository;

import com.github.renny.todolist.modules.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findByUser_Id(Long userId);
    Optional<Todo> findByIdAndUser_Id(Long todoId,Long userId);
}
