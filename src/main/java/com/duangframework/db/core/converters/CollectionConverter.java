package com.duangframework.db.core.converters;

import com.duangframework.db.core.ConverterKit;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.ClassKit;
import com.duangframework.db.utils.DataType;
import com.mongodb.DBObject;

import java.lang.reflect.Field;
import java.util.*;


/**
 * Collection(List/Set) 类型转换器
 *
 *
 * @author Laotang
 */
public class CollectionConverter extends TypeConverter {

    public CollectionConverter() {
        super(List.class, Set.class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if (null == value) {
            return null;
        }

        Class<?> genericTypeClass = ClassKit.getGenericTypeClass(field);
        List valueList = (List)value;
        if (DataType.isBaseType(genericTypeClass)) {
            return new Converter(field, getName(field), valueList);
        } else {
            List<Object> decodeList = new ArrayList(valueList.size());
            for (Object objectItem : valueList) {
                DBObject dbObject = (DBObject)convertValueObj(objectItem);
                Object resultObj = ConverterKit.duang().decode(dbObject.toMap(), genericTypeClass);
                decodeList.add(resultObj);
            }
            if(!decodeList.isEmpty()) {
                return new Converter(field, getName(field), decodeList);
            }
        }
        return null;
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        Class<?> genericTypeClass = ClassKit.getGenericTypeClass(field);
        List valueList = (List)value;
        if(DataType.isBaseType(genericTypeClass)) {
            return new Converter(field, getName(field), valueList);
        } else {
            List<Map<String, Object>> encodeList = new ArrayList(valueList.size());
            for (Object objectItem : valueList) {
                Map<String, Object> map = ConverterKit.duang().encode(objectItem);
                if(null != map && !map.isEmpty()) {
                    encodeList.add(map);
                }
            }
            if(!encodeList.isEmpty()) {
                return new Converter(field, getName(field), encodeList);
            }
            return null;
        }
    }
}
