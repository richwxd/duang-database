package com.duangframework.db.core;

import com.duangframework.db.core.converters.Converter;
import com.duangframework.db.utils.DataType;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.DBObject;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
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

    protected static final Logger logger = LoggerFactory.getLogger(TypeConverter.class);

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

    protected Object convertValueObj(Object valueObj) {
        if(ToolsKit.isEmpty(valueObj)) {
            throw new DbException("转换值对象时，valueObj不能为空");
        }
        if (valueObj instanceof  DBObject) {
            return (DBObject)valueObj;
        }
        else if (valueObj instanceof Document) {
            return (Document)valueObj;
        }
        else {
            return valueObj;
        }
    }


    /**
     * 转换为List集合
     * @param value 待转换的数组对象
     * @return List集合
     */
    protected Object convertToList(Object value) {
        try {
            int length = Array.getLength(value);
            List<Object> list = new ArrayList(length);
            for (int i = 0; i < length; i++) {
                list.add(Array.get(value, i));
            }
            return list;
        } catch (Exception e) {
            logger.error("array convert to list is fail: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 转换为数组
     * @param type 字段属性类型
     * @param values 值
     * @return 数组对象
     */
    protected Object convertToArray(Class type, List<?> values) {
        final Object exampleArray = Array.newInstance(type, values.size());
        try {
            return values.toArray((Object[]) exampleArray);
        } catch (ClassCastException e) {
            for (int i = 0; i < values.size(); i++) {
                Array.set(exampleArray, i, values.get(i));
            }
            return exampleArray;
        }
    }

    /**
     * 转换数据集合，(List/Array)
     * @param field 字段属性
     * @param value 值
     * @return
     */
    protected Converter converterEncodeDataSetType(Field field, Object value) {

        if(value instanceof List) {
            return new Converter(field, getName(field), (List)value);
        }
        else if (DataType.isArray(field.getType())) {
            return new Converter(field, getName(field), convertToList(value));
        }

        return null;

    }

    protected Converter converterDecodeDataType(Field field, Object value) {
        if (value instanceof List) {
            Class<?> fieldType = field.getType();
            final Class<?> type = fieldType.isArray() ? fieldType.getComponentType() : fieldType;
            return new Converter(field, getName(field), convertToArray(type, (List<?>) value));
        }
        return null;
    }


    /**
     * 解码
     * @param field 字符属性
     * @param valueObj 值对象
     * @return
     */
    public abstract Converter decode(Field field, Object value) throws DbException;

    /**
     *  编码
     *
     * @param field entity field对象
     * @param value entity field value
     * @return  Converter
     */
    public abstract Converter encode(Field field, Object value) throws DbException;
}
