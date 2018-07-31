/**
 * 
 */
package com.elasticsearch.server.esServer;

import org.elasticsearch.client.RestHighLevelClient;
import org.montnets.elasticsearch.client.pool.PoolConfig;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.common.enums.EsConnect;
import org.montnets.elasticsearch.config.EsConnectConfig;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: Th.java
* @Description: 多线程测试
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月31日 下午2:29:06 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月31日     chenhj          v1.0.0               修改原因
*/
public class ThreadTest {
private static EsConnectionPool pool;
public static void main(String[] args) {
	//初始化连接池
	init();
    for ( int i = 0; i < 20; i++) {  //模拟20个客户端请求
        try {
        	RestHighLevelClient client = pool.getConnection();//从池子中获取一个线程 ，如果池子为空，先调用工厂的makeObject()创建
        	System.out.println(i);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
    try {
        Thread. sleep(8000); // 休息一会儿，再使用线程池
    } catch (InterruptedException ex1) {
    }
    System. out.println( "--------初次访问线程池要创建，下面模拟第二次访问线程池，不用再创建，直接使用----------------------" );
    for ( int i = 0; i < 10; i++) {
        try {
        	RestHighLevelClient client = pool.getConnection();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
}
public static void init(){
	/**************连接池设置*******************/
	PoolConfig config = new PoolConfig();
	//池中最大连接数 默认 8   连接数 = ((核心数 * 2) + 有效磁盘数)
	config.setMaxTotal(8);
	//最大空闲数,当超过这个数的时候关闭多余的连接 默认8
	config.setMaxIdle(8);
	//最少的空闲连接数  默认0
	config.setMinIdle(0); 
	//当连接池资源耗尽时,调用者最大阻塞的时间,超时时抛出异常 单位:毫秒数   -1表示无限等待
	config.setMaxWaitMillis(1000);
	// 连接池存放池化对象方式,true放在空闲队列最前面,false放在空闲队列最后  默认为true
	config.setLifo(true); 
	// 连接空闲的最小时间,达到此值后空闲连接可能会被移除,默认即为30分钟  
	config.setMinEvictableIdleTimeMillis(1000L * 60L * 30L); 
	// 连接耗尽时是否阻塞,默认为true,为false时则抛出异常 
	config.setBlockWhenExhausted(true); 
	//向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取。默认为false。建议保持默认值.
	config.setTestOnBorrow(true);
	/**************ES集群配置*******************/
		EsConnectConfig esConnectConfig = new EsConnectConfig();
		esConnectConfig.setClusterName("bigData-cluster");
		String [] nodes={"192.169.2.98:9200","192.169.2.188:9200","192.169.2.156:9200","192.169.0.24:9200"};
		esConnectConfig.setNodes(nodes);
		esConnectConfig.setScheme(EsConnect.HTTP);
		pool = new EsConnectionPool(config, esConnectConfig);
	}
}