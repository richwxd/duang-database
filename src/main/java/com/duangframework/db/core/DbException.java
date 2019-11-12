package com.duangframework.db.core;

public class DbException extends RuntimeException {

    public DbException() {

    }

    public DbException(String message) {
        super(message);
    }

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }

}
