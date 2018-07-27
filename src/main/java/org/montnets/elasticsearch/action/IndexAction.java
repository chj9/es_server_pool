package org.montnets.elasticsearch.action;




import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.montnets.elasticsearch.client.EsInit;
import org.montnets.elasticsearch.client.JSONArray;
import org.montnets.elasticsearch.client.JSONObject;

import com.elasticsearch.util.MyTools;

/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: IndexAction.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月26日 下午7:59:20 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月26日     chenhj          v1.0.0               修改原因
*/
public class IndexAction {
	 /**
     * 判断指定的索引名是否存在
     * @return  存在：true; 不存在：false;
     */
	public boolean isExistsIndex(String index){
        boolean isExists=true;
		try {
			RestClient restClient = EsConstant.client.getRhlClient().getLowLevelClient();
	        Response response = restClient.performRequest("HEAD","/"+index,Collections.<String, String>emptyMap());
	        isExists =response.getStatusLine().getReasonPhrase().equals("OK");
		} catch (IOException e) {
			logger.error("检查INDEX是否存在报错",e);
			isExists=false;
		}
        return isExists;
    }
    /**
     * 如果索引库不存在则创建一个
     * @return  成功：true; 失败：false;
     */
  public boolean createIndex(String index){
    	boolean falg = true;
    	String file = app.getMappingfile();
    	if(MyTools.isEmpty(file)){
    		logger.error("数据模板文件路径配置不能为空!");
			return false;
    	}
		//数据模版
		 String mapping_file_path[] =file.split(",");
    	//数据模板
		if (mapping_file_path==null) {
			logger.error("数据模板文件路径配置不能为空!");
			return false;
		}else{
			EsConstant.MAPPING_ARR=new JSONArray();
			for(int i=0;i<mapping_file_path.length;i++){
				String pathName = mapping_file_path[i];
					InputStream in = EsInit.class.getClassLoader().getResourceAsStream(pathName);
					String mapping = MyTools.inputstr2Str(in,"UTF-8");
					if(MyTools.isEmpty(mapping)){
							logger.error("数据模板文件不存在！,检查路径是否输入正确,异常文件名:{}",pathName);
							return false;
					}
					JSONObject json = JSON.parseObject(mapping);
					EsConstant.MAPPING_ARR.add(json);
			}
		}
	String indexflag = null;//标志位
	JSONObject mapping = null;
	//循环找出对应index的数据模板
	for(Object f:EsConstant.MAPPING_ARR){
		mapping = (JSONObject) f;
		//是否是子集
		if(index.startsWith(mapping.getString("index")))
		{
			indexflag=mapping.getString("index");
			break;//跳出内循环
		}
	}
	if(null==indexflag||null==mapping||mapping.isEmpty()){
		logger.error(index+"找不到指定的数据模板,查看文件是否正确...索引名:{}",index);
		return false;
	}
	String type = indexflag;
	JSONObject jsonMap = mapping.getJSONObject("mappings");
	for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
		type=entry.getKey();
		break;
    }
	//开始创建库
    CreateIndexRequest request = new CreateIndexRequest(index); 
    	try {
			//加载数据类型
	    	request.mapping(type,jsonMap.toJSONString(),XContentType.JSON);
	    	//分片数
	    	request.settings(mapping.getString("settings"),XContentType.JSON);
			CreateIndexResponse createIndexResponse = EsConstant.client.getRhlClient().indices().create(request);
			falg = createIndexResponse.isAcknowledged();
			if(falg){
				//设置查询单次返回最大值
				maxResultWindow(index);
			}
			logger.info("创建索引库"+index+",状态为:"+falg);
		} catch (IOException e) {
			logger.error("创建INDEX报错",e);
			falg=false;
		}catch (NullPointerException e) {
			logger.error("模板文件中的mappings或settings不能为空",e);
			falg=false;
		}
    	return falg;
    }
    /**
     * 设置每次可最大取多少数据，超过此数据条数报错
     * @throws IOException 
     */
    private void maxResultWindow(String index) throws IOException{
    	RestClient restClient = EsConstant.client.getRhlClient().getLowLevelClient();
        try {
        	JSONObject json = new JSONObject();
        	JSONObject json1 = new JSONObject();
        	json1.put("max_result_window", EsConstant.MAX_RESULT_WINDOW+"");
        	json.put("index",json1);
   		 	String source =json.toJSONString();
   		 	HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
			restClient.performRequest("PUT","/"+index+"/_settings",Collections.<String, String>emptyMap(),entity);
		} catch (IOException e) {
			throw e;
		}
    }
}
