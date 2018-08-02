package org.montnets.elasticsearch.common.jsonparser.tokenizer;


/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: TokenType.java
* @Description: 该类的功能描述
*	参考http://www.json.org/对 JSON 的定义   数据类型
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月31日 下午5:56:44 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月31日     chenhj          v1.0.0               修改原因
 */
public enum TokenType {
	/******{**********/
    BEGIN_OBJECT(1,"{"),
    /******}**********/
    END_OBJECT(2,"}"),
    /******[**********/
    BEGIN_ARRAY(4,"["),
    /******]**********/
    END_ARRAY(8,"]"),
    /******null**********/
    NULL(16,"null"),
    /******数字**********/
    NUMBER(32,"数字"),
    /******字符串**********/
    STRING(64,"字符串"),
    /******布尔值**********/
    BOOLEAN(128,"true/false"),
    /******:**********/
    SEP_COLON(256,"分隔符 :"),
    /******,**********/
    SEP_COMMA(512,"分隔符 ,"),
    /******文档结束**********/
    END_DOCUMENT(1024,"文档结束");

    TokenType(int code,String describe) {
        this.code = code;
        this.describe=describe;
    }

    private int code;
    private String describe;
    
    public String getDescribe() {
		return describe;
	}
	public int getTokenCode() {
        return code;
    }
}
