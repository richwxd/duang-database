package com.duangframework.db.core;

import com.duangframework.db.core.converters.Converter;
import com.duangframework.db.core.converters.StringConverter;
import com.duangframework.db.utils.ClassKit;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ConverterKit {

    private static final Logger LOG = LoggerFactory.getLogger(ConverterKit.class);
    private static final ConverterKit CONVERTER_KIT = new ConverterKit();
    private final List<TypeConverter> untypedTypeEncoders = new LinkedList<TypeConverter>();
    private final Map<Class, TypeConverter> tcMap = new ConcurrentHashMap<Class, TypeConverter>();
    private final List<Class<? extends TypeConverter>> registeredConverterClasses = new ArrayList<Class<? extends TypeConverter>>();

    private static class ConverterKitHolder {
        private static final ConverterKit INSTANCE = new ConverterKit();
    }
    private ConverterKit() {
        addConverter(new StringConverter());
    }
    public static final ConverterKit duang() {
        return ConverterKitHolder.INSTANCE;
    }

    public TypeConverter addConverter(final TypeConverter tc) {
        if (tc.getSupportedTypes() != null) {
            for (final Class c : tc.getSupportedTypes()) {
                addTypedConverter(c, tc);
            }
        } else {
            untypedTypeEncoders.add(tc);
        }
        registeredConverterClasses.add(tc.getClass());
//        tc.setMapper(mapper);
        return tc;
    }

    private void addTypedConverter(final Class type, final TypeConverter tc) {
        if (!tcMap.containsKey(type)) {
            tcMap.put(type, tc);
        }
    }

    public <T> T encode(Object entityObject) throws DbException{
        if(ToolsKit.isEmpty(entityObject)) {
            throw new NullPointerException("Entity Convetor Document Fail: " + entityObject.getClass().getCanonicalName() + " is null!");
        }
        Field[] fields = ClassKit.getFields(entityObject.getClass());
        Document document = new Document();
        for(Field field : fields) {
            TypeConverter typeConverter = getEncoder(field.getType());
            Converter c = typeConverter.encode(field, entityObject);
            if(c.isNotNull() && c.isPersist()) {
                document.put(c.getKey(), c.getValue());
            }
        }
        return null;
    }

    /**
     * 取编码器
     *
     * @param c 字段的属性Class
     * @return 编码器
     */
    private TypeConverter getEncoder(final Class c) {
        TypeConverter typeConverter = tcMap.get(c);
        if(ToolsKit.isNotEmpty(typeConverter)) {
            return typeConverter;
        }

        for (TypeConverter tc : untypedTypeEncoders) {
            if (tc.getSupportedTypes().contains(c)) {
                typeConverter = tc;
            }
        }
        if(ToolsKit.isEmpty(typeConverter)) {
            throw new DbException("类型编码器不存在");
        }
        return typeConverter;
    }

}
