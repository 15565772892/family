package org.family.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cdc.connectors.mysql.source.MySqlSource;
import org.apache.flink.cdc.connectors.mysql.table.StartupOptions;
import org.apache.flink.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.family.core.config.DataSyncConfig;
import org.springframework.stereotype.Service;

/**
 * MySQL CDC 到 ClickHouse 数据同步服务实现
 */
@Slf4j
@Service
public class DataSyncServiceImpl implements DataSyncService {

    private final DataSyncConfig config;

    private volatile StreamExecutionEnvironment env;
    private volatile boolean running = false;

    public DataSyncServiceImpl(DataSyncConfig config) {
        this.config = config;
        log.info("DataSyncServiceImpl 初始化完成");
        log.info("MySQL: {}:{}, Database: {}", getMysqlHostname(), getMysqlPort(), getMysqlDatabase());
        log.info("ClickHouse: {}:{}", getClickhouseHost(), getClickhousePort());
    }

    // 简化的 Getter 方法，绕过 Lombok 嵌套类问题
    private String getMysqlHostname() { return config.getMysql().getHostname(); }
    private Integer getMysqlPort() { return config.getMysql().getPort(); }
    private String getMysqlDatabase() { return config.getMysql().getDatabase(); }
    private String getMysqlUsername() { return config.getMysql().getUsername(); }
    private String getMysqlPassword() { return config.getMysql().getPassword(); }
    private String getMysqlTables() { return config.getMysql().getTables(); }

    private String getClickhouseHost() { return config.getClickhouse().getHost(); }
    private Integer getClickhousePort() { return config.getClickhouse().getPort(); }
    private String getClickhouseJdbcUrl() { return config.getClickhouse().getJdbcUrl(); }
    private String getClickhouseUsername() { return config.getClickhouse().getUsername(); }
    private String getClickhousePassword() { return config.getClickhouse().getPassword(); }
    private String getClickhouseDatabase() { return config.getClickhouse().getDatabase(); }

    @Override
    public void startSync() {
        if (running) {
            log.warn("数据同步已经在运行中");
            return;
        }

        try {
            log.info("开始启动 MySQL 到 ClickHouse 的数据同步...");

            // 1. 创建 Flink 执行环境
            env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.setParallelism(1);
            env.enableCheckpointing(5000);
            env.getCheckpointConfig().setMinPauseBetweenCheckpoints(3000);

            // 2. 构建表名列表
            String[] tableList = buildTableList();

            // 3. 配置 MySQL CDC Source
            MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                    .hostname(getMysqlHostname())
                    .port(getMysqlPort())
                    .databaseList(getMysqlDatabase())
                    .tableList(tableList)
                    .username(getMysqlUsername())
                    .password(getMysqlPassword())
                    .startupOptions(StartupOptions.initial())
                    .deserializer(new JsonDebeziumDeserializationSchema())
                    .build();

            // 4. 定义表结构（用于 ClickHouse）
            String createTableSql = buildCreateTableSql();
            executeCreateTable(createTableSql);

            // 5. 处理 CDC 数据并写入 ClickHouse
            env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL-CDC-Source")
                    .addSink(new org.family.core.sink.ClickHouseSink(
                            getClickhouseJdbcUrl(),
                            getClickhouseUsername(),
                            getClickhousePassword(),
                            getMysqlDatabase() + "." + getMysqlTables()
                    ));

            // 6. 执行任务
            env.execute("MySQL-to-ClickHouse-CDC-Sync");
            running = true;

            log.info("数据同步任务已启动");

        } catch (Exception e) {
            log.error("启动数据同步失败", e);
            throw new RuntimeException("启动数据同步失败", e);
        }
    }

    @Override
    public void stopSync() {
        if (env != null) {
            try {
                env.close();
                running = false;
                log.info("数据同步任务已停止");
            } catch (Exception e) {
                log.error("停止数据同步失败", e);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * 构建表名列表
     */
    private String[] buildTableList() {
        String tables = getMysqlTables();
        int idx = 0;
        String[] result = new String[0];
        for (String table : tables.split(",")) {
            String[] tmp = new String[idx + 1];
            System.arraycopy(result, 0, tmp, 0, idx);
            tmp[idx] = getMysqlDatabase() + "." + table.trim();
            result = tmp;
            idx++;
        }
        return result;
    }

    /**
     * 构建 ClickHouse 建表语句
     */
    private String buildCreateTableSql() {
        return String.format("""
                CREATE TABLE IF NOT EXISTS %s.%s (
                    id UInt64,
                    username String,
                    email String,
                    phone String,
                    status Int32,
                    createTime DateTime,
                    updateTime DateTime
                ) ENGINE = MergeTree()
                ORDER BY id
                SETTINGS index_granularity = 8192
                """,
                getClickhouseDatabase(),
                getMysqlTables()
        );
    }

    /**
     * 执行建表语句
     */
    private void executeCreateTable(String sql) {
        try (var conn = java.sql.DriverManager.getConnection(
                getClickhouseJdbcUrl(),
                getClickhouseUsername(),
                getClickhousePassword())) {
            try (var stmt = conn.createStatement()) {
                stmt.execute(sql);
                log.info("ClickHouse 表创建/检查完成");
            }
        } catch (Exception e) {
            log.warn("ClickHouse 建表失败，可能已存在：{}", e.getMessage());
        }
    }
}