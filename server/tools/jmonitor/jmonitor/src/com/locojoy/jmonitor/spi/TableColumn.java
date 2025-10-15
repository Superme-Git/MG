package com.locojoy.jmonitor.spi;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;


class TableColumn {
	
	static enum ColumnType{
		TBYTE("byte", "TINYINT"),
		TSHORT("short", "SMALLINT"),
		TINT("int", "INT"),
		TLONG("long", "BIGINT");
		
		private final String str;
		private final String sqltype;
		
		ColumnType(String s, String sql){
			str = s;
			sqltype = sql;
		}
		
		public String toString(){
			return str;
		}
		
		public String sqlType(){
			return sqltype;
		}
		
		private static final Map<String, ColumnType> map = new HashMap<String, ColumnType>();
		static {
			for(ColumnType ct : values()){
				map.put(ct.toString(), ct);
			}
		}
		public static ColumnType fromString(String s){
			return map.get(s);
		}
	}
	
	
	private String origname;
	private String origattr;
	private ColumnType type;

	private String name;
	private String attr;
	private String baseattr;

	
	
	private TableColumn(Element ele, String prefix) throws Exception {// 为什么要加这个前缀，请看JMXServer取的时候的处理。

		attr = origattr = ele.getAttribute("attr");
		if (attr.length() == 0)
			throw new Exception("column.attr empty");
		attr = prefix + attr;
		
		name = origname = ele.getAttribute("name");
		if (origname.length() == 0)
			name = makeNameFromAttr(origattr);

		if (name.equalsIgnoreCase(ZID))
			throw new Exception( name + " column.name equals " + ZID);
		if (name.equalsIgnoreCase(TIME))
			throw new Exception( name + " column.name equals " + TIME);
		if (name.length() > MAX_COLUMN_LEN)
			throw new Exception( name + " column.name len > " + MAX_COLUMN_LEN);

		type = ColumnType.fromString( ele.getAttribute("type") );
		if (null == type)
			throw new Exception(type + " column.type " + type + " not allowed");

		int i = origattr.indexOf('.');
		baseattr = (i == -1) ? origattr : origattr.substring(0, i);
	}
	
	static TableColumn asMapKey( Element ele ) throws Exception{
		return new TableColumn(ele, "key.");
	}
	
	static TableColumn asMapValue( Element ele ) throws Exception{
		return new TableColumn(ele, "value.");
	}
	
	static TableColumn asValue(Element ele) throws Exception{
		return new TableColumn( ele, "");
	}
	
	String getOrigName() {
		return origname;
	}

	String getOrigAttr() {
		return origattr;
	}

	String getName() {
		return name;
	}

	String getAttr() {
		return attr;
	}

	String getBaseAttr() {
		return baseattr;
	}

	ColumnType getType() {
		return type;
	}

	String DDL() {
		StringBuffer sb = new StringBuffer();
		sb.append(name).append(" ").append(type.sqltype);
		return sb.toString();
	}
	
	
	static final String ZID  = "zid";
	static final String TIME = "time";

	static final int MAX_COLUMN_LEN = 64;
	
	static String makeNameFromAttr(String attr) {
		return attr.replace('.', '_');
	}

}
