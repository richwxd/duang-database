package com.duangframework.db.core;

import com.duangframework.db.annotation.Bean;
import com.duangframework.db.core.converters.*;
import com.duangframework.db.utils.ClassKit;
import com.duangframework.db.utils.DataType;
import com.duangframework.db.utils.ObjectKit;
import com.duangframework.db.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ConverterKit {

    private static final Logger LOG = LoggerFactory.getLogger(ConverterKit.class);

    private static final ConverterKit CONVERTER_KIT = new ConverterKit();

    /**Entity类型(一般是指自定义的类型)转换器Map*/
    private final List<TypeConverter> entityTcList = new ArrayList<TypeConverter>(1);

    /** 基础类型转换器Map*/
    private final Map<Class, TypeConverter> tcMap = new ConcurrentHashMap<Class, TypeConverter>();

    private final List<Class<? extends TypeConverter>> registeredConverterClasses = new ArrayList<Class<? extends TypeConverter>>();

    private static class ConverterKitHolder {
        private static final ConverterKit INSTANCE = new ConverterKit();
    }

    private ConverterKit() {
        addConverter(new StringConverter());
        addConverter(new IntegerConverter());
        addConverter(new LongConverter());
        addConverter(new FloatConverter());
        addConverter(new DoubleConverter());
        addConverter(new DateConverter());
        addConverter(new CollectionConverter());
        addConverter(new MapConverter());
        addConverter(new EntityConverter());


        //字段上有注解的转换器
        addConverter(new ObjectIdConverter());
    }
    public static final ConverterKit duang() {
        return ConverterKitHolder.INSTANCE;
    }

    public TypeConverter addConverter(final TypeConverter tc) {
        if (null != tc.getSupportedTypes()) {
            for (final Class c : tc.getSupportedTypes()) {
                addTypedConverter(c, tc);
            }
        } else {
            entityTcList.add(tc);
        }
//        registeredConverterClasses.add(tc.getClass());
//        tc.setMapper(mapper);
        return tc;
    }

    private void addTypedConverter(final Class type, final TypeConverter tc) {
        if (!tcMap.containsKey(type)) {
            tcMap.put(type, tc);
        }
    }

    /**
     * 编码，实体类转换成Map对象
     * @param entityObject
     * @return
     * @throws DbException
     */
    public Map<String,Object> encode(Object entityObject) throws DbException {
        if(ToolsKit.isEmpty(entityObject)) {
            throw new DbException("entity converter map is fail: " + entityObject.getClass().getCanonicalName() + " is null!");
        }
        Field[] fields = ClassKit.getFields(entityObject.getClass());
        Map<String, Object> encodeMap = new HashMap<>();
        for(Field field : fields) {
            TypeConverter typeConverter = getTypeConverter(field);
            Converter c = typeConverter.encode(field, ToolsKit.getFieldValue(field, entityObject));
            if(null != c && c.isNotNull() && c.isPersist()) {
                encodeMap.put(c.getKey(), c.getValue());
            }
        }
        return encodeMap;
    }

    /**
     * 解码，由数据库记录集转换为实体类对象
     * @param dbObject  数据库记录集对象
     * @param entityClass 实体类对象
     * @param <T>   泛型对象
     * @return
     * @throws DbException
     */
    public <T> T decode(Map<String, Object> dbObject, Class<?> entityClass) throws DbException {
        if(ToolsKit.isEmpty(dbObject)) {
            throw new DbException("db result converter entity is fail: " + entityClass.getSimpleName() + " is null!");
        }
        Field[] fields = ClassKit.getFields(entityClass);
        Object entityObject = ObjectKit.newInstance(entityClass);
        try {
            for (Field field : fields) {
                TypeConverter typeConverter = getTypeConverter(field);
                Converter c = typeConverter.decode(field, dbObject.get(ToolsKit.getFieldName(field)));
                if (null != c) {
                    field.setAccessible(true);
                    field.set(entityObject, c.getValue());
                }
            }
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
        return (T)entityObject;
    }

    public TypeConverter getTypeConverter(final Class<?> c) {
        TypeConverter typeConverter =  tcMap.get(c);

        if(null != typeConverter) {
            return typeConverter;
        }

        for (TypeConverter tc : entityTcList) {
            if(DataType.isBeanType(c)) {
                typeConverter = tc;
            }
        }

        if(ToolsKit.isEmpty(typeConverter)) {
            throw new DbException("类型编码器不存在");
        }
        return typeConverter;
    }

    /**
     * 取编码器
     * 如字段上有注解，优先取注解对应的转换器
     *
     * @param field 字段属性对象
     * @return 编码器
     */
    private TypeConverter getTypeConverter(final Field field) {
        TypeConverter typeConverter = null;
        Annotation[] annotations = field.getAnnotations();
        if(ToolsKit.isNotEmpty(annotations)) {
            for (Annotation annotation : annotations) {
                Class<?> c = annotation.annotationType();
                typeConverter =  tcMap.get(c);
                if(null != typeConverter) {
                    return typeConverter;
                }
            }
        }

        return getTypeConverter(field.getType());
    }

}
