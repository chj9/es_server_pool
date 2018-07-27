package org.montnets.elasticsearch.client.pool;



/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: ConnectionException.java
* @Description: 连接异常
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月27日 上午9:36:16 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月27日     chenhj          v1.0.0               修改原因
 */
public class ConnectionException extends RuntimeException {

    private static final long serialVersionUID = -6503525110247209484L;

    public ConnectionException() {
        super();
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(Throwable e) {
        super(e);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
