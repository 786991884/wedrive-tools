package com.terminal.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/10/19
 * @modify 数据包缓存
 * @copyright opentsp
 */
public class DataPacketCache {

    private static Map<String, String> cache = new ConcurrentHashMap<String, String>();

    private static DataPacketCache instance = new DataPacketCache();
    private DataPacketCache(){}
    public static DataPacketCache getInstance (){
        return instance;
    }


    public Map<String, String> get(){
        Map<String, String> temp = cache;
        return temp;
    }

    public void add(String key , String value){
        cache.put(key, value);
    }

    public void remove(String key){
        cache.remove(key);
    }


    public String get(String key){

        return cache.get(key);
    }

}
