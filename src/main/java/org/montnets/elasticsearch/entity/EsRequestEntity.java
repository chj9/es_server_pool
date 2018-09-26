package org.montnets.elasticsearch.entity;


import java.io.Serializable;

import org.montnets.elasticsearch.common.enums.Constans;
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
	private int pageNo = 1;
	/***每页数据****/
	private int pageSize = 10;
	/***是否需要分页,默认禁止分页****/
	private boolean needPaging = false;
	/*****滚动取数据的时候单次取多少,默认一次1000*******/
	private int limit=1000;
	/*******本次请求条件数据总数*********/
	private long totalCount = -1L;
	/**
	 * 集群ID
	 */
	private String poolId = Constans.DEFAULT_POOL_ID;
	
	public EsRequestEntity(String index,String type){
		this.index=index;
		this.type = type;
	}
	public EsRequestEntity(String index,String type,String poolId){
		this.index=index;
		this.type = type;
		this.poolId=poolId;
	}
	public EsRequestEntity(){
	}
   
	/**
	 * 集群ID 默认 ES-POOL1
	 */
	public String getPoolId() {
		return poolId;
	}
	/**
	 * 设置集群ID 默认 ES-POOL1
	 * @param poolId
	 */
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	/**
	 * 索引库type
	 * @return
	 */
	public String getType() {
		return type;
	}
	/**
	 *设置 索引库type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 本次请求条件数据总数
	 * @return
	 */
	public long getTotalCount() {
		return totalCount;
	}
	/**
	 * 设置本次请求条件数据总数
	 * @param totalCount
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * 分页下标
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}
	/**
	 * 滚动取数据的时候单次取多少,默认一次1000
	 * @return
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * 滚动取数据的时候单次取多少,默认一次1000
	 * @param limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 索引库ID字段名,如果不设置默认自动生成
	 * @return
	 */
	public String getIdFieldName() {
		return idFieldName;
	}
	/**
	 * 索引库ID字段名,如果不设置默认自动生成
	 * @param idFieldName
	 */
	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}
	/**
	 * 索引库名
	 * @return
	 */
	public String getIndex() {
			return index;
	}
	/**
	 * 设置索引库名
	 * @param index
	 */
	public void setIndex(String index) {
			this.index = index;
	}
	/**
	 * 设置分页下标
	 * @param pageNo
	 */
	public void setPageNo(int pageNo){
		    this.pageNo = pageNo;
		    if (pageNo < 1){
		    	this.pageNo = 1;
		    }
	}
	/**
	 * 设置每页数量
	 * @return
	 */
	 public int getPageSize(){
		 return this.pageSize;
	}
	/**
	 * 设置每页数量
	 * @param pageSize
	 */
	public void setPageSize(int pageSize){
		    this.pageSize = pageSize;
	}
	/**
	 * 分页开始下标
	 * @return
	 */
	  public int getStartIndex() {
			    return getFirst();
	}
	  /**
	   * 分页开始
	   * @return
	   */
	  public int getFirst(){
		    return (this.pageNo - 1) * this.pageSize;
	}
	 /**
	  * 是否需要分页，默认false
	  * @return
	  */
	public boolean isNeedPaging() {
			return needPaging;
	}
	/**
	  * 是否需要分页，默认false
	  */
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
