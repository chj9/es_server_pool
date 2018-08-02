package org.montnets.elasticsearch.common.jsonparser;


import java.io.IOException;
import java.io.StringReader;

import org.montnets.elasticsearch.common.jsonparser.parser.Parser;
import org.montnets.elasticsearch.common.jsonparser.tokenizer.CharReader;
import org.montnets.elasticsearch.common.jsonparser.tokenizer.TokenList;
import org.montnets.elasticsearch.common.jsonparser.tokenizer.Tokenizer;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: JSONParser.java
* @Description: 该类的功能描述
* 自定义json解析器
* 原理:输入是一个 JSON 字符串，输出是一个 JSON 对象
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月31日 下午5:46:19 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月31日     chenhj          v1.0.0               修改原因
 */
public class JSONParser {
    private Tokenizer tokenizer = new Tokenizer();
    private Parser parser = new Parser();
    public Object fromJSON(String json) throws IOException {
        CharReader charReader = new CharReader(new StringReader(json));
        TokenList tokens = tokenizer.tokenize(charReader);
        return parser.parse(tokens);
    }
}
