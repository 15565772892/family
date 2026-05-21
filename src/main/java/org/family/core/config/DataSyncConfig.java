package org.family.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据同步配置类
 */
@Configuration
@ConfigurationProperties(prefix = "data.sync")
@Data
public class DataSyncConfig {

    /**
     * MySQL 配置
     */
    private MySql mysql = new MySql();

    /**
     * ClickHouse 配置
     */
    private ClickHouse clickhouse = new ClickHouse();

    // Explicit getters for the main config class
    public MySql getMysql() { return mysql; }
    public void setMysql(MySql mysql) { this.mysql = mysql; }
    public ClickHouse getClickhouse() { return clickhouse; }
    public void setClickhouse(ClickHouse clickhouse) { this.clickhouse = clickhouse; }

    @Data
    public static class MySql {
        /**
         * 主机地址
         */
        private String hostname = "localhost";
        /**
         * 端口
         */
        private Integer port = 3306;
        /**
         * 数据库名
         */
        private String database = "test_db";
        /**
         * 用户名
         */
        private String username = "root";
        /**
         * 密码
         */
        private String password = "password";
        /**
         * 需要同步的表（逗号分隔）
         */
        private String tables = "users";

        // Explicit getters
        public String getHostname() { return hostname; }
        public void setHostname(String hostname) { this.hostname = hostname; }
        public Integer getPort() { return port; }
        public void setPort(Integer port) { this.port = port; }
        public String getDatabase() { return database; }
        public void setDatabase(String database) { this.database = database; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getTables() { return tables; }
        public void setTables(String tables) { this.tables = tables; }
    }

    @Data
    public static class ClickHouse {
        /**
         * JDBC 地址
         */
        private String jdbcUrl = "jdbc:clickhouse://localhost:8123/default";
        /**
         * 主机地址
         */
        private String host = "localhost";
        /**
         * 端口
         */
        private Integer port = 8123;
        /**
         * 数据库名
         */
        private String database = "default";
        /**
         * 用户名
         */
        private String username = "default";
        /**
         * 密码
         */
        private String password = "";
        /**
         * HTTP 端口
         */
        private Integer httpPort = 8123;

        // Explicit getters
        public String getJdbcUrl() { return jdbcUrl; }
        public void setJdbcUrl(String jdbcUrl) { this.jdbcUrl = jdbcUrl; }
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public Integer getPort() { return port; }
        public void setPort(Integer port) { this.port = port; }
        public String getDatabase() { return database; }
        public void setDatabase(String database) { this.database = database; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Integer getHttpPort() { return httpPort; }
        public void setHttpPort(Integer httpPort) { this.httpPort = httpPort; }
    }
}