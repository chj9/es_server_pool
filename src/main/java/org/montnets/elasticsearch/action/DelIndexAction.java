package org.montnets.elasticsearch.action;



import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: DelIndexAction.java
* @Description: 该类的功能描述
* es删除工具类
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月13日 下午3:04:59 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月14日     chenhj          v1.0.0               修改原因
 */
public class DelIndexAction {
	  
	  private String index;
	  private String type;	  
	  private RestHighLevelClient rhlClient;
	  private	QueryBuilder queryBuilder;

	  private static final Logger LOG = LoggerFactory.getLogger("esLog");

	public DelIndexAction(RestHighLevelClient rhlClient,String index,String type){
		this.index=index;
		this.type =type;
		this.rhlClient=rhlClient;
	}
	 /**
	  * 设置过滤条件
	  */
	 public DelIndexAction setQueryBuilder(QueryBuilder queryBuilder) {
			this.queryBuilder = queryBuilder;
			return this;
	 }
	/**
	 * 根据ID删除数据
	* @author chenhongjie 
	*/
	public  boolean  delDocById(String idvalue) throws Exception{
			 DeleteRequest request = new DeleteRequest(index,type,idvalue); 
			 //DeleteResponse deleteResponse = 
			 DeleteResponse deleteResponse =  rhlClient.delete(request);
			 
			 return deleteResponse.status()==RestStatus.OK;
			// deleteResponse.isFragment();
			 //return true;
			 //return deleteResponse.

	}
	/**
	 * 根据搜索内容删除数据
	* @author chenhongjie 
	 */
	public  boolean  delDocByQuery() throws Exception{
		 try {
			 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		     //是否有自定义条件
		     if(queryBuilder==null)throw new RuntimeException("请设置删除条数,或者你是想删除整个库?");
		     searchSourceBuilder.query(queryBuilder);
		     
		     LOG.debug("删除的内容条件:{}",searchSourceBuilder.toString());
		     //取低级客户端API来执行这步操作
		     RestClient restClient = rhlClient.getLowLevelClient();
			 String endPoint = "/" + index + "/" + type +"/_delete_by_query?conflicts=proceed&timeout=10m";
			 //删除的条件
			 String source = searchSourceBuilder.toString();
			 HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
			 Response response = restClient.performRequest("POST", endPoint,Collections.<String, String> emptyMap(),entity);
			 return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
			 //return true;
		 } catch (Exception e) {			
				throw e;
		}
	}
}
