package com.elasticsearch.server.poolTest;


import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.concurrent.FutureCallback;

/**
 * @author XueXianlei
 * @Description:
 * @date Created in 2018/2/26 11:06
 */
public class TestCallback implements FutureCallback<HttpResponse> {
    @Override
    public void completed(HttpResponse httpResponse) {
        System.out.println("completed");
        HttpClientUtils.closeQuietly(httpResponse);

    }

    @Override
    public void failed(Exception e) {
        System.out.println("failed");
        e.printStackTrace();
    }

    @Override
    public void cancelled() {
        System.out.println("取消");
    }
}
