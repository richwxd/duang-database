package com.duangframework.db.mongodb;

import com.duangframework.db.common.Query;
import com.duangframework.db.core.IConnectOptions;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.BasicDBObject;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.DBCollectionCountOptions;

import java.util.concurrent.TimeUnit;

/**
 * Count参数配置
 * Created by laotang on 2019/11/19.
 */
public class CountOptions<T> {
    private DBCollectionCountOptions options;

    public CountOptions(Query<T> query, IConnectOptions connectOptions) {
        this.options = new DBCollectionCountOptions();
        duang(query, connectOptions);
    }

    private void duang(Query<T> query, IConnectOptions connectOptions) {
        if(ToolsKit.isNotEmpty(query.getHint())) {
            options.hint(new BasicDBObject(query.getHint()));
        }
        options.maxTime(connectOptions.getMaxTime(), TimeUnit.MILLISECONDS);
        options.readPreference(ReadPreference.secondaryPreferred());
    }

    public DBCollectionCountOptions getOptions() {
        return options;
    }
}
