package com.duangframework.db.mongodb;

import com.duangframework.db.core.ConnectOptions;
import com.duangframework.db.core.ServerNodeAddress;
import com.duangframework.db.enums.EnvEnum;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.annotations.NotThreadSafe;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB链接参数
 *
 * @author Laotang
 */
public class MongodbConnectOptions extends ConnectOptions {


    private MongodbConnectOptions() {

    }

    private MongodbConnectOptions(String host, int port, String database, String username, String password, Long maxTimeMS,
                                  Long maxAwaitTimeMS,EnvEnum envEnum) {
        super(host, port, database, username, password, maxTimeMS, maxAwaitTimeMS, envEnum);
    }

    private MongodbConnectOptions(String url) {
        super(url);
    }

    @NotThreadSafe
    public static class Builder {

        /**host*/
        private String host;
        /**端口*/
        private Integer port;
        /**数据库名称*/
        private String database;
        /**用户名*/
        private String username;
        /**密码*/
        private String password;
        /**以URL方式进行连接*/
        private String url;
        /**最大执行时间*/
        private Long maxTimeMS;
        /**最大等待时间*/
        private Long maxAwaitTimeMS;
        /**环境*/
        private EnvEnum envEnum;
        /**服务器节点地址集合*/
        private List<ServerNodeAddress> serverNodeAddressList = new ArrayList<>();

        public Builder() {
            this.host = "127.0.0.1";
            this.port = 27017;
            maxTimeMS = 3000L;
            maxAwaitTimeMS = 3000L;
            envEnum = EnvEnum.PRO;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }


        public Builder dataBase(String database) {
            this.database = database;
            return this;
        }

        public Builder userName(String username) {
            this.username = username;
            return this;
        }

        public Builder passWord(String password) {
            this.password = password;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder serverNodeAddressList(List<ServerNodeAddress> serverNodeAddressList) {
            this.serverNodeAddressList.addAll(serverNodeAddressList);
            return this;
        }

        public Builder getMaxTime(Long maxTimeMS) {
            this.maxTimeMS = maxTimeMS;
            return this;
        }

        public Builder getMaxAwaitTim(Long maxAwaitTimeMS) {
            this.maxAwaitTimeMS = maxAwaitTimeMS;
            return this;
        }

        public Builder env(EnvEnum envEnum) {
            this.envEnum = envEnum;
            return this;
        }

        public MongodbConnectOptions build() {
            return ToolsKit.isEmpty(url) ?
                    new MongodbConnectOptions(host, port, database, username, password, maxTimeMS, maxAwaitTimeMS, envEnum) :
                    new MongodbConnectOptions(url);
        }

    }
}
