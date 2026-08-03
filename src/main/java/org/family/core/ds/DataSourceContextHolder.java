package org.family.core.ds;

/**
 * 数据源切换:当前线程中保存和获取“当前应该使用哪个数据源”
 */
public class DataSourceContextHolder {

    public static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }

}
