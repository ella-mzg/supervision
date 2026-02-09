package com.sdv.backend.service;

import com.sdv.backend.dto.CreateTodoRequest;
import com.sdv.backend.dto.TodoDto;
import com.sdv.backend.dto.UpdateTodoRequest;
import com.sdv.backend.entity.Todo;
import com.sdv.backend.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {

    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<TodoDto> list() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public TodoDto get(Long id) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));
        return toDto(todo);
    }

    public TodoDto create(CreateTodoRequest request) {
        Todo saved = repository.save(new Todo(request.title()));
        return toDto(saved);
    }

    public TodoDto update(Long id, UpdateTodoRequest request) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));

        if (request.title() != null) todo.setTitle(request.title());
        if (request.done() != null) todo.setDone(request.done());

        return toDto(repository.save(todo));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Todo not found: " + id);
        }
        repository.deleteById(id);
    }

    private TodoDto toDto(Todo todo) {
        return new TodoDto(todo.getId(), todo.getTitle(), todo.isDone());
    }
}
