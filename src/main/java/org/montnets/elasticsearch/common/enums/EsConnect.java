package org.montnets.elasticsearch.common.enums;




import org.apache.http.Header;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsConnect.java
* @Description: ES连接常量类 
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月27日 下午4:13:19 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月27日     chenhj          v1.0.0               修改原因
 */
public enum EsConnect {
	INSTANCE;
	
	/**
	 * 连接协议
	 */
	public static final String HTTP ="http"; 
	/**
	 * 连接协议
	 */
	public static final String HTTPS ="https";
	/**
	 * POST
	 */
	public static final String POST ="POST";
	/**
	 * 空的请求报头
	 */
	public static final Header[] EMPTY_HEADERS = new Header[0];
	/**
	 * 默认连接超时
	 */
    public static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 1000;
    /**
     * 默认网络超时
     */
    public static final int DEFAULT_SOCKET_TIMEOUT_MILLIS = 30000;
    /**
     * ES客户端请求超时默认时间,注意:就算超时了命令还是在后台执行
     */
    public static final int DEFAULT_MAX_RETRY_TIMEOUT_MILLIS = DEFAULT_SOCKET_TIMEOUT_MILLIS;
    /**
     * 默认连接请求超时
     */
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT_MILLIS = 500;
    /**
     *  知识点:maxConnTotal 和 maxConnPerRoute 的区别？
		maxConnTotal 是整个连接池的大小，根据自己的业务需求进行设置
		maxConnPerRoute 是单个路由连接的最大数，可以根据自己的业务需求进行设置
		比如maxConnTotal =200，maxConnPerRoute =100，那么，如果只有一个路由的话，那么最大连接数也就是100了；如果有两个路由的话，那么它们分别最大的连接数是100，总数不能超过200
     */
    
    /**
     * 路由连接的最大数，实际的单个连接池大小，如tps定为50，那就配置50
     */
    public static final int DEFAULT_MAX_CONN_PER_ROUTE = 10;
    /**
     * 连接池的大小,最大不要超过1000
     */
    public static final int DEFAULT_MAX_CONN_TOTAL = 30;
}
