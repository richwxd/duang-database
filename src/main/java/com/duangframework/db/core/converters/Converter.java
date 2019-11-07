package com.duangframework.db.core.converters;

import com.duangframework.db.annotation.Persist;
import com.duangframework.db.core.DbException;
import com.duangframework.db.utils.ToolsKit;

import java.lang.reflect.Field;

/**
 * 转换内容对象
 *
 * @author Laotang
 */
public class Converter {

    private Field field;
    /**关键字*/
    private String key;
    /**值*/
    private Object value;

    public Converter(Field field, String key, Object value) {
        setField(field);
        setKey(key);
        setValue(value);
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    private void setValue(Object value) {
        if(!ToolsKit.isAllowNull() && ToolsKit.isEmpty(value)) {
            throw new DbException("值不允许为null，如需更改，请更改配置文件[allow.save.null]");
        }
        this.value = value;
    }

    private void setField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public boolean isNotNull() {
        return null != field && !ToolsKit.isAllowNull();
    }

    public boolean isPersist() {
        return field.isAnnotationPresent(Persist.class);
    }

    @Override
    public String toString() {
        return "Converter{" +
                "field='" + field + '\'' +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
