package com.anir.springdatajpamultipledatasources;

import com.anir.springdatajpamultipledatasources.todos.Todo;
import com.anir.springdatajpamultipledatasources.todos.TodoRepository;
import com.anir.springdatajpamultipledatasources.topics.Topic;
import com.anir.springdatajpamultipledatasources.topics.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.AbstractEnvironment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootApplication
public class SpringDataJpaMultipleDatasourcesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "multipledatasources");
        SpringApplication.run(SpringDataJpaMultipleDatasourcesApplication.class, args);
    }

    @Autowired
	TodoRepository todoRepository;
    @Autowired
	TopicRepository topicRepository;

    @Override
    public void run(String... args) throws Exception {
        Topic topic = new Topic();
        topic.setTitle("topic 2");
        topicRepository.save(topic);

        Todo todo = new Todo();
        todo.setTitle("todo 2");
        todoRepository.save(todo);

        System.out.println(topicRepository.findAll());
        System.out.println(todoRepository.findAll());
    }

    private static void extracted() {
        DataSource ds = DataSourceBuilder.create().url("jdbc:h2:~/todos").username("test").password("test").driverClassName("org.h2.Driver").build();
        try(
				Connection connection = ds.getConnection();
				PreparedStatement ps = connection.prepareStatement("SELECT * FROM TODO");
				ResultSet rs = ps.executeQuery();
        ) {
            while(rs.next()) {
                System.out.println(rs.getInt("ID") + " " + rs.getBoolean("COMPLETED") + " " + rs.getString("TITLE"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
