package org.montnets.elasticsearch.handle.action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.montnets.elasticsearch.common.util.MyTools;
import org.montnets.elasticsearch.entity.EsRequestEntity;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: InsertIndexAction.java
* @Description: 该类的功能描述
*ES插入数据公共类  
* @version: v1.0.0
* @author: chenhj
* @date: 2018年5月25日 下午3:13:22 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月12日     chenhj          v1.0.0               修改原因
 */
public class InsertHandler {
	  /**索引库*/
	  private String index;
	  /**索引表*/
	  private String type;	  
	  private RestHighLevelClient rhlClient;
	  private boolean docAsUpsert=false;
	  /**
	   * 插入失败数据的集合
	   */
	  private List<String> listFailuresData =null;
	  /*****ID字段名*******/
	  private String idFieldName=null;
	  private static final Logger LOG = LogManager.getLogger(InsertHandler.class);
	public InsertHandler(RestHighLevelClient rhlClient,EsRequestEntity esRequestEntity){
		this.index=Objects.requireNonNull(esRequestEntity.getIndex(), "index can not null");
		this.type =Objects.requireNonNull(esRequestEntity.getType(), "type can not null");		
		this.rhlClient=rhlClient;
	}
	/**
	 * 设置是否存在更新不存在插入
	 * @param docAsUpsert true：存在更新不存在插入  false:数据直接覆盖    默认为false
	 * @return
	 */
	public InsertHandler docAsUpsert(boolean docAsUpsert){
		this.docAsUpsert=docAsUpsert;
		return this;
	}
	/**
	 * 设置ID字段名 从map中get
	 * @param idFieldName
	 * @return
	 */
	public InsertHandler setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
		return this;
	}
	public  boolean insertBulk(List<Map<String,Object>> list) throws Exception{
		if(docAsUpsert&&Objects.isNull(idFieldName)){
			throw new NullPointerException("如果是设置docAsUpsert为true,则idField必须存在");
		}
		return insert(Objects.requireNonNull(list, "list can not null"),idFieldName);
	}
	public  boolean insertOne(Map<String,Object> map) throws Exception{
		if(docAsUpsert&&Objects.isNull(idFieldName)){
			throw new NullPointerException("如果是设置docAsUpsert为true,则idField必须存在");
		}
		return insert(Objects.requireNonNull(map, "map can not null"),idFieldName);
	}
	/**
	 * 批量插入ES库
	 * @param list 需要插入的数据列表
	 * @param idkey 每条数据的主键名
	 */
	private boolean insert(Object dataObj,String idField) throws Exception{
		 boolean falg=true;
		 try {
		  String id = null;
		 /***********批量插入****************/
	     if(dataObj instanceof List){
	    	 BulkRequest request = new BulkRequest();
	    	 @SuppressWarnings("unchecked")
			List<Map<String,Object>> list = (List<Map<String, Object>>) dataObj;
			 for(Map<String,Object> map:list){
				    //如果数据为空或者null则跳过
				 	if(Objects.isNull(map)||!map.isEmpty()){
				 		continue;
				 	}
				 	 if(Objects.nonNull(idField)){		
						 id = String.valueOf(map.get(idField));
						 //如果没有这个ID字段名则跳出不给保存
						 if(MyTools.isEmpty(id)||"null".equals(id)){
							 continue;
						 }
				 	 }
					 if(docAsUpsert){
						 //存在更新,不存在插入
					     request.add(new UpdateRequest(index,type,id).doc(map,XContentType.JSON).docAsUpsert(docAsUpsert).retryOnConflict(5));					
					 }else{
						 if(Objects.nonNull(id)){
							 //直接插入,存在直接覆盖
							 request.add(new IndexRequest(index,type,id).source(map,XContentType.JSON));
						 }else{
							 request.add(new IndexRequest(index,type).source(map,XContentType.JSON));
						 }
					 }
			 }
			 int actionNum = request.numberOfActions();
			 int actionNumTemp=0;
			 //如果不为空就写入
			 if(actionNum>0){
				 BulkResponse bulkResponse = rhlClient.bulk(request);
				 //响应失败的数据过来写入日志
				 if(bulkResponse.hasFailures()){
					 listFailuresData = new ArrayList<String>();
					 for (BulkItemResponse bulkItemResponse : bulkResponse) {
						    if (bulkItemResponse.isFailed()) { 
						        BulkItemResponse.Failure failure = bulkItemResponse.getFailure(); 
						        listFailuresData.add(failure.toString());
						        actionNumTemp=actionNumTemp+1;
						    }
						}
				 }
			 } 
			 //如果批量提交的数据和失败的条数一样,则判定为保存失败
			 if(actionNumTemp==actionNum){
				 LOG.error("如果批量提交的数据和失败的条数一样,则判定为保存失败!数据失败条数:{}",actionNumTemp);
				 falg=false;
			 }
	     /************单条插入**************/		 
	     }else if(dataObj instanceof Map){
	    	@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) dataObj;
			//如果数据为空或者null则跳过
			if(Objects.isNull(map)||!map.isEmpty()){
			 	return false;
			}
			 	 if(Objects.nonNull(idField)){		
					 id = String.valueOf(map.get(idField));
					 if(MyTools.isEmpty(id)||"null".equals(id)){
						 throw new RuntimeException("主键必须设值!!!当前主键"+idFieldName+"="+id);
					 }
			 	 }
			 if(docAsUpsert){
				 //存在更新,不存在插入
				 UpdateRequest request = new UpdateRequest(index,type,id).doc(map,XContentType.JSON).docAsUpsert(docAsUpsert)
						 .retryOnConflict(5);	
				 rhlClient.update(request);
			 }else{
				 if(Objects.nonNull(id)){
					 rhlClient.index( new IndexRequest(index,type,id).source(map,XContentType.JSON));
				 }else{
					 rhlClient.index( new IndexRequest(index,type).source(map,XContentType.JSON));
				 }
			 }
	     }
		} catch (Exception e) {
			throw e;
		}
	    return falg; 
	}
	/**
	 * 获取插入失败数据的集合
	 * @return 如果为null则没有失败数据
	 */
	public List<String> getListFailuresData() {
		return listFailuresData;
	}
	
}
