package org.montnets.elasticsearch.common.jsonparser.exception;


/**
 * Created by code4wt on 17/5/11.
 */
public class JsonTypeException extends RuntimeException {

    /**
	 *@Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	public JsonTypeException(String message) {
        super(message);
    }
}
