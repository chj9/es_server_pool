/**
 * 
 */
package org.montnets.elasticsearch.common.enums;
/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: ConditionType.java
* @Description: 该类的功能描述
*	条件类型
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月2日 上午11:43:09 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月2日     chenhj          v1.0.0               修改原因
*/
public enum ConditionType {
	/****大于*********/
	gt("gt"),
	/*****大于等于********/
	gte("gte"),
	/*****小于********/
	lt("lt"),
	/*****小于等于********/
	lte("lte"),
	/*****等于********/
	equal("equal"),
	/******不等于*******/
	unequal("unequal"),
	/*****字段存在********/
	exist("exist"),
	/*****字段不存在********/
	unexist("unexist");
	private String condType;
	private ConditionType(String condType){
		this.condType=condType;
	}
	public String toString() {
		return condType;
	}
}
