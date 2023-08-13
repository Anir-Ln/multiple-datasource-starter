package com.anir.springdatajdbcmultipledatasources.todos;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table
public class Todo {

    @Id
    private Long id;
    private String title;

    public Todo() {
    }

    public Todo(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
