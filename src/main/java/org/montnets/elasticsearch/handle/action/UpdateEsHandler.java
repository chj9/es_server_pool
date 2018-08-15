package org.montnets.elasticsearch.handle.action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.montnets.elasticsearch.client.EsPool;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.common.exception.EsIndexMonException;
import org.montnets.elasticsearch.common.util.PoolUtils;
import org.montnets.elasticsearch.entity.EsRequestEntity;
import org.montnets.elasticsearch.handle.IBasicHandler;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: InsertIndexAction.java
* @Description: 该类的功能描述
*ES更新数据公共类  
* @version: v1.0.0
* @author: chenhj
* @date: 2018年5月25日 下午3:13:22 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月12日     chenhj          v1.0.0               修改原因
 */
public class UpdateEsHandler implements IBasicHandler{
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
	  /*********对象池*******************/
	  private EsConnectionPool pool = null;
	  /*******条件**********/
	  //private	QueryBuilder queryBuilder;
	  private Script script=null;
	  private SearchSourceBuilder searchSourceBuilder;
	@Override
	public void builder(EsRequestEntity esRequestEntity){
		Objects.requireNonNull(esRequestEntity, "EsRequestEntity can not null");
		this.index=Objects.requireNonNull(esRequestEntity.getIndex(), "index can not null");
		this.type =Objects.requireNonNull(esRequestEntity.getType(), "type can not null");
		this.pool=EsPool.ESCLIENT.getPool();
		this.rhlClient=pool.getConnection();
	}
	/**
	 * 设置是否存在更新不存在插入
	 * @param docAsUpsert true：存在更新不存在插入  false:数据直接覆盖    默认为false
	 * @return
	 */
	public UpdateEsHandler docAsUpsert(boolean docAsUpsert){
		this.docAsUpsert=docAsUpsert;
		return this;
	}
	 /**
	  * 设置过滤条件
	  */
//	 public UpdateEsHandler setQueryBuilder(final QueryBuilder queryBuilder) {
//		 Objects.requireNonNull(queryBuilder, "QueryBuilder can not null");
//		 this.queryBuilder = queryBuilder;
//		 return this;
//	 }
	/**
	 * 设置ID字段名 从map中get
	 * @param idFieldName
	 * @return
	 */
	public UpdateEsHandler setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
		return this;
	}
	/**
	 * 设置脚本更新
	*/
	public UpdateEsHandler setScript(final String scriptName,final Script script) {
		this.script = Objects.requireNonNull(script, "script can not null");
		return this;
	}
	/**
	 * 根据ID键来批量更新
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public  boolean updateBulk(List<Map<String,Object>> list) throws Exception{
		return update(Objects.requireNonNull(list, "list can not null"),idFieldName);
	}
	/**
	 * 根据ID键单条更新
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public  boolean updateOne(Map<String,Object> map) throws Exception{
		return update(Objects.requireNonNull(map, "map can not null"),idFieldName);
	}
	/**
	 * 根据条件更新 条件通过setQueryBuilder设置
	 * @param isSync 是否同步更新   true：同步更新   false:异步更新
	 * 如果一次更新数据量大建议使用异步更新
	 * @return
	 * @throws Exception 
	 */
//	public boolean updateByQuery(boolean isSync) throws Exception{
//		boolean falg=true;
//		 try {
//			 searchSourceBuilder = new SearchSourceBuilder(); 
//		     //是否有自定义条件
//		     if(queryBuilder==null){
//		    	 throw new RuntimeException("请设置更新条数,或者你是想更新整个库?");
//		     }
//		     if(script!=null){
//		    	 searchSourceBuilder.scriptField("", script);
//		     }
//		     searchSourceBuilder.query(queryBuilder);
//		     //取低级客户端API来执行这步操作
//		     RestClient restClient = rhlClient.getLowLevelClient();
//			 String endPoint = "/" + index + "/" + type +"/_update_by_query?conflicts=proceed&scroll_size=100000&timeout=1000s";
//			 //删除的条件
//			 String source = searchSourceBuilder.toString();
//			 HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
//			 if(isSync) {
//				
//			     Response response = restClient.performRequest("POST", endPoint,Collections.<String, String> emptyMap(),entity);
//			     falg = response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
//			     return falg;
//			 }
//			 //异步执行
//			 RecordUpdate reLog = new RecordUpdate();
//			 reLog.setCommand(endPoint);
//			 reLog.setEntity(entity);
//			 reLog.setRestClient(restClient);
//			 reLog.setLogStr(searchSourceBuilder.toString());
//			 new Thread(reLog, "UPDATE_"+System.currentTimeMillis()).start();
//			 return true;
//		 } catch (Exception e) {			
//				throw e;
//		}
//	}
	/**
	 * 更新ES库
	 * @param list 需要更新的数据列表
	 * @param idkey 每条数据的主键名
	 */
	private boolean update(Object dataObj,String idField) throws Exception{
		  boolean falg=true;
		try {
		  Objects.requireNonNull(idFieldName, "idFieldName can not null");
		 /***********批量更新****************/
	      if(dataObj instanceof List){
	    	 BulkRequest request = new BulkRequest();
	    	 @SuppressWarnings("unchecked")
			List<Map<String,Object>> list = (List<Map<String, Object>>) dataObj;
	    	 if(list==null||list.isEmpty()){
	    		 throw new NullPointerException("数据集合不能为空");
	    	 }
			 for(Map<String,Object> map:list){
				    //如果数据为空或者null则跳过
				 	if(Objects.isNull(map)||!map.isEmpty()){
				 		continue;
				 	}
				 	UpdateRequest updateRequest = updateOne(map,idField);	
					if(updateRequest!=null){
						request.add(updateRequest);					
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
				 falg = false;
		}
	     /************单条插入**************/		 
	     }else if(dataObj instanceof Map){
	    	@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) dataObj;
			//如果数据为空或者null则跳过
			if(Objects.isNull(map)||!map.isEmpty()){
			 	return false;
			}
			UpdateRequest updateRequest = updateOne(map,idField);	
			if(updateRequest!=null){
				rhlClient.update(updateRequest);
			}
	     }
		} catch (Exception e) {
			 throw new EsIndexMonException("数据更新失败",e);
		}
	    return falg; 
	}
	/**
	 * 抽离组成代码
	 * @param map
	 * @param idField
	 * @return
	 */
	private UpdateRequest updateOne(Map<String, Object> map,String idField){
			String id = null;
			id = String.valueOf(map.get(idField));
			//如果没有这个ID字段名则跳出不给保存
			if(PoolUtils.isEmpty(id)||"null".equals(id)){
				 return null;
			}
			UpdateRequest updateRequest = new  UpdateRequest(index,type,id);
			if(script!=null){
				updateRequest.script(script);
			}
			updateRequest.doc(map,XContentType.JSON).docAsUpsert(docAsUpsert).retryOnConflict(5);
			return updateRequest;
		
	}
	/**
	 * 获取更新失败数据的集合
	 * @return 如果为null则没有失败数据
	 */
	public List<String> getListFailuresData() {
		return listFailuresData;
	}
	@Override
	public String toDSL() {
		if(searchSourceBuilder!=null){
			return Objects.requireNonNull(searchSourceBuilder, "not request DSL!").toString();
		}
		return "not request DSL!";
	}	
	/**
	 * 该方法主要是验证那个参数没输入,辅助类
	 */
	@Override
	public void validate() throws NullPointerException {
  		Objects.requireNonNull(rhlClient, "RestHighLevelClient can not null");
  		Objects.requireNonNull(index, "index can not null");
  		Objects.requireNonNull(type,"type can not null");
	}
	/* (non-Javadoc)
	 * @see org.montnets.elasticsearch.handle.IBasicHandle#close()
	 */
	@Override
	public void close() {
		if(rhlClient!=null){
			pool.returnConnection(rhlClient);
		}
	}
}