package org.montnets.elasticsearch.common.jsonparser.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.montnets.elasticsearch.common.jsonparser.BeautifyJsonUtils;
import org.montnets.elasticsearch.common.jsonparser.exception.JsonTypeException;

/**
 * JSON数组实体类
 */
public class JsonArray implements Iterable<Object> {

    private List<Object> list = new ArrayList<Object>();

    public void add(Object obj) {
        list.add(obj);
    }

    public Object get(int index) {
        return list.get(index);
    }
    public int size() {
        return list.size();
    }
    public JsonObject getJsonObject(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonObject)) {
            throw new JsonTypeException("Type of value is not JsonObject");
        }

        return (JsonObject) obj;
    }

    public JsonArray getJsonArray(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonArray)) {
            throw new JsonTypeException("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }

    @Override
    public String toString() {
        return BeautifyJsonUtils.beautify(this);
    }

    public Iterator<Object> iterator() {
        return list.iterator();
    }
}
