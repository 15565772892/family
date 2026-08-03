package org.family.core.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * Spring 提供的抽象类，它内部维护了一个 Map<Object, DataSource>。每次获取连接时，它会调用 determineCurrentLookupKey()
 * 方法来决定从 Map 里取哪个数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }

}
