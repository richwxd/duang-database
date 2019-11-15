package com.duangframework.db.core;

public interface IDbClient<T> {

    /**
     * 客户端在缓存池里唯一的ID
     * @return
     */
    String getClientId();

    /**
     * 取客户端链接信息对象
     * @return
     */
    IConnectOptions getOptions();

    /**
     * 取客户端实例
     * @return
     */
    T getClient() throws DbException;

    /**
     * 关闭客户端实例
     * @throws Exception
     */
    void close() throws DbException;

}
