package com.locojoy.jmonitor;

import java.util.List;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.locojoy.jmonitor.MultiLine.SepKey;

public abstract class ChartChain {
	
	public final static String TYPE_ASCOUNTER = "counter";
	public final static String TYPE_ASCOUNTER_SHOWRATE = "counterrate";
	public final static String TYPE_SHOWRATE = "rate";
	
	public final static String GRAPH_PIE = "pie";
	
	static public class First implements JSONAware{
		private boolean chart;
		private String name;
		
		private String key;
		private Integer days;
		private Integer zid;
		private List<String> values;
		private SepKey sepkey; //格式为<Key>.<Min>.<Max>.<Sep>
		private String graph; //pie, column, line
		private String type;
		private int pieslices;
		
		public First(){
		}
		public First(String cn){
			chart = true;
			name = cn;
		}
		public First(String b, String k, Integer d, Integer z, List<String> vs, SepKey sk, String graph, String type, int slices){
			chart = false;
			name = b;
			key = k;
			days = d;
			zid = z;
			values = vs;
			sepkey = sk;
			this.graph = graph;
			this.type = type;
			this.pieslices = slices;
		}
		
		public boolean isChart(){
			return chart;
		}
		public String getName(){
			return name;
		}
		public String getKey(){
			return key;
		}
		public Integer getDays(){
			return days;
		}
		public Integer getZid(){
			return zid;
		}
		public List<String> getValues(){
			return values;
		}
		public SepKey getSepkey(){
			return sepkey;
		}
		public String getGraph(){
			return graph;
		}
		public String getType(){
			return type;
		}
		public int getPieSlices(){
			return pieslices;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public String toJSONString() {
			JSONObject jo = new JSONObject();
			jo.put("chart", chart);
			jo.put("name", name);
			
			if (!chart){
				jo.put("key", key);
				jo.put("days", days);
				jo.put("zid", zid);
				jo.put("values", values);
				if (null != sepkey)
					jo.put("sepkey", sepkey);
				if (null != graph && !graph.isEmpty())
					jo.put("graph", graph);
				
				if (!type.isEmpty())
					jo.put("type", type);
				
				if (pieslices > 0)
					jo.put("pieslices", pieslices);
			}
			return JSONValue.toJSONString(jo);
		}
	}
	
	static public class Next implements JSONAware{
		private boolean chart;
		private String name;
		
		private List<String> values;
		private SepKey sepkey;
		private String graph;
		private String type; 
		private int pieslices;
		
		public Next(){
		}
		public Next(String n){
			name = n;
			chart = true;
		}
		public Next(String n, List<String> vs, SepKey sk, String graph, String type, int slices){
			name = n;
			values = vs;
			sepkey = sk;
			chart = false;
			this.graph = graph;
			this.type = type;
			this.pieslices = slices;
		}
		public boolean isChart(){
			return chart;
		}
		public String getName(){
			return name;
		}
		public List<String> getValues(){
			return values;
		}
		public SepKey getSepkey(){
			return sepkey;
		}
		public String getGraph(){
			return graph;
		}
		public String getType(){
			return type;
		}
		public int getPieSlices(){
			return pieslices;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public String toJSONString() {
			JSONObject jo = new JSONObject();
			jo.put("chart", chart);
			if (chart){
				jo.put("name", name);
			}else{
				jo.put("key", name);
				jo.put("values", values);
				if (null != sepkey)
					jo.put("sepkey", sepkey);
				if (null != graph && !graph.isEmpty())
					jo.put("graph", graph);
				
				if (!type.isEmpty())
					jo.put("type", type);
				if (pieslices > 0)
					jo.put("pieslices", pieslices);
			}
			return JSONValue.toJSONString(jo);
		}
		
	}
	
	public abstract String getChartChainName();
	public abstract String getXmlDescription();
	
	public abstract First getFirst();
	public abstract List<Next> getNexts();
}
