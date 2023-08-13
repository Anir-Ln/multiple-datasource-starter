package com.anir.springdatajdbcmultipledatasources.topics;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.H2Dialect;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
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
        basePackageClasses = Topic.class,
        basePackages = {"com.anir.springdatajdbcmultipledatasources.topics" },
        jdbcOperationsRef = "topicJdbcOperations",
        transactionManagerRef = "topicJdbcTransactionManager",
        dataAccessStrategyRef = "topicDataAccessStrategy"
)
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class, JdbcRepositoriesAutoConfiguration.class}
)
public class TopicConfiguration {
    @Bean
    @Qualifier("topic")
    public PlatformTransactionManager topicJdbcTransactionManager(
            @Qualifier("topic") DataSource topicsDataSource) {
        return new JdbcTransactionManager(Objects.requireNonNull(topicsDataSource));
    }

    @Bean
    @Qualifier("topic")
    public NamedParameterJdbcOperations topicJdbcOperations(@Qualifier("topic") DataSource topicsDataSource) {
        return new NamedParameterJdbcTemplate(topicsDataSource);
    }

//    @ConfigurationProperties("spring.datasource.topics")
//    public DataSourceProperties topicsDataSourceProperties() {
//        return new DataSourceProperties();
//    }

    @Bean
    @Qualifier("topic")
    public DataSource topicDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:~/topics");
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        return dataSource;
    }


    @Bean
    @Qualifier("topic")
    public DataAccessStrategy topicDataAccessStrategy(
            @Qualifier("topicJdbcOperations") NamedParameterJdbcOperations operations,
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

//    @Bean
//    public Dialect dialect() {
//        return H2Dialect.INSTANCE;
//    }
//
//    @Bean
//    @Primary
//    public JdbcConverter jdbcConverter(
//            JdbcMappingContext mappingContext,
//            @Qualifier("todoJdbcOperations") NamedParameterJdbcOperations operations,
//            @Lazy @Qualifier("todoDataAccessStrategy") RelationResolver relationResolver,
//            JdbcCustomConversions conversions
//    ) {
//        DefaultJdbcTypeFactory jdbcTypeFactory = new DefaultJdbcTypeFactory(
//                operations.getJdbcOperations());
//        Dialect dialect = H2Dialect.INSTANCE;
//        return new BasicJdbcConverter(mappingContext, relationResolver, conversions, jdbcTypeFactory,
//                dialect.getIdentifierProcessing());
//    }

}
