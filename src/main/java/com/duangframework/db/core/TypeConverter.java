package com.duangframework.db.core;

import com.duangframework.db.annotation.Param;
import com.duangframework.db.annotation.Persist;
import com.duangframework.db.core.converters.Converter;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.DBObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类型转换基类
 *
 * @author Laotang
 */
public abstract class TypeConverter implements IDataConverter {

    private List<Class> supportedTypes;

    protected TypeConverter() {
    }

    protected TypeConverter(final Class... types) {
        supportedTypes = copy(types);
    }

    /**
     * 取字段名
     * @param field 字段
     * @return  字段名
     */
    protected String getName(Field field) {
        return ToolsKit.getFieldName(field);
    }

    private List<Class> copy(final Class[] array) {
        if(ToolsKit.isEmpty(array)) {
            return null;
        }
        List<Class> classList = new ArrayList<Class>(array.length);
        Class[] classArray = Arrays.copyOf(array, array.length);
        for(Class cls : classArray) {
            classList.add(cls);
        }
        return classList;
    }

    final List<Class> getSupportedTypes() {
        return supportedTypes;
    }

    /**
     * 解码
     * @param object
     * @param dbObject
     * @return
     */
    public abstract Object decode(Object object, DBObject dbObject) throws DbException;

    /**
     *  编码
     */
    public abstract Converter encode(Field field, Object value) throws DbException;
}
