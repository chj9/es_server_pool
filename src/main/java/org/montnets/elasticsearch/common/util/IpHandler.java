package org.montnets.elasticsearch.common.util;




import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: NetAddrUtil.java
* @Description: 该类的功能描述
*	从url中分离ip和port
* @version: v1.0.0
* @author: chenhj
* @date: 2018年5月25日 下午5:04:44 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月14日     chenhj          v1.0.0               修改原因
 */
public class IpHandler {
	
	 private String ip;
	 private Integer port;
     private static Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d{0,5})?");  
    /** 
     * 从url中分析出hostIP:PORT<br/> 
     * @param url 
     * */  
    public  void IpPortFromUrl(String url) {  

        String host = "";  
       
        Matcher matcher = p.matcher(url);  
        if (matcher.find()) {  
            host = matcher.group() ;  
        }  
        // 如果  
        if(host.contains(":") == false){  
        	this.ip=host;
        	this.port=80;
        }else{ 
        	String[] ipPortArr = host.split(":");  
        	this.ip=ipPortArr[0];
        	this.port=Integer.valueOf(ipPortArr[1].trim());
        }
    }  
      
    public String getIp() {
		return ip;
	}

	public Integer getPort() {
		return port;
	}
}
