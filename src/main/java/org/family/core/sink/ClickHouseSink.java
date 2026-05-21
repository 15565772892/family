package org.family.core.sink;

import lombok.extern.log4j.Log4j2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClickHouse Sink - 使用 JDBC 批量写入
 */
public class ClickHouseSink extends RichSinkFunction<String> {

    private static final Logger log = LoggerFactory.getLogger(ClickHouseSink.class);

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String tableName;

    public ClickHouseSink(String jdbcUrl, String username, String password, String tableName) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        log.info("ClickHouse Sink 初始化成功：{}", jdbcUrl);
    }

    @Override
    public void invoke(String value, Context context) throws Exception {
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        try {
            // 将 JSON 格式的数据转换为 INSERT SQL
            String sql = buildInsertSql(value);

            // 批量执行插入（Flink 会处理并发）
            try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
                try (var stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    log.debug("写入 ClickHouse: {}", sql.substring(0, Math.min(100, sql.length())));
                }
            }
        } catch (Exception e) {
            log.error("写入 ClickHouse 失败：{}", value, e);
            throw new RuntimeException("写入 ClickHouse 失败", e);
        }
    }

    /**
     * 构建 INSERT SQL
     */
    private String buildInsertSql(String json) {
        // 解析 JSON 并构建 INSERT 语句
        // 这里假设 JSON 格式为：{"after":{"id":1,"username":"test","email":"test@example.com"...}}
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName).append(" VALUES (");

        // 从 JSON 中提取值（简化版本，实际应使用 JSON 解析库）
        try {
            java.util.Map<String, Object> data = parseJson(json);
            List<String> values = extractValues(data);
            sb.append(String.join(",", values));
        } catch (Exception e) {
            log.error("解析 JSON 失败：{}", json);
            sb.append("'").append(json.replace("'", "''")).append("'");
        }

        sb.append(")");
        return sb.toString();
    }

    /**
     * 简单的 JSON 解析（生产环境建议使用 Jackson 或 Fastjson）
     */
    private java.util.Map<String, Object> parseJson(String json) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();

        // 提取 after 部分
        String afterMatch = extractAfter(json);
        if (afterMatch != null) {
            // 解析字段
            parseFields(afterMatch, result);
        } else {
            parseFields(json, result);
        }

        return result;
    }

    private String extractAfter(String json) {
        int startIndex = json.indexOf("\"after\":");
        if (startIndex != -1) {
            int braceCount = 0;
            int startBrace = -1;
            for (int i = startIndex + 8; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '{') {
                    if (startBrace == -1) {
                        startBrace = i;
                    }
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0 && startBrace != -1) {
                        return json.substring(startBrace, i + 1);
                    }
                }
            }
        }
        return null;
    }

    private void parseFields(String json, java.util.Map<String, Object> result) {
        // 简单解析 JSON 字段
        String[] pairs = json.substring(1, json.length() - 1).split(",(?=(?:[^\"']*[\"'][^\"']*[\"'])*(?![^\"']*[\"']))");
        for (String pair : pairs) {
            int colonIndex = pair.indexOf(':');
            if (colonIndex > 0) {
                String key = pair.substring(1, colonIndex - 1).trim();
                String value = pair.substring(colonIndex + 1).trim();

                // 去除引号
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                result.put(key, value);
            }
        }
    }

    private List<String> extractValues(java.util.Map<String, Object> data) {
        List<String> values = new ArrayList<>();

        Long id = (Long) data.get("id");
        String username = (String) data.get("username");
        String email = (String) data.get("email");
        String phone = (String) data.get("phone");
        Integer status = (Integer) data.get("status");
        LocalDateTime createTime = null;
        LocalDateTime updateTime = null;

        // 尝试解析时间
        Object createTimeObj = data.get("createTime");
        if (createTimeObj instanceof String) {
            createTime = LocalDateTime.parse((String) createTimeObj);
        }

        Object updateTimeObj = data.get("updateTime");
        if (updateTimeObj instanceof String) {
            updateTime = LocalDateTime.parse((String) updateTimeObj);
        }

        values.add(id != null ? id.toString() : "NULL");
        values.add("'" + escapeSql(username) + "'");
        values.add("'" + escapeSql(email) + "'");
        values.add("'" + escapeSql(phone) + "'");
        values.add(status != null ? status.toString() : "NULL");
        values.add(createTime != null ? "'" + createTime.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "'" : "NULL");
        values.add(updateTime != null ? "'" + updateTime.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "'" : "NULL");

        return values;
    }

    private String escapeSql(String value) {
        return value == null ? "" : value.replace("'", "''");
    }

    @Override
    public void close() throws Exception {
        log.info("ClickHouse Sink 关闭");
        super.close();
    }
}