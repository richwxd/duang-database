package com.duangframework.db.mongodb;

import com.duangframework.db.DbClientFatory;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.ConverterKit;
import com.duangframework.db.core.IDao;
import com.duangframework.db.entity.IdEntity;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Mongodb Dao对象，提供可操作的方法
 * @param <T>
 */
public class MongoDao<T> implements IDao<T> {

    private MongoClient client;
    private Class<T> entityClass;
    private MongoDatabase db;
    private MongoCollection<Document> collection;
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
        db = client.getDatabase(mongoClient.getOptions().getDataBase());
        collection = db.getCollection(ToolsKit.getEntityName(entityClass));
//        collectionKey = MongoUtils.convert2DBFields(ToolsKit.getFields(entityClass));
    }


    @Override
    public T save(T entity) throws DbException {
        IdEntity idEntity = (IdEntity)entity;
        if(ToolsKit.isEmpty(idEntity.getId())){
            idEntity.setId(null);
        }
        return doSaveOrUpdate(idEntity) ? entity : null;
    }


    /**
     * 实际执行保存及更新的方法
     * @param entity        要操作的对象
     * @return  成功返回true
     * @throws Exception
     */
    private boolean doSaveOrUpdate(IdEntity entity) throws DbException {

        Map<String, Object> encodeMap = ConverterKit.duang().encode(entity);

        if (encodeMap.isEmpty()) {
            throw new DbException("encodeMap为空");
        }

        String id = entity.getId();
        try {
            Document document = new Document(encodeMap);
            if(ToolsKit.isEmpty(id)) {
                collection.insertOne(document);
                entity.setId(document.get(IdEntity.ID_FIELD).toString());
            } else {
                update(id, document);
            }
            return true;
        }catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }

    }

    /**
     * 根据id更新记录
     * @param id 记录ID
     * @param document  记录对象
     * @return
     */
    public boolean update(String id, Document document) {

        if (!ObjectId.isValid(id)){
            throw new DbException("id is not ObjectId!");
        }

        Document query = new Document(IdEntity.ID_FIELD, new ObjectId(id));
        //查询记录不存在时，不新增记录
        UpdateOptions options = new UpdateOptions();
        options.upsert(false); //为true则新增记录
        document.remove(IdEntity.ENTITY_ID_FIELD);
        BasicDBObject updateDbo = new BasicDBObject(Operator.SET, document);

        return collection.updateOne(query, updateDbo, options).isModifiedCountAvailable();
    }

}
