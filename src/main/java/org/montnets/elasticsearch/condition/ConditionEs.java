package org.montnets.elasticsearch.condition;
import java.util.Objects;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.montnets.elasticsearch.common.enums.ConditionType;
/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: ConditionLogic.java
* @Description: 该类的功能描述
* 封装ES的查询条件逻辑,与,或
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月2日 上午11:11:16 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月2日     chenhj          v1.0.0               修改原因
*/
public class ConditionEs {
	private QueryBuilder queryBuilder=null;
	private BoolQueryBuilder boolQueryBuilder =null;
	private static final String AND = "and";
	private static final String OR = "or";
	public ConditionEs(){
		 boolQueryBuilder = QueryBuilders.boolQuery();
	}
	/**
	 * 或逻辑
	 * 这个方法一般只有命令 exist、unexist使用
	 * @param command 命令
	 * @param field 字段名,可为多个字段数组
	 * @return
	 */
	public ConditionEs or(ConditionType command,String field) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(OR,command,field,null);
		return this;
	} 
	/**
	 * 或逻辑
	 * 这个方法一般是 gt、gte、lt、lte、equal、unequal使用
	 * @param command 命令
	 * @param field 字段名
	 * @param value 内容
	 * @return
	 */
	public ConditionEs or(ConditionType command,String field,Object value) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(OR,command,field,value);
		return this;
	} 
	/**
	 * 与逻辑
	 * 这个方法一般只有命令 exist、unexist使用
	 * @param command 命令
	 * @param field 字段名,可为多个字段数组
	 * @return
	 */
	public ConditionEs and(ConditionType command,String field) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(AND,command,field,null);
		return this;
	}
	/**
	 * 与逻辑
	 * 这个方法一般是 gt、gte、lt、lte、equal、unequal使用
	 * @param command 命令
	 * @param field 字段名
	 * @param value 内容
	 * @return
	 * @throws Exception 
	 */
	public ConditionEs and(ConditionType command,String field,Object value) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(AND,command,field,value);
		return this;
	}
	/**
	 * 命令处理
	 * @param logic 逻辑
	 * @param command 命令
 	 * @param field 字段
	 * @param value 内容
	 */
	private void commandHandler(String logic,ConditionType command,String field,Object value) throws IllegalAccessException{
		if(AND.equals(logic)){
			
		}else if(OR.equals(logic)){
			if(command==ConditionType.unexist){
				throw new IllegalAccessException("unexist 不支持或查询");
			}
		}
		switch (command) {
			case gt:
				    if(AND.equals(logic)){
				    	boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gt(value));
				    }else if(OR.equals(logic)){
				    	boolQueryBuilder.should(QueryBuilders.rangeQuery(field).gt(value));
				    }
					break;
			case gte:
				    if(AND.equals(logic)){
				    	boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gte(value));
				    }else if(OR.equals(logic)){
				    	boolQueryBuilder.should(QueryBuilders.rangeQuery(field).gte(value));
				    }
					break;
			case lt:
				    if(AND.equals(logic)){
				    	boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lt(value));
				    }else if(OR.equals(logic)){
				    	boolQueryBuilder.should(QueryBuilders.rangeQuery(field).lt(value));
				    }
				    break;
			case lte: 
				    if(AND.equals(logic)){
				    	boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lte(value));
				    }else if(OR.equals(logic)){
				    	boolQueryBuilder.should(QueryBuilders.rangeQuery(field).lte(value));
				    }
					break;
			case equal:
					if(AND.equals(logic)){
						boolQueryBuilder.must(QueryBuilders.termQuery(field,value));
				    }else if(OR.equals(logic)){
				    	boolQueryBuilder.should(QueryBuilders.termQuery(field,value));
				    }
					break;
			case unequal:
					if(AND.equals(logic)){
						 boolQueryBuilder.must(QueryBuilders.existsQuery(field));
						 boolQueryBuilder.mustNot(QueryBuilders.termQuery(field,value));
				    }else if(OR.equals(logic)){
						 boolQueryBuilder.mustNot(QueryBuilders.termQuery(field,value));
				    }
					break;
			case exist:
					if(AND.equals(logic)){
						 boolQueryBuilder.must(QueryBuilders.existsQuery(field));
				    }else if(OR.equals(logic)){
						 boolQueryBuilder.should(QueryBuilders.existsQuery(field));
				    }
					break;
			case unexist:
					if(AND.equals(logic)){
						 boolQueryBuilder.mustNot(QueryBuilders.existsQuery(field));
				    }
					break;
			default:
				break;
		}
	}
	/**
	 * 生成条件
	 * @return
	 */
	public QueryBuilder toResult(){
		this.queryBuilder = Objects.requireNonNull(boolQueryBuilder,"条件不能为空!");
		return queryBuilder;
	}
	/**
	 * 打印生成的DSL语句
	 * @return
	 */
	public String toDSL(){
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		if(boolQueryBuilder==null){
			//查询全部
			boolQueryBuilder.must(QueryBuilders.matchAllQuery());
		}
		searchSourceBuilder.query(toResult());
		return searchSourceBuilder.toString();
	}
}
