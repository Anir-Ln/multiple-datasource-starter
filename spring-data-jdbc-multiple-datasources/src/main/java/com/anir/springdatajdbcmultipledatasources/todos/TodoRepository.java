package com.anir.springdatajdbcmultipledatasources.todos;

import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {
}
