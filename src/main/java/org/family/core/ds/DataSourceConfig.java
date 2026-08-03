package org.family.core.ds;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.liyz.jdbc-url}")
    private String urlLiyz;
    @Value("${spring.datasource.liyz.username}")
    private String usernameLiyz;
    @Value("${spring.datasource.liyz.password}")
    private String passwordLiyz;
    @Value("${spring.datasource.liyz.driver-class-name}")
    private String driverClassNameLiyz;

    @Value("${spring.datasource.clickhouse.jdbc-url}")
    private String urlClickhouse;
    @Value("${spring.datasource.clickhouse.username}")
    private String usernameClickhouse;
    @Value("${spring.datasource.clickhouse.password}")
    private String passwordClickhouse;
    @Value("${spring.datasource.clickhouse.driver-class-name}")
    private String driverClassNameClickhouse;

    @Bean
    //@Qualifier("dataSourceLiyz")
    @ConfigurationProperties(prefix = "spring.datasource.liyz")
    public DataSource dataSourceLiyz() {
        HikariDataSource dataSource = DataSourceBuilder.create()
                .url(urlLiyz)
                .username(usernameLiyz)
                .password(passwordLiyz)
                .driverClassName(driverClassNameLiyz)
                .type(HikariDataSource.class).build();
        return dataSource;
    }

    @Bean
    //@Qualifier("dataSourceClickhouse")
    @ConfigurationProperties(prefix = "spring.datasource.clickhouse")
    public DataSource dataSourceClickhouse() {
        HikariDataSource dataSource = DataSourceBuilder.create()
                .url(urlClickhouse)
                .username(usernameClickhouse)
                .password(passwordClickhouse)
                .driverClassName(driverClassNameClickhouse)
                .type(HikariDataSource.class).build();
        return dataSource;
    }


    @Bean
    @Qualifier("multipleDataSource")
    public DynamicDataSource dynamicDataSource(DataSource dataSourceLiyz
            ,DataSource dataSourceClickhouse) {

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(dataSourceLiyz);

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("dataSourceLiyz", dataSourceLiyz);
        dataSourceMap.put("dataSourceClickhouse", dataSourceClickhouse);

        dynamicDataSource.setTargetDataSources(dataSourceMap);

        return dynamicDataSource;
    }

}
