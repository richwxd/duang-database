package com.duangframework.db.vtor.utils;

import com.duangframework.db.annotation.Bean;
import com.duangframework.db.annotation.Entity;
import com.duangframework.db.entity.IdEntity;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.core.VtorFactory;

import java.util.Collection;
import java.util.Map;

/**
 * 使用自定义的注解来进行验证
 * Created by laotang on 2018/6/29.
 */
public class VtorKit {

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static <T> T validate(T obj) throws ValidatorException {
        if(null == obj) {
            throw new NullPointerException("validate object is fail : obj is null");
        }
        if(obj instanceof IdEntity || obj instanceof java.io.Serializable || obj.getClass().isAnnotationPresent(Bean.class) || obj.getClass().isAnnotationPresent(Entity.class)) {
            VtorFactory.duang().validator(obj);
        } else if(obj instanceof Map){
            VtorFactory.duang().validator((Map)obj);
        } else if(obj instanceof Collection){
            VtorFactory.duang().validator((Collection)obj);
        } else {
            throw new ValidatorException("框架暂对该对象不支持注解验证，请注意对象或集合元素是否实现java.io.Serializable接口及设置了@Bean或@Entity注解");
        }
        return (T)obj;
    }
}
