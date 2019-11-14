package com.duangframework.db.common;

import com.duangframework.db.core.IDao;

public class Update<T> {

    private IDao<T> dao;

    public Update() {

    }

    private Update(IDao<T> dao) {
        this.dao = dao;
    }

    public static class Builder<T> {
        private IDao<T> dao;
        public Builder() {}
        public Update.Builder dao(IDao<T> dao) {
            this.dao = dao;
            return this;
        }
        public Update builder() {
            return new Update(dao);
        }
    }

}
