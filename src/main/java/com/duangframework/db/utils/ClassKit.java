package com.duangframework.db.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ClassKit {

    private static final ConcurrentMap<String, Field[]> FIELD_MAPPING_MAP = new ConcurrentHashMap<String, Field[]>();

    private ClassKit() {

    }

    /**
     * 取出类的全名，包括包名
     * @param cls                       类
     * @param isLowerCase       是否转为小写
     * @return
     */
    public static String getClassName(Class<?> cls, boolean isLowerCase) {
        String name = cls.getName();
        return isLowerCase ? name.toLowerCase() : name;
    }

    /**
     * 根据class对象反射出所有属性字段，静态字段除外
     * @param cls
     * @return
     */
    public static Field[] getFields(Class<?> cls){
        String key = getClassName(cls, ToolsKit.isFieldToLowerCase());
        Field[] field = null;
        if(FIELD_MAPPING_MAP.containsKey(key)){
            field = FIELD_MAPPING_MAP.get(key);
        }else{
            field = getAllFields(cls);
            FIELD_MAPPING_MAP.put(key, field);
        }
        return (null == field) ? null : field;
    }

    /**
     * 取出类里的所有字段
     * @param cls
     * @return	Field[]
     */
    private static Field[] getAllFields(Class<?> cls) {
        List<Field> fieldList = new ArrayList<Field>();
        fieldList.addAll(filterStaticFields(cls.getDeclaredFields()));
        Class<?> parent = cls.getSuperclass();
        //查找父类里的属性字段
        while(null != parent && parent != Object.class){
            fieldList.addAll(filterStaticFields(parent.getDeclaredFields()));
            parent = parent.getSuperclass();
        }
        return fieldList.toArray(new Field[fieldList.size()]);
    }

    /**
     * 过滤静态方法
     * @param fields
     * @return
     */
    private static List<Field> filterStaticFields(Field[] fields){
        List<Field> result = new ArrayList<Field>();
        for (Field field : fields) {
            if(!Modifier.isStatic(field.getModifiers())){		//静态字段不取
                field.setAccessible(true);	//设置可访问私有变量
                result.add(field);
            }
        }
        return result;
    }

    /**
     * 取字段属性里的泛型类型
     * @param field 字段属性
     * @return 泛型类
     */
    public static Class<?> getGenericTypeClass(Field field) {
        ParameterizedType paramTypeItem = (ParameterizedType)field.getGenericType();
        Type[] types = paramTypeItem.getActualTypeArguments();
        if(types.length == 1) {
            return (Class)types[0];
        } else if (types.length == 2) {
            return (Class)types[1];
        } else {
            return Object.class;
        }
    }

}
