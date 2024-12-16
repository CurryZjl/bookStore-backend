package com.example.bookstore.mainService.config;

import org.neo4j.driver.Driver;
import org.neo4j.driver.internal.SessionFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = "com.example.bookstore.mainService.neo4jrepo") // 指定 Neo4j Repository 的包路径
//@EntityScan(basePackages = "com.example.bookstore.mainService.entity")
public class Neo4jConfig {
    @Bean("neo4jTemplate")
    public Neo4jTemplate neo4jTemplate(
            Neo4jClient neo4jClient,
            Neo4jMappingContext neo4jMappingContext,
            Driver driver,
            DatabaseSelectionProvider databaseNameProvider) {

        // 手动创建并配置 Neo4jTransactionManager
        Neo4jTransactionManager transactionManager = new Neo4jTransactionManager(driver, databaseNameProvider);

        // 返回一个绑定到 Neo4jTransactionManager 的 Neo4jTemplate
        return new Neo4jTemplate(neo4jClient, neo4jMappingContext, transactionManager);
    }

    // 1. 此处为了修改默认事务，必须改。加载了Neo4J依赖库之后，transactionManager变成Neo4jTransactionManager，不增加此处，启动会报错，Mysql无法使用。
    @Bean("transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    // 2. Neo4J的事务管理
//    @Bean("neo4jTransactionManager")
//    public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory) {
//        return new Neo4jTransactionManager(sessionFactory);
//    }
}
