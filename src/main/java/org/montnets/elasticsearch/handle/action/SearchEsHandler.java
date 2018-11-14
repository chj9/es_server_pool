package org.montnets.elasticsearch.handle.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.montnets.elasticsearch.client.EsPool;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.common.enums.Constans;
import org.montnets.elasticsearch.common.jsonparser.JSONParser;
import org.montnets.elasticsearch.common.jsonparser.model.JsonObject;
import org.montnets.elasticsearch.common.util.Utils;
import org.montnets.elasticsearch.condition.ConditionEs;
import org.montnets.elasticsearch.entity.EsRequestEntity;
import org.montnets.elasticsearch.entity.ScrollEntity;
import org.montnets.elasticsearch.handle.IBasicHandler;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: SearchHandler.java
* @Description:  es查询处理
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月30日 上午10:34:22 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月30日     chenhj          v1.0.0               修改原因
 */
public class SearchEsHandler implements IBasicHandler{
	  private String index;
	  private String type;	  
	  private RestHighLevelClient rhlClient;
	  private	QueryBuilder queryBuilder;
	  private  SearchSourceBuilder searchSourceBuilder;
	  /*********排序字段*************/
	  private String sortField;
	  /**********排序方法************/
	  private SortOrder sortOrder;
	  /********数据集合体*********/
	  private EsRequestEntity esRequestEntity;
	  private Script script=null;
	  private String scriptName;
	  /*******只要哪些字段********/
	  private String[] includeFields =Strings.EMPTY_ARRAY;
	  /*******排除哪些字段********/
	  private String[] excludeFields = Strings.EMPTY_ARRAY;
	  /*********对象池*******************/
	  private EsConnectionPool pool = null;
	  private String poolId = Constans.DEFAULT_POOL_ID;
	  /**
	   * 这两个参数是获取版本和解释,官方都是默认true,我这里为了减少返回数据设置默认false,如果需要设置为true,在设置中设置即可
	   */
	  private boolean  version = false;
	  private boolean  explain = false;
	  private long totalCount;
	@Override
	public void builder(EsRequestEntity esRequestEntity){
		Objects.requireNonNull(esRequestEntity, "EsRequestEntity can not null");
		this.index=Objects.requireNonNull(esRequestEntity.getIndex(), "index can not null");
		this.type =Objects.requireNonNull(esRequestEntity.getType(), "type can not null");
		this.esRequestEntity=esRequestEntity;
		this.poolId=esRequestEntity.getPoolId();
		this.pool=EsPool.ESCLIENT.getPool(poolId);
		this.rhlClient=pool.getConnection();
	}
	/**
	 * 设置脚本
	*/
	public SearchEsHandler setScript(final String scriptName,final Script script) {
		this.scriptName=Objects.requireNonNull(scriptName, "scriptName can not null");
		this.script = Objects.requireNonNull(script, "script can not null");
		return this;
	}
	 /**
	  * 设置过滤条件
	  */
	 public SearchEsHandler setQueryBuilder(final ConditionEs queryBuilder) {
		 this.queryBuilder = queryBuilder.toResult();
		 return this;
	 }
	 public SearchEsHandler setQueryBuilder(final QueryBuilder queryBuilder) {
		 this.queryBuilder = queryBuilder;
		 return this;
	 }
	 /**
	  * 设置排序(可选)
	  * @param field 排序的参数
	  * @param order 排序方法
	  */
	 public SearchEsHandler addSort(final String field,final SortOrder sortOrder){
		 this.sortField=Objects.requireNonNull(field, "field can not null");
		 this.sortOrder= Objects.requireNonNull(sortOrder, "order can not null");
		 return this;
	 }
	 /**
	  * 字段过滤(可选)
	  * @param includeFields 需要的字段
	  * @param excludeFields 不需要的字段
	  */
	 public SearchEsHandler fetchSource(String[] includeFields,String[] excludeFields){
		 if(excludeFields!=null){
			this.excludeFields=excludeFields; 
		 }
		 if(includeFields!=null){
			  this.includeFields=includeFields;
		 }
		 return this;
	 }
	 
	 public SearchEsHandler setVersion(boolean version) {
		this.version = version;
		return this;
	}
	public SearchEsHandler setExplain(boolean explain) {
		this.explain = explain;
		return this;
	}
	/**
	  * 数据转为map后返回
	  * @return
	  * @throws Exception
	  */
	 public  List<Map<String, Object>>  sraechSourceAsList() throws Exception{
		 SearchHits hits = sraech(); 
		 return hitsToList(hits);
	 }
	/**
	 * 普通查询方法
	 */
	public  SearchHits sraech() throws Exception{
		 searchSourceBuilder = new SearchSourceBuilder(); 
		 try {
			SearchRequest searchRequest = new SearchRequest(index); 
			searchRequest.types(type);
			//是否需要分页
	    	if(esRequestEntity.isNeedPaging()){
			    searchSourceBuilder.from(esRequestEntity.getStartIndex()).size(esRequestEntity.getPageSize());  
	    	}else{
	    		//如果不分页则设置每次多少数据,默认1000条
	    		searchSourceBuilder.size(esRequestEntity.getLimit());
	    	}
			//是否需要排序,没有则不需要
		    if(Utils.isNotEmpty(sortField)&&Objects.nonNull(sortOrder)){
				searchSourceBuilder.sort(new FieldSortBuilder(sortField).order(sortOrder));
			}
		    //设置过滤字段
			if(includeFields!=null||excludeFields!=null){
				 searchSourceBuilder.fetchSource(includeFields,excludeFields);
			 }
			 if(Objects.nonNull(script)){
				 searchSourceBuilder.scriptField(scriptName, script);
			 }
			 //不需要解释
			 searchSourceBuilder.explain(explain);
			 //不需要版本号
			 searchSourceBuilder.version(version);
		     //是否有自定义条件
		     if(Objects.nonNull(queryBuilder)){
		    	 searchSourceBuilder.query(queryBuilder); 
		     }
			 searchRequest.source(searchSourceBuilder); 
			 SearchResponse searchResponse = rhlClient.search(searchRequest);
			 SearchHits hits = searchResponse.getHits();
			 totalCount=hits.getTotalHits();
			 return hits; 
		 } catch (Exception e) {
			 throw e;
		 }
	}
	/**
	 * 根据ID查询
	 */
	public  Map<String, Object>  sraechById(String idvalue) throws Exception{
		Objects.requireNonNull(idvalue, "id can not null");
		Map<String, Object> map =new HashMap<String, Object>(16);
		 try {
			 GetRequest getRequest = new GetRequest(index,type,idvalue);
		     //设置过滤字段
			 if(Objects.nonNull(includeFields)||Objects.nonNull(excludeFields)){
				 FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includeFields, excludeFields);
				 getRequest.fetchSourceContext(fetchSourceContext);
			 }
			 GetResponse getResponse = rhlClient.get(getRequest);
			 if(getResponse.isExists()){
				 map=getResponse.getSourceAsMap();
			 };
		 } catch (Exception e) {			
				throw e;
		}
	    return map; 
	}
	/**
	 * 滚动遍历数据接口
	 * @return
	 * @throws IOException
	 */
	public  ScrollEntity<Map<String, Object>> searchScroll(ScrollEntity<Map<String, Object>> scrollEntity) throws IOException{
		 List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		try {
		 String scrollId = scrollEntity.getScrollId();
		 //首次进入
		if(Utils.isEmpty(scrollId)){
			SearchRequest searchRequest = new SearchRequest(index);
			searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.size(esRequestEntity.getLimit()); 
		     //设置过滤字段
			 if(Objects.nonNull(includeFields)||Objects.nonNull(excludeFields)){
				 searchSourceBuilder.fetchSource(includeFields,excludeFields);
			 }
			 //脚本遍历
			 if(Objects.nonNull(script)){
				 searchSourceBuilder.scriptField(scriptName, script);
			 }
			 
			 //测试用
			// searchSourceBuilder.profile(true);
			 //不需要解释
			 searchSourceBuilder.explain(explain);
			 //不需要版本号
			 searchSourceBuilder.version(version);
		     //是否有自定义条件
		     if(Objects.nonNull(queryBuilder)){
		    	 searchSourceBuilder.query(queryBuilder);      
		     }
		     	searchRequest.source(searchSourceBuilder);
		     	if(Objects.nonNull(scrollEntity.getKeepAlive())){
		     		searchRequest.scroll(TimeValue.timeValueMinutes(scrollEntity.getKeepAlive()));
		     	}else{
		     		//默认十秒
		     		searchRequest.scroll(TimeValue.timeValueMinutes(10L));
		     	}
		     	SearchResponse searchResponse = rhlClient.search(searchRequest);
		     	scrollId = searchResponse.getScrollId(); 
		     	SearchHits hits = searchResponse.getHits();
		     	//转换
		     	list=hitsToList(hits);
		}
		//非首次进入
		else{
				SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId); 
				scrollRequest.scroll(TimeValue.timeValueMinutes(1));
				SearchResponse searchScrollResponse = rhlClient.searchScroll(scrollRequest);
				SearchHits hits = searchScrollResponse.getHits(); 
		     	//转换
		     	list=hitsToList(hits);
				scrollId = searchScrollResponse.getScrollId();  
			}
			scrollEntity.setScrollId(scrollId);
			scrollEntity.setDataList(list);
			return scrollEntity; 
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 清除滚动ID
	 * @param scrollId 滚动ID
	 * @return
	 * @throws IOException
	 */
	public  boolean clearScroll(String scrollId) throws IOException{
			Objects.requireNonNull(scrollId, "scrollId can not null");
			ClearScrollRequest clearScrollRequest = new ClearScrollRequest(); 
			clearScrollRequest.addScrollId(scrollId);
			ClearScrollResponse clearScrollResponse = rhlClient.clearScroll(clearScrollRequest);
			boolean succeeded = clearScrollResponse.isSucceeded();
			return succeeded;
		
	}
	/**
	 * 批量ID查询,类型SQL中的in查询
	 * @throws Exception 
	 */
	public List<Map<String, Object>> searchByIds(Set<String> ids) throws Exception{
		return executeSearchByIds(ids,null);
	}
	/**
	 * 批量ID查询,类型SQL中的in查询,把ID放在响应的数据中
	 * @param ids ID列表
	 * @param idField id在响应数据中的字段名,方便获取
	 * @throws Exception 
	 */
	public List<Map<String, Object>> searchByIds(Set<String> ids,String idField) throws Exception{
			return executeSearchByIds(ids,idField);
	}
	private List<Map<String, Object>> executeSearchByIds(Set<String> ids,String idField) throws Exception{
	   	if(ids==null||ids.isEmpty()){
    		throw new NullPointerException("ids can not null and empty");
    	}
	   	List<Map<String, Object>> dataList= null;
 		try {
 			MultiGetRequest request = new MultiGetRequest();
 			//需要的字段
		    //设置过滤字段
 			FetchSourceContext fetchSourceContext =
 			        new FetchSourceContext(true, includeFields,excludeFields);
 			//查询组装
 			for(String id:ids){
 				request.add(new MultiGetRequest.Item(index,type,id).fetchSourceContext(fetchSourceContext));
 			}
 			MultiGetResponse response = rhlClient.multiGet(request);
 			MultiGetItemResponse[] resultItems = response.getResponses();
 			if(resultItems==null||resultItems.length<=0){
 				return dataList;
 			}
 			GetResponse resultGet = null;
 			dataList  = new ArrayList<Map<String, Object>>();
 			for(MultiGetItemResponse result:resultItems){
 				resultGet = result.getResponse();  
 				if(resultGet==null){
 					continue;
 				}
 				if(resultGet.isExists()){
 					 Map<String, Object> reMap = resultGet.getSourceAsMap();
 					  if(Utils.isNotEmpty(idField)){
 						 reMap.put(idField,resultGet.getId());
 					  }
 					  dataList.add(reMap);
 				}else {
 				}
 			}
 		} catch (Exception e) {
 			throw e;
 		}
         return dataList;
	}
    /**  
     * 查询总数
     * @return  
     * @throws Exception  
     */  
    public  Long count() throws Exception {  
	    	 searchSourceBuilder = new SearchSourceBuilder(); 
		     //是否有自定义条件
		     if(Objects.nonNull(queryBuilder)){
		    	 searchSourceBuilder.query(queryBuilder);
		     }
		     //取低级客户端API来执行这步操作
		     RestClient restClient = rhlClient.getLowLevelClient();
			 String endPoint = "/" + index + "/" + type +"/_count";
			 //删除的条件
			 String source = searchSourceBuilder.toString();
			 HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
			 Response response =restClient.performRequest("GET", endPoint,Collections.<String, String> emptyMap(),entity);
			 String responseBody = EntityUtils.toString(response.getEntity());
			 JSONParser jsonParser = new JSONParser();
		     JsonObject json = (JsonObject) jsonParser.fromJSON(responseBody);
			 Integer count =(Integer)json.get("count");
			 return count.longValue();
    }
    /**
     * 根据ID查询数据是否存在
     * @param ID值
     */
    public  boolean existsDocById(String idvalue){
    	Objects.requireNonNull(idvalue, "id can not null");
    	boolean isExists=false;
 		try {
 			RestClient restClient = rhlClient.getLowLevelClient();
 			String endPoint = "/" + index + "/" + type +"/"+idvalue.trim();
 	        Response response = restClient.performRequest("HEAD",endPoint,Collections.<String, String>emptyMap());
 	        isExists ="OK".equals(response.getStatusLine().getReasonPhrase());
 		} catch (IOException e) {
 			isExists=false;
 		}
         return isExists;
    }
    /**
     * 根据批量传进来的ID查询数据是否不存在,返回不存在数据列表
     * @param ids 要查询id列表
     * @param idFiled ID的字段名
     * @return 响应不存在的ID
     * @throws Exception 
     */ 
     public  Set<String> unExistsDocByIds(Set<String> ids,String idFiled) throws Exception{
    	return existsIds(ids,idFiled,false);
    }
    /**
     * 根据批量传进来的ID查询数据是否存在,返回存在的数据列表
     * @param ids 要查询id列表
     * @param idFiled ID的字段名
     * @return 响应存在的ID
     * @throws Exception 
     */
    public  Set<String> existsDocByIds(Set<String> ids,String idFiled) throws Exception{
    	return existsIds(ids,idFiled,true);
    }
    public  Set<String> existsIds(Set<String> ids,String idFiled,boolean dataType) throws Exception{
    	if(ids==null||ids.isEmpty()){
    		throw new NullPointerException("id can not null and empty");
    	}
 		try {
 			MultiGetRequest request = new MultiGetRequest();
 			//需要的字段
 			String[] includes = new String[] {Objects.requireNonNull(idFiled, "idFiled can not null")};
 			FetchSourceContext fetchSourceContext =
 			        new FetchSourceContext(true, includes, Strings.EMPTY_ARRAY);
 			//查询组装
 			for(String id:ids){
 				request.add(new MultiGetRequest.Item(index,type,id).fetchSourceContext(fetchSourceContext));
 			}
 			MultiGetResponse response = rhlClient.multiGet(request);
 			MultiGetItemResponse[] resultItems = response.getResponses();
 			if(resultItems==null||resultItems.length<=0){
 				return null;
 			}
 			GetResponse resultGet = null;
 			ids.clear();
 			for(MultiGetItemResponse result:resultItems){
 				//assertNull(result.getFailure());              
 				resultGet = result.getResponse();  
 				if(resultGet==null){
 					continue;
 				}
 				if(dataType&&resultGet.isExists()){
 					ids.add(result.getId());
 				}else if(!dataType&&!resultGet.isExists()){
 					ids.add(result.getId());
 				}
 			}
 		} catch (Exception e) {
 			throw e;
 		}
         return ids;
    }
	/**
     * 获取当前请求的所有条数	  
	 * 只有在执行一次查询之后才会有总数,与搜索请求匹配的总命中数。
	 * 如果需要根据条件查询总数请使用{@link count}方法
     */
	public long getTotalCount() {
		return totalCount;
	}
	/**
	 * SearchHits 转 List的公共方法
	 * @param hits
	 * @return
	 */
	private List<Map<String, Object>> hitsToList(SearchHits hits){
		 List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		 Map<String, DocumentField> map = null;
		 for (SearchHit hit : hits) {
			  /************如果是脚本查询将是另外一个取值方法***************/
			  if(Objects.nonNull(script)){
				    	map = hit.getFields();
				    	Map<String, Object> mapScript = new HashMap<>(16);
				    	Object value = map.get(scriptName).getValue();
				    	mapScript.put(scriptName,value);
				    	list.add(mapScript);
			  }
			  //普通取值方法
			  else{
			    list.add(hit.getSourceAsMap());
			  }
		  }
		 return list;
	}
	/**
	 * 打印当前请求的DSL语句,只有在执行查询方法后才能有DSL语句
	 */
	@Override
	public String toDSL() {
		return Objects.requireNonNull(searchSourceBuilder, "not request DSL!").toString();
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
		//if(rhlClient!=null){
			pool.returnConnection(rhlClient);
		//}
	}
}
