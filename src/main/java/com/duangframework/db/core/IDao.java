package com.duangframework.db.core;

import com.duangframework.db.common.Page;
import com.duangframework.db.common.Query;
import com.duangframework.db.common.Update;
import org.bson.Document;

import java.util.List;

/**
 * Dao接口,定义操作方法
 *
 * @author Laotang
 */
public interface IDao<T> {

    /**
     * 保存对象
     * @param entity 待保存的对象
     * @return 成功返回Entity对象
     */
    T save(T entity) throws DbException;

    /**
     * 查找一条记录
     * @param query
     * @return
     * @throws DbException
     */
    T findOne(Query<T> query) throws DbException;

    /**
     *根据查询条件查找记录
     * @param query
     * @return
     * @throws DbException
     */
    List<T> findList(Query<T> query) throws DbException;

    /**
     * 根据查询条件更新记录
     * @param query 查询对象
     * @param update 更新对象
     * @return 成功更新返回true
     */
    boolean update(Query<T> query, Update<T> update);

    /**
     * 分页查询
     * @param query 查询条件
     * @return 分页Page对象
     * @throws DbException
     */
    Page<T> findPage(Query query) throws DbException;

    /**
     * 新增记录时，必须要保证有ID值
     * 即由外部指定ObjectId值再新增
     * @param entity
     * @return 新增时是否成功
     * @throws Exception
     */
    boolean insert(T entity) throws DbException;

    /**
     * 根据查询条件进行汇总
     * @param query		查询条件
     * @return  记录数
     */
    long count(Query query) throws DbException;
}
