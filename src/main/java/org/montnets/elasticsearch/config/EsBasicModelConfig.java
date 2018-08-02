package org.montnets.elasticsearch.config;
import java.io.Serializable;
import java.util.Objects;

import org.montnets.elasticsearch.entity.SettingEntity;
/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: EsBasicModel.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月24日 下午2:32:43 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月24日     chenhj          v1.0.0               修改原因
*/
public class EsBasicModelConfig implements Serializable{
	private static final long serialVersionUID = 1L;
	/***索引库***/
	private String index;
	private String type;
	private String settings;
	private String mappings;	
   
	public EsBasicModelConfig(String index,String type){
			   this.index=index;
			   this.type = type;
	}
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSettings() {
		return settings;
	}

	public void setSettings(SettingEntity settings) {
		this.settings = Objects.requireNonNull(settings, "setting can not null").toDSL();
	}
	public void setSettings(String settings) {
		this.settings = Objects.requireNonNull(settings, "setting can not null");
	}
	public String getMappings() {
		return mappings;
	}

	public void setMappings(String mappings) {
		this.mappings = mappings;
	}

	@Override
	public String toString() {
		return "EsBasicModelConfig [index=" + index + ", type=" + type + ", settings=" + settings + ", mappings="
				+ mappings + "]";
	}	
	

}
