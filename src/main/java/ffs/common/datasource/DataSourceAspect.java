package ffs.common.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <p>标题: 数据源切面</p>
 * <p>描述: 根据注解或方法名自动配置数据源</p>
 * <p>版权: Copyright (c) 2016</p>
 * <p>公司: </p>
 *
 * @version: 1.0
 * @author: 法方帅
 */
//@Component
@Aspect
public class DataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    /**
     * 处理dao层的方法，设置数据源
     *
     * @param pjp 切面的参数
     * @return 方法返回结果
     */
    @Around("daoPointCut()")
    public Object setDSForDao(ProceedingJoinPoint pjp) throws Throwable {
        // 如果当前没有设置数据源 或者 不保持当前数据源 则设置数据源
        if (CustomDataSourceHolder.getDataSource() == null || !CustomDataSourceHolder.getDataSource().isHold()) {
            setDataSource(pjp, true);
        }
        return pjp.proceed();
    }

    /**
     * 处理加了@DataSource注解的方法
     *
     * @param pjp 切面的参数
     * @return 方法返回结果
     */
    @Around("annotationPointCut()")
    public Object setDSForService(ProceedingJoinPoint pjp) throws Throwable {
        setDataSource(pjp, false);
        return process(pjp);
    }

    /**
     * 执行方法，释放数据源设置
     *
     * @param pjp 切入点参数
     * @return 方法返回结果
     * @throws Throwable
     */
    private Object process(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        try {
            result = pjp.proceed();
        } finally {
            // 方法执行完毕清楚当前数据源设置
            CustomDataSourceHolder.clear();
        }
        return result;
    }

    /**
     * 设置数据源
     *
     * @param jp     切入点参数
     * @param forDao 是否dao层方法
     */
    private void setDataSource(JoinPoint jp, boolean forDao) {
        String methodName = jp.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) jp.getSignature()).getMethod().getParameterTypes();
        Method m;
        boolean isReadMethod = false;
        try {
            if (forDao) { // 处理dao层方法，需要解析接口
                Class<?>[] interfaces = jp.getTarget().getClass().getInterfaces();
                // 根据方法名前缀判断是否是读方法
                isReadMethod = methodName.matches("(get|find|is).*");
                m = interfaces[0].getMethod(methodName, parameterTypes);
            } else { // 非dao层方法，直接解析类
                m = jp.getTarget().getClass().getMethod(methodName, parameterTypes);
            }
            if (m != null) {
                DataSource dataSource;
                if (m.isAnnotationPresent(DataSource.class)) { // 如果设置了注解则使用注解设置的数据源
                    dataSource = m.getAnnotation(DataSource.class);
                    // dao层不能保持数据源
                    if (forDao) {
                        dataSource = newDataSourceInstance(dataSource.value(), false);
                    }
                } else if (isReadMethod) { // 为读操作设置slave数据源
                    dataSource = newDataSourceInstance(DataSource.Type.SLAVE, false);
                } else { // 否则使用默认数据源
                    dataSource = newDataSourceInstance(DataSource.Type.MASTER, false);
                }
                CustomDataSourceHolder.setDataSource(dataSource);
                logger.info("数据源切换到: " + dataSource.value());
            }
        } catch (Exception e) {
            logger.error("设置数据源出错", e);
        }
    }

    /**
     * 创建数据源注解实例
     *
     * @param value  数据源类型
     * @param isHold 是否保持当前数据源
     * @return 注解实例
     */
    private DataSource newDataSourceInstance(final DataSource.Type value, final boolean isHold) {
        return new DataSource() {
            @Override
            public Type value() {
                return value;
            }

            @Override
            public boolean isHold() {
                return isHold;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return DataSource.class;
            }
        };
    }

    /**
     * 切入点：dao层的方法
     */
    @Pointcut("execution(* ffs.platform.dao.*.*(..))")
    private void daoPointCut() {

    }

    /**
     * 切入点：数据源注解
     */
    @Pointcut("@annotation(DataSource)")
    private void annotationPointCut() {

    }
}
