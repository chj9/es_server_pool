package com.elasticsearch.server.esServer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.montnets.elasticsearch.client.EsPool;
import org.montnets.elasticsearch.client.pool.PoolConfig;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.common.enums.ConditionType;
import org.montnets.elasticsearch.common.enums.EsConnect;
import org.montnets.elasticsearch.condition.ConditionEs;
import org.montnets.elasticsearch.config.EsBasicModelConfig;
import org.montnets.elasticsearch.config.EsConnectConfig;
import org.montnets.elasticsearch.entity.EsRequestEntity;
import org.montnets.elasticsearch.entity.ScrollEntity;
import org.montnets.elasticsearch.entity.SettingEntity;
import org.montnets.elasticsearch.handle.action.AggregationEsHandler;
import org.montnets.elasticsearch.handle.action.DeleteEsHandler;
import org.montnets.elasticsearch.handle.action.IndexEsHandler;
import org.montnets.elasticsearch.handle.action.InsertEsHandler;
import org.montnets.elasticsearch.handle.action.SearchEsHandler;
import org.montnets.elasticsearch.handle.action.UpdateEsHandler;
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
	EsConnectionPool pool =null;
       //连接池对象 = 连接池所维护的对象的创建工厂 + 连接池对象配置  
    public void EsConnectPoolDemo() throws Exception {

		
	 try {
		 	init();
			//获得激活数
			 logger.info("池中激活数:{}",pool.getNumActive());
			//获得空闲数
			 logger.info("池中空闲数:{}",pool.getNumIdle());
		 	//queryCountTest();
			 deleteTest();
			//获得激活数
			 logger.info("池中激活数:{}",pool.getNumActive());
			//获得空闲数
			 logger.info("池中空闲数:{}",pool.getNumIdle());
			
		}catch (Exception e) {
			e.printStackTrace();
			//关闭连接池
		//	pool.close();
		}finally {
			 //连接用完还给连接池,这是必须的,放在finally是为了防止发生异常不能正常把正常连接返回造成堵塞
			// 被归还的对象的引用，不可以再次归还
			// java.lang.IllegalStateException: Object has already been retured to this pool or is invalid
//			pool.returnConnection(client);
//			//获得激活数
//			 logger.info("池中激活数:{}",pool.getNumActive());
//			//获得空闲数
//			 logger.info("池中空闲数:{}",pool.getNumIdle());
		}
		
    }
    public void init(){
    	/**************连接池设置*******************/
    	//如果池已经初始化,先关闭再初始化.
    	if(pool!=null){
    				pool.close();
    	}
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
		//设置集群名称
		esConnectConfig.setClusterName("bigData-cluster");
		//集群IP数组
		String [] nodes={"192.169.2.98:9200","192.169.2.188:9200","192.169.2.156:9200","192.169.0.24:9200"};
		esConnectConfig.setNodes(nodes);
		//设置集群连接协议,默认http
		esConnectConfig.setScheme(EsConnect.HTTP);
		//把连接池配置和ES集群配置加载进池中
		pool = new EsConnectionPool(config, esConnectConfig);
		/***************如果不使用我的封装处理类可这样获取对象返还对象********************/
		//获取对象
		//RestHighLevelClient client = pool.getConnection();
		//返还对象
		//pool.returnConnection(client);
		//设为程序全局可用这个连接池
		EsPool.ESCLIENT.setPool(pool);
    }
    public void indexTest(){
//		/**************索引库设置以及检查创建****************/
		IndexEsHandler index = new IndexEsHandler();
		try {
	//		//新建一个索引库
			EsBasicModelConfig indexConfig = new EsBasicModelConfig("demo", "demo");
	//		//设置数据模版(不设置ES会自动识别)
			indexConfig.setMappings("{\"demo\": {\"properties\" : {\"id\": {\"type\": \"long\"}}}}");
			//设置索引设置(默认 5个分片,一个副本)
			SettingEntity setting = new SettingEntity(5,1,5,12000);//分片,副本,刷新,单次可取最大数据条数和分页深度
			indexConfig.setSettings(setting);
			//这里设置为null即可
			index.builder(null);
	//		//开始创建
			logger.info("创建索引库:{}",index.createIndex(indexConfig));
	//		//检查索引库是否存在
			logger.info("检查索引库是否存在:{}",index.existsIndex("demo"));
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			index.close();
		}
    }
    public void insertTest(){
		 /*************索引库插入示例*******************/
		 EsRequestEntity esRequestEntity = new EsRequestEntity("demo","demo");
		 InsertEsHandler insert = new InsertEsHandler();
		 try {
			 //存在更新不存在插入   true:开启  false：关闭(默认)
			 insert.docAsUpsert(true);
			 //要插入的内容
			 List<Map<String,Object>> list = new ArrayList<>();
			 Map<String,Object> map = new HashMap<>();
			 map.put("id",523456);
			 list.add(map);
			 //自定义ID值,从数据map中获取,如果不自定义取消掉即可,不定义将自动生成ID
			 insert.setIdFieldName("id");
			 //设置配置
			 insert.builder(esRequestEntity);
			 //执行批量插入
			 insert.insertBulk(list);
			 //执行单条插入
			 insert.insertOne(map);
			 //查看是否有插入失败的数据
			 logger.info("插入失败数据:{}",insert.getListFailuresData());
		} catch (Exception e) {
				// TODO: handle exception
		}finally{
			insert.close();
		}
    }
    public void queryEasyTest(){
		 /***************索引库查询示例1:普通查询****************/	
    		EsRequestEntity esRequestEntity = new EsRequestEntity("demo","demo");
			SearchEsHandler search = new SearchEsHandler();
			try {
				 //设置获取多少条数据,默认1W条
				 esRequestEntity.setLimit(1);
				 //设置条件
				 //search.setQueryBuilder(null);
				 //设置配置
				 search.builder(esRequestEntity);
				 //设置排序,如示例则是根据id字段倒序
				 search.addSort("id",SortOrder.DESC);
				 List<Map<String,Object>> dataList=search.sraechSourceAsList();
				 //数据
				 logger.info("普通查询数据:{}",dataList);
				 logger.info("普通查询DSL:{}",search.toDSL());
			} catch (Exception e) {
				
			}finally{
				search.close();
			}
    }
    public void queryCountTest(){
		 /***************索引库查询示例1:普通查询****************/	
    	  EsRequestEntity esRequestEntity = new EsRequestEntity("demo","demo");
		  SearchEsHandler search = new SearchEsHandler();
		  try {
			 //设置条件
			 //search.setQueryBuilder(null);
			 //设置配置
			 search.builder(esRequestEntity);
			 //根据条件查询总数
			 logger.info("查询总数:{}",search.count());
			 //获取条件DSL,可直接在Kibana中直接运行
			 logger.info("总数查询DSL:{}",search.toDSL());
			} catch (Exception e) {
					// TODO: handle exception
			}finally {
				//使用完关闭,避免造成堵塞
				search.close();	
			}
    }
    public void queryPageTest(){
		/***************索引库查询示例2:分页查询****************/	
    	 EsRequestEntity esRequestEntity = new EsRequestEntity("demo","demo");
		 //实例一个查询对象
		 SearchEsHandler search = new SearchEsHandler();
		 try {
				 //注意:浅分页建议用,深分页不建议使用
				 //设置需要分页
				 esRequestEntity.setNeedPaging(true);
				 //页数,默认1
				 esRequestEntity.setPageNo(1);
				//每页的数据量,默认10条
				 esRequestEntity.setPageSize(1);
				 //设置配置(可不设置)
				 search.addSort("id", SortOrder.DESC);
				 //设置配置
				 search.builder(esRequestEntity);
				 //执行查询，dataList为查询出来的数据
				 List<Map<String,Object>> dataList=search.sraechSourceAsList();
				 logger.info("分页查询数据:{}",dataList);
				 //该分页数据总量,只有执行分页了才能得到数据总量
				 long total = search.getTotalCount();
				 logger.info("分页查询总数:{}",total);
				 logger.info("分页查询DSL:{}",search.toDSL());
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				search.close();
			}
    }
    public void scrollQueryTest(){
		 /***************索引库查询示例2:数据遍历****************/		
    	EsRequestEntity esRequestEntity = new EsRequestEntity("demo","demo");
		 ScrollEntity<Map<String,Object>>  scrollEntity = new ScrollEntity<>();
		 //实例一个查询对象
		 SearchEsHandler search = new SearchEsHandler();
		try {
		 //设置滚动间隔
		 scrollEntity.setKeepAlive(10);
		 //设置配置
		 search.builder(esRequestEntity);
		 List<Map<String,Object>> dataList=null;
		 do{
			 //执行遍历
			 scrollEntity=search.searchScroll(scrollEntity);
			 //获取数据
			 dataList=scrollEntity.getDataList();
			 logger.info("滚动查询数据:{}",dataList);
			  //对数据进行处理的逻辑....
			//如果List为空则是取不到数据了,意味着已经取出完毕
		}while(Objects.nonNull(dataList)&&!dataList.isEmpty());
		 logger.info("滚动查询DSL:{}",search.toDSL());
		 //清除遍历ID(可选)
		 search.clearScroll(scrollEntity.getScrollId());
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			search.close();
		}
    }
    public void aggQueryTest(){
    	/*************************/
    	EsRequestEntity esRequestEntity = new EsRequestEntity("demo","demo");
	    //以下聚合类似select recvtime as datatm,mftid,count() as msgNum from im_msg group by recvtime,mftid
	    //自定义时间
	    String DEFAULT_FORMAT="yyyyMMdd";
	    AggregationEsHandler aggsearch = new AggregationEsHandler();
	    try {
		    //设置聚合字段
			AggregationBuilder datatm=AggregationBuilders.dateHistogram("datatm").field("recvtime").format(DEFAULT_FORMAT).interval(86400000);
			AggregationBuilder mftId=AggregationBuilders.terms("mftid").field("mftid");
			//合并聚合字段
			datatm.subAggregation(mftId);
			//设置聚合对象
			aggsearch.setAggregationBuilder(datatm);
		    //设置配置
		    aggsearch.builder(esRequestEntity);
			//执行聚合
			List<Map<String, Object>> resultList =aggsearch.sraechAgg("msgNum");
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			aggsearch.close();
		}
    }
    public void updateTest(){
    	EsRequestEntity esRequestEntity = new EsRequestEntity("demo","demo");
		/************数据更新*************/
		UpdateEsHandler update = new UpdateEsHandler();
		try {
			 //要更新的内容
			 List<Map<String,Object>> list = new ArrayList<>();
			 //更新的数据
			 Map<String,Object> map = new HashMap<>();
			 map.put("id",523456);
			 list.add(map);
			 //必须需要,否则抛出异常
			update.setIdFieldName("id");
			//设置配置
			update.builder(esRequestEntity);
			//执行批量更新
			update.updateBulk(list);
			//执行单条更新
			update.updateOne(map);
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			update.close();
		}
    }
    public void deleteTest(){
    	EsRequestEntity esRequestEntity = new EsRequestEntity("im_msg_test","im_msg");
		/************数据删除*************/
		DeleteEsHandler del = new DeleteEsHandler();
		try {
		ConditionEs con = new ConditionEs().and(ConditionType.gt,"recvtime","2018-08-01 00:00:00.000")
				.and(ConditionType.lt, "recvtime","2018-08-04 23:59:59.999");
		//设置条件
		del.setQueryBuilder(con);
		//根据ID删除数据
		//del.delById("id值");
		//创建接口
		del.builder(esRequestEntity);
		//注意:true为同步删除 ,false为异步删除  需要删除量大时建议使用异步
		del.delDocByQuery(false);

		logger.info("删除查询DSL:{}",del.toDSL());
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			del.close();
		}
    }
    public void conditionTest(){
    	try {
    		ConditionEs con = new ConditionEs()
    				//*******以下开始设置条件**********/
    				//name字段必须存在
    				.and(ConditionType.exist,"name")
    				//age字段必须不存在
    				.and(ConditionType.unexist,"age")
    				//class 必须等于 12
    				.and(ConditionType.equal, "class",12)
    				//name 必须不等于lilin
    				.and(ConditionType.unequal, "bb","lilin")
    				//creattm 必须大于2013-08-02
    				.and(ConditionType.gt, "creattm","2013-08-02")
    				//creattm 必须大于等于2013-08-02
    				.and(ConditionType.gte, "creattm","2013-08-02")
    				//creattm 必须小于2013-08-02
    				.and(ConditionType.lt, "creattm","2013")
    				//creattm 必须小于等于2013-08-02
    				.and(ConditionType.lte, "creattm","2013")
    				//name字段或存在
    				.or(ConditionType.exist,"name")
    				//age字段或不存在
    				.or(ConditionType.unexist,"age")
    				//class 或等于 12
    				.or(ConditionType.equal, "class",12)
    				//name 或不等于lilin
    				.or(ConditionType.unequal, "bb","lilin")
    				//creattm 或大于2013-08-02
    				.or(ConditionType.gt, "creattm","2013-08-02")
    				//creattm 或大于等于2013-08-02
    				.or(ConditionType.gte, "creattm","2013-08-02")
    				//creattm 或小于2013-08-02
    				.or(ConditionType.lt, "creattm","2013")
    				//creattm 或小于等于2013-08-02
    				.or(ConditionType.lte, "creattm","2013");
    		org.elasticsearch.index.query.QueryBuilder queryBuilder =con.toResult();
    		//可直接在kibana中使用
    		System.out.println("打印生成的查询JSON："+con.toDSL());
		} catch (Exception e) {
			// TODO: handle exception
		}

		
		
    }
    public static void main(String[] args) throws Exception {
		new EsTest().EsConnectPoolDemo();
	}
    
    
}
