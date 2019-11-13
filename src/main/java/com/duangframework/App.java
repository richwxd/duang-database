package com.duangframework;

import com.duangframework.db.DbClientFatory;
import com.duangframework.db.common.Query;
import com.duangframework.db.entity.Logs;
import com.duangframework.db.mongodb.MongoDao;
import com.duangframework.db.mongodb.MongodbClient;
import com.duangframework.db.mongodb.MongodbConnectOptions;
import org.bson.types.ObjectId;

import java.util.Date;

public class App {
    public static void main(String[] args) {
        MongodbClient mongodbClient = new MongodbClient(new MongodbConnectOptions.Builder()
                .dataBase("openAGV")
                .host("172.20.10.6")
                .port(27017)
                .userName("admin")
                .passWord("1b88ab6d")
                .build());
        try {
//            MongoClient client = mongodbClient.getClient();
            DbClientFatory.CLIENT_MAP.put(mongodbClient.getClientId(), mongodbClient);
            Logs entity = new Logs();
            entity.setCreateTime(new Date());
            entity.setState(200);
            String requestId = new ObjectId().toString();
            entity.setRequestId(requestId);
            entity.setResult("##,,A030,,s,,rptmag,,1::1::1::1,,ac74,,ZZ");
            entity.setType("rptmag");
            MongoDao<Logs> logsDao = new MongoDao<>(mongodbClient.getClientId(), Logs.class);
            logsDao.save(entity);

            Query query = new Query();
            query.eq(Logs.REQUEST_ID, requestId);
            Logs logs = logsDao.findOne(query);
            System.out.println(logs.toString());

//            List<String> dbList = client.getDatabaseNames();
//            System.out.println("dbList:   " + dbList);
//            client.getDatabaseNames().iterator().forEachRemaining(new Consumer<String>() {
//                @Override
//                public void accept(String s) {
//                    System.out.println("###############: " + s);
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Order o = new Order("", Order.SortEnum.ASC).add("", null);
    }
}
