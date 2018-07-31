package com.elasticsearch.server.esServer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.sort.SortOrder;
import org.montnets.elasticsearch.client.pool.PoolConfig;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.common.enums.EsConnect;
import org.montnets.elasticsearch.config.EsBasicModelConfig;
import org.montnets.elasticsearch.config.EsConnectConfig;
import org.montnets.elasticsearch.entity.EsRequestEntity;
import org.montnets.elasticsearch.entity.ScrollEntity;
import org.montnets.elasticsearch.handle.action.IndexHandler;
import org.montnets.elasticsearch.handle.action.InsertHandler;
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
	private static Logger logger = LogManager.getLogger(EsTest.class);
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
		//把资源返回连接池时检查是否有效
		config.setTestOnReturn(true);
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
		indexConfig.setMaxResultDataCount(10000);//设置最大可取多少数据,或分页能到多深的数据 默认10000(不建议设置大)
//		//开始创建
		logger.info("创建索引库:{}",index.createIndex(indexConfig));
//		//检查索引库是否存在
		logger.info("检查索引库是否存在:{}",index.existsIndex("demo"));
		 /*************索引库插入示例*******************/
			 EsRequestEntity esRequestEntity = new EsRequestEntity("demo","demo");
			 InsertHandler insert = new InsertHandler(client, esRequestEntity);
			 //要插入的内容
			 List<Map<String,Object>> list = new ArrayList<>();
			 Map<String,Object> map = new HashMap<>();
			 map.put("id",523456+"rfdd");
			 list.add(map);
			 //自定义ID值,从数据map中获取,如果不自定义取消掉即可
			 insert.setIdFieldName("id");
			 //批量插入
			 insert.insertBulk(list);
			 //查看是否有插入失败的数据
			 logger.info("插入失败数据:{}",insert.getListFailuresData());
		 /***************索引库查询示例1:普通查询****************/	
			 //设置获取多少条数据,默认1W条
			 esRequestEntity.setLimit(1);
			 SearchHandler search = new SearchHandler(client, esRequestEntity);
			 //设置条件
			 //search.setQueryBuilder(null);
			 //设置排序,如示例则是根据id字段倒序
			 search.addSort("id",SortOrder.DESC);
			 List<Map<String,Object>> dataList=search.sraechSourceAsList();
			 //数据
			 logger.info("普通查询数据:{}",dataList);
			 logger.info("普通查询DSL:{}",search.toDSL());
			 //根据条件查询总数
			 logger.info("查询总数:{}",search.count());
			 //获取条件DSL,可直接在Kibana中直接运行
			 logger.info("总数查询DSL:{}",search.toDSL());
		 /***************索引库查询示例2:数据遍历****************/		
			 ScrollEntity<Map<String,Object>>  scrollEntity = new ScrollEntity<>();
			 //设置滚动间隔
			 scrollEntity.setKeepAlive(10);
			 do{
				 //执行遍历
				 scrollEntity=search.searchScroll(scrollEntity);
				 //获取数据
				 dataList=scrollEntity.getDataList();
				 logger.info("滚动查询数据:{}",dataList);
				 /******对数据进行处理的逻辑*********/
				  //....
			}while(Objects.nonNull(dataList)&&!dataList.isEmpty());//如果List为空则是取不到数据了,意味着已经取出完毕
			 logger.info("滚动查询DSL:{}",search.toDSL());
			 //清除遍历ID(可选)
			 search.clearScroll(scrollEntity.getScrollId());
		/***************索引库查询示例2:分页查询****************/	 
	    //注意:浅分页建议用,深分页不建议使用
			 //设置需要分页
			 esRequestEntity.setNeedPaging(true);
			 //页数,默认1
			 esRequestEntity.setPageNo(1);
			//每页的数据量,默认10条
			 esRequestEntity.setPageSize(1);
			 //重新实例一个查询对象
			 SearchHandler search1 = new SearchHandler(client, esRequestEntity);
			 //执行查询，dataList为查询出来的数据
			 dataList=search1.sraechSourceAsList();
			 logger.info("分页查询数据:{}",dataList);
			 //该分页数据总量,只有执行分页了才能得到数据总量
			 long total = search1.getTotalCount();
			 logger.info("分页查询总数:{}",total);
			 logger.info("分页查询DSL:{}",search1.toDSL());
		}catch (Exception e) {
			e.printStackTrace();
			//关闭连接池
			pool.close();
		}finally {
			 //连接用完还给连接池,这是必须的,放在finally是为了防止发生异常不能正常把正常连接返回造成堵塞
			// 被归还的对象的引用，不可以再次归还
			// java.lang.IllegalStateException: Object has already been retured to this pool or is invalid
			pool.returnConnection(client);
			//获得激活数
			 logger.info("池中激活数:{}",pool.getNumActive());
			//获得空闲数
			 logger.info("池中空闲数:{}",pool.getNumIdle());
		}
		
    }
    public static void main(String[] args) throws Exception {
		new EsTest().EsConnectPoolDemo();
	}
}
