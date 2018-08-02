package org.montnets.elasticsearch.common.jsonparser.tokenizer;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: Token.java
* @Description: 内容实体
*封装词类型和字面量
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月31日 下午5:59:32 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月31日     chenhj          v1.0.0               修改原因
 */
public class Token {
	/**********内容类型***********/
    private TokenType tokenType;
    /**********内容值***********/
    private String value;

    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", value='" + value + '\'' +
                '}';
    }
}
