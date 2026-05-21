package org.family.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 数据同步控制器
 */
@Tag(name = "数据同步", description = "MySQL CDC 实时数据同步到 ClickHouse")
interface DataSyncController {

    @PostMapping(path = "sync/sync", name = "启动数据同步", produces = "application/json")
    @Operation(summary = "启动 MySQL 到 ClickHouse 的实时数据同步",
               description = "使用 Flink CDC 将 MySQL 数据库的变更实时同步到 ClickHouse")
    @ApiResponse(responseCode = "200", description = "同步任务已启动")
    String syncData();

    @DeleteMapping(path = "sync/stop", name = "停止数据同步", produces = "application/json")
    @Operation(summary = "停止数据同步",
               description = "停止正在运行的 Flink CDC 同步任务")
    @ApiResponse(responseCode = "200", description = "同步任务已停止")
    String stopSync();

    @GetMapping(path = "sync/status", name = "同步状态", produces = "application/json")
    @Operation(summary = "获取同步状态",
               description = "查询当前数据同步任务的运行状态")
    @ApiResponse(responseCode = "200", description = "同步状态信息")
    SyncStatus getSyncStatus();
}

/**
 * 同步状态响应对象
 */
class SyncStatus {
    private boolean running;
    private String message;
    private long timestamp;

    public SyncStatus(boolean running, String message) {
        this.running = running;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
