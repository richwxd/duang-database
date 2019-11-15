package com.duangframework.db.core;

import java.util.List;

/**
 * 连接属性接口
 *
 * @author Laotang
 * @since 1.0
 */
public interface IConnectOptions {

    /**
     * 链接的host地址，不包括端口部份
     * @return  host地址
     */
    String getHost();

    /**
     * 链接的port
     * @return 整数port
     */
    Integer getPort();

    /**
     * 用户名
     * @return 用户名
     */
    String getUserName();

    /**
     * 密码
     * @return 密码
     */
    String getPassword();

    /**
     * 数据库名称
     * @return  数据库名称
     */
    String getDataBase();

    /**
     * 以URL方式链接到数据库
     * @return  链接URL
     */
    String getUrl();

    /**
     * 集群节点地址
     * @return
     */
    List<ServerNodeAddress> getServerAddressList();

    /**最大执行时间*/
    Long getMaxTime();

    /**最大执行时间*/
    Long getMaxAwaitTim();
}
