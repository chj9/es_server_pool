package org.montnets.elasticsearch.common.exception;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsIndexMonException.java
* @Description: 该类的功能描述
*  ES操作索引异常
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月15日 上午9:43:30 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月15日     chenhj          v1.0.0               修改原因
 */
public  class EsIndexMonException  extends RuntimeException {
    protected static Log log = LogFactory.getLog(EsIndexMonException.class);


        private static final long serialVersionUID = 1L;

        /**
         * 错误编码
         */
        private String errorCode;

        /**
         * 构造一个基本异常.
         *
         * @param message
         *            信息描述
         */
        public EsIndexMonException(String message)
        {
            super(message);
        }
        /**
         * 构造一个基本异常.
         *
         * @param message
         *            信息描述
         * @param cause
         *            根异常类（可以存入任何异常）
         */
        public EsIndexMonException(String message, Throwable cause)
        {
            super(message, cause);
        }
        
        public String getErrorCode()
        {
            return errorCode;
        }

        public void setErrorCode(String errorCode)
        {
            this.errorCode = errorCode;
        }
        
}
