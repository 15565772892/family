package org.family.core.service;

/**
 * 数据同步服务接口
 */
public interface DataSyncService {

    /**
     * 启动数据同步（阻塞模式）
     */
    void startSync();

    /**
     * 停止数据同步
     */
    void stopSync();

    /**
     * 检查同步状态
     */
    boolean isRunning();
}
