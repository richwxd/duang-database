package com.duangframework.db.core;

import com.duangframework.db.enums.EnvEnum;

import java.util.ArrayList;
import java.util.List;

public abstract class ConnectOptions implements IConnectOptions {

    public static final String HOST_FIELD = "host";
    public static final String PORT_FIELD = "port";
    public static final String DATABASE_FIELD = "database";
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String URL_FIELD = "url";

    protected String host;
    protected int port;
    protected String database;
    protected String username;
    protected String password;
    protected String url;
    protected List<ServerNodeAddress> serverNodeAddressList = new ArrayList<>();
    protected Long maxTimeMS = 1000L;
    protected Long maxAwaitTimeMS = 1000L;
    protected EnvEnum envEnum;

    public ConnectOptions(){

    }

    public ConnectOptions(String host, int port, String database) {
        this(host,port, database, null, null);
    }

    public ConnectOptions(String host, int port, String database, String username, String password) {
        this(host, port, database, username, password, 1000L, 1000L, EnvEnum.DEV);
    }

    public ConnectOptions(String host, Integer port, String database, String username, String password,
                          Long maxTimeMS, Long maxAwaitTimeMS, EnvEnum envEnum) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        serverNodeAddressList.add(new ServerNodeAddress(host, port, "Main Database"));
        this.maxTimeMS = maxTimeMS;
        this.maxAwaitTimeMS = maxAwaitTimeMS;
        this.envEnum = envEnum;
    }

    public ConnectOptions(String url) {
        this.url = url;
    }

    public ConnectOptions(List<ServerNodeAddress> serverNodeAddressList) {
        this.serverNodeAddressList.addAll(serverNodeAddressList);
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getDataBase() {
        return database;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public List<ServerNodeAddress> getServerAddressList() {
        return serverNodeAddressList;
    }

    @Override
    public Long getMaxTime() {
        return maxTimeMS;
    }

    @Override
    public Long getMaxAwaitTim() {
        return maxAwaitTimeMS;
    }

    @Override
    public EnvEnum getEnv() {
        return envEnum;
    }

    @Override
    public String toString() {
        return "ConnectOptions{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", serverNodeAddressList=" + serverNodeAddressList +
                '}';
    }
}
