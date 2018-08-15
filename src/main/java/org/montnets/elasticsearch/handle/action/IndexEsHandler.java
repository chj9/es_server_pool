package org.montnets.elasticsearch.handle.action;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.XContentType;
import org.montnets.elasticsearch.client.EsPool;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.config.EsBasicModelConfig;
import org.montnets.elasticsearch.entity.EsRequestEntity;
import org.montnets.elasticsearch.handle.IBasicHandler;

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
public class IndexEsHandler implements IBasicHandler{
	//private static Logger logger = LogManager.getLogger(IndexEsHandler.class);
	private RestHighLevelClient client;
	  /*********对象池*******************/
	  private EsConnectionPool pool = null;
	private void builder(){
		this.pool=EsPool.ESCLIENT.getPool();
		this.client=pool.getConnection();
	}
	 /**
     * 判断指定的索引名是否存在
     * @param index 索引名
     * @return  存在：true; 不存在：false;
	 * @throws IOException 
     */
	public boolean existsIndex(String index) throws IOException{
		Objects.requireNonNull(index, "index can not null");
        boolean isExists=true;
		try {
			RestClient restClient = client.getLowLevelClient();
	        Response response = restClient.performRequest("HEAD","/"+index,Collections.<String, String>emptyMap());
	        isExists ="OK".equals(response.getStatusLine().getReasonPhrase());
		} catch (IOException e) {
			throw e;
		}
        return isExists;
    }
 public boolean createIndex(final EsBasicModelConfig esBasicModelConfig) throws IOException{
		 return create(esBasicModelConfig);
 }
    /**
     * 如果索引库不存在则创建一个
     * @return  成功：true; 失败：false;
     * @throws IOException 
     */
  private boolean create(final EsBasicModelConfig esBasicModelConfig) throws IOException{
    	boolean falg = true;
    Objects.requireNonNull(esBasicModelConfig,"esBasicModelConfig is not null");
	String type = Objects.requireNonNull(esBasicModelConfig.getType(),"type is not null");
	String index=Objects.requireNonNull(esBasicModelConfig.getIndex(),"index is not null");
	String mapping = esBasicModelConfig.getMappings();
	String setting = esBasicModelConfig.getSettings();
	//开始创建库
    CreateIndexRequest request = new CreateIndexRequest(index); 
    	try {
    		if(Objects.nonNull(mapping)){
			//加载数据类型
	    	request.mapping(type,mapping,XContentType.JSON);
    		}
    		if(Objects.nonNull(setting)){
	    	//分片数
	    	request.settings(setting,XContentType.JSON);
    		}
			CreateIndexResponse createIndexResponse = client.indices().create(request);
			falg = createIndexResponse.isAcknowledged();
		} catch (IOException e) {
			throw e;
		}
    	return falg;
    }
    /**
     * 设置每次可最大取多少数据，超过此数据条数报错
     * @param index 索引名
     * @param 可查询最大条数 默认 10000
     * @throws IOException 
     */
    public void maxResultWindow(String index,Integer maxResultData) throws IOException{
    	try {
	    	Objects.requireNonNull(index,"index is not null");
	    	Objects.requireNonNull(maxResultData,"maxResultData is not null");
	    	RestClient restClient =client.getLowLevelClient();
        	String source ="{\"index\":{\"max_result_window\": \"%d\"}}";
        	source=String.format(source, maxResultData);
   		 	HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
			restClient.performRequest("PUT","/"+index+"/_settings",Collections.<String, String>emptyMap(),entity);
		} catch (IOException e) {
			throw e;
		}
    }
	@Override
	public String toDSL() {
		return "not DSL";
	}
	public void builder(@Nullable EsRequestEntity esRequestEntity) {
		builder();
	}
	@Override
	public void validate() throws NullPointerException {
		Objects.requireNonNull(client, "RestHighLevelClient can not null");
	}
	@Override
	public void close() {
		if(client!=null){
			pool.returnConnection(client);
		}
	}
}
