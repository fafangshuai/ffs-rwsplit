package ffs.common.datasource;

/**
 * <p>标题: 自定义数据源保持类</p>
 * <p>描述: 设置和获取当前全局的数据源配置</p>
 * <p>版权: Copyright (c) 2016</p>
 * <p>公司: </p>
 *
 * @version: 1.0
 * @author: 法方帅
 */
public class CustomDataSourceHolder {

    private CustomDataSourceHolder() {}
    // 用来保存数据源
    private static final ThreadLocal<DataSource> holder = new ThreadLocal<DataSource>();

    /**
     * 设置数据源
     * @param dataSource 数据源注解对象
     */
    public static void setDataSource(DataSource dataSource) {
        holder.set(dataSource);
    }

    /**
     * 获取数据源
     * @return 数据源注解对象
     */
    public static DataSource getDataSource() {
        return holder.get();
    }

    /**
     * 清除保存的数据源
     */
    public static void clear() {
        holder.remove();
    }
}
