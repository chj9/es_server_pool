package org.montnets.elasticsearch.entity;


import java.io.Serializable;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsRequestEntity.java
* @Description: ES索引库属性
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月27日 下午5:56:42 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月27日     chenhj          v1.0.0               修改原因
 */
public class EsRequestEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/***索引库***/
	private String index;
	/***索引库ID字段名,如果不设置默认自动生成***/
	private String idFieldName;
	/***索引库类表 ES7.0后删除***/
	private String type;
	/***当前页****/
	private int pageNo = 1;//
	/***每页数据****/
	private int pageSize = 10;//
	/***是否需要分页,默认禁止分页****/
	private boolean needPaging = false;//
	/*****滚动取数据的时候单次取多少,默认一次1000*******/
	private int limit=1000;
	/*******本次请求条件数据总数*********/
	private long totalCount = -1L;

   

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageNo() {
		return pageNo;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public EsRequestEntity(String index,String type){
		this.index=index;
		this.type = type;
	}
	public EsRequestEntity(){
	}
	public String getIdFieldName() {
		return idFieldName;
	}
	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}
	public String getIndex() {
			return index;
	}
	public void setIndex(String index) {
			this.index = index;
	}
	public void setPageNo(int pageNo){
		    this.pageNo = pageNo;
		    if (pageNo < 1)this.pageNo = 1;
	}
	 public int getPageSize(){
		 return this.pageSize;
	}
	public void setPageSize(int pageSize){
		    this.pageSize = pageSize;
	}
	  public int getStartIndex() {
			    return getFirst();
	}
	  public int getFirst(){
		    return (this.pageNo - 1) * this.pageSize;
	}
	public boolean isNeedPaging() {
			return needPaging;
	}
	public void setNeedPaging(boolean needPaging) {
			this.needPaging = needPaging;
	}
	@Override
	public String toString() {
		return "EsRequestEntity [index=" + index + ", idFieldName=" + idFieldName + ", type=" + type + ", pageNo="
				+ pageNo + ", pageSize=" + pageSize + ", needPaging=" + needPaging + ", limit=" + limit
				+ ", totalCount=" + totalCount + "]";
	}
	
}
