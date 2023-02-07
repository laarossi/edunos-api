package com.api.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {
    @Resource
    Environment env;

    @Bean
    public DataSource dataSource(){
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(env.getProperty("datasource.driver"));
        dataSourceBuilder.url(env.getProperty("datasource.url"));
        dataSourceBuilder.username(env.getProperty("datasource.username"));
        dataSourceBuilder.password(env.getProperty("datasource.password"));
        return dataSourceBuilder.build();
    }

    @Bean
    @RequestScope
    public Connection connection(@Autowired DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

}
