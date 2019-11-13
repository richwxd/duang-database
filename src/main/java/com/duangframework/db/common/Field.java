package com.duangframework.db.common;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 字段对象
 * @author laotang
 */
public class Field {

    private Collection<String> fields =  new ArrayList<>();;

    public Field() {
    }
    public Field(Collection<String> collection) {
        fields.addAll(collection);
    }

    /**
     * 添加查询返回字段
     * @param fieldName		字段名
     * @return
     */
    public Field add(String fieldName) {
        fields.add(fieldName);
        return this;
    }

    public Collection<String> getFields(){
        return fields;
    }
}
