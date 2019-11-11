package com.duangframework.db.utils;

import com.duangframework.db.annotation.Entity;
import com.duangframework.db.annotation.Param;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public final class ToolsKit {

    private ToolsKit() {

    }

    /***
     * 判断传入的对象是否为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,为空或等于0时返回true
     */
    public static boolean isEmpty(Object obj) {
        return checkObjectIsEmpty(obj, true);
    }

    /***
     * 判断传入的对象是否不为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,不为空或不等于0时返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return checkObjectIsEmpty(obj, false);
    }

    @SuppressWarnings("rawtypes")
    private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
        if (null == obj) {
            return bool;
        }
        else if (obj == "" || "".equals(obj)) {
            return bool;
        }
        else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            try {
                Double.parseDouble(obj + "");
            } catch (Exception e) {
                return bool;
            }
        } else if (obj instanceof String) {
            if (((String) obj).length() <= 0) {
                return bool;
            }
            if ("null".equalsIgnoreCase(obj+"")) {
                return bool;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0) {
                return bool;
            }
        }
        return !bool;
    }

    /**
     * 取实体类名称
     * 如果有指定@Entity注解且设置name属性，则返回指定的名称
     * 如果没有指定，则返回类的SimpleName
     *
     * @param entityClass 实体类
     * @return  名称
     */
    public static String getEntityName(Class<?> entityClass) {
        java.util.Objects.requireNonNull(entityClass, "entityClass is null");
        Entity entity = entityClass.getAnnotation(Entity.class);
        if(isNotEmpty(entity) && isNotEmpty(entity.name())) {
            return entity.name();
        }
        return entityClass.getSimpleName();
    }

    /**
     * 取成员变量名称
     * 如果有设置注解Param name，则优先取注解指定的name值
     *
     * @param field 字段属性
     * @return 字段名
     */
    public static String getFieldName(Field field) {
        java.util.Objects.requireNonNull(field, "field is null");
        Param param = field.getAnnotation(Param.class);
        return ToolsKit.isEmpty(param) ? field.getName() :
                ToolsKit.isEmpty(param.name()) ? field.getName() : param.name();
    }

    /**
     * 取成员变量值
     *
     * @param field 类的字段，成员变量
     * @param entityObj Entity或Dto对象
     * @return
     */
    public static Object getFieldValue(Field field, Object entityObj) {
        java.util.Objects.requireNonNull(field, "field is null");
        java.util.Objects.requireNonNull(entityObj, "entityObj is null");
        try {
            field.setAccessible(true);
            return field.get(entityObj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否允许值为null
     * @return 允许返回true
     */
    public static boolean isAllowNull() {
        return false;
    }

    /**
     * 保存到数据库的字段是否全部转换为小写
     *
     * @return true为转换为小写
     */
    public static boolean isFieldToLowerCase() {
        return false;
    }
}
