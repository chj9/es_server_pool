package org.montnets.elasticsearch.client.pool.es;



import java.io.IOException;
import java.util.Objects;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.montnets.elasticsearch.enums.EsConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**  
 * @Title:  RestClient.java   
 * @Description:  TODO(用一句话描述该文件做什么)  
 * Es中的rest客户端
 * 文档地址:
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.2/java-rest-high-getting-started-initialization.html 
 * @author: chenhongjie     
 * @date:   2018年4月8日 下午4:43:43   
 * @version V1.0 
 */
public class RestClientFactory {
		private static final Logger logger = LoggerFactory.getLogger(RestClientFactory.class);
	 	public  int CONNECT_TIMEOUT_MILLIS = EsConnect.DEFAULT_CONNECT_TIMEOUT_MILLIS;//连接时间
	    public  int SOCKET_TIMEOUT_MILLIS = EsConnect.DEFAULT_SOCKET_TIMEOUT_MILLIS;//等待时间
	    public  int CONNECTION_REQUEST_TIMEOUT_MILLIS = EsConnect.DEFAULT_CONNECTION_REQUEST_TIMEOUT_MILLIS;
	    public  int MAX_RETRY_TIMEOUT_MILLIS=EsConnect.DEFAULT_MAX_RETRY_TIMEOUT_MILLIS; //查询超时时间设为5分钟
	    public  int MAX_CONN_PER_ROUTE = EsConnect.DEFAULT_MAX_CONN_PER_ROUTE;
	    public  int MAX_CONN_TOTAL =EsConnect.DEFAULT_MAX_CONN_TOTAL; 	    
	    private HttpHost[] HTTP_HOST;
	    private RestClientBuilder builder;
	    private RestHighLevelClient restHighLevelClient;
	    protected  RestClientFactory (Integer maxConnectNum, Integer maxConnectPerRoute,HttpHost... httpHost){
	        HTTP_HOST = httpHost;
	        MAX_CONN_TOTAL = maxConnectNum;
	        MAX_CONN_PER_ROUTE = maxConnectPerRoute;
	    }
	    protected  RestClientFactory(Integer connectTimeOut, Integer socketTimeOut,
	            Integer connectionRequestTime,int retryTimeoutMillis,Integer maxConnectNum, Integer maxConnectPerRoute,HttpHost... httpHost){
	        HTTP_HOST = httpHost;
	        CONNECT_TIMEOUT_MILLIS = connectTimeOut;
	        SOCKET_TIMEOUT_MILLIS = socketTimeOut;
	        CONNECTION_REQUEST_TIMEOUT_MILLIS = connectionRequestTime;
	        MAX_CONN_TOTAL = maxConnectNum;
	        MAX_RETRY_TIMEOUT_MILLIS=retryTimeoutMillis;
	        MAX_CONN_PER_ROUTE = maxConnectPerRoute;
	    }
	    protected void init(){
	        builder = RestClient.builder(HTTP_HOST);
	        //设置超时
	        builder.setMaxRetryTimeoutMillis(MAX_RETRY_TIMEOUT_MILLIS);
	        setConnectTimeOutConfig();
	        setMutiConnectConfig();
	        restHighLevelClient = new RestHighLevelClient(builder);
	      //  restClient = builder.build();
	        logger.info("Initialization elasticsearch success!!");
	    }
	    // 配置连接时间延时
	    private void setConnectTimeOutConfig(){
	        builder.setRequestConfigCallback(new RequestConfigCallback() {
	            @Override
	            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
	                requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
	                requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
	                requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
	                return requestConfigBuilder;
	            }
	        });
	    }
	    /**
	     * 使用异步httpclient时设置并发连接数
	     */
	    private void setMutiConnectConfig(){
	        builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
	            @Override
	            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
	                httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
	                httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
	                return httpClientBuilder;
	            }
	        });
	    }
	    protected  RestHighLevelClient getRhlClient() {
	        return restHighLevelClient;
	    }
	    protected void close() {
	        if (Objects.nonNull(restHighLevelClient)) {
	            try {
	            	restHighLevelClient.close();
	            } catch (IOException e) {
	            	logger.error(" Failure to shut down：",e);
	            }
	        }
	        logger.info("Close restHighLevelClient Success");
	    }
}
