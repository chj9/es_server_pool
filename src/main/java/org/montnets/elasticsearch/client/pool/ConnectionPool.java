package org.montnets.elasticsearch.client.pool;



import java.io.Serializable;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: ConnectionPool.java
* @Description: 连接池接口
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月27日 上午9:32:08 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月27日     chenhj          v1.0.0               修改原因
 */
public interface ConnectionPool<T> extends Serializable  {

    /**
     * <p>Title: getConnection</p>
     * <p>Description: 获取连接</p>
     *
     * @return 连接
     */
    public abstract T getConnection();

    /**
     * <p>Title: returnConnection</p>
     * <p>Description: 返回连接</p>
     *
     * @param conn 连接
     */
    public void returnConnection(T conn);

    /**
     * <p>Title: invalidateConnection</p>
     * <p>Description: 废弃连接</p>
     *
     * @param conn 连接
     */
    public void invalidateConnection(T conn);
}
