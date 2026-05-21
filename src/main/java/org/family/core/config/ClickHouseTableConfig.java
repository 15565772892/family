package org.family.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClickHouse SQL 建表语句配置
 */
@Component
@ConfigurationProperties(prefix = "data.sync.clickhouse.sql")
@Data
public class ClickHouseTableConfig {

    /**
     * 用户表 DDL 语句
     */
    private String createUserTable = """
            CREATE TABLE IF NOT EXISTS ${database}.${table} (
                id UInt64,
                username String,
                email String,
                phone String,
                status Int32 DEFAULT 1,
                createTime DateTime,
                updateTime DateTime
            ) ENGINE = MergeTree()
            ORDER BY id
            SETTINGS index_granularity = 8192
            """;
}