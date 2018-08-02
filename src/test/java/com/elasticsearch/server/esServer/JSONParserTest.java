package com.elasticsearch.server.esServer;



import java.nio.file.Files;
import java.nio.file.Paths;

import org.montnets.elasticsearch.common.jsonparser.JSONParser;
import org.montnets.elasticsearch.common.jsonparser.model.JsonArray;
import org.montnets.elasticsearch.common.jsonparser.model.JsonObject;


/**
 * Created by code4wt on 17/9/1.
 */
public class JSONParserTest {

    

	public static void main(String[] args) throws Exception {
		fromJSON2();
	}

    
    public static void fromJSON2() throws Exception {
        String json = "{\"count\": 10000002,\"_shards\": {\"total\": 8,\"successful\": 8,\"skipped\": 0,\"failed\": 0}}";
        JSONParser jsonParser = new JSONParser();
        JsonObject jsonArray = (JsonObject) jsonParser.fromJSON(json);
        
        System.out.println(jsonArray.get("count"));
    }

    
    public void beautifyJSON() throws Exception {
        String json = "{\"name\": \"狄仁杰\", \"type\": \"射手\", \"ability\":[\"六令追凶\",\"逃脱\",\"王朝密令\"],\"history\":{\"DOB\":630,\"DOD\":700,\"position\":\"宰相\",\"dynasty\":\"唐朝\"}}";
        System.out.println("原 JSON 字符串：");
        System.out.println(json);
        System.out.println("\n");
        System.out.println("美化后的 JSON 字符串：");
        JSONParser jsonParser = new JSONParser();
        JsonObject drj = (JsonObject) jsonParser.fromJSON(json);
        System.out.println(drj);
    }

//    private String getJSON() throws IOException {
//        String url = "http://music.163.com/weapi/v3/playlist/detail";
//        List<BasicNameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("params", "kJMudgZJvK8p8STuuxRpkUvO71Enw4C9y91PBkVTv2SMVnWG30eDKK1iAPcXnEah"));
//        params.add(new BasicNameValuePair("encSecKey", "d09b0b95b7d5b4e68aa7a16d6177d3f00a78bfa013ba59f309d41f18a2b4ea066cdea7863866b6283f403ddcd3bfb51f73f8ad3c6818269ceabff934a645196faf7a9aae0edde6e232b279fd495140e6252503291cf819eabbd9f3373648775201a70f179b7981d627257d3bba5a5e1b99d0732ce3e898db3614d82bcbe1a6a8"));
//        Response response = Request.Post(url)
//                .bodyForm(params)
//                .execute();
//
//        return response.returnContent().asString(Charset.forName("utf-8"));
//    }
}