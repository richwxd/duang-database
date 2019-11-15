package com.duangframework.db.mongodb;

import com.mongodb.client.model.DBCollectionUpdateOptions;

/**
 * 更新参数配置
 *
 * @author Laotang
 */
public class UpdateOptions {

    private DBCollectionUpdateOptions options;

    public UpdateOptions() {
        this.options = new DBCollectionUpdateOptions();
        duang();
    }

    private void duang() {
        //查询记录不存在时，不新增记录, 为true则新增记录
        options.upsert(false);
        // 是否符合查询多条记录
        options.multi(true);
        // 写入验证，为true是退出验证
        options.bypassDocumentValidation(false);
    }

    public DBCollectionUpdateOptions getOptions() {
        return options;
    }
}
