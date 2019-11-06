package com.duangframework.db.core;

/**
 * 服务集群地址对象
 *
 * @author Laotang
 */
public class ServerNodeAddress {

    private String host;
    private Integer port;
    private String desc;

    public ServerNodeAddress() {
    }

    public ServerNodeAddress(String host, Integer port, String desc) {
        this.host = host;
        this.port = port;
        this.desc = desc;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ServerNodeAddress{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", desc='" + desc + '\'' +
                '}';
    }
}
