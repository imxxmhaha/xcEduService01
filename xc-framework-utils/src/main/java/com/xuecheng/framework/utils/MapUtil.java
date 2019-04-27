package com.xuecheng.framework.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作Map的Util类
 */
public class MapUtil {

	// 获取map里字符串值
	public static String getStrValue(Map m, String name) {
		if (m == null) {
			return "";
		}
		Object t = m.get(name);
		if (t == null)
			return "";
		return ((String) m.get(name).toString()).trim();
	}


	//map转成bean
	 public static Object convertMap(Class type, Map map)
	            throws IntrospectionException, IllegalAccessException,
	            InstantiationException, InvocationTargetException {
	        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
	        Object obj = type.newInstance(); // 创建 JavaBean 对象

	        // 给 JavaBean 对象的属性赋值
	        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
	        for (int i = 0; i< propertyDescriptors.length; i++) {
	            PropertyDescriptor descriptor = propertyDescriptors[i];
	            String propertyName = descriptor.getName();

	            if (map.containsKey(propertyName)) {
	                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
	                Object value = map.get(propertyName);

	                Object[] args = new Object[1];
	                args[0] = value;

	                descriptor.getWriteMethod().invoke(obj, args);
	            }
	        }
	        return obj;
	    } 
	 
	 
	 
	    public static String underlineToCamel2(String param){
	        param = param.toLowerCase();//此处为全部转小写，方便根据_判定后一位转驼峰
	        if (param==null||"".equals(param.trim())){
	            return "";
	        }
	        //使用正则表达式
	        StringBuilder sb=new StringBuilder(param);
	        Matcher mc= Pattern.compile("_").matcher(param);
	        int i=0;
	        while (mc.find()){
	            int position=mc.end()-(i++);
	            //String.valueOf(Character.toUpperCase(sb.charAt(position)));
	            sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());
	        }
	        return sb.toString();
	    }
	    
	    public static Map<String, Object> toReplaceUnderlineKeyLow(Map<String, Object> map) {
	        Map re_map = new HashMap();
	        if(re_map != null) {
	            Iterator var2 = map.entrySet().iterator();
	            while(var2.hasNext()) {
	                Entry<String, Object> entry = (Entry)var2.next();
	                String key =   (String)entry.getKey();
	                if(!StringUtils.isEmpty(key) && key.contains("_")) {
	                	 key = underlineToCamel2((String)entry.getKey());
	                }
	                re_map.put(key,MapUtil.getStrValue(map,(String)entry.getKey()));
	            }
	            map.clear();
	        }
//	        re_map.put("testkey", "");
	        return re_map;
	    }
	    
	    public static List<Map> toReplaceUnderlineKeyLow(List<Map> underlineList){
			for (int i = 0; i < underlineList.size(); i++) {
				Map resMap = toReplaceUnderlineKeyLow(underlineList.get(i));
				underlineList.set(i, resMap);
			}
			return underlineList;
	    }
	    
	    public static void main(String[] args) {
	    	List<Map> list = new ArrayList<>();
			Map map = new HashMap<>();
			map.put("xxmName", "小鸣哥");
			map.put("xxm_age", "18");
			map.put("xxmAddr", "白石洲");
			map.put("xxm_gender", "male");
			
			Map map2 = new HashMap<>();
			map2.put("xc_name", "李双");
			map2.put("xcAge", "17");
			map2.put("xc_addr", "武汉");
			map2.put("xcGender", "female");
			
			list.add(map);
			list.add(map2);
			List<Map> replaceUnderlineKeyLow = toReplaceUnderlineKeyLow(list);
			System.out.println(replaceUnderlineKeyLow);
		}

}
