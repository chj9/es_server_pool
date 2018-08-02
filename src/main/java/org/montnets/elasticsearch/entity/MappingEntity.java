/**
 * 
 */
package org.montnets.elasticsearch.entity;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: MappingEntity.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月1日 下午5:28:21 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月1日     chenhj          v1.0.0               修改原因
*/
public class MappingEntity {
	@Override
	public String toString() {
		
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
					.startObject()
						.field("user", "kimchy")
						.field("postDate", new Date())
						.field("message", "trying out Elasticsearch")
					.endObject();
			String json = Strings.toString(builder);
			System.out.println(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "MappingEntity []";
	}
	public static void main(String[] args) {
		new MappingEntity().toString();
	}
}	
