package com.anir.springdatajdbcmultipledatasources;

import com.anir.springdatajdbcmultipledatasources.todos.Todo;
import com.anir.springdatajdbcmultipledatasources.todos.TodoRepository;
import com.anir.springdatajdbcmultipledatasources.topics.Topic;
import com.anir.springdatajdbcmultipledatasources.topics.TopicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoriesTests {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TodoRepository todoRepository;

    @Test
    void testTodoRepository() {
        Todo todo = new Todo();
        todo.setTitle("todo 4");
        todoRepository.save(todo);
    }

    @Test
    void testTopicRepository() {
        Topic topic = new Topic();
        topic.setTitle("topic 4");
        topicRepository.save(topic);
    }
}
