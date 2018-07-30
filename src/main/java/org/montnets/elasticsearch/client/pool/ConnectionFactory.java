package org.montnets.elasticsearch.client.pool;
import org.apache.commons.pool2.PooledObjectFactory;
import java.io.Serializable;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: ConnectionFactory.java
* @Description: 连接工厂接口
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月27日 上午9:35:49 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月27日     chenhj          v1.0.0               修改原因
 */
public interface ConnectionFactory<T> extends PooledObjectFactory<T>, Serializable {

    /**
     * <p>Title: createConnection</p>
     * <p>Description: 创建连接</p>
     *
     * @return 连接
     * @throws Exception
     */
    public abstract T createConnection() throws Exception;
 }
