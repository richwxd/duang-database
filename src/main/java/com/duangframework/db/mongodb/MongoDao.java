package com.duangframework.db.mongodb;

import com.duangframework.db.DbClientFatory;
import com.duangframework.db.common.Page;
import com.duangframework.db.common.Query;
import com.duangframework.db.common.Update;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.ConverterKit;
import com.duangframework.db.core.IDao;
import com.duangframework.db.entity.IdEntity;
import com.duangframework.db.utils.MongoUtils;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.BSON;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.function.Consumer;

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

    @Override
    public boolean update(Query<T> query, Update<T> update) {
        return false;
    }

    /**
     * 根据条件查询记录
     * @param mongoQuery		查询条件对象
     * @return 泛型对象
     * @throws Exception
     */
    public T findOne(Query mongoQuery) throws DbException {
        if(ToolsKit.isEmpty(mongoQuery)) {
            throw new DbException("Mongodb findOne is Fail: mongoQuery is null");
        }
        List<T> resultList = findList(mongoQuery);
        if(ToolsKit.isEmpty(resultList)) {
            return null;
        }
        return resultList.get(0);
    }

    /**
     * 根据查询条件查找记录
     * @param mongoQuery		查询条件
     * @return 结果集合，元素为指定的泛型
     * @throws Exception
     */
    @Override
    public List<T> findList(Query mongoQuery) throws DbException {
        if(null == mongoQuery) {
            throw new DbException("Mongodb findList is Fail: mongoQuery is null");
        }
        Bson queryDoc = new BasicDBObject(mongoQuery.getQuery());
        FindIterable<Document> documents = collection.find(queryDoc);
        documents = builderQueryDoc(documents, mongoQuery);
        final List<T> resultList = new ArrayList();
        for(Document document : documents) {
            resultList.add((T) ConverterKit.duang().decode(new HashMap<>(document), entityClass));
        }
        return resultList;
    }

    private Map<String, Object> document2Map(Document document) {
        Map<String,Object> resultMap = new HashMap<>(document.size());
        for(Iterator<Map.Entry<String,Object>> iterator = document.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String,Object> entry = iterator.next();
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }

    private FindIterable<Document> builderQueryDoc(FindIterable<Document> documents, Query mongoQuery) {
        Page<T> page = mongoQuery.getPageObj();
        BasicDBObject fieldDbo = (BasicDBObject)MongoUtils.convert2DBFields(mongoQuery.getFields());
        if(ToolsKit.isNotEmpty(fieldDbo) && !fieldDbo.isEmpty()) {
            documents.projection(fieldDbo);
        }
        BasicDBObject orderDbo =  (BasicDBObject)MongoUtils.convert2DBOrder(mongoQuery.getOrderObj());
        if(ToolsKit.isNotEmpty(orderDbo) && !orderDbo.isEmpty()) {
            documents.sort(orderDbo);
        }
        if((page.getSkipNum() > -1 || page.getPageNo() > 0) && page.getPageSize() > 1) {
            documents.skip(getSkipNum(page));
            documents.limit(page.getPageSize());
        }
        BasicDBObject hintDbo = new BasicDBObject(mongoQuery.getHint());
        if(ToolsKit.isNotEmpty(hintDbo) && !hintDbo.isEmpty()) {
            documents.hint(hintDbo);
        }
        if(ToolsKit.isEmpty(documents)) {
            throw new NullPointerException("ducuments is null");
        }
        return documents;
    }

    /**
     * 确定跳过的行数
     * @param page
     * @return
     */
    private int getSkipNum(Page<T> page) {
        int skipNum = page.getSkipNum();
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        if(skipNum > -1) {
            return skipNum;
        } else if(pageSize == -1) {
            return 0;
        } else {
            return (pageNo>0 ? (pageNo-1) : pageNo) * pageSize;
        }
    }

    public Query<T> query() {
        return new Query.Builder()
//                .entityClass(entityClass)
//                .database(db)
//                .collection(collection)
//                .keys(collectionKey.toMap())
                .dao(this)
                .builder();
    }

    public Update<T> update() {
        return new Update.Builder()
                .dao(this)
                .builder();
    }

}
