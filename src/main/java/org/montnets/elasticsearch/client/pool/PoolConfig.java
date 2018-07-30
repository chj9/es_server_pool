package org.montnets.elasticsearch.client.pool;



import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.Serializable;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: PoolConfig.java
* @Description: 默认池配置
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月30日 下午1:48:55 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月30日     chenhj          v1.0.0               修改原因
 */
public class PoolConfig extends GenericObjectPoolConfig<Object> implements Serializable {

    /**
     * DEFAULT_TEST_WHILE_IDLE
     */
    public static final boolean DEFAULT_TEST_WHILE_IDLE = true;
    /**
     * DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS
     */
    public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 60000;
    /**
     * DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS
     */
    public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 30000;
    /**
     * DEFAULT_NUM_TESTS_PER_EVICTION_RUN
     */
    public static final int DEFAULT_NUM_TESTS_PER_EVICTION_RUN = -1;
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2414567557372345057L;

    /**
     * 默认构造方法
     */
    public PoolConfig() {
        setTestWhileIdle(DEFAULT_TEST_WHILE_IDLE);
        setMinEvictableIdleTimeMillis(DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
        setTimeBetweenEvictionRunsMillis(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
        setNumTestsPerEvictionRun(DEFAULT_NUM_TESTS_PER_EVICTION_RUN);
    }
}
