package org.montnets.elasticsearch.config;



import java.io.Serializable;
import java.util.Arrays;
import org.apache.http.HttpHost;
import org.montnets.elasticsearch.common.enums.EsConnect;
import org.montnets.elasticsearch.common.util.IpHandler;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsConfig.java
* @Description: 
*	ES连接配置类
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月24日 上午11:54:35 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月24日     chenhj          v1.0.0               修改原因
*/
public class EsConnectConfig implements Serializable{
	private static final long serialVersionUID = 1L;
	/*********以下为ES集群配置*********/
	/**
	 * 节点IP列表 格式:ip:port   如 127.0.0.1:9200
	 */
	private  String[] nodes;
	/**
	 * 连接协议
	 */
	private  String scheme=EsConnect.HTTP;
	/**
	 * 集群名称
	 */
	private  String clusterName;
    /**
     * username
     */
 //   private  String username;

    /**
     * password
     */
  //  private  String password;
    
    
	/*********以下为连接配置*********/
    /******设置连接超时时间********/
    private  int connectTimeoutMillis = EsConnect.DEFAULT_CONNECT_TIMEOUT_MILLIS;
 	/******设置网络超时时间********/
    private  int socketTimeoutMillis = EsConnect.DEFAULT_SOCKET_TIMEOUT_MILLIS;
 	 /******设置连接请求超时时间********/
    private  int connectionRequestTimeoutMillis = EsConnect.DEFAULT_CONNECTION_REQUEST_TIMEOUT_MILLIS;
 	 /******设置ES连接超时时间********/
    private  int maxRetryTimeoutMillis=EsConnect.DEFAULT_MAX_RETRY_TIMEOUT_MILLIS;
 	 /******设置路由连接最大数********/
    private  int maxConnPerRoute = EsConnect.DEFAULT_MAX_CONN_PER_ROUTE;
 	 /******设置连接池大小********/
    private  int maxConnTotal =EsConnect.DEFAULT_MAX_CONN_TOTAL; 	  
    
	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}
	/**
	 * 设置连接超时时间
	 * @param connectTimeoutMillis 单位:毫秒
	 */
	public void setConnectTimeoutMillis(int connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
	}
	public int getSocketTimeoutMillis() {
		return socketTimeoutMillis;
	}
	/**
	 * 设置网络超时时间
	 * @param socketTimeoutMillis 单位:毫秒
	 */
	public void setSocketTimeoutMillis(int socketTimeoutMillis) {
		this.socketTimeoutMillis = socketTimeoutMillis;
	}
	public int getConnectionRequestTimeoutMillis() {
		return connectionRequestTimeoutMillis;
	}
	/**
	 * 设置连接请求超时时间
	 * @param connectionRequestTimeoutMillis 单位:毫秒
	 */
	public void setConnectionRequestTimeoutMillis(int connectionRequestTimeoutMillis) {
		this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
	}
	public int getMaxRetryTimeoutMillis() {
		return maxRetryTimeoutMillis;
	}
	/**
	 * 设置ES连接超时时间
	 * @param maxRetryTimeoutMillis 单位:毫秒
	 */
	public void setMaxRetryTimeoutMillis(int maxRetryTimeoutMillis) {
		this.maxRetryTimeoutMillis = maxRetryTimeoutMillis;
	}
	public int getMaxConnPerRoute() {
		return maxConnPerRoute;
	}
	 /**
	  * 设置路由连接最大数，路由连接的最大数，实际的单个连接池大小，如tps定为50，那就配置50
	  * @param maxConnPerRoute
	  */
	public void setMaxConnPerRoute(int maxConnPerRoute) {
		this.maxConnPerRoute = maxConnPerRoute;
	}
	public int getMaxConnTotal() {
		return maxConnTotal;
	}
	/**
	 * 设置连接池大小,连接池的大小,最大不要超过1000
	 * @param maxConnTotal
	 */
	public void setMaxConnTotal(int maxConnTotal) {
		this.maxConnTotal = maxConnTotal;
	}

	public  String getScheme() {
		return scheme;
	}
	/**
	 * 连接集群协议
	 * @param scheme 默认http
	 */
	public  void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public  String getClusterName() {
		return clusterName;
	}
	/**
	 * 设置集群名称
	 * @param clusterName
	 */
	public  void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public  HttpHost[] getNodes() throws IllegalAccessException {
		if(nodes==null||nodes.length==0){
			throw new IllegalAccessException("非法节点,nodes为："+Arrays.toString(nodes));
		}
		HttpHost[] ips = new HttpHost[nodes.length];
	   	 for(int i=0;i<nodes.length;i++){
			 try {
	    		 String url = nodes[i];
	    		 IpHandler addr = new  IpHandler();  
	    	     addr.IpPortFromUrl(url);
	        	 HttpHost httpHost = new HttpHost(addr.getIp(),addr.getPort(),scheme);
	        	 ips[i]=httpHost;
			} catch (Exception e) {
				throw new RuntimeException("执行异常",e);
			}
		 }
		return ips;
	}
	/**
	 * 设置集群IP
	 * @param nodes 格式为ip:端口  如 127.0.0.1:9200
	 */
	public  void setNodes(String[] nodes) {
		this.nodes = nodes;
	}
	@Override
	public String toString() {
		return "EsConnectConfig [nodes=" + Arrays.toString(nodes) + ", scheme=" + scheme + ", clusterName="
				+ clusterName + "]";
	}
}
