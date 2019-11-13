package com.duangframework.db.core;

import com.duangframework.db.core.converters.Converter;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.DBObject;
import org.bson.Document;

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
     *设置字段值
     * @param entityObj 实体类对象
     * @param field 字段属性
     * @param value 值
     */
    protected void setFieldValue(Object entityObj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(entityObj, value);
        } catch (IllegalAccessException iae) {
            throw new DbException(iae.getMessage());
        }
    }

    /**
     * 转换值对象
     * @param field 字段属性
     * @param valueObj 值
     * @return
     */
    protected Object convertValueObj(Field field, Object valueObj) {
        if(ToolsKit.isEmpty(field) || ToolsKit.isEmpty(valueObj)) {
            throw new DbException("转换值对象时，Field或valueObj不能为空");
        }
        if (valueObj instanceof  DBObject) {
            return ((DBObject)valueObj).get(getName(field));
        }
        else if (valueObj instanceof Document) {
            return ((Document)valueObj).get(getName(field));
        }
        else {
            return valueObj;
        }
    }

    /**
     * 解码
     * @param field 字符属性
     * @param valueObj 值对象
     * @return
     */
    public abstract Converter decode(Field field, Object valueObj) throws DbException;

    /**
     *  编码
     *
     * @param field entity field对象
     * @param value entity field value
     * @return  Converter
     */
    public abstract Converter encode(Field field, Object value) throws DbException;
}
