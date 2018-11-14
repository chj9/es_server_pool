package com.elasticsearch.server.poolTest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author XueXianlei
 * @Description:
 * @date Created in 2018/2/26 11:37
 */
public class HttpSyncClient {

    private static int socketTimeout = 5000;// 设置等待数据超时时间5秒钟 根据业务调整

    private static int connectTimeout = 2000;// 连接超时

    private static int maxConnNum = 100;// 连接池最大连接数

    private static int maxPerRoute = 30;// 每个主机的并发最多只有1500

    private static PoolingHttpClientConnectionManager cm;

    private static RequestConfig requestConfig;

    private static final String DEFAULT_ENCODING = Charset.defaultCharset()
            .name();

    // proxy代理相关配置
    private String host = "";
    private int port = 0;
    private String username = "";
    private String password = "";

    private CloseableHttpClient httpClient;


    // 应用启动的时候就应该执行的方法
    public HttpSyncClient() {

        this.httpClient = createClient();

    }

    public CloseableHttpClient createClient() {

        try {
            //添加对https的支持，该sslContext没有加载客户端证书
            // 如果需要加载客户端证书，请使用如下sslContext,其中KEYSTORE_FILE和KEYSTORE_PASSWORD分别是你的证书路径和证书密码
            //KeyStore keyStore  =  KeyStore.getInstance(KeyStore.getDefaultType()
            //FileInputStream instream =   new FileInputStream(new File(KEYSTORE_FILE));
            //keyStore.load(instream, KEYSTORE_PASSWORD.toCharArray());
            //SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore,KEYSTORE_PASSWORD.toCharArray())
            // .loadTrustMaterial(null, new TrustSelfSignedStrategy())
            //.build();

            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build();

            LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslsf)
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(maxConnNum);
            cm.setDefaultMaxPerRoute(maxPerRoute);
        }catch (Exception e){
            e.printStackTrace();
        }

        requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(5000)
                .setSocketTimeout(socketTimeout).setRedirectsEnabled(true).build();


        return  HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();

    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

}
