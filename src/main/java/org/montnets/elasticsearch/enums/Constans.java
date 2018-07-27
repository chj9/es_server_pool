package org.montnets.elasticsearch.enums;


import java.util.concurrent.atomic.AtomicLong;

/**
 * 
* @Title: Constans
* @Description:
* 常量类 
* @Version:1.0.0  
* @author pancm
* @date 2018年3月23日
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
     

     /**
      * 时间格式化字符串 yyyy-MM-dd HH:mm:ss.SSS
      */
     public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
     
     /**
      * 时间格式化字符串1 yyyyMMddHHmm
      */
     public static final String TIME_FORMAT1 = "yyyyMMddHHmm";
     
     /**
      * 时间格式化字符串2 yyyy-MM-dd HH:mm:ss
      */
     public static final String TIME_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
     
     /**
      * 时间格式化字符串3 yyyy-MM-dd
      */
     public static final String TIME_FORMAT3 = "yyyy-MM-dd";
     
     /**
      * 时间格式化字符串4 yyyyMMdd
      */
     public static final String TIME_FORMAT4 = "yyyyMMdd";

     /**
      * 时间格式化字符串5 yyyyMMddHHmmssSSS
      */
     public static final String TIME_FORMAT5 = "yyyyMMddHHmmssSSS";
     
     /**
      * 时间格式化字符串 yyyy-MM-dd HH:mm:ss.SSSSSSS
      */
     public static final String TIME_FORMAT6 = "yyyy-MM-dd HH:mm:ss.SSSSSSS";
     
     /**
      * 时间格式化字符串 yyyyMM
      */
     public static final String TIME_FORMAT7 = "yyyyMM";
     
     /**
      * 时间格式化字符串HH:mm:ss
      */
     public static final String TIME_FORMAT8 = "HH:mm:ss";
     
     
     public static final String DEFAULT_TIME="1900-01-01 00:00:00.000";
     
     //******   *****/
     
     /** IM_MSG上行*/
     public static final String IMMSG_MO = "2001";
     /**IM_MSG下行 */
     public static final String IMMSG_MT = "1001";
     
     /**im_msg表 */
     public static final String IM_MSG = "im_msg";

     /** 一次转移im_msg表中最大的数量 */
     public static final Integer IM_MSG_COUNT = 100000;
     
     /** 自增id */
     public static  AtomicLong IMMSG_ID = new AtomicLong();
     
     /** 尝试次数*/
     public static final Integer TEST_COUNT=500;
     
     /** 项目名称*/
     public static final String PROJECT_NAME="im_ras";

     /**
  	 * 程序版本
  	 */
  	public  static final String VERSION = "V1.2.0.16";
  	
  	 /**
  	  * 程序信息
  	 */
  	 public  static final String VERSION_MAG = "IM报表分析系统";
     
}
