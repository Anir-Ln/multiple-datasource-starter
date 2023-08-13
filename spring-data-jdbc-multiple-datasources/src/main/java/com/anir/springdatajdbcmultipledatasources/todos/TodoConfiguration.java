package com.anir.springdatajdbcmultipledatasources.todos;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(
        basePackages = {"com.anir.springdatajdbcmultipledatasources.todos" },
        jdbcOperationsRef = "todoJdbcOperations",
        transactionManagerRef = "todoTransactionManager",
        dataAccessStrategyRef = "todoDataAccessStrategy"
)
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class, JdbcRepositoriesAutoConfiguration.class}
)
public class TodoConfiguration {
    @Bean
    @Qualifier("todo")
    public PlatformTransactionManager todoTransactionManager(
            @Qualifier("todo") DataSource todoDataSource) {
        return new JdbcTransactionManager(Objects.requireNonNull(todoDataSource));
    }

    @Bean
    @Qualifier("todo")
    public NamedParameterJdbcOperations todoJdbcOperations(@Qualifier("todo") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

//    @ConfigurationProperties("spring.datasource.todos")
//    public DataSourceProperties dataSourceProperties() {
//        return new DataSourceProperties();
//    }

    @Bean
    @Qualifier("todo")
//    @ConfigurationProperties("spring.datasource.todos.hikari")
    public DataSource todoDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:~/todos");
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        return dataSource;
//        return dataSourceProperties()
//                .initializeDataSourceBuilder()
//                .build();
    }


    @Bean
    @Qualifier("todo")
    public DataAccessStrategy todoDataAccessStrategy(
            @Qualifier("todo") NamedParameterJdbcOperations operations,
            JdbcConverter jdbcConverter,
            JdbcMappingContext context,
            Dialect dialect
    ) {
        return new DefaultDataAccessStrategy(
                new SqlGeneratorSource(context, jdbcConverter, dialect),
                context,
                jdbcConverter,
                operations,
                new SqlParametersFactory(context, jdbcConverter),
                new InsertStrategyFactory(operations, new BatchJdbcOperations(operations.getJdbcOperations()), dialect)
        );
    }


    @Bean
    public JdbcConverter jdbcConverter(
            JdbcMappingContext mappingContext,
            @Qualifier("todo") NamedParameterJdbcOperations operations,
            @Lazy @Qualifier("todo") RelationResolver relationResolver,
            JdbcCustomConversions conversions,
            Dialect dialect
    ) {
        DefaultJdbcTypeFactory jdbcTypeFactory = new DefaultJdbcTypeFactory(
                operations.getJdbcOperations());
        return new BasicJdbcConverter(mappingContext, relationResolver, conversions, jdbcTypeFactory,
                dialect.getIdentifierProcessing());
    }
}
