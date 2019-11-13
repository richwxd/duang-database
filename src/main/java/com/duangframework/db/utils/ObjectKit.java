package com.duangframework.db.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
