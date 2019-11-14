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
 * Double类型转换器
 *
 * @author Laotang
 */
public class MapConverter extends TypeConverter {

    public MapConverter() {
        super(Map.class);
    }

    @Override
    public Converter decode(Field field, Object value) throws Exception {
        if (null == value) {
            return null;
        }

        Class<?> genericTypeClass = ClassKit.getGenericTypeClass(field);
        Map valueMap = (Map)value;
        if (DataType.isBaseType(genericTypeClass)) {
            return new Converter(field, getName(field), valueMap);
        } else {
            Map<Object,Object> decodeMap = new LinkedHashMap(valueMap.size());
            for(Iterator<Map.Entry> iterator = valueMap.entrySet().iterator(); iterator.hasNext();){
                Map.Entry entry = iterator.next();
                Document document = (Document) entry.getValue();
                Object resultObj =  ConverterKit.duang().decode(document, genericTypeClass);
                if (null != resultObj) {
                    decodeMap.put(entry.getKey(), resultObj);
                }
            }
            if (!decodeMap.isEmpty()) {
                return new Converter(field, getName(field), decodeMap);
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
        Map valueMap = (Map)value;
        if (DataType.isBaseType(genericTypeClass)) {
            return new Converter(field, getName(field), valueMap);
        } else {
            Map<Object, Map> encodeMap = new LinkedHashMap(valueMap.size());
            for(Iterator<Map.Entry> iterator = valueMap.entrySet().iterator(); iterator.hasNext();){
                Map.Entry entry = iterator.next();
                Map<String, Object> map = ConverterKit.duang().encode(entry.getValue());
                if (null != map && !map.isEmpty()) {
                    encodeMap.put(entry.getKey(), map);
                }
            }
            if(!encodeMap.isEmpty()) {
                return new Converter(field, getName(field), encodeMap);
            }
            return null;
        }
    }
}
