package com.elasticsearch.server.esServer;

import org.montnets.elasticsearch.config.EsBasicModelConfig;
import org.montnets.elasticsearch.config.EsConnectConfig;

/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) {
		EsConnectConfig esConnectConfig = new EsConnectConfig();
		EsBasicModelConfig esBasicModelConfig = new EsBasicModelConfig("demo1","1", "2");
		esConnectConfig.add(esBasicModelConfig);
		System.out.println(esConnectConfig.getIndexList());
	}
}
  