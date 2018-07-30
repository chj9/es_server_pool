package com.elasticsearch.server.esServer;


import org.apache.commons.pool2.PooledObject;
import org.elasticsearch.client.RestHighLevelClient;
import org.montnets.elasticsearch.client.pool.PoolConfig;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.common.enums.EsConnect;
import org.montnets.elasticsearch.config.EsBasicModelConfig;
import org.montnets.elasticsearch.config.EsConnectConfig;
import org.montnets.elasticsearch.entity.EsRequestEntity;
import org.montnets.elasticsearch.handle.action.IndexHandler;
import org.montnets.elasticsearch.handle.action.SearchHandler;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsTest.java
* @Description: ES连接池使用示例
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月30日 下午4:00:05 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月30日     chenhj          v1.0.0               修改原因
 */
public class EsTest {

       //连接池对象 = 连接池所维护的对象的创建工厂 + 连接池对象配置  
    public void EsConnectPoolDemo() throws Exception {
    	/**************连接池设置*******************/
		PoolConfig config = new PoolConfig();
		//池中最大连接数 默认 8
		config.setMaxTotal(8);
		//最大空闲数,当超过这个数的时候关闭多余的连接 默认8
		config.setMaxIdle(8);
		//最少的空闲连接数  默认0
		config.setMinIdle(0); 
		//当连接池资源耗尽时,调用者最大阻塞的时间,超时时抛出异常 单位:毫秒数   -1表示无限等待
		config.setMaxWaitMillis(10000);
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
		EsConnectionPool pool = new EsConnectionPool(config, esConnectConfig);
		/******获取一个连接(该连接会一直存在池中直到被销毁)*********/
		RestHighLevelClient client = pool.getConnection();
	 try {
		/**************索引库设置以及检查创建****************/
		IndexHandler index = new IndexHandler(client);
//		//新建一个索引库
		EsBasicModelConfig indexConfig = new EsBasicModelConfig("demo", "demo");
//		//设置数据模版(不设置ES会自动识别)
		indexConfig.setMappings("{\"demo\": {\"properties\" : {\"id\": {\"type\": \"long\"}}}}");
//		//设置索引设置(默认 5个分片,一个副本)
		indexConfig.setSettings("{\"number_of_shards\" :5,\"number_of_replicas\" : 1,\"refresh_interval\" : \"5s\"}");
//		//设置最大可取多少数据,或分页能到多深的数据 默认10000(不建议设置大)
		indexConfig.setMaxResultDataCount(10000);
//		//开始创建
		System.out.println("创建索引库:"+index.createIndex(indexConfig));
//		//检查索引库是否存在
		System.out.println("检查索引库是否存在:"+index.existsIndex("demo"));

		/***************索引库查询示例****************/
		//EsRequestEntity esRequestEntity = new EsRequestEntity<>("im_msg_test","im_msg");
		
		//SearchHandler search = new SearchHandler(client, esRequestEntity);
		//search.setQueryBuilder(null);
		//System.out.println(search.count());
		//System.out.println(search.toDSL());
		
		
		//获得激活数
		System.out.println(pool.getNumActive());
		//获得空闲数
		System.out.println(pool.getNumIdle());
		//关闭连接池
		pool.close();
		} finally {
			//连接用完还给连接池
			// 被归还的对象的引用，不可以再次归还
			// java.lang.IllegalStateException: Object has already been retured to this pool or is invalid
			pool.returnConnection(client);
		}
    }
    public static void main(String[] args) throws Exception {
		new EsTest().EsConnectPoolDemo();
	}
}
