package ffs.common.datasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>标题: 指定数据源注解</p>
 * <p>描述: 用来指定某个方法的数据源</p>
 * <p>版权: Copyright (c) 2016</p>
 * <p>公司: </p>
 *
 * @version: 1.0
 * @author: 法方帅
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSource {
    /**
     * 设置数据源类型
     * @see Type
     */
    Type value() default Type.MASTER;

    /**
     * 是否保持当前数据源，服务层使用保证整个方法内使用同一个数据源
     */
    boolean isHold() default false;

    /**
     * 数据源类型：主、从
     */
    enum Type {
        MASTER, SLAVE
    }
}
