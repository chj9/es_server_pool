/**
 * 
 */
package org.montnets.elasticsearch.entity;

import java.io.Serializable;
import java.util.List;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: ScrollResponse.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
 * @param <T>
* @date: 2018年7月31日 上午10:03:33 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月31日     chenhj          v1.0.0               修改原因
*/
public class ScrollEntity<T> implements Serializable{

	/**
	 *@Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
   
	/*****响应数据集  不能主动设置*******/
	private List<T> dataList = null;//响应数据集
	/*****滚动数据时候产生的游标ID 不能主动设置*******/
	private String scrollId;
	/***游标ID在不使用后保持激活多久  单位 秒***/
	private Integer keepAlive;
	
	public Integer getKeepAlive() {
		return keepAlive;
	}
	public void setKeepAlive(Integer keepAlive) {
		this.keepAlive = keepAlive;
	}
	public List<T> getDataList() {
		return dataList;
	}
	/**
	 * 禁止主动设置
	 * @param dataList
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	public String getScrollId() {
		return scrollId;
	}
	/**
	 * 禁止主动设置
	 * @param scrollId
	 */
	public void setScrollId(String scrollId) {
		this.scrollId = scrollId;
	}
	@Override
	public String toString() {
		return "ScrollEntity [dataList=" + dataList + ", scrollId=" + scrollId + "]";
	}
	
}
