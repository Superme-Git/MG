package com.locojoy.jmonitor;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public abstract class Localize {

	public static class KeyLocal implements JSONAware {
		private String local;
		private Map<Long, String> seps = new HashMap<Long, String>();

		public KeyLocal() {
		}

		public KeyLocal(String l, Map<Long, String> s) {
			local = l;
			seps = s;
		}

		public String getLocal() {
			return local;
		}

		public Map<Long, String> getSeps() {
			return seps;
		}

		@SuppressWarnings("unchecked")
		@Override
		public String toJSONString() {
			JSONObject jo = new JSONObject();
			jo.put("local", local);
			jo.put("seps", seps);
			return JSONValue.toJSONString(jo);
		}
	}

	public abstract String getName();

	public abstract String getXmlDescription() ;

	public abstract boolean isBaseKey();
	
	public boolean isReadOnly(){ 
		return false;
	}
	
	public String getLocalName() {
		return null;
	};

	public Map<String, KeyLocal> getKeyLocals() {
		return null;
	};

	public Map<String, String> getValueLocals() {
		return null;
	};

}
