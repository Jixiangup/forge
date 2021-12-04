package com.bnyte.forge.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @auther bnyte
 * @date 2021-12-05 02:47
 * @email bnytezz@gmail.com
 */
public class JacksonUtils {

    /**
     * 初始化mapper对象，jackson需要
     */
    private  static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * 将Java对象转换为json字符串
     * @param data 需要被转换的对象
     * @return 返回转换之后的json字符串，如果对象为空则此时直接返回null
     */
    public static String toJSONString(Object data) {
        if (data == null) return null;
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
