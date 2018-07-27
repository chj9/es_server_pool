package com.elasticsearch.server.esServer;


import org.elasticsearch.client.RestHighLevelClient;
import org.montnets.elasticsearch.client.pool.PoolConfig;
import org.montnets.elasticsearch.client.pool.es.EsConnectionPool;
import org.montnets.elasticsearch.config.EsConnectConfig;
import org.montnets.elasticsearch.enums.EsConnect;

public class EsTest {


    public void test() throws Exception {

		PoolConfig config = new PoolConfig();
		config.setMaxTotal(20);
		//最大空闲数
		config.setMaxIdle(5);
		//最大等待时间
		config.setMaxWaitMillis(1000);
		config.setTestOnBorrow(true);
//		String scheme= "http";
//		 //集群IP
//		//String hostname[]="192.169.2.98:9200,192.169.2.188:9200,192.169.2.156:9200,192.169.0.24:9200".split(",");
//		String hostname[]="192.169.0.176:9200,192.169.0.87:9200,192.169.0.88:9200".split(",");
//		 //indexs
//		 String indexs[] = "im_msg".split(",");
//		 // 集群名称
//		 String clusterName= "bigData-cluster";
		EsConnectConfig esConnectConfig = new EsConnectConfig();
		esConnectConfig.setClusterName("bigData-cluster");
		String [] nodes={"192.169.2.98:9200","192.169.2.188:9200","192.169.2.156:9200","192.169.0.24:9200"};
		esConnectConfig.setNodes(nodes);
		esConnectConfig.setScheme(EsConnect.HTTP);
		
		EsConnectionPool pool = new EsConnectionPool(config, esConnectConfig);
		RestHighLevelClient conn = pool.getConnection();
		System.out.println(pool.getNumActive());
		System.out.println(pool.getNumIdle());
		System.out.println(conn.ping());
		pool.returnConnection(conn);
		
		pool.close();
    }
    public static void main(String[] args) throws Exception {
		new EsTest().test();
	}
}
