package com.duangframework.db.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 排序字段值定义
 *
 * @author Laotang
 */
public class Order {

    private String field;
    private Integer order;

    private Map<String, Integer> orderLinkedMap = new LinkedHashMap<>();

    /**
     * ASC: 1
     * DESC: -1
     */
    public enum SortEnum {
        ASC, DESC
    }

    public Order(String field, SortEnum sortEnum) {
        add(field, sortEnum);
    }

    /**
     * 添加排序
     * @param fieldName		排序的字段名
     * @param sortEnum	排序方向枚举
     * @return
     */
    public Order add(String fieldName, SortEnum sortEnum) {
        orderLinkedMap.put(fieldName, SortEnum.ASC.equals(sortEnum.name()) ? 1 : -1);
        return this;
    }

}
