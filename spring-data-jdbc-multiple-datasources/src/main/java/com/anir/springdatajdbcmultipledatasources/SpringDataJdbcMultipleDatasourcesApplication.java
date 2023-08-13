package com.anir.springdatajdbcmultipledatasources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootApplication
public class SpringDataJdbcMultipleDatasourcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJdbcMultipleDatasourcesApplication.class, args);
    }

    private static void extracted() {
        DataSource ds = DataSourceBuilder.create().url("jdbc:h2:~/todos").username("test").password("test").driverClassName("org.h2.Driver").build();
        try (
                Connection connection = ds.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM TODO");
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                System.out.println(rs.getInt("ID") + " " + rs.getBoolean("COMPLETED") + " " + rs.getString("TITLE"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
