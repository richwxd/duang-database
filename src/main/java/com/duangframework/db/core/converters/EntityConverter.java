package com.duangframework.db.core.converters;

import com.duangframework.db.core.ConverterKit;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Entity 类型转换器
 *
 * @author Laotang
 */
public class EntityConverter extends TypeConverter {

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        Document document = (Document)value;
        Object decodeObj = ConverterKit.duang().decode(document, field.getType());
        if(null != decodeObj) {
            return new Converter(field, getName(field), decodeObj);
        }

        return null;
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        Map<String, Object> encodeMap = ConverterKit.duang().encode(value);
        if(null != encodeMap && !encodeMap.isEmpty()) {
            return new Converter(field, getName(field), encodeMap);
        }

        return null;
    }


}
