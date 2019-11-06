package com.duangframework.db.core;

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

}
