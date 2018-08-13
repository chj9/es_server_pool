package org.montnets.elasticsearch.client;

import java.util.Objects;

import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;

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
	private EsConnectionPool pool=null;
    public  EsConnectionPool getPool() {
         	return pool;
     }
	/**
	 * 设置池对象
	 * @param pool
	 */
	public void setPool(final EsConnectionPool pool) {
		this.pool = Objects.requireNonNull(pool,"pool can not null!please init pool");
	}	
	/**
	 * 关闭连接池
	 */
	public void closePool(){
		if(pool!=null&&!pool.isClosed()){
			pool.close();
		}
	}

}
