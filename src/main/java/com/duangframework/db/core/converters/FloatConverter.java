package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;
import com.mongodb.DBObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;


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
    public void decode(Object entityObj, Field field, DBObject dbObject) throws DbException {

        if(null == object) {
            return ;
        }

        Class<?> type = field.getType();

        Object objectValue = null;
        if (DataType.isFloat(type) || DataType.isFloatObject(type)) {
            objectValue = dbObject.get(getName(field));
        }
        else if (DataType.isArray(type) || DataType.isListType(type)) {
            Class<?> targetClass = field.getType();
            final Class<?> type = targetClass.isArray() ? targetClass.getComponentType() : targetClass;
            objectValue = convertToArray(type, (List<?>) value);
        } else if(DataType.isMapType())

        setFieldValue(entityObj, field, Float.parseFloat(String.valueOf(objectValue)));
    }


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


}
