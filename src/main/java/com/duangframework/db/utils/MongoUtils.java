package com.duangframework.db.utils;

import com.duangframework.db.common.Order;
import com.duangframework.db.core.DbException;
import com.duangframework.db.entity.IdEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.util.*;

public class MongoUtils {

    /**
     * 转换成ObjectId
     * @param values 待转换的数据对象
     * @return
     */
    public static Object toObjectIds(Object values) {
        if(values instanceof Object[]){
            List<ObjectId> idList = new ArrayList<ObjectId>();
            Object[] tmp = (Object[]) values;
            for (Object value : tmp) {
                if (value != null) {
                    boolean isObjectId = ToolsKit.isValidDuangId(value.toString());
                    if (isObjectId) {
                        ObjectId dbId = new ObjectId(value.toString());
                        idList.add(dbId);
                    }
                }
            }
            return idList;
        } else {
            boolean isObjectId = ToolsKit.isValidDuangId(values.toString());
            if (isObjectId) {
                return new ObjectId(values.toString());
            } else {
                throw new DbException("toObjectId is Fail: ["+values+"] is not ObjectId or Empty");
            }
        }
    }

    /**
     *  将取出的类属性字段转换为Mongodb的DBObject
     * @param fields
     * @return
     */
    public static DBObject convert2DBFields(Field[] fields) {
        if (ToolsKit.isEmpty(fields)) {
            return null;
        }
        List<String> fieldList = new ArrayList<>(fields.length);
        for (int i = 0; i < fields.length; i++) {
            fieldList.add(fields[i].getName());
        }
        return convert2DBFields(fieldList);
    }

    /**
     *  将取出的类属性字段转换为Mongodb的DBObject
     * @param coll
     * @return
     */
    public static DBObject convert2DBFields(Collection<String> coll) {
        if (ToolsKit.isEmpty(coll)) {
            return null;
        }
        DBObject fieldsObj = new BasicDBObject();
        for (Iterator<String> it = coll.iterator(); it.hasNext();) {
            fieldsObj.put(it.next(), true);
        }
        return fieldsObj;
    }


    /**
     *  转换为查询对象
     * @param orderLinkedMap
     * @return
     */
    public static DBObject convert2DBOrder(Map orderLinkedMap) {
        DBObject orderObj = new BasicDBObject();
        if (ToolsKit.isNotEmpty(orderLinkedMap)) {
            for (Iterator<Map.Entry<String, String>> it = orderLinkedMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                orderObj.putAll(builderOrder(entry.getKey(), entry.getValue()));
            }
        }
        return orderObj;
    }

    /**
     * 构建排序对象
     * @param fieldName 要排序的字段
     * @param orderBy	排序字符串，asc(1)或desc(-1)
     * @return
     */
    private static DBObject builderOrder(String fieldName, String orderBy){
        if(ToolsKit.isEmpty(fieldName) || ToolsKit.isEmpty(orderBy)) {
            return null;
        }
        if(ToolsKit.isEmpty(orderBy)){
            return BasicDBObjectBuilder.start(IdEntity.ID_FIELD, -1).get();		//默认用OID时间倒序
        }else{
            return BasicDBObjectBuilder.start(fieldName, Order.SortEnum.DESC.name().equalsIgnoreCase(orderBy) ? -1 : 1).get();
        }
    }



}
