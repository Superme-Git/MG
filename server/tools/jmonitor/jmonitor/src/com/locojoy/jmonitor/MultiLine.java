package com.locojoy.jmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONAware;
import org.json.simple.JSONValue;

import org.json.simple.JSONObject;

public final class MultiLine {
	
	static public final class SepKey implements JSONAware{
		private String key;
		private int min;
		private int max;
		private int sep;
		
		public SepKey(){
		}
		
		public SepKey(String k, int i, int a, int s){
			key = k;
			min = i;
			max = a;
			sep = s;
		}
		
		public String getKey(){
			return key;
		}
		public int getMin(){
			return min;
		}
		public int getMax(){
			return max;
		}
		public int getSep(){
			return sep;
		}
		
		@Override
		public String toString(){
			return toJSONString();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public String toJSONString() {
			JSONObject jo = new JSONObject();
			jo.put("key", key);
			jo.put("min", min);
			jo.put("max", max);
			jo.put("sep", sep);
			return JSONValue.toJSONString(jo);
		}
		
		public void verify() throws Exception{
			if (key.length() == 0)
				throw new Exception("sepkey key empty");
			if (min > max)
				throw new Exception("sepkey min > max");
			if (sep <= 0)
				throw new Exception("sepkey sep <= 0");
		}

		
	}
	
	static public final class Range {
		private Long low;// 都是闭区间,null表示无区间。
		private Long up;
		
		public Range(){
		}
		public Range(Long l, Long u) {
			low = l;
			up = u;
		}
		public Long getLow() {
			return low;
		}
		public Long getUp() {
			return up;
		}
		
		public boolean verify(){
			if (low != null && up != null)
				return low <= up;
			return true;
		}
	}

	private String bean;
	private List<String> values = new ArrayList<String>();
	private String key;
	
	private Map<String, MultiLine.Range> ranges = new HashMap<String, MultiLine.Range>();
	private boolean dayline;
	
	private SepKey sepkey;
	
	public MultiLine(String b, List<String> vs, String k, boolean d, SepKey sk){
		bean = b;
		values = vs;
		key = k;
		dayline = d;
		sepkey = sk;
	}
	
	public MultiLine(){
	}
	
	public String getBean(){
		return bean;
	}
	
	public List<String> getValues(){
		return values;
	}
	
	public String getKey(){
		return key;
	}
	
	public boolean isDayline(){
		return dayline;
	}
	
	public Map<String, MultiLine.Range> getRanges(){
		return ranges;
	}
	public SepKey getSepkey(){
		return sepkey;
	}
	
	public void verify() throws Exception{
		if (values.size() > 1 && null != sepkey)
			throw new Exception("multiline MUST has one value where sepkey exist");
		if (null != sepkey)
			sepkey.verify();
	}
}
