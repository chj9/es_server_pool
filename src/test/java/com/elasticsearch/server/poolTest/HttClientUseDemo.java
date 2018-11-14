package com.elasticsearch.server.poolTest;


import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XueXianlei
 * @Description:
 * @date Created in 2018/2/26 11:42
 */
public class HttClientUseDemo extends HttpClientService {

    public static void main(String[] args) {


        for (int i = 0; i<10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new HttClientUseDemo().getConfCall();
                }
            }).start();
        }
    }

    public void getConfCall() {

        String url = "http://www.baidu.com";

        exeHttpReq(url, false, null, null, new TestCallback());
    }

    public void exeHttpReq(String baseUrl, boolean isPost,
                           List<BasicNameValuePair> urlParams,
                           List<BasicNameValuePair> postBody,
                           FutureCallback<HttpResponse> callback) {

        try {

            exeSyncReq(baseUrl, isPost, urlParams, postBody, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
