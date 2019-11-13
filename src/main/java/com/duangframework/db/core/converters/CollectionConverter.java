package com.duangframework.db.core.converters;

import com.duangframework.db.core.ConverterKit;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.ClassKit;
import com.duangframework.db.utils.DataType;
import org.bson.Document;

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
    public Converter decode(Field field, Object value) throws Exception {

        if (null == value) {
            return null;
        }

        Class<?> genericTypeClass = ClassKit.getGenericTypeClass(field);
        List valueList = (List)value;
        if (DataType.isBaseType(genericTypeClass)) {
            return new Converter(field, getName(field), valueList);
        } else {
            List resultList = new ArrayList(valueList.size());
            for (Object objectItem : valueList) {
                Document document = (Document)convertValueObj(objectItem);
                Object resultObj = ConverterKit.duang().decode(document, genericTypeClass);
                resultList.add(resultObj);
            }
            return new Converter(field, getName(field), resultList);
        }
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
            List resultList = new ArrayList(valueList.size());
            for (Object objectItem : valueList) {
                Map<String, Object> map = ConverterKit.duang().encode(objectItem);
                resultList.add(map);
            }
            return new Converter(field, getName(field), resultList);
        }

    }


}
