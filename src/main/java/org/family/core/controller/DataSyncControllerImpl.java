package org.family.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.family.core.service.DataSyncService;
import org.springframework.stereotype.Controller;

/**
 * 数据同步控制器实现
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class DataSyncControllerImpl implements DataSyncController {

    private final DataSyncService dataSyncService;

    @Override
    public String syncData() {
        try {
            dataSyncService.startSync();
            return "{\"success\":true,\"message\":\"数据同步任务已启动\"}";
        } catch (Exception e) {
            log.error("启动数据同步失败", e);
            return "{\"success\":false,\"message\":\"启动失败：" + e.getMessage() + "\"}";
        }
    }

    @Override
    public String stopSync() {
        try {
            dataSyncService.stopSync();
            return "{\"success\":true,\"message\":\"数据同步任务已停止\"}";
        } catch (Exception e) {
            log.error("停止数据同步失败", e);
            return "{\"success\":false,\"message\":\"停止失败：" + e.getMessage() + "\"}";
        }
    }

    @Override
    public SyncStatus getSyncStatus() {
        boolean running = dataSyncService.isRunning();
        return new SyncStatus(
                running,
                running ? "同步任务正在运行" : "同步任务未启动"
        );
    }
}
