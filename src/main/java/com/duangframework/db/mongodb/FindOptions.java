package com.duangframework.db.mongodb;

import com.duangframework.db.common.Page;
import com.duangframework.db.common.Query;
import com.duangframework.db.core.IConnectOptions;
import com.duangframework.db.utils.MongoUtils;
import com.duangframework.db.utils.ToolsKit;
import com.mongodb.BasicDBObject;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.DBCollectionFindOptions;

import java.util.concurrent.TimeUnit;

/**
 * 查询参数配置
 *
 * @author Laotang
 */
public class FindOptions<T> {

    private DBCollectionFindOptions options;

    /**
     * 构造方法
     * @param query 查询对象
     * @param connectOptions 链接对象
     */
    public FindOptions(Query<T> query, IConnectOptions connectOptions) {
        options = new DBCollectionFindOptions();
        setDefaultOptions(connectOptions);
        duang(query);
    }

    private void duang(Query<T> query) {
        Page<T> page = query.getPageObj();
        //查询返回字段
        BasicDBObject fieldDbo = (BasicDBObject) MongoUtils.convert2DBFields(query.getFields());
        if(ToolsKit.isNotEmpty(fieldDbo) && !fieldDbo.isEmpty()) {
            options.projection(fieldDbo);
        }
        // 排序
        BasicDBObject orderDbo =  (BasicDBObject)MongoUtils.convert2DBOrder(query.getOrderObj());
        if(ToolsKit.isNotEmpty(orderDbo) && !orderDbo.isEmpty()) {
            options.sort(orderDbo);
        }
        //分页
        if((page.getSkipNum() > -1 || page.getPageNo() > 0) && page.getPageSize() > 1) {
            options.skip(getSkipNum(page));
            options.limit(page.getPageSize());
        }
        // 强制查询条件
        BasicDBObject hintDbo = new BasicDBObject(query.getHint());
        if(ToolsKit.isNotEmpty(hintDbo) && !hintDbo.isEmpty()) {
            options.hint(hintDbo);
        }

    }

    DBCollectionFindOptions getOptions() {
        return options;
    }

    /**设置默认部份*/
    private void setDefaultOptions(IConnectOptions connectOptions) {
        options.maxTime(connectOptions.getMaxTime(), TimeUnit.MILLISECONDS);
        options.maxAwaitTime(connectOptions.getMaxAwaitTim(), TimeUnit.MILLISECONDS);
        options.readPreference(ReadPreference.secondaryPreferred());
    }

    /**
     * 确定跳过的行数
     * @param page 分布对象
     * @return  行数
     */
    private int getSkipNum(Page<T> page) {
        int skipNum = page.getSkipNum();
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        if(skipNum > -1) {
            return skipNum;
        } else if(pageSize == -1) {
            return 0;
        } else {
            return (pageNo>0 ? (pageNo-1) : pageNo) * pageSize;
        }
    }

}
