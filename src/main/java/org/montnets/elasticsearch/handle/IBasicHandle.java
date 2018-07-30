/**
 * 
 */
package org.montnets.elasticsearch.handle;

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
public interface IBasicHandle {
	/**
	 * 打印查询JSON语句
	 * @return
	 */
	public String toDSL();
}
