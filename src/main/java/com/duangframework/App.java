package com.duangframework;

import com.duangframework.db.DbClientFatory;
import com.duangframework.db.common.Query;
import com.duangframework.db.common.Update;
import com.duangframework.db.entity.IdEntity;
import com.duangframework.db.entity.Logs;
import com.duangframework.db.enums.EnvEnum;
import com.duangframework.db.mongodb.MongoDao;
import com.duangframework.db.mongodb.MongodbDbClient;
import com.duangframework.db.mongodb.MongodbConnectOptions;
import com.duangframework.db.utils.DuangId;
import org.bson.types.ObjectId;

import java.util.*;

public class App {
    public static void main(String[] args) {
        MongodbDbClient mongodbClient = new MongodbDbClient(new MongodbConnectOptions.Builder()
                .dataBase("openAGV")
                .host("192.168.8.184")
                .port(27017)
                .userName("admin")
                .passWord("1b88ab6d")
                .env(EnvEnum.PRO)
//                .dataBase("test")
//                .host("42.96.139.238")
//                .port(14823)
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
            entity.setStringList(new ArrayList<String>(){{
                this.add("laotang");
                this.add("laotang1");
                this.add("laotang2");
            }});
            entity.setIntegerList(new ArrayList<Integer>(){{
                this.add(1);
                this.add(2);
                this.add(3);
            }});
            entity.setDoubleList(new ArrayList<Double>(){{
                this.add(1.1D);
                this.add(2.2D);
                this.add(3.3D);
            }});

            List<Logs> logss = new ArrayList<>();
            logss.add(new Logs(new ObjectId().toString()));
            logss.add(new Logs(new ObjectId().toString()));
            logss.add(new Logs(new ObjectId().toString()));
            entity.setLogsList(logss);

            Map<String,String> strMap = new HashMap<>();
            strMap.put("name","laotang");
            strMap.put("sex","男");
            strMap.put("年龄","40");
            entity.setStringMap(strMap);

            Map<String, Logs> logsMap = new HashMap<>();
            logsMap.put("第1位", new Logs(new ObjectId().toString()));
            logsMap.put("第2位", new Logs(new ObjectId().toString()));
            logsMap.put("第3位", new Logs(new ObjectId().toString()));
            entity.setLogsMap(logsMap);

            Logs logs123 = new Logs();
            logs123.setId(new DuangId().toString());
            logs123.setRequestId(new DuangId().toHexString());
            logs123.setCmd("getmag");
            logs123.setCreateTime(new Date());
            logs123.setUpdateTime(new Date());
            logs123.setResult("##,,A030,,s,,getmag,,0,,ad7d,,ZZ");
            logs123.setLogsMap(logsMap);
            logs123.setStringMap(strMap);
            entity.setLogItem(logs123);


            Integer[] integerArray = new Integer[2];
            integerArray[0] = 1;
            integerArray[1] = 2;
            entity.setIntegerArray(integerArray);

            entity.setStatus(IdEntity.STATUS_FIELD_SUCCESS);

            MongoDao<Logs> logsDao = new MongoDao<>(mongodbClient.getClientId(), Logs.class);
            logsDao.save(entity);

//            Query query = new Query();
//            query.eq(Logs.REQUEST_ID, requestId);
//                        Logs logs = logsDao.findOne(query);

            Logs logs = logsDao.query().eq(Logs.REQUEST_ID, requestId).findOne();
//            logsDao.query(query).update(udpate).execute();

            System.out.println(logs.toString());
            System.out.println(logs.getRequestId());
            System.out.println(logs.getIntegerArray()[0]);


            Update<Logs> update = new Update<>();
            update.set(Logs.REQUEST_ID, UUID.randomUUID().toString());
            System.out.println(update.getUpdate());

            Query<Logs> query =new Query();
            query.eq(IdEntity.ID_FIELD, logs.getId());
//            query.eq(Logs.REQUEST_ID, requestId);
            System.out.println(query.getQuery());

            boolean isUpdate = logsDao.update(query,update);
            System.out.println("##############:" + isUpdate);

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
