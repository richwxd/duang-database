package com.duangframework.db.mongodb;

import com.duangframework.db.DbClientFatory;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.ConverterKit;
import com.duangframework.db.core.IDao;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Mongodb Dao对象，提供可操作的方法
 * @param <T>
 */
public class MongoDao<T> implements IDao<T> {

    private MongoClient client;
    private Class<T> entityClass;
    private DB db;
    private DBCollection collection;
    protected DBObject collectionKey;

    public MongoDao(String clientId, Class<T> entityClass) {
        this.entityClass =entityClass;
        duang(clientId);
    }

    private void duang(String clientId) {
        if(ToolsKit.isEmpty(clientId)) {
            throw new DbException("clientId不能为空");
        }

        MongodbClient mongoClient = DbClientFatory.duang().id(clientId).getClient();
        if(ToolsKit.isEmpty(mongoClient)) {
            throw new DbException("根据["+clientId+"]取MongoClient时，MongoClient为空，请检查！");
        }
        client =  mongoClient.getClient();
        db = client.getDB(mongoClient.getOptions().getDataBase());
        collection = db.getCollection(ToolsKit.getEntityName(entityClass));
//        collectionKey = MongoUtils.convert2DBFields(ToolsKit.getFields(entityClass));
    }

    @Override
    public T save(T entity) throws DbException {
        return (T) ConverterKit.duang().encode(entity);
    }
}
