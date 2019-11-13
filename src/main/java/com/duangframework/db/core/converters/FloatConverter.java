package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Field;


/**
 * Double类型转换器
 *
 * @author Laotang
 */
public class FloatConverter extends TypeConverter {

    public FloatConverter() {
        super(float.class, Float.class, Float[].class);
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {
        return null;
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

//        Class<?> type = field.getType();
//
//        Object toFieldValueObj = null;
//        if (DataType.isFloat(type) || DataType.isFloatObject(type)) {
//            toFieldValueObj = convertValueObj(field, valueObj);
//        }
        /*
        else if (DataType.isArray(type) || DataType.isListType(type)) {
            Class<?> targetClass = field.getType();
            final Class<?> type = targetClass.isArray() ? targetClass.getComponentType() : targetClass;
            toFieldValueObj = convertToArray(type, (List<?>) valueObj);

        } else if(DataType.isMapType(type)) {

        }
        */
//        setFieldValue(entityObj, field, Float.parseFloat(String.valueOf(toFieldValueObj)));
        return new Converter(field, getName(field), (Float)value);
    }

    /*
    private Object convertToArray(final Class type, final List<?> values) {
        final Object array = Array.newInstance(type, values.size());
        try {
            return values.toArray((Object[]) array);
        } catch (Exception e) {
            for (int i = 0; i < values.size(); i++) {
                Array.set(array, i, decode(Float.class, values.get(i)));
            }
            return array;
        }
    }
    */

}
