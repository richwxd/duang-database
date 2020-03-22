package com.duangframework.db.mongodb;

import com.duangframework.db.DbClientFatory;
import com.duangframework.db.common.Page;
import com.duangframework.db.common.Query;
import com.duangframework.db.common.Update;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.ConverterKit;
import com.duangframework.db.core.IConnectOptions;
import com.duangframework.db.core.IDao;
import com.duangframework.db.entity.IdEntity;
import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.utils.VtorKit;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Mongodb Dao对象，提供可操作的方法
 * @param <T>
 */
public class MongoDao<T> implements IDao<T> {

    private MongoClient client;
    private Class<T> entityClass;
    private IConnectOptions connectOptions;

    private DB db;
    private DBCollection dbCollection;
    protected DBObject collectionKey;

    public MongoDao() {

    }

    public MongoDao(String clientId, Class<T> entityClass) {
        this.entityClass =entityClass;
        duang(clientId);
    }

    private void duang(String clientId) {
        MongodbDbClient mongoClient = ToolsKit.isEmpty(clientId) ?
                DbClientFatory.duang().getDefaultClient() :
                DbClientFatory.duang().id(clientId).getClient();

        if(ToolsKit.isEmpty(mongoClient)) {
            throw new DbException("根据["+clientId+"]取MongoClient时，MongoClient为空，请检查！");
        }
        client =  mongoClient.getClient();
        connectOptions = mongoClient.getOptions();

        db = client.getDB(connectOptions.getDataBase());
        dbCollection = db.getCollection(ToolsKit.getEntityName(entityClass));

//        database = client.getDatabase(connectOptions.getDataBase());
//        collection = database.getCollection(ToolsKit.getEntityName(entityClass));

//        collectionKey = MongoUtils.convert2DBFields(ToolsKit.getFields(entityClass));
    }


    @Override
    public T save(T entity) throws DbException {

        // 保存入库前，先进行参数验证
        try {
            entity = VtorKit.validate(entity);
        } catch (ValidatorException ve) {
            throw new DbException("该操作失败，实体类参数验证不通过: " + ve.getMessage(), ve);
        }

        // 验证通过后再保存到数据库
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
     * @throws DbException
     */
    private boolean doSaveOrUpdate(IdEntity entity) throws DbException {

        Map<String, Object> encodeMap = ConverterKit.duang().encode(entity);

        if (encodeMap.isEmpty()) {
            throw new DbException("encodeMap为空");
        }

        String id = entity.getId();
        try {
            DBObject dbObject = new BasicDBObject(encodeMap);
            if(ToolsKit.isEmpty(id)) {
                dbCollection.save(dbObject, new InsertOptions().getWriteConcern());
                entity.setId(dbObject.get(IdEntity.ID_FIELD).toString());
            } else {
                update(id, dbObject);
            }
            return true;
        }catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }

    }

    /**
     * 根据id更新记录
     * @param id 记录ID
     * @param dbObject  记录对象
     * @return 更新成功返回true
     */
    public boolean update(String id, DBObject dbObject) throws DbException {

        if (!ObjectId.isValid(id)){
            throw new DbException("id is not ObjectId!");
        }

        DBObject query = new BasicDBObject(IdEntity.ID_FIELD, new ObjectId(id));
        dbObject.removeField(IdEntity.ENTITY_ID_FIELD);
        BasicDBObject updateDbo = new BasicDBObject(Operator.SET, dbObject);
        WriteResult writeResult =  dbCollection.update(query, updateDbo, new UpdateOptions().getOptions());
        return writeResult.getN() > 0 ? true : false;
    }

    @Override
    public boolean update(Query<T> query, Update<T> update) {
        DBObject queryDbo = new BasicDBObject(query.getQuery());
        DBObject updateDbo = new BasicDBObject(update.getUpdate());
        if(ToolsKit.isEmpty(queryDbo) || ToolsKit.isEmpty(updateDbo)) {
            throw new DbException("Mongodb Update is Fail: queryDbo or updateDbo is null");
        }
        WriteResult writeResult = dbCollection.update(queryDbo, updateDbo, new UpdateOptions().getOptions());
        return writeResult.getN() > 0 ? true : false;
    }

    /**
     * 根据条件查询记录
     * @param query		查询条件对象
     * @return 泛型对象
     * @throws Exception
     */
    public T findOne(Query query) throws DbException {
        if(ToolsKit.isEmpty(query)) {
            throw new DbException("Mongodb findOne is Fail: mongoQuery is null");
        }
        List<T> resultList = findList(query);
        if(ToolsKit.isEmpty(resultList)) {
            return null;
        }
        return resultList.get(0);
    }

    /**
     * 根据查询条件查找记录
     * @param query		查询条件
     * @return 结果集合，元素为指定的泛型
     * @throws Exception
     */
    @Override
    public List<T> findList(Query query) throws DbException {
        if(null == query) {
            throw new DbException("Mongodb findList is Fail: query is null");
        }
        DBObject queryDbo = new BasicDBObject(query.getQuery());
        DBCursor dbCursor = dbCollection.find(queryDbo, new FindOptions(query, connectOptions).getOptions());
        final List<T> resultList = new ArrayList();
        while (dbCursor.hasNext()) {
            DBObject dbObject = dbCursor.next();
            resultList.add((T) ConverterKit.duang().decode(dbObject.toMap(), entityClass));
        }
        return resultList;
    }

    /**
     * 分页查找记录，按Page对象返回
     * @param query		查询条件
     * @return	分页对象
     * @throws Exception
     */
    @Override
    public Page<T> findPage(Query query) throws DbException {
        if(ToolsKit.isEmpty(query)) {
            throw new DbException("Mongodb findPage is Fail: query is null");
        }
        Page<T> page = query.getPage();
        List<T> list = findList(query);
        if(ToolsKit.isEmpty(list)) {
            return null;
        }
        page.setResult(list);
        if(page.isAutoCount()) {
            page.setTotalCount(count(query));
        }
        return page;
    }

    /**
     * 根据查询条件进行汇总
     * @param query		查询条件
     * @return  记录数
     */
    @Override
    public long count(Query query) throws DbException {
        DBObject queryDbo = new BasicDBObject(query.getQuery());
        return dbCollection.count(queryDbo, new CountOptions(query, connectOptions).getOptions());
    }

    /**
     * 新增记录时，必须要保证有ID值
     * 即由外部指定ObjectId值再新增
     * @param entity
     * @return 新增时是否成功
     * @throws Exception
     */
    @Override
    public boolean insert(T entity) throws DbException {
        IdEntity idEntity = (IdEntity)entity;
        if(ToolsKit.isEmpty(idEntity.getId())) {
            throw new DbException("document is fail: id is null");
        }
        Map<String,Object> map = ConverterKit.duang().encode(idEntity);
        if(ToolsKit.isEmpty(map)) {
            throw new DbException("encode entity to map is fail: map is null");
        }
        try {
            DBObject insertDbo = new BasicDBObject(map);
            dbCollection.insert(insertDbo, new InsertOptions().getWriteConcern());
            return true;
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public Query<T> query() {
        return new Query.Builder()
                .dao(this)
                .env(connectOptions.getEnv())
                .builder();
    }

    public Update<T> update() {
        return new Update.Builder()
                .dao(this)
                .builder();
    }

}
