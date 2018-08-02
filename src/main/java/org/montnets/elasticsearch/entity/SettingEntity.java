/**
 * 
 */
package org.montnets.elasticsearch.entity;

import java.io.Serializable;

import org.montnets.elasticsearch.common.jsonparser.model.JsonObject;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: SettingEntity.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月1日 下午4:48:27 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月1日     chenhj          v1.0.0               修改原因
*/
public class SettingEntity implements Serializable{
	/**
	 *@Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private int numberOfShards=5;
	private int numberOfReplicas=1;
	/*****刷新频率 单位:秒*********/
	private int refreshInterval =5;
	private int maxResultWindow=10000;
	
	public SettingEntity(int numberOfShards,int numberOfReplicas,int refreshInterval){
		this.numberOfShards=numberOfShards;
		this.numberOfReplicas=numberOfReplicas;
		this.refreshInterval=refreshInterval;
	}
	public SettingEntity(int numberOfShards,int numberOfReplicas,int refreshInterval,int maxResultWindow){
		this.numberOfShards=numberOfShards;
		this.numberOfReplicas=numberOfReplicas;
		this.refreshInterval=refreshInterval;
		this.maxResultWindow=maxResultWindow;
	}
	public SettingEntity(){
		
	}
	
	public int getNumberOfShards() {
		return numberOfShards;
	}
	/**
	 * 分片数
	 * @param numberOfShards 默认5
	 */
	public void setNumberOfShards(int numberOfShards) {
		this.numberOfShards = numberOfShards;
	}
	public int getNumberOfReplicas() {
		return numberOfReplicas;
	}
	/**
	 * 副本数 
	 * @param numberOfReplicas 默认1
	 */
	public void setNumberOfReplicas(int numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
	}
	public int getRefreshInterval() {
		return refreshInterval;
	}
	/**
	 * 刷新频率 单位:秒
	 * @param refreshInterval 默认5秒 设置为-1为无限刷新
	 */
	public void setRefreshInterval(int refreshInterval) {
		if(refreshInterval<-1){
			refreshInterval=-1;
		}
		this.refreshInterval = refreshInterval;
	}
	public String toDSL(){
		JsonObject json = new JsonObject();
		json.put("number_of_shards", numberOfShards);
		json.put("number_of_replicas", numberOfReplicas);
		json.put("refresh_interval", refreshInterval+"s");
		json.put("max_result_window", maxResultWindow);
		return json.toString();
	}
	@Override
	public String toString() {
		return "SettingEntity [numberOfShards=" + numberOfShards + ", numberOfReplicas=" + numberOfReplicas
				+ ", refreshInterval=" + refreshInterval + ", maxResultWindow=" + maxResultWindow + "]";
	}

}
