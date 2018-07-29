package org.montnets.elasticsearch.handle.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
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
import org.montnets.elasticsearch.common.util.MyTools;
import org.montnets.elasticsearch.entity.EsRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @Title:  SearchIndexAction.java   
 * @Description:  es查询工具类
 * @author: chenhongjie     
 * @date:   2018年5月25日 上午9:14:11   
 * @version V1.0
 */
public class SearchAction {
	  private String index;
	  private String type;	  
	  private RestHighLevelClient rhlClient;
	  private	QueryBuilder queryBuilder;
	  private String sortfield;
	  private String order;
	  private EsRequestEntity<?> esBean;
	  private Script script=null;
	  private String[] includeFields =null;
	  private String[] excludeFields = null;
	  private static final Logger LOG = LoggerFactory.getLogger("esLog");
	  /**
	   * 只有在执行一次查询之后才会有总数
	   */
	  private long totalCount;
		public SearchAction(RestHighLevelClient rhlClient,EsRequestEntity<?> esBean){
		this.index=esBean.getIndex();
		this.type =esBean.getIndex();
		if(esBean.getType()!=null){
			this.type =esBean.getType();
		}
		this.esBean=esBean;
		this.rhlClient=rhlClient;
	}
	/**
	 * 设置脚本
	*/
	public SearchAction setScript(Script script) {
		this.script = script;
		return this;
	}
	 /**
	  * 设置过滤条件
	  */
	 public SearchAction setQueryBuilder(QueryBuilder queryBuilder) {
			this.queryBuilder = queryBuilder;
			return this;
	 }
	 /**
	  * 设置排序(可选)
	  * @param field 排序的参数
	  * @param order 排序方法
	  */
	 public SearchAction addSort(String field, String order){
		 this.sortfield=field;
		 this.order=order;
		 return this;
	 }
	 /**
	  * 字段过滤(可选)
	  * @param includeFields 需要的字段
	  * @param excludeFields 不需要的字段
	  */
	 public SearchAction fetchSource(String[] includeFields,String[] excludeFields){
		 this.excludeFields=excludeFields;
		 this.includeFields=includeFields;
		 return this;
	 }
	/**
	 * 查询方法
	* @author chenhongjie 
	 */
	public  List<Map<String, Object>>  sraech() throws Exception{
		 List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		 try {
			 SearchRequest searchRequest = new SearchRequest(index); 
			 searchRequest.types(type);
			 	//是否需要分页
	    		if(esBean.isNeedPaging()){
			        searchSourceBuilder.from(esBean.getStartIndex()).size(esBean.getPageSize());  
	    		}else{
	    			searchSourceBuilder.size(esBean.getLimit());
	    		}
			//是否需要排序,没有则不需要
		    if(MyTools.isNotEmpty(sortfield)&&MyTools.isNotEmpty(order)){
					   if("desc".equals(order)){
						   searchSourceBuilder.sort(new FieldSortBuilder(sortfield).order(SortOrder.DESC));
					   }else if("asc".equals(order)){
						   searchSourceBuilder.sort(new FieldSortBuilder(sortfield).order(SortOrder.ASC));
			      }
			  }
		     //设置过滤字段
			 if(includeFields!=null||excludeFields!=null){
				 searchSourceBuilder.fetchSource(includeFields,excludeFields);
			 }
			 if(script!=null){
				 searchSourceBuilder.scriptField("mont", script);
			 }
			 //不需要解释
			 searchSourceBuilder.explain(false);
			 //不需要版本号
			 searchSourceBuilder.version(false);
		     //是否有自定义条件
		     if(queryBuilder!=null)searchSourceBuilder.query(queryBuilder); 
		     LOG.debug("通用查询条件:{}",searchSourceBuilder.toString());
			 searchRequest.source(searchSourceBuilder); 

			 SearchResponse searchResponse = rhlClient.search(searchRequest);
			 SearchHits hits = searchResponse.getHits();
			 
		     for (SearchHit hit : hits) {
		    	
		    	 list.add(hit.getSourceAsMap());
		      }
			 totalCount=hits.getTotalHits();
		 } catch (Exception e) {			
			 LOG.error("查询失败!查询条件:{},原因是:",searchSourceBuilder.toString());
			 throw e;
		}
	    return list; 
	}
	/**
	 * 查询方法
	* @author chenhongjie 
	 */
	public  EsRequestEntity<?>  sraechScript() throws Exception{
		 List<Long>  list = new ArrayList<Long>();
		 String scrollId = esBean.getScrollId();	
		 SearchHits hits = null;
		 //首次进入
			if(MyTools.isEmpty(scrollId)){
				SearchRequest searchRequest = new SearchRequest(index);
				
				//searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
				SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
				searchSourceBuilder.size(esBean.getLimit()); 
				 if(script!=null){
					 searchSourceBuilder.scriptField("mont", script);
				 }
				 //测试用
				// searchSourceBuilder.profile(true);
				 //不需要解释
				 searchSourceBuilder.explain(false);
				 //不需要版本号
				 searchSourceBuilder.version(false);
			     //是否有自定义条件
			     if(queryBuilder!=null)searchSourceBuilder.query(queryBuilder);      
			     searchRequest.source(searchSourceBuilder);
			     LOG.info("(最大,最小,平均)查询条件:{}",searchSourceBuilder.toString());
			     searchRequest.scroll(TimeValue.timeValueMinutes(2));//数据保持多久 
			     SearchResponse searchResponse = rhlClient.search(searchRequest);
			     scrollId = searchResponse.getScrollId(); 
			      hits = searchResponse.getHits();

			}
			//非首次进入
			else{
				SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId); 
				scrollRequest.scroll(TimeValue.timeValueMinutes(1));
				SearchResponse searchScrollResponse = rhlClient.searchScroll(scrollRequest);
				hits = searchScrollResponse.getHits(); 
				scrollId = searchScrollResponse.getScrollId();  
			}
			//导出数据
		    for (SearchHit hit : hits) {
		    	 Map<String, DocumentField> map = hit.getFields();
		    	 try {
		    		 Long difference =(long) Math.abs(map.get("mont").getValue());
		    		 list.add(difference);
				} catch (Exception e) {
					//这里异常不处理
				}
		    }
			esBean.setScrollId(scrollId);
			//esBean.setDataList(list);
			return esBean; 
	}
	/**
	 * 根据ID查询
	* @author chenhongjie 
	 */
	public  Map<String, Object>  sraechById(String idvalue) throws Exception{
		Map<String, Object> map =new HashMap<String, Object>();
		 try {
			 GetRequest getRequest = new GetRequest(index,type,idvalue);
		     //设置过滤字段
			 if(includeFields!=null||excludeFields!=null){
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
	public EsRequestEntity<Map<String, Object>> searchScroll() throws IOException{
		 List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		 String scrollId = esBean.getScrollId();
		//首次进入
		if(MyTools.isEmpty(scrollId)){
			SearchRequest searchRequest = new SearchRequest(index);
			
			//searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.size(esBean.getLimit()); 
		     //设置过滤字段
			 if(includeFields!=null||excludeFields!=null){
				 searchSourceBuilder.fetchSource(includeFields,excludeFields);
			 }
			 //测试用
			// searchSourceBuilder.profile(true);
			 //不需要解释
			 searchSourceBuilder.explain(false);
			 //不需要版本号
			 searchSourceBuilder.version(false);
		     //是否有自定义条件
		     if(queryBuilder!=null)searchSourceBuilder.query(queryBuilder);      
		     	searchRequest.source(searchSourceBuilder);
		     	searchRequest.scroll(TimeValue.timeValueMinutes(10));//数据保持多久 
		     	SearchResponse searchResponse = rhlClient.search(searchRequest);
		     	scrollId = searchResponse.getScrollId(); 
		     	SearchHits hits = searchResponse.getHits();
		    for (SearchHit hit : hits) {
		    	 list.add(hit.getSourceAsMap());
		    }
		}
		//非首次进入
		else{
			SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId); 
			scrollRequest.scroll(TimeValue.timeValueMinutes(1));
			SearchResponse searchScrollResponse = rhlClient.searchScroll(scrollRequest);
			SearchHits hits = searchScrollResponse.getHits(); 
		    for (SearchHit hit : hits) {
		    	 list.add(hit.getSourceAsMap());
		    }
			scrollId = searchScrollResponse.getScrollId();  
		}
		EsRequestEntity<Map<String, Object>> esBeanTemp = new EsRequestEntity<>();
		esBeanTemp.setScrollId(scrollId);
		esBeanTemp.setDataList(list);
		return esBeanTemp; 
	}
	public boolean clearScroll(String scrollId) throws IOException{
		ClearScrollRequest clearScrollRequest = new ClearScrollRequest(); 
		clearScrollRequest.addScrollId(scrollId);
		ClearScrollResponse clearScrollResponse = rhlClient.clearScroll(clearScrollRequest);
		boolean succeeded = clearScrollResponse.isSucceeded();
		return succeeded;
	}
    /**  
     * 查询总数
     * @return  
     * @throws Exception  
     */  
    public Long count() throws Exception {  
    	 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
	     //是否有自定义条件
	     if(queryBuilder!=null)searchSourceBuilder.query(queryBuilder);

	     LOG.info("总数查询条件:{}",searchSourceBuilder.toString());
	     //取低级客户端API来执行这步操作
	     RestClient restClient = rhlClient.getLowLevelClient();
		 String endPoint = "/" + index + "/" + type +"/_count";
		 //删除的条件
		 String source = searchSourceBuilder.toString();
		 HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
		 Response response =restClient.performRequest("GET", endPoint,Collections.<String, String> emptyMap(),entity);
		 String responseBody = EntityUtils.toString(response.getEntity());
		 ObjectMapper mapper = new ObjectMapper();
		 @SuppressWarnings("rawtypes")
		 Map map =mapper.readValue(responseBody, Map.class);
		 // JSONObject json =JSON.parseObject(responseBody);
		 //Long count = json.getLong("count");
		 Long count =(Long) map.get("count");
		 return count;
    }
    /**
     * 根据ID查询数据是否存在
     * @param ID值
     */
    public  boolean existsDocById(String idvalue){
    	boolean isExists=false;
 		try {
 			RestClient restClient = EsConstant.client.getRhlClient().getLowLevelClient();
 			String endPoint = "/" + index + "/" + type +"/"+idvalue.trim();
 	        Response response = restClient.performRequest("HEAD",endPoint,Collections.<String, String>emptyMap());
 	        isExists =response.getStatusLine().getReasonPhrase().equals("OK");
 		} catch (IOException e) {
 			isExists=false;
 		}
         return isExists;
    }
	/**
     * 获取当前请求的所有条数
     */
	public long getTotalCount() {
		return totalCount;
	}
}
