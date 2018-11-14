package com.elasticsearch.server.poolTest;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * @author XueXianlei
 * @Description:
 * @date Created in 2018/2/26 11:40
 */
public class HttpClientService {


    protected void exeAsyncReq(String baseUrl, boolean isPost,
            List<BasicNameValuePair> urlParams,
                               List<BasicNameValuePair> postBody, FutureCallback callback)
            throws Exception {

        if (baseUrl == null) {
        	System.out.println("we don't have base url, check config");
            throw new Exception("missing base url");
        }

        HttpRequestBase httpMethod;
        CloseableHttpAsyncClient hc = null;
        try {
            hc = HttpClientFactory.getInstance().getHttpAsyncClientPool()
                    .getAsyncHttpClient();

            hc.start();

            HttpClientContext localContext = HttpClientContext.create();
            BasicCookieStore cookieStore = new BasicCookieStore();

            if (isPost) {
                httpMethod = new HttpPost(baseUrl);

                if (null != postBody) {
                    System.out.println("exeAsyncReq post postBody={}"+postBody);
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                            postBody, "UTF-8");
                    ((HttpPost) httpMethod).setEntity(entity);
                }

                if (null != urlParams) {

                    String getUrl = EntityUtils
                            .toString(new UrlEncodedFormEntity(urlParams));

                    httpMethod.setURI(new URI(httpMethod.getURI().toString()
                            + "?" + getUrl));
                }

            } else {

                httpMethod = new HttpGet(baseUrl);

                if (null != urlParams) {

                    String getUrl = EntityUtils
                            .toString(new UrlEncodedFormEntity(urlParams));

                    httpMethod.setURI(new URI(httpMethod.getURI().toString()
                            + "?" + getUrl));
                }
            }

            System.out.println("exeAsyncReq getparams:" + httpMethod.getURI());

            localContext.setAttribute(HttpClientContext.COOKIE_STORE,
                    cookieStore);

            hc.execute(httpMethod, localContext, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void exeSyncReq(String baseUrl, boolean isPost,
                               List<BasicNameValuePair> urlParams,
                               List<BasicNameValuePair> postBody, FutureCallback callback)
            throws Exception {
    		//获取可关闭的 httpCilent
        CloseableHttpClient httpClient = HttpClientFactory.getInstance().getHttpSyncClientPool()
                .getHttpClient();

        HttpPost httpPost = new HttpPost(baseUrl);

        String strResult = "";
        try {
            if(urlParams != null){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParams,"UTF-8");
                //设置post求情参数
                httpPost.setEntity(entity);
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Integer statusCode = null;
            if(httpResponse != null){
                statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                }
            }else{
                System.out.println("返回响应码=" + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(httpClient != null){
                    httpClient.close(); //释放资源
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(strResult);


    }

    protected String getHttpContent(HttpResponse response) {

        HttpEntity entity = response.getEntity();
        String body = null;

        if (entity == null) {
            return null;
        }

        try {

            body = EntityUtils.toString(entity, "utf-8");

        } catch (ParseException e) {

            System.out.println("the response's content inputstream is corrupt");
            e.printStackTrace();
        } catch (IOException e) {

        	System.out.println("the response's content inputstream is corrupt");
        	e.printStackTrace();
        }
        return body;
    }
}
