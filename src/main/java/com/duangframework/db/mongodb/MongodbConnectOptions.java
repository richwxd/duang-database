package com.duangframework.db.mongodb;

import com.duangframework.db.core.ConnectOptions;
import com.duangframework.db.core.ServerNodeAddress;
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

    private MongodbConnectOptions(String host, int port, String database, String username, String password, Long maxTimeMS, Long maxAwaitTimeMS) {
        super(host, port, database, username, password, maxTimeMS, maxAwaitTimeMS);
    }

    private MongodbConnectOptions(String url) {
        super(url);
    }

    @NotThreadSafe
    public static class Builder {

        private String host;
        private Integer port;
        private String database;
        private String username;
        private String password;
        private String url;
        private Long maxTimeMS;
        private Long maxAwaitTimeMS;

        private List<ServerNodeAddress> serverNodeAddressList = new ArrayList<>();

        public Builder() {
            this.host = "127.0.0.1";
            this.port = 27017;
            maxTimeMS = 1000L;
            maxAwaitTimeMS = 1000L;
        }

        public MongodbConnectOptions.Builder host(String host) {
            this.host = host;
            return this;
        }

        public MongodbConnectOptions.Builder port(int port) {
            this.port = port;
            return this;
        }

        public MongodbConnectOptions.Builder dataBase(String database) {
            this.database = database;
            return this;
        }

        public MongodbConnectOptions.Builder userName(String username) {
            this.username = username;
            return this;
        }

        public MongodbConnectOptions.Builder passWord(String password) {
            this.password = password;
            return this;
        }

        public MongodbConnectOptions.Builder url(String url) {
            this.url = url;
            return this;
        }

        public MongodbConnectOptions.Builder serverNodeAddressList(List<ServerNodeAddress> serverNodeAddressList) {
            this.serverNodeAddressList.addAll(serverNodeAddressList);
            return this;
        }

        public MongodbConnectOptions.Builder getMaxTime(Long maxTimeMS) {
            this.maxTimeMS = maxTimeMS;
            return this;
        }
        public MongodbConnectOptions.Builder getMaxAwaitTim(Long maxAwaitTimeMS) {
            this.maxAwaitTimeMS = maxAwaitTimeMS;
            return this;
        }

        public MongodbConnectOptions build() {
            return ToolsKit.isEmpty(url) ?
                    new MongodbConnectOptions(host, port, database, username, password, maxTimeMS, maxAwaitTimeMS) :
                    new MongodbConnectOptions(url);
        }

    }
}
