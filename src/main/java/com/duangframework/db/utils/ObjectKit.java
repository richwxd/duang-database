package com.duangframework.db.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 对象工具
 *
 * @author Laotang
 */
public final class ObjectKit {

    private static final Logger logger = LoggerFactory.getLogger(ObjectKit.class);

    private ObjectKit(){

    }

    /**
     * 通过反射创建实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> commandClass) {
        T instance;
        try {
            instance = (T) commandClass.newInstance();
        } catch (Exception e) {
            logger.error("创建实例出错！", e);
            throw new RuntimeException(e);
        }
        return instance;
    }


    /**
     * 获取成员变量
     * @param  obj 对象
     * @aram field  变量字段
     */
    public static Object getFieldValue(Object obj, Field field) {
        Object propertyValue = null;
        try {
            field.setAccessible(true);
            propertyValue = field.get(obj);
        } catch (Exception e) {
            logger.error("获取成员变量出错！", e);
            throw new RuntimeException(e);
        }
        return propertyValue;
    }



}
