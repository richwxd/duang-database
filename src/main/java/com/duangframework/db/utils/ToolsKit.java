package com.duangframework.db.utils;

import com.duangframework.db.annotation.Entity;
import com.duangframework.db.annotation.Id;
import com.duangframework.db.annotation.IdType;
import com.duangframework.db.annotation.Param;
import com.duangframework.db.entity.IdEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public final class ToolsKit {

    private static final Logger logger = LoggerFactory.getLogger(ToolsKit.class);

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
        Id idParam = field.getAnnotation(Id.class);
        if(null != idParam) {
            return IdType.OID.equals(idParam.type()) ? IdEntity.ID_FIELD : IdEntity.ENTITY_ID_FIELD;
        }
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
            Class<?> fieldClass = field.getType();
            if (fieldClass.isArray()) {
                return (Object[])field.get(entityObj); // 换成数组
//                Object arrayObj = field.get(entityObj);
//               if (null == arrayObj) {
//                   return null;
//               }
//                int length =Array.getLength(arrayObj);
//                Object exampleArray = Array.newInstance(fieldClass.getComponentType(), length);
//                System.arraycopy(arrayObj, 0,exampleArray, 0, length);
//                return exampleArray;
            } else {
                return field.get(entityObj);
            }
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


    /**
     * 验证是否为MongoDB 的ObjectId
     *
     * @param str
     *            待验证字符串
     * @return  如果是则返回true
     */
    public static boolean isValidDuangId(String str) {
        if (ToolsKit.isEmpty(str)) {
            return false;
        }
        int len = str.length();
        if (len != 24) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if ((c < '0') || (c > '9')) {
                if ((c < 'a') || (c > 'f')) {
                    if ((c < 'A') || (c > 'F')) {
                        logger.warn(str + " is not DuangId!!");
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * 根据format字段，格式化日期
     * @param date          日期
     * @param format        格式化字段
     * @return
     */
    public static String formatDate(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     *  将字符串日期根据format格式化字段转换成日期类型
     * @param stringDate    字符串日期
     * @param format           格式化日期
     * @return
     */
    public static Date parseDate(String stringDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(stringDate);
        } catch (ParseException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }
}
