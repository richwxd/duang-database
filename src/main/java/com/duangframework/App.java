package com.duangframework;

import com.duangframework.db.common.Order;
import com.duangframework.db.mongodb.MongodbClient;
import com.duangframework.db.mongodb.MongodbConnectOptions;
import com.mongodb.MongoClient;

public class App {
    public static void main(String[] args) {
        MongodbClient mongodbClient = new MongodbClient(new MongodbConnectOptions.Builder()
                .dataBase("openAGV")
                .host("")
                .build());
        try {
            MongoClient client = mongodbClient.getClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Order o = new Order("", Order.SortEnum.ASC).add("", null);
    }
}
