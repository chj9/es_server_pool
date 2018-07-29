package com.elasticsearch.server.esServer;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.Version;
import org.montnets.elasticsearch.config.EsBasicModelConfig;
import org.montnets.elasticsearch.config.EsConnectConfig;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		  String json = "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.readValue(json, Map.class));
	}
}
  