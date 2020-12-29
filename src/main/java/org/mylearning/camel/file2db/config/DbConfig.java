package org.mylearning.camel.file2db.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "datasource")
    public DataSource dataSource() {
        DataSource ds = DataSourceBuilder.create().build();
        return ds;
    }

}