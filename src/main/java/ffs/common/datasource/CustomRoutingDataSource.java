package ffs.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * <p>标题: 数据源路由</p>
 * <p>描述: 重写determineCurrentLookupKey方法设置数据源</p>
 * <p>版权: Copyright (c) 2016</p>
 * <p>公司: </p>
 *
 * @version: 1.0
 * @author: 法方帅
 */
public class CustomRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        DataSource dataSource = CustomDataSourceHolder.getDataSource();
        // 如果数据源设置为空 或则 类型为主库
        if (dataSource == null || dataSource.value() == DataSource.Type.MASTER) {
            return "master";
        }
        return "slave";
    }
}
