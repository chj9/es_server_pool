package com.elasticsearch.server.poolTest;


/**
 * @author XueXianlei
 * @Description:
 * @date Created in 2018/2/26 11:40
 */
public class HttpClientFactory {
    private static HttpAsyncClient httpAsyncClient = new HttpAsyncClient();

    private static HttpSyncClient httpSyncClient = new HttpSyncClient();


    private HttpClientFactory() {
    }

    private static HttpClientFactory httpClientFactory = new HttpClientFactory();

    public static HttpClientFactory getInstance() {

        return httpClientFactory;

    }

    public HttpAsyncClient getHttpAsyncClientPool() {
        return httpAsyncClient;
    }

    public HttpSyncClient getHttpSyncClientPool() {
        return httpSyncClient;
    }

}
