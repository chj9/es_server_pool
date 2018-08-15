package org.montnets.elasticsearch.common.exception;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsInitMonException.java
* @Description: ES客户端异常
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月15日 上午9:43:57 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月15日     chenhj          v1.0.0               修改原因
 */
public  class EsClientMonException  extends RuntimeException {
    protected static Log log = LogFactory.getLog(EsClientMonException.class);


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
        public EsClientMonException(String message)
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
        public EsClientMonException(String message, Throwable cause)
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
