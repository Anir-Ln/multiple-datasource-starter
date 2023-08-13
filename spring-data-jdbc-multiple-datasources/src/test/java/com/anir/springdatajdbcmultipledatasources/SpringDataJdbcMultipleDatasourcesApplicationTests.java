package com.anir.springdatajdbcmultipledatasources;

import com.anir.springdatajdbcmultipledatasources.todos.Todo;
import com.anir.springdatajdbcmultipledatasources.todos.TodoRepository;
import com.anir.springdatajdbcmultipledatasources.topics.Topic;
import com.anir.springdatajdbcmultipledatasources.topics.TopicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
class SpringDataJdbcMultipleDatasourcesApplicationTests {


    @Test
    void contextLoads() {
    }

    @Test
    void testTodosDB() {
        ResultSet rs = dbQuery("jdbc:h2:~/todos", "select * from TODO");
    }

    @Test
    void testTopicsDB() {
        ResultSet rs = dbQuery("jdbc:h2:~/topics", "select * from TOPIC");
    }



    private ResultSet dbQuery(final String URL, final String SQL) {
        DataSource ds = DataSourceBuilder.create().url(URL)
                .username("test")
                .password("test")
                .driverClassName("org.h2.Driver")
                .build();

        try (
                Connection connection = ds.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
        ) {
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
