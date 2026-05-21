package org.family.core.parser;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.family.core.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Debezium JSON 解析器
 * 用于解析 MySQL CDC 产生的 Debezium 格式 JSON 数据
 */
@Slf4j
public class DebeziumJsonParser {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * 解析 Debezium JSON 为 UserDto
     *
     * @param json Debezium 格式 JSON
     * @return UserDto 对象
     */
    public UserDto parse(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            JSONObject root = JSON.parseObject(json);

            // 获取操作类型
            String opType = getOpType(root);

            // DELETE 操作只有 after 有数据，INSERT/UPDATE 两者都有
            JSONObject after = root.getJSONObject("after");
            JSONObject before = root.getJSONObject("before");

            if (after == null && before != null) {
                after = before;
            }

            if (after == null) {
                log.warn("CDC 事件没有 after 或 before 数据：{}", json);
                return null;
            }

            return JSONObject.parseObject(after.toString(),UserDto.class);

        } catch (Exception e) {
            log.error("解析 CDC JSON 失败：{}", json, e);
            throw new RuntimeException("解析 CDC JSON 失败", e);
        }
    }

    /**
     * 获取操作类型
     */
    private String getOpType(JSONObject root) {
        String op = root.getString("op");
        if ("d".equals(op)) {
            return "DELETE";
        } else if ("u".equals(op)) {
            return "UPDATE";
        } else if ("c".equals(op)) {
            return "INSERT";
        }
        return op != null ? op.toUpperCase() : "UNKNOWN";
    }

    /**
     * 从 Map 中获取 Long 值
     */
    private Long getLong(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 从 Map 中获取 String 值
     */
    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 获取并格式化状态值
     */
    private Integer getStatus(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            return 1; // 默认启用
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    /**
     * 解析时间字段
     */
    private LocalDateTime parseTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        String str = value.toString();
        try {
            return LocalDateTime.parse(str, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            // 尝试去除毫秒部分
            int dotIndex = str.indexOf('.');
            if (dotIndex > 0) {
                str = str.substring(0, dotIndex);
            }
            try {
                return LocalDateTime.parse(str, DATE_TIME_FORMATTER);
            } catch (Exception e2) {
                log.warn("无法解析时间：{}", value);
                return null;
            }
        }
    }
}