package org.montnets.elasticsearch.client.pool;



import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.Serializable;

/**
 * <p>Title: PoolConfig</p>
 * <p>Description: 默认池配置</p>
 *
 * @author Victor
 * @version 1.0
 * @see GenericObjectPoolConfig
 * @see Serializable
 * @since 2015年9月19日
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
     * <p>Title: PoolConfig</p>
     * <p>Description: 默认构造方法</p>
     */
    public PoolConfig() {
        // defaults to make your life with connection pool easier :)
    	//setA
        setTestWhileIdle(DEFAULT_TEST_WHILE_IDLE);
        setMinEvictableIdleTimeMillis(DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
        setTimeBetweenEvictionRunsMillis(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
        setNumTestsPerEvictionRun(DEFAULT_NUM_TESTS_PER_EVICTION_RUN);
    }
}
