package org.montnets.elasticsearch.config;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public void setConnectTimeoutMillis(int connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
	}
	public int getSocketTimeoutMillis() {
		return socketTimeoutMillis;
	}
	public void setSocketTimeoutMillis(int socketTimeoutMillis) {
		this.socketTimeoutMillis = socketTimeoutMillis;
	}
	public int getConnectionRequestTimeoutMillis() {
		return connectionRequestTimeoutMillis;
	}
	public void setConnectionRequestTimeoutMillis(int connectionRequestTimeoutMillis) {
		this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
	}
	public int getMaxRetryTimeoutMillis() {
		return maxRetryTimeoutMillis;
	}
	public void setMaxRetryTimeoutMillis(int maxRetryTimeoutMillis) {
		this.maxRetryTimeoutMillis = maxRetryTimeoutMillis;
	}
	public int getMaxConnPerRoute() {
		return maxConnPerRoute;
	}
	public void setMaxConnPerRoute(int maxConnPerRoute) {
		this.maxConnPerRoute = maxConnPerRoute;
	}
	public int getMaxConnTotal() {
		return maxConnTotal;
	}
	public void setMaxConnTotal(int maxConnTotal) {
		this.maxConnTotal = maxConnTotal;
	}
	private List<EsBasicModelConfig> indexList;
	public EsConnectConfig(){
		indexList = new ArrayList<EsBasicModelConfig>();
	}
	public  String getScheme() {
		return scheme;
	}
	public  void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public  String getClusterName() {
		return clusterName;
	}
	public  void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public List<EsBasicModelConfig> getIndexList() {
		return indexList;
	}
	public void add(EsBasicModelConfig esBasicModelConfig) {
		this.indexList.add(esBasicModelConfig);
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
	public  void setNodes(String[] nodes) {
		this.nodes = nodes;
	}
	@Override
	public String toString() {
		return "EsConnectConfig [nodes=" + Arrays.toString(nodes) + ", scheme=" + scheme + ", clusterName="
				+ clusterName + "]";
	}
}
