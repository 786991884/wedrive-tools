package com.simulation.terminal.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	private static Properties p = new Properties();  

	/** 
	 * 读取properties配置文件信息 
	 */  
	static{  
		try {  
			p.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("ServerConfig.properties"));  
		} catch (IOException e) {  
			e.printStackTrace();   
		}  
	}  
	
	 /** 
     * 根据key得到value的值
     */  
    public static String getValue(String key)  
    {  
        return p.getProperty(key);
    }  
    /**
     *  properties set值
     */
    public static void setValue(String key,String value){
    	p.setProperty(key, value);
    }
}
