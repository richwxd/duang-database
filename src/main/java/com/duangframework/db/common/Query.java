package com.duangframework.db.common;


import com.duangframework.db.core.DbException;
import com.duangframework.db.core.IDao;
import com.duangframework.db.entity.IdEntity;
import com.duangframework.db.mongodb.MongoDao;
import com.duangframework.db.mongodb.Operator;
import com.duangframework.db.utils.DataType;
import com.duangframework.db.utils.MongoUtils;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 查询对象
 *
 * @author laotang
 */
public class Query<T> {

    private final static Logger logger = LoggerFactory.getLogger(Query.class);

    private Map queryObj;
    private Order orderObj;
    private Field fieldObj;
    private Page<T> pageObj;
    private Map hintObj;

//    private MongoDatabase db;
//    private MongoCollection collection;
//    private Class<T> clazz;
//    private Map keys;
    private IDao<T> dao;


    public Query() {
        this(true);
    }

    public Query(boolean isAddSuccessStatus) {
        queryObj = new LinkedHashMap();
        orderObj = new Order();
        fieldObj = new Field();
        hintObj = new LinkedHashMap();
        pageObj = new Page<T>(0, 1);

        // 默认查询审核通过的数据
//        if(isAddSuccessStatus) {
//            queryObj.put(IdEntity.STATUS_FIELD, IdEntity.STATUS_FIELD_SUCCESS);
//        }
    }

    private Query(IDao<T> dao) {
        this();
        this.dao = dao;
    }

    public static class Builder<T> {
        private IDao<T> dao;
        public Builder() {}
        public Builder dao(IDao<T> dao) {
            this.dao = dao;
            return this;
        }
        public Query builder() {
            return new Query(dao);
        }
    }

    /**
     * 等于
     *
     * @param key   字段名
     * @param value 内容值
     * @return
     */
    public Query<T> eq(String key, Object value) {
        append2DBObject(key, null, value);
        return this;
    }

    /**
     * 不等于
     *
     * @param key   字段名
     * @param value 内容值
     * @return
     */
    public Query<T> ne(String key, Object value) {
        append2DBObject(key, Operator.NE, value);
        return this;
    }

    /**
     * 大于(>)
     *
     * @param key   字段名
     * @param value 内容值
     * @return
     */
    public Query<T> gt(String key, Object value) {
        append2DBObject(key, Operator.GT, value);
        return this;
    }

    /**
     * 大于等于(>=)
     *
     * @param key   字段名
     * @param value 内容值
     * @return
     */
    public Query<T> gte(String key, Object value) {
        append2DBObject(key, Operator.GTE, value);
        return this;
    }

    /**
     * 小于(<)
     *
     * @param key   字段名
     * @param value 内容值
     * @return
     */
    public Query<T> lt(String key, Object value) {
        append2DBObject(key, Operator.LT, value);
        return this;
    }

    /**
     * 小于等于(<=)
     *
     * @param key   字段名
     * @param value 内容值
     * @return
     */
    public Query<T> lte(String key, Object value) {
        append2DBObject(key, Operator.LTE, value);
        return this;
    }

    /**
     * in查询
     *
     * @param key   字段名
     * @param value 内容集合
     * @return
     */
    public Query<T> in(String key, Object... value) {
        append2DBObject(key, Operator.IN, value);
        return this;
    }

    /**
     * not in 查询
     *
     * @param key   字段名
     * @param value 内容集合
     * @return
     */
    public Query<T> nin(String key, Object... value) {
        append2DBObject(key, Operator.NIN, value);
        return this;
    }

    /**
     * 多条件or查询
     *
     * @param mongoQueries 条件
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Query<T> or(Query... mongoQueries) {
        List orDboList = (List) queryObj.get(Operator.OR);
        if (ToolsKit.isEmpty(orDboList)) {
            orDboList = new ArrayList();
            queryObj.put(Operator.OR, orDboList);
        }
        for (Query q : mongoQueries) {
            orDboList.add(q.getQuery());
        }
        return this;
    }

    /**
     * 多条件and查询
     *
     * @param mongoQueries 条件
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Query<T> and(Query... mongoQueries) {
        List andDboList = (List) queryObj.get(Operator.AND);
        if (ToolsKit.isEmpty(andDboList)) {
            andDboList = new ArrayList();
            queryObj.put(Operator.AND, andDboList);
        }
        for (Query q : mongoQueries) {
            andDboList.add(q.getQuery());
        }
        return this;
    }

    /**
     * 强制指定使用索引
     *
     * @param indexName
     * @return
     */
    public Query<T> hint(String indexName) {
        hintObj = new Hint().set(indexName, 1).getObject();
        return this;
    }

    public Query<T> hint(Hint hint) {
        hintObj = hint.getObject();
        return this;
    }

    /**
     * 模糊查询
     *
     * @param key   字段名
     * @param value 内容值
     * @return
     */
    public Query<T> like(String key, Object value) {
        return regex(key, ".*" + value + ".*");
    }

    /**
     * 正则表达式查询
     *
     * @param key   字段名
     * @param value 正则表达式字符串
     * @return
     */
    public Query<T> regex(String key, String value) {
        append2DBObject(key, Operator.REGEX, Pattern.compile(value).pattern());
        return this;
    }

    /**
     * 查询字段是否存在
     *
     * @param key   字段值
     * @param value true为存在， false为不存在
     * @return
     */
    public Query<T> exist(String key, boolean value) {
//		mongoDB.runCommand({distinct:"Fans",key:"_id",query:{"commUserDatas.50":{"$exists":true}}});
        append2DBObject(key, Operator.EXISTS, value);
        return this;
    }

    /**
     * 对内嵌文档的多个键进行查询
     *
     * @param key        内嵌文档字段名
     * @param mongoQuery 查询对象
     * @return
     */
    public Query<T> em(String key, Query<T> mongoQuery) {
        DBObject dbo = new BasicDBObject(Operator.ELEMMATCH, mongoQuery.getQuery());
        queryObj.put(key, dbo);
        return this;
    }

    private void append2DBObject(String key, String oper, Object value) {
        if (ToolsKit.isEmpty(key)) {
            throw new DbException("query key is null...");
        }
        value = DataType.conversionVariableType(value);
        if ((IdEntity.ID_FIELD.equals(key) || IdEntity.ENTITY_ID_FIELD.equals(key)) && ObjectId.isValid(value.toString())) {
            append(key, oper, MongoUtils.toObjectIds(value));
        } else {
            append(key, oper, value);
        }
    }

    private void append(String key, String oper, Object value) {
        if (ToolsKit.isEmpty(oper)) {
            queryObj.put(key, value);        //如果没有操作符的话就全部当作等于查找
        } else {
            Object obj = queryObj.get(key);
            DBObject dbo = null;
            if (obj instanceof DBObject) {
                ((DBObject) obj).put(oper, value);                //追加到原来的dbo对象
            } else {
                dbo = new BasicDBObject(oper, value);
                queryObj.put(key, dbo);
            }
        }
    }

    public Query<T> fields(Field field) {
        this.fieldObj = field;
        return this;
    }

    @Deprecated
    public Query<T> fields(Collection<String> fields) {
        for (Iterator<String> it = fields.iterator(); it.hasNext(); ) {
            fieldObj.add(it.next());
        }
        return this;
    }

    public Query<T> order(String fieldName, Order.SortEnum orderByEnum) {
        this.orderObj.add(fieldName, orderByEnum);
        return this;
    }

    public Query<T> order(Order order) {
        this.orderObj = order;
        return this;
    }

    public Query<T> page(Page<T> page) {
        this.pageObj = page;
        return this;
    }

    @Deprecated
    public Query<T> pageNo(int pageNo) {
        this.pageObj.setPageNo(pageNo);
        return this;
    }

    @Deprecated
    public Query<T> pageSize(int pageSize) {
        this.pageObj.pageSize(pageSize);
        return this;
    }

    @Deprecated
    public Query<T> skip(int skip) {
        this.pageObj.setSkipNum(skip);
        return this;
    }

    public Map getQuery() {
        logger.debug(" query: " + queryObj.toString());
        return queryObj;
    }

    public Map getOrderObj() {
        logger.debug(" orderObj: " + orderObj.toString());
        return orderObj.getOrderMap();
    }

    public Collection<String> getFields() {
        logger.debug(" fields: " + fieldObj.toString());
        return fieldObj.getFields();
    }

    public Page<T> getPageObj() {
        return pageObj;
    }

    public Map getHint() {
        return hintObj;
    }

    private void checkSingle(DBObject orderDbo, Page<T> page) {
        if (ToolsKit.isNotEmpty(orderDbo) || page.getPageNo() != 0 || page.getPageSize() != 0) {
            logger.error("orderBy: " + orderDbo.toString() + "       pageNo: " + page.getPageNo() + "          pageSize: " + page.getPageSize());
            throw new DbException("findOne时, orderBy或pageNo或pageSize参数不能有值");
        }
    }

    public T findOne() {
        return dao.findOne(this);
    }

    public List<T> findList() {
        return dao.findList(this);
    }
}

