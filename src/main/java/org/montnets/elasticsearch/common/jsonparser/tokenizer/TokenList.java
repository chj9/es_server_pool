package org.montnets.elasticsearch.common.jsonparser.tokenizer;


import java.util.ArrayList;
import java.util.List;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: TokenList.java
* @Description: 词法分析器
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月31日 下午5:49:12 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月31日     chenhj          v1.0.0               修改原因
 */
public class TokenList {

    private List<Token> tokens = new ArrayList<Token>();

    private int pos = 0;

    public void add(Token token) {
        tokens.add(token);
    }

    public Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    public Token peekPrevious() {
        return pos - 1 < 0 ? null : tokens.get(pos - 2);
    }

    public Token next() {
        return tokens.get(pos++);
    }

    public boolean hasMore() {
        return pos < tokens.size();
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "tokens=" + tokens +
                '}';
    }
}
