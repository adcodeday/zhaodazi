package org.lu.zhaodazi.common.constant;

public class RedisKey {
    private static final String BASE_KEY = "mallchat:";
    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }

}