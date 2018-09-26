package org.montnets.elasticsearch.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.common.enums.Constans;
import org.montnets.elasticsearch.common.exception.EsPoolMonException;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsPoolClient.java
* @Description: 自定义客户端
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月2日 上午9:12:21 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月2日     chenhj          v1.0.0               修改原因
 */
public enum EsPool {
	/**
	 * 自定义客户端
	 */
	ESCLIENT;
	
	private Map<String,EsConnectionPool> poolMap = new ConcurrentHashMap<String,EsConnectionPool>();
	
	/**
	 * 获取默认连接池,ID为ES-POOL1
	 * @return
	 */
    public  EsConnectionPool getPool() {
    	EsConnectionPool pool= poolMap.get(Constans.DEFAULT_POOL_ID);
    	return pool;
     }
	/**
	 * 获取连接池
	 * @param poolId 连接池ID
	 */
    public  EsConnectionPool getPool(final  String poolId) {
    	EsConnectionPool pool= poolMap.get(poolId);
    	return pool;
     }
	/**
	 * 设置池对象,默认ID为ES-POOL1
	 * @param pool
	 */
	public void setPool(final EsConnectionPool pool) {
		if(pool==null||pool.isClosed()){
    		throw new EsPoolMonException("该连接池已经被关闭,请重新初始化");
    	}
		poolMap.put(Constans.DEFAULT_POOL_ID,pool);
	}	
	/**
	 *  设置池对象,自定义池ID
	 * @param pool 对象池
	 * @param poolId 池ID
	 */
	public void setPool(final EsConnectionPool pool,String poolId) {
		if(pool==null||pool.isClosed()){
    		throw new EsPoolMonException("该连接池已经被关闭,请重新初始化");
    	}
		if(poolId==null){
    		throw new NullPointerException("连接池ID不能为空");
    	}
		poolMap.put(poolId,pool);
	}	
	/**
	 * 关闭该进程已打开的所有连接池
	 */
	public void closePool(){
		for (EsConnectionPool pool : poolMap.values()) { 
			if(pool!=null&&!pool.isClosed()){
				pool.close();
			}
		}
	}
	/**
	 * 需要关闭的连接池(不推荐直接指定连接池关闭)
	 * @param pool
	 */
	public void closePool(final  EsConnectionPool pool){
		if(pool!=null&&!pool.isClosed()){
			pool.close();
		}
	}
	/**
	 * 根据ID关闭连接池(推荐)
	 * @param poolId
	 */
	public void closePool(String poolId){
		if(poolId==null){
    		throw new NullPointerException("连接池ID不能为空");
    	}
		EsConnectionPool pool= poolMap.get(poolId);
		closePool(pool);
		poolMap.remove(poolId);
	}
}
