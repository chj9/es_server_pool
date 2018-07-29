package org.montnets.elasticsearch.common.enums;

import org.elasticsearch.Version;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: Constans.java
* @Description: 程序常量类 
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月27日 下午6:40:29 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月27日     chenhj          v1.0.0               修改原因
 */
public enum Constans {
	INSTANCE;
	
	/**
	 * 分隔符
	 */
	public static final String SEPERATOR ="|";
	/**
	 *  符号"|"的拆分符
	 */
	public static final String SEPERATOR_SPLIT ="\\|";
	
    /**
     * 时间分隔符
     */
     public static final String TIME_SEPARATOR = ":";

     /**
      * 逗号分隔符
      */
     public static final String COMMA_SIGN=",";
     /**
      * 中文逗号分隔符
      */
     public static final String COMMA_SIGN_CH="，";
     
     /**
      * 横线分隔符
      */
     public static final String LINE_SIGN="-";
     public static final String DEFAULT_TIME="1900-01-01 00:00:00.000";
     /**
  	 * 程序版本
  	 */
  	public  static final String VERSION = "V1.0";
  	
  	 /**
  	  * 程序信息
  	 */
  	 public  static final String VERSION_MAG = "elasticsearch 连接池...";
  	 /**
  	  * ES版本
  	  */
  	 public static final String ES_VERSION=Version.CURRENT.toString();
     
}
