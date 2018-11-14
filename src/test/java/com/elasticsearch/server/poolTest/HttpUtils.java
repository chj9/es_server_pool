package com.elasticsearch.server.poolTest;


import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.util.List;

/**
 * @author XueXianlei
 * @Description:
 * @date Created in 2018/3/13 13:23
 */
public class HttpUtils {


    /**
     * 使用httpClient发送http get请求
     * @param url
     * @return
     */
    public static String sendGetByHttpClient(String url){
        String result = "";

        CloseableHttpClient httpClient = HttpClientFactory.getInstance().getHttpSyncClientPool().getHttpClient();
        CloseableHttpResponse response = null;
        try {

            HttpGet httpGet = new HttpGet(url);

            response = httpClient.execute(httpGet);
            //判断状态码是否等于200
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(response.getEntity());
                }
            }
        } catch (IOException e) {
        	System.out.println("httpClient连接失败:" + url);
        }finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 发送post请求
     * @param url
     * @param params
     * @return
     */
    public static String sendPostByHttpClient(String url, List<BasicNameValuePair> params) {
        CloseableHttpClient httpClient = HttpClientFactory.getInstance().getHttpSyncClientPool().getHttpClient();
        CloseableHttpResponse httpResponse = null;
        String strResult = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                //设置post求情参数
                httpPost.setEntity(entity);
            }
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null) {
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return strResult;
    }
}
