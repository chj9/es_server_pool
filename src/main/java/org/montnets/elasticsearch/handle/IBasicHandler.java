/**
 * 
 */
package org.montnets.elasticsearch.handle;

import org.montnets.elasticsearch.entity.EsRequestEntity;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: IBasicHandle.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月27日 下午5:54:38 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月27日     chenhj          v1.0.0               修改原因
*/
public interface IBasicHandler {
	/**
	 * 打印查询JSON语句
	 * @return
	 */
	public String toDSL();
	/**
	 * 设置请求的基本设置,如连接对象、index、type、是否需要分页等等设置,所有操作接口必须实现这个接口才能执行操作
	 * @param esRequestEntity
	 */
	public void builder(EsRequestEntity esRequestEntity);
	/**
	 * 验证配置是否都确
	 */
	public void validate() throws NullPointerException;
	/**
	 * 每个操作接口使用完及时关闭返还连接
	 */
	public void close();
}
