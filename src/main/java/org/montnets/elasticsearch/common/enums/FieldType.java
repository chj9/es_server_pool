package org.montnets.elasticsearch.common.enums;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: Constans.java
* @Description: 程序常量类 
*注意这里不是全部
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月27日 下午6:40:29 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月27日     chenhj          v1.0.0               修改原因
 */
public enum FieldType {
	LONG("long"),
	KEYWORD("keyword"),
	INTEGER("integer"),
	DATE("date"),
	BYTE("byte"),
	SHORT("short"),
	DOUBLE("double"),
	FLOAT("float"),
	BOOLEAN("boolean"),
	TEXT("text"),
	BINARY("binary");
	private String type;
	private FieldType(String type){
		this.type=type;
	}
	public String getType() {
		return type;
	}
	
}
