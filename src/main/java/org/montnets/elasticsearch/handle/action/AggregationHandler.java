package org.montnets.elasticsearch.handle.action;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.range.IpRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.montnets.elasticsearch.entity.EsRequestEntity;
import org.montnets.elasticsearch.handle.IBasicHandle;
/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: AggIndexAction.java
* @Description: 聚合处理
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月12日 下午2:29:43 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月12日     chenhj          v1.0.0               修改原因
*/
public class AggregationHandler implements IBasicHandle{
	  private String index;
	  private String type;	  
	  private RestHighLevelClient rhlClient;
	  private QueryBuilder queryBuilder;
	  private Script script=null;
	  private String count ="count";
	  private SearchSourceBuilder searchSourceBuilder;
	  private  AggregationBuilder aggregationBuilder;
   	  public AggregationHandler(RestHighLevelClient rhlClient,EsRequestEntity entity){
		this.index=Objects.requireNonNull(entity.getIndex(),"index can not null");
		this.type =Objects.requireNonNull(entity.getType(),"type can not null");
		this.rhlClient=rhlClient;
	}
	 /**
	  * 设置脚本
	  */
	 public AggregationHandler setScript(Script script) {
			this.script = script;
			return this;
	 }
	 /**
	  * 设置聚合
	  */
	 public AggregationHandler setAggregationBuilder(AggregationBuilder aggregationBuilder) {
			this.aggregationBuilder = aggregationBuilder;
			return this;
	 }
	 /**
	  * 设置过滤条件
	  */
	 public AggregationHandler setQueryBuilder(QueryBuilder queryBuilder) {
			this.queryBuilder = queryBuilder;
			return this;
	 }
	/**
	* 普通聚合查询
	* @return
	* @throws Exception
	*/
	 public  List<Map<String, Object>>  sraechAgg() throws Exception{
		 return sraechEsAgg(count);
	 }
	/**
	* 普通聚合查询
	* @param countField 总数的参数名,如果没有将取默认参数名 count
	* @return
	* @throws Exception
	*/
	public  List<Map<String, Object>>  sraechAgg(String countField) throws Exception{
		 this.count=countField;
		 return sraechEsAgg(count);
	}
	private List<Map<String,Object>> listmap =null;
	private Map<String,Object> map =null;
	private synchronized  List<Map<String, Object>>  sraechEsAgg(String countField) throws Exception{
		listmap= new ArrayList<Map<String,Object>>();
		map= new HashMap<String,Object>(16);
		 try {
			 SearchRequest searchRequest = new SearchRequest(index); 
			 searchRequest.types(type);
			 searchSourceBuilder = new SearchSourceBuilder(); 
		     if(script!=null){
		    	 searchSourceBuilder.scriptField("myscript", script);
		     }
		     if(aggregationBuilder!=null){
		    	 searchSourceBuilder.aggregation(aggregationBuilder);
		     }
			 //不需要解释
			 searchSourceBuilder.explain(false);
			 //不需要原始数据
			 searchSourceBuilder.fetchSource(false);
			 //不需要版本号
			 searchSourceBuilder.version(false);
		     //是否有自定义条件
		     if(queryBuilder!=null){
		    	 searchSourceBuilder.query(queryBuilder); 
		     }
			 searchRequest.source(searchSourceBuilder); 
			 SearchResponse searchResponse = rhlClient.search(searchRequest);
			 Aggregations aggregations = searchResponse.getAggregations();			
			 aggHandle(aggregations);
			 return listmap;
		 } catch (Exception e) {
			throw e;
		}
	}
 /**
  * 因为聚合的特殊性后续会写到文档中
  * @param agg
  */
 private void aggHandle(Aggregations agg){
	 String name ="";
	 Long longValue = 0L;
  for(Aggregation data:agg){
		 name = data.getName();
		 Object obj = agg.get(name);
	 if(obj instanceof Terms){
		 Terms terms = (Terms) obj;
		 name = terms.getName();
		for (Terms.Bucket entry : terms.getBuckets()) {	
     		map.put(name, entry.getKey());
     		longValue = entry.getDocCount();
     		map.put(count, longValue);
     		List<Aggregation> list = entry.getAggregations().asList();
     		if(list.isEmpty()){
    			listmap.add(clonMap(map));
    			//map.clear();
     		}else{
     			aggHandle(entry.getAggregations());
     		}
		}
	 }else if(obj instanceof ParsedDateHistogram){
		 ParsedDateHistogram terms = (ParsedDateHistogram) obj; 
		 List<? extends Bucket> buckets = terms.getBuckets(); 
		 name = terms.getName();
		 for(Bucket entry:buckets){
			 	map.put(name,entry.getKeyAsString());
	     		longValue = entry.getDocCount();
	     		map.put(count, longValue);
			List<Aggregation> list = entry.getAggregations().asList();
	     	if(list.isEmpty()){
	    		listmap.add(clonMap(map));
	    		//map.clear();
	     	}else{
	     		aggHandle(entry.getAggregations());
     		}
		 }
	 }else if(obj instanceof Max){
		 Max max =(Max) obj;
		 name = max.getName();
		 Double value = max.getValue();
		 longValue  =value.longValue();
		 map.put(name, longValue);
		 listmap.add(clonMap(map));
	 }else if(obj instanceof Min){
		 Min min =(Min) obj;
		 Double value = min.getValue();
		 longValue  =value.longValue();
		 map.put(min.getName(), longValue);
		 listmap.add(clonMap(map));
	 }else if(obj instanceof Avg){
		 Avg avg = (Avg) obj;
		 Double value = avg.getValue();
		 longValue  =value.longValue();
		 map.put(avg.getName(), longValue);
		 listmap.add(clonMap(map));
	 }else if(obj instanceof Sum){
		 Sum sum = (Sum) obj;
		 Double value = sum.getValue();
		 longValue  =value.longValue();
		 map.put(sum.getName(), longValue);
		 listmap.add(clonMap(map));
	 }else if(obj instanceof IpRangeAggregationBuilder){
		 
	 }
   }
 }
	/**
	 * 克隆map对象到另外一个map对象里面去
	 * @param map
	 * @return
	 */
	private Map<String,Object> clonMap(Map<String,Object> mapTo){
		Map<String,Object> map = new HashMap<String,Object>(16);
		for (Map.Entry<String,Object> entry : mapTo.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			map.put(key, value);
		}
		return map;
	}
	@Override
	public String toDSL() {
		// TODO Auto-generated method stub
		return searchSourceBuilder.toString();
	}
}
