package com.clover.spark.Constants;

public interface Constants {
    // 30 sec, the time for waiting until a connection is established
    static final int CONNECTION_TIMEOUT = 30 * 1000;
    // 30 sec, the time for waiting for a connection from connection pool
    static final int CONNECTION_REQUEST_TIMEOUT = 30 * 1000;
    // 60 sec, the time for waiting for data
    static final int SOCKET_TIMEOUT = 60 * 1000;
    // 20 sec
    static final int DEFAULT_KEEP_ALIVE_TIME = 20 * 1000;

    // Connection pool
    static final int MAX_ROUTE_CONNECTIONS     = 40;
    static final  int MAX_TOTAL_CONNECTIONS     = 40;
    static final int MAX_LOCALHOST_CONNECTIONS = 80;

    //POST URL for storing / persist data in cassandra.
    static final String CASSANDRA_STREAM_INSERT_URI = "";

    static final String THREAD_NAME_PREFIX = "Async-Clover-";
}
