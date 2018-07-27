package org.montnets.elasticsearch.action;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.montnets.elasticsearch.util.MyTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class InsertIndexAction {
	  /**索引库*/
	  private String index;
	  /**索引表*/
	  private String type;	  
	  private RestHighLevelClient rhlClient;
		private static final Logger LOG = LoggerFactory.getLogger("esLog");
	public InsertIndexAction(RestHighLevelClient rhlClient,String index,String type){
		this.index=index;
		this.type =type;
		this.rhlClient=rhlClient;
	}
	/**
	 * 批量插入ES库
	 * @param list 需要插入的数据列表
	 * @param idkey 每条数据的主键名
	 */
	public  boolean insertBulkIndex(List<Map<String,Object>> list,String idkey,boolean docAsUpsert) throws Exception{
		 BulkRequest request = new BulkRequest();
		 boolean falg=true;
		 try {
		 for(Map<String,Object> map:list){
			     //有ID则是修改没有则是新增			
				 String id = String.valueOf(map.get(idkey));
				 if(MyTools.isEmpty(id)||"null".equals(id)){
					 continue;
				 } 
				 if(docAsUpsert){
					 //存在更新,不存在插入
					 request.add(new UpdateRequest(index,type,id).doc(map,XContentType.JSON).docAsUpsert(docAsUpsert).retryOnConflict(5));
				 }else{
					 //直接插入,存在直接覆盖
					 request.add(new IndexRequest(index,type,id).source(map,XContentType.JSON));
				 }
		 }
		 int actionNum = request.numberOfActions();
		 int actionNumTemp=0;
		 //如果不为空就写入
		 if(actionNum>0){
			 BulkResponse bulkResponse = rhlClient.bulk(request);
			 //响应失败的数据过来写入日志
			 if(bulkResponse.hasFailures()){
				 for (BulkItemResponse bulkItemResponse : bulkResponse) {
					    if (bulkItemResponse.isFailed()) { 
					        BulkItemResponse.Failure failure = bulkItemResponse.getFailure(); 
					        LOG.error("写入失败!数据:{}",failure.toString());
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
		} catch (Exception e) {
			LOG.error("批量保存参数保存异常："+e);
			throw e;
		}
	    return falg; 
	}
	public  boolean insertIndex(Map<String,Object> map,String idkey,boolean docAsUpsert) throws Exception{
		 boolean falg=true;
		 try {
			     //有ID则是修改没有则是新增			
			String id = String.valueOf(map.get(idkey));
			if(MyTools.isEmpty(id)||"null".equals(id)){
				 throw new RuntimeException("主键必须设值!!!当前主键"+idkey+"="+id);
			} 
			 UpdateRequest request = new UpdateRequest(index,type,id).doc(map,XContentType.JSON).docAsUpsert(docAsUpsert)
					 .retryOnConflict(5);
			 rhlClient.update(request);
		} catch (Exception e) {
			LOG.error("单条数据参数保存异常：{}",map);
			throw e;
		}
	    return falg; 
	}
}
