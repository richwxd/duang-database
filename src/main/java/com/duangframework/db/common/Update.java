package com.duangframework.db.common;

import com.duangframework.db.core.IDao;
import com.duangframework.db.entity.IdEntity;
import com.duangframework.db.mongodb.Operator;
import com.duangframework.db.utils.MongoUtils;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class Update<T> {

    private final static Logger logger = LoggerFactory.getLogger(Update.class);

    private IDao<T> dao;
    private Map updateObj;
    private DBCollection dbCollection;
    private Class<T> clazz;
    private Query<T> query;


    public Update() {
        updateObj = new LinkedHashMap();
        // 默认更新审核通过的数据
        set(IdEntity.STATUS_FIELD, IdEntity.STATUS_FIELD_SUCCESS);
    }

    private Update(IDao<T> dao) {
        this.dao = dao;
    }

    public static class Builder<T> {
        private IDao<T> dao;
        public Builder() {}
        public Update.Builder dao(IDao<T> dao) {
            this.dao = dao;
            return this;
        }
        public Update builder() {
            return new Update(dao);
        }
    }


    public Map getUpdate() {
        logger.debug(" update: " + updateObj.toString());
        if (updateObj.keySet().isEmpty()) {
            throw new IllegalArgumentException("update can not be null");
        }
        return updateObj;
    }


    /**
     * 将符合查询条件的key更新为value
     *
     * @param key   要更新列名
     * @param value 更新后的值
     * @return
     */
    public Update<T> set(String key, Object value) {
        if (null == value) {
            throw new NullPointerException("value is null");
        }
        append(key, Operator.SET, MongoUtils.getEncode(value));
        return this;
    }

    /**
     * 将值添加到符合查询条件的对象中
     *
     * @param key   要添加列名
     * @param value 要添加的值
     * @return
     */
    public Update<T> push(String key, Object value) {
        if (null == value) {
            throw new NullPointerException("value is null");
        }
        append(key, Operator.PUSH, MongoUtils.getEncode(value));
        return this;
    }

    /**
     * 将符合查询条件的值从array/list/set中删除
     *
     * @param key   要删除列名
     * @param value 要删除的值
     * @return
     */
    public Update<T> pull(String key, Object value) {
        if (null == value) {
            throw new NullPointerException("value is null");
        }
        append(key, Operator.PULL, MongoUtils.getEncode(value));
        return this;
    }

    /**
     * 自增或自减数值
     *
     * @param key   要自增或自减的字段名
     * @param value 数值
     * @return
     */
    public Update<T> inc(String key, Object value) {
        append(key, Operator.INC, MongoUtils.getEncode(value));
        return this;
    }


    /**
     *
     * @param key 字段名称
     * @param oper 操作符
     * @param value 值
     */
    private void append(String key, String oper, Object value) {
        DBObject dbo = new BasicDBObject(key, value);
        DBObject obj = (DBObject) updateObj.get(oper);
        if (ToolsKit.isNotEmpty(obj)) {
            //追加到原来的dbo对象
            obj.putAll(dbo);
        } else {
            updateObj.put(oper, dbo);
        }
    }

}
