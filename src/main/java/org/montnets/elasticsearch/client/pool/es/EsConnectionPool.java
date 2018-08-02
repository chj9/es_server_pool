package org.montnets.elasticsearch.client.pool.es;
import java.util.Objects;

import org.elasticsearch.client.RestHighLevelClient;
import org.montnets.elasticsearch.client.pool.ConnectionPool;
import org.montnets.elasticsearch.client.pool.PoolBase;
import org.montnets.elasticsearch.client.pool.PoolConfig;
import org.montnets.elasticsearch.config.EsConnectConfig;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsConnectionPool.java
* @Description: ES连接池
*

* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月26日 下午3:07:39 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月26日     chenhj          v1.0.0               修改原因
 */
public class EsConnectionPool extends PoolBase<RestHighLevelClient> implements ConnectionPool<RestHighLevelClient> {
	private static final long serialVersionUID = 1L;
	/**
	 * 这样设置的话连接池将都取默认值
	 * @param esConfig ES配置
	 */
    public EsConnectionPool(final EsConnectConfig esConfig) {
        this(new PoolConfig(), esConfig);
    }
    /**
     * 连接池配置
     * @param poolConfig 连接池配置
     * @param esConfig ES配置
     */
    public EsConnectionPool(final PoolConfig poolConfig, final EsConnectConfig esConfig) {
        super(Objects.requireNonNull(poolConfig, "poolConfig can not null")
        				,new EsConnectionFactory(Objects.requireNonNull(esConfig, "esConfig can not null")));
    }
    @Override
    public RestHighLevelClient getConnection() {
        return super.getResource();
    }
    @Override
    public void returnConnection(RestHighLevelClient client) {
        super.returnResource(client);
    }
    @Override
    public void invalidateConnection(RestHighLevelClient client) {
        super.invalidateResource(client);
    }
}
