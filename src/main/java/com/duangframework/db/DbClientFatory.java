package com.duangframework.db;

import com.duangframework.db.core.IDao;
import com.duangframework.db.core.IDbClient;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class DbClientFatory {

    private String clientId;
    public static final Map<String, IDbClient> CLIENT_MAP = new HashMap();
    public static final Map<Class, Object> DAO_MAP = new HashMap();

    private static class DbClientHolder {
        private static final DbClientFatory INSTANCE = new DbClientFatory();
    }
    private DbClientFatory() {
    }
    public static final DbClientFatory duang() {
        clear();
        return DbClientHolder.INSTANCE;
    }
    private static void clear(){

    }



    /**
     *设置客户端ID
     * @param clientId 客户端ID
     * @return
     */
    public DbClientFatory id(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * 根据客户端ID取得客户端对象
     * @param <T> 客户端泛型
     * @return
     */
    public <T> T getClient() {
        if (CLIENT_MAP.size() > 1) {
            java.util.Objects.requireNonNull(clientId, "客户端ID不能为空");
            return (T) CLIENT_MAP.get(clientId);
        } else {
            return getDefaultClient();
        }
    }

    public <T> T getDefaultClient() {
        return (T) CLIENT_MAP.entrySet().iterator().next().getValue();
    }

    public void setDao(Class clazz, Object dao) {
        DAO_MAP.put(clazz, dao);
    }

    public <T> T getDao(Class clazz) {
        return (T) DAO_MAP.get(clazz);
    }
}
