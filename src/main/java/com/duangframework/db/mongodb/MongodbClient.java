package com.duangframework.db.mongodb;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.IClient;
import com.duangframework.db.core.IConnectOptions;
import com.duangframework.db.core.ServerNodeAddress;
import com.duangframework.db.utils.MD5;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.*;
import com.mongodb.client.model.DBCollectionFindOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Mongodb Client
 *
 * @author Laotang
 */
public class MongodbClient implements IClient<MongoClient> {

    private static final Logger logger = LoggerFactory.getLogger(MongodbClient.class);

    private IConnectOptions connectOptions;
    private MongoClient mongoClient;
    private MongoClientOptions.Builder clientOptionsBuilder;
    private String clientId;
    private DBCollectionFindOptions findOptions;

    public MongodbClient(IConnectOptions connectOptions) {
        this.connectOptions = connectOptions;
    }


    public MongodbClient(String clientId, IConnectOptions connectOptions) {
        this.clientId = clientId;
        this.connectOptions = connectOptions;
    }

    /**
     * 有自定义的优先取，没有则取指定的database，如果还是没有就根据MD5(host:port)作为ID
     *
     * @return 客户端ID
     */
    @Override
    public String getClientId() {
        if(ToolsKit.isEmpty(clientId)) {
            clientId = connectOptions.getDataBase();
            if(ToolsKit.isEmpty(clientId)) {
                List<ServerNodeAddress> serverNodeAddressList = connectOptions.getServerAddressList();
                StringBuilder clientIdString = new StringBuilder();
                for (ServerNodeAddress address : serverNodeAddressList) {
                    clientIdString.append(address.getHost()).append(":").append(address.getPort());
                }
                clientId = MD5.MD5Encode(clientIdString.toString());
            }
        }
        return clientId;
    }

    @Override
    public IConnectOptions getOptions() {
        return null;
    }

    @Override
    public MongoClient getClient() throws DbException {
        java.util.Objects.requireNonNull(connectOptions, "链接属性对象不能为空");
        clientOptionsBuilder = MongoClientOptions.builder().readPreference(ReadPreference.secondaryPreferred());
        String url = connectOptions.getUrl();
        if(ToolsKit.isEmpty(url)) {
            MongoCredential credential =  security();
            mongoClient = ToolsKit.isEmpty(credential) ? new MongoClient(getServerAddressList(), clientOptionsBuilder.build()) :
                    new MongoClient(getServerAddressList(), credential, clientOptionsBuilder.build());
        } else {
            mongoClient = getClientWithURL(url);
        }
        return mongoClient;
    }

    /*
     * 取链接地址
     * @return
     */
    private List<ServerAddress> getServerAddressList() {
        List<ServerNodeAddress> nodeTmpList = connectOptions.getServerAddressList();
        List<ServerAddress> mongoNodeList = null;
        if( ToolsKit.isNotEmpty(nodeTmpList) ) {
            mongoNodeList = new ArrayList<>(nodeTmpList.size());
            for (ServerNodeAddress nodeAddress : nodeTmpList) {
                mongoNodeList.add(new ServerAddress(nodeAddress.getHost(), nodeAddress.getPort()));
                logger.warn("connect mongodb : " + nodeAddress.toString());
            }
        }
        if(ToolsKit.isEmpty(mongoNodeList)) {
            throw new NullPointerException("链接MongoDB节点集合为空，请确保按规则填写节点host与port");
        }
        return mongoNodeList;
    }

    /**
     *  数据库鉴权, 目前仅支持SHA1方式加密
     *
     * @return MongoCredential
     */
    private MongoCredential security() {
        MongoCredential credential = null;
        if(ToolsKit.isNotEmpty(connectOptions.getUserName()) && ToolsKit.isNotEmpty(connectOptions.getPassword()) ) {
            credential = MongoCredential.createScramSha1Credential(
                    connectOptions.getUserName(),
                    connectOptions.getDataBase(),
                    connectOptions.getPassword().toCharArray());
        }
        return credential;
    }

    private MongoClient getClientWithURL(String url) {
        MongoClientURI mongoClientURI = new MongoClientURI(url, clientOptionsBuilder);
        return new MongoClient(mongoClientURI);
    }

    @Override
    public void close() throws DbException {
        if(ToolsKit.isNotEmpty(mongoClient)) {
            mongoClient.close();
        }
    }
}
