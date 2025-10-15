package com.locojoy.jmonitor.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.management.ObjectName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.locojoy.db.ApplicationThreadFrameWork;
import com.locojoy.db.Context;
import com.locojoy.jmonitor.ITable;
import com.locojoy.jmonitor.Manager.TableData;
import com.locojoy.jmonitor.MultiLine;
import com.locojoy.jmonitor.spi.LocalizeImpl.BaseKey;
import com.locojoy.jmonitor.spi.TableColumn.ColumnType;
import com.locojoy.jmonitor.util.DomHelper;

class Table implements ITable {

	private String domain; // 至少有domain，和type
	private String type;
	private String name;
	private String orig_tablename;

	private int fetch_period_minute; // >0 && <60*24
	private int fetch_offset_minute; // >=0 && <60*24
	private boolean peak; // <1>为true代表每个value都是峰值类型的，比如在线数。GAUGE或COUNTER。<2>为false对应的含义为需要累积
	private int sample; // <1>peak=true时代表每隔多久抽取数据一次,默认为360。<2>peak=false时不起作用，直接累积一天值。
	
	private boolean dynamic_mbean; //动态MBean

	private Map<String, TableColumn> keycolumn_map = new HashMap<String, TableColumn>();
	private Map<String, TableColumn> column_map = new HashMap<String, TableColumn>();

	private TableManager manager;
	private Element element;
	private ObjectName objectName;
	private String identification;
	private String tablename;
	
	private String xmlDescription;

	private List<Long> table_renames = new ArrayList<Long>();

	Table(TableManager man, Element ele) throws Exception {

		manager = man;
		element = ele;

		domain = ele.getAttribute("domain");
		type = ele.getAttribute("type");
		name = ele.getAttribute("name");
		if (domain.isEmpty())
			throw new Exception("domain empty");
		if (type.isEmpty())
			throw new Exception("type empty");

		identification = makeIdentification(domain, type, name);
		orig_tablename = ele.getAttribute("tablename");

		fetch_period_minute = Integer.parseInt(ele.getAttribute("fetch_period_minute"));
		if (fetch_period_minute <= 0 || fetch_period_minute > 1440 || (1440 % fetch_period_minute) != 0)
			throw new Exception(identification + " fetch_period_minute <= 0 || > 1440 || 1440% != 0");

		String off = ele.getAttribute("fetch_offset_minute");
		fetch_offset_minute = off.isEmpty() ? 0 : Integer.parseInt(off);
		if (fetch_offset_minute < 0 || fetch_offset_minute > 1440)
			throw new Exception(identification + " fetch_offset_minute < 0 || > 1440");

		tablename = orig_tablename.isEmpty() ? identification : orig_tablename;
		if (tablename.length() > MAX_TABLENAME_LEN)
			throw new Exception("tablename > " + MAX_TABLENAME_LEN);
		if (fetch_period_minute != 1400) {
			if (tablename.toUpperCase().endsWith(SUFFIX_DAY_SUMZID))
				throw new Exception(tablename + " with " + SUFFIX_DAY_SUMZID);

			if (tablename.toUpperCase().endsWith(SUFFIX_DAY))
				throw new Exception(tablename + " suffix " + SUFFIX_DAY);

			if (tablename.toUpperCase().endsWith(SUFFIX_SUMZID))
				throw new Exception(tablename + " suffix " + SUFFIX_SUMZID);
		}

		peak = ele.getAttribute("peak").equals("true");

		if (peak && fetch_period_minute < 1440) {
			String sam = ele.getAttribute("sample");
			sample = sam.isEmpty() ? 360 : Integer.parseInt(sam);
			if (sample < 0 || sample > 1440 || (sample % fetch_period_minute) != 0)
				throw new Exception(identification + " sample < 0 || > 1440 || %fetch_period_minute != 0");
		}
		
		dynamic_mbean = ele.getAttribute("dynamic_mbean").equals("true");

		Set<String> nameset = new HashSet<String>();
		Set<String> attrset = new HashSet<String>();

		for (Element e : DomHelper.getElementsByName(ele, "keycolumn")) {
			TableColumn c = dynamic_mbean ? TableColumn.asValue(e) : TableColumn.asMapKey(e);
			keycolumn_map.put(c.getName(), c);
			if (!nameset.add(c.getName().toLowerCase()))
				throw new Exception(c + " name duplicate");
			if (!attrset.add(c.getAttr()))
				throw new Exception(c + " attr duplicate");
		}

		for (Element e : DomHelper.getElementsByName(ele, "column")) {
			TableColumn c = (keycolumn_map.isEmpty() || dynamic_mbean) ? TableColumn.asValue(e) : TableColumn.asMapValue(e);
			column_map.put(c.getName(), c);

			if (!nameset.add(c.getName().toLowerCase()))
				throw new Exception(c + " name duplicate");
			if (!attrset.add(c.getAttr()))
				throw new Exception(c + " attr duplicate");
		}

		if (column_map.isEmpty())
			throw new Exception("column empty");

		StringBuilder sb = new StringBuilder();
		sb.append(domain).append(":");
		sb.append("type=").append(type);
		if (name.length() > 0)
			sb.append(",name=").append(name);
		else if (keycolumn_map.isEmpty()) // 动态key，由beancoder保证objectname里不含name.其他都假设含有name
			sb.append(",*");
		objectName = ObjectName.getInstance(sb.toString());
		
		if (dynamic_mbean && ( !(keycolumn_map.size() == 1) || !(column_map.size() == 1) ) )
			throw new Exception("dynamic_mbean should has only 1 keycolumn, 1 column");
		
		fetchTableRenames();
		
		xmlDescription = xml();
	}

	private void fetchTableRenames() {
		if (fetch_period_minute == 1440)
			return;

		Context ctx = ApplicationThreadFrameWork.getContext();
		Connection conn = ctx.getConnection();
		Statement st = null;
		ResultSet rs = null;
		boolean close_ok = true;
		try {
			st = conn.createStatement();

			Set<Long> rns = new TreeSet<Long>();
			rs = st.executeQuery("SHOW TABLE STATUS LIKE \"" + tablename + "_2%\"");
			while (rs.next()) {
				String day = rs.getString("Name").substring(tablename.length() + 1);
				try {
					Date dt = sdf.parse(day);
					rns.add(dt.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			synchronized (table_renames) {
				table_renames.addAll(rns);
			}

		} catch (SQLException e) {
			close_ok = false;
		} finally {
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e1) {
				close_ok = false;
			} finally {
				try {
					if (null != st)
						st.close();
				} catch (SQLException e2) {
					close_ok = false;
				}
			}
			if (close_ok)
				ctx.returnConnection(conn);
			else
				ctx.discardConnection(conn);

		}

	}

	private List<Long> getTableRenames() {
		synchronized (table_renames) {
			return new ArrayList<Long>(table_renames);
		}
	}

	long getCurrentTableSize() {

		Context ctx = ApplicationThreadFrameWork.getContext();
		Connection conn = ctx.getConnection();
		Statement st = null;
		ResultSet rs = null;
		boolean close_ok = true;
		try {
			st = conn.createStatement();

			rs = st.executeQuery("SHOW TABLE STATUS LIKE \"" + tablename + "\"");
			while (rs.next()) {
				return rs.getLong("Data_length");
			}
		} catch (SQLException e) {
			close_ok = false;
		} finally {
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e1) {
				close_ok = false;
			} finally {
				try {
					if (null != st)
						st.close();
				} catch (SQLException e2) {
					close_ok = false;
				}
			}
			if (close_ok)
				ctx.returnConnection(conn);
			else
				ctx.discardConnection(conn);
		}
		return 0;
	}

	TableManager getTableManager() {
		return manager;
	}

	Element getElement() {
		return element;
	}

	String getDomain() {
		return domain;
	}

	String getType() {
		return type;
	}

	String getName() {
		return name;
	}

	boolean isPeak() {
		return peak;
	}

	String getTableName() {
		return tablename;
	}

	ObjectName getObjectName() {
		return objectName;
	}

	int getFetchPeriodMinute() {
		return fetch_period_minute;
	}

	int getFetchOffsetMinute() {
		return fetch_offset_minute;
	}

	Collection<TableColumn> getColumnList() {
		return Collections.unmodifiableCollection(column_map.values());
	}

	Collection<TableColumn> getKeyColumnList() {
		return Collections.unmodifiableCollection(keycolumn_map.values());
	}

	public String toString() {
		return identification;
	}

	public String getIdentification() {
		return identification;
	}
	
	@Override
	public String getXmlDescription()  {
		return xmlDescription;
	}
	
	private String xml() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element ele = document.createElement("table");
		ele.setAttribute("domain", domain);
		ele.setAttribute("type", type);
		if (name.length() > 0)
			ele.setAttribute("name", name);
		if (orig_tablename.length() > 0)
			ele.setAttribute("tablename", orig_tablename);
		ele.setAttribute("fetch_offset_minute", String.valueOf(fetch_offset_minute));
		ele.setAttribute("fetch_period_minute", String.valueOf(fetch_period_minute));
		ele.setAttribute("peak", String.valueOf(peak));
		
		if (dynamic_mbean)
			ele.setAttribute("dynamic_mbean", String.valueOf(true));
		
		if (peak && fetch_period_minute < 1440) {
			ele.setAttribute("sample", String.valueOf(sample));
		}

		for (TableColumn c : keycolumn_map.values()) {
			Element e = document.createElement("keycolumn");
			e.setAttribute("attr", c.getOrigAttr());
			e.setAttribute("type", c.getType().toString());
			if (c.getOrigName().length() > 0)
				e.setAttribute("name", c.getOrigName());
			ele.appendChild(e);
		}

		for (TableColumn c : column_map.values()) {
			Element e = document.createElement("column");
			e.setAttribute("attr", c.getOrigAttr());
			e.setAttribute("type", c.getType().toString());
			if (c.getOrigName().length() > 0)
				e.setAttribute("name", c.getOrigName());
			ele.appendChild(e);
		}

		document.appendChild(ele);
		return DomHelper.documentToString(document);
	}
	

	@Override
	public boolean isWorking() {
		return true;
	}
	

	@Override
	public List<String> getKeyList() {
		List<String> keys = new ArrayList<String>();
		keys.add(TableColumn.TIME);
		keys.add(TableColumn.ZID);
		for (TableColumn c : keycolumn_map.values())
			keys.add(c.getName());
		return keys;
	}

	@Override
	public List<String> getValueList() {
		List<String> vals = new ArrayList<String>();
		for (TableColumn c : column_map.values())
			vals.add(c.getName());
		return vals;
	}
	
	
	boolean isDynamicMBean() {
		return dynamic_mbean;
	}


	boolean isValueNumber() {
		return column_map.size() == 1;
	}

	String getFirstValueAttr() {
		for (TableColumn c : column_map.values())
			return c.getAttr();
		return "";
	}

	Set<String> getBaseAttrs() {
		assert !keycolumn_map.isEmpty();

		Set<String> attrs = new HashSet<String>();
		for (TableColumn c : keycolumn_map.values())
			attrs.add(c.getBaseAttr());
		for (TableColumn c : column_map.values())
			attrs.add(c.getBaseAttr());
		return attrs;
	}

	Set<String> getColumnAttrs() {
		Set<String> attrs = new HashSet<String>();
		for (TableColumn c : keycolumn_map.values())
			attrs.add(c.getAttr());
		for (TableColumn c : column_map.values())
			attrs.add(c.getAttr());
		return attrs;
	}

	// ///////////////////////////////////

	void prepareDb(boolean dropOld) throws Exception {
		createTable_day_sumzid_dropold(false, false, dropOld);
		createTable_day_sumzid_dropold(false, true, dropOld);

		if (fetch_period_minute < 1440) {
			createTable_day_sumzid_dropold(true, false, dropOld);
			createTable_day_sumzid_dropold(true, true, dropOld);
		}
	}

	void renameDb(long daytime) {

		if (fetch_period_minute == 1440)
			return;

		String tname = getTableName_day_sumzid(false, false);
		String tnameAt = getTableNameAt(tname, daytime);
		try {
			executeUpdate(String.format("RENAME TABLE %s TO %s", tname, tnameAt));
			createTable_day_sumzid_dropold(false, false, false);
		} catch (Exception e) {
		}

		tname = getTableName_day_sumzid(false, true);
		tnameAt = getTableNameAt(tname, daytime);
		try {
			executeUpdate(String.format("RENAME TABLE %s TO %s", tname, tnameAt));
			createTable_day_sumzid_dropold(false, true, false);
		} catch (Exception e) {
		}
		synchronized (table_renames) {
			table_renames.add(daytime);
		}
	}

	void storeDb(int zid, long time, Map<String, Long> keymap) {
		
		if (dynamic_mbean){
			
			BaseKey bk = manager.getConfigure().getLocalizeManager().getBaseKeyForDynamicMBean(identification);
			if (null == bk){
				logger.warning(identification + ".storeDb as DynamicMBean can not found basekey");
				return;
			}
			
			for(Map.Entry<String, Long> ent : keymap.entrySet()){
				List<String> columnName = new ArrayList<String>();
				List<Object> columnObj = new ArrayList<Object>();
				
				columnName.add(TableColumn.TIME);
				columnObj.add(time);

				columnName.add(TableColumn.ZID);
				columnObj.add(zid);
				
				columnName.add(keycolumn_map.keySet().iterator().next());
				columnObj.add( bk.getKeyNumber(ent.getKey()) );
				
				columnName.add(column_map.keySet().iterator().next());
				columnObj.add( ent.getValue() );
				
				Context ctx = ApplicationThreadFrameWork.getContext();
				try {
					ctx.insert(tablename, columnName, columnObj);
				} catch (Exception e) {
					logger.throwing("Table", "storeDb", e);
				} finally {
					ctx.doFinally();
				}
			}
			
			return;
		}
		
		List<String> columnName = new ArrayList<String>();
		List<Object> columnObj = new ArrayList<Object>();
		
		columnName.add(TableColumn.TIME);
		columnObj.add(time);

		columnName.add(TableColumn.ZID);
		columnObj.add(zid);
		
		for (TableColumn c : keycolumn_map.values()) {
			long v = keymap.get(c.getAttr());
			columnName.add(c.getName());
			columnObj.add(v);
		}
		for (TableColumn c : column_map.values()) {
			long v = keymap.get(c.getAttr());
			columnName.add(c.getName());
			columnObj.add(v);
		}

		Context ctx = ApplicationThreadFrameWork.getContext();
		try {
			ctx.insert(tablename, columnName, columnObj);
		} catch (Exception e) {
			logger.throwing("Table", "storeDb", e);
		} finally {
			ctx.doFinally();
		}
	}

	void storeDb_SUMZID(long time) {
		String tname = getTableName_day_sumzid(false, true);
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + tname);

		sb.append(" SELECT ").append(time).append(", ");
		for (TableColumn c : keycolumn_map.values())
			sb.append(c.getName()).append(", ");

		for (String name : column_map.keySet())
			sb.append("SUM(").append(name).append(") as ").append(name).append(", ");
		sb = sb.deleteCharAt(sb.lastIndexOf(","));

		sb.append(" FROM ").append(tablename);
		sb.append(" WHERE ").append(TableColumn.TIME).append(" = ").append(time);

		sb.append(" GROUP BY ").append(TableColumn.TIME).append(", ");
		for (TableColumn c : keycolumn_map.values())
			sb.append(c.getName()).append(", ");
		sb = sb.deleteCharAt(sb.lastIndexOf(","));

		String insertsql = sb.toString();
		try {
			executeUpdate(insertsql);
		} catch (Exception e) {
		}

	}

	void storeDb_DAY(long daytime) {
		storeDb_DAY_fromTable(daytime, tablename);
	}

	void storeDb_DAY_fromTable(long daytime, String fromtable) {
		if (fetch_period_minute == 1440)
			return;

		// 分ZID
		String tname = getTableName_day_sumzid(true, false);
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(tname);

		if (!isPeak()) {
			sb.append(" SELECT ");
			sb.append(daytime).append(", ");
			sb.append(TableColumn.ZID).append(", ");

			for (String name : keycolumn_map.keySet())
				sb.append(name).append(", ");
			for (String name : column_map.keySet())
				sb.append("SUM").append("(").append(name).append(") as ").append(name).append(", ");
			sb = sb.deleteCharAt(sb.lastIndexOf(","));

			sb.append(" FROM ").append(fromtable);

			sb.append(" WHERE ").append(TableColumn.TIME).append(" >= ").append(daytime - MS_PER_DAY).append(" AND ").append(TableColumn.TIME).append(" < ")
					.append(daytime);

			sb.append(" GROUP BY ").append(TableColumn.ZID).append(", ");
			for (String name : keycolumn_map.keySet())
				sb.append(name).append(", ");
			sb = sb.deleteCharAt(sb.lastIndexOf(","));
		} else {
			sb.append(" SELECT ");
			sb.append(TableColumn.TIME).append(", ");
			sb.append(TableColumn.ZID).append(", ");

			for (String name : keycolumn_map.keySet())
				sb.append(name).append(", ");

			for (String name : column_map.keySet())
				sb.append(name).append(", ");
			sb = sb.deleteCharAt(sb.lastIndexOf(","));

			sb.append(" FROM ").append(fromtable);

			sb.append(" WHERE ").append(TableColumn.TIME).append(" >= ").append(daytime - MS_PER_DAY).append(" AND ").append(TableColumn.TIME).append(" < ")
					.append(daytime).append(" AND (").append(TableColumn.TIME).append("-").append(fetch_offset_minute).append(") % ").append(sample * 60000)
					.append(" = 0");
		}

		try {
			logger.info("storeDaySql: " + sb.toString());
			executeUpdate(sb.toString());
		} catch (Exception e) {
		}

		// 合ZID
		String stname = getTableName_day_sumzid(true, true);
		sb = new StringBuilder();
		sb.append("INSERT INTO ").append(stname);

		if (!isPeak()) {
			sb.append(" SELECT ").append(daytime).append(", ");
			for (String name : keycolumn_map.keySet())
				sb.append(name).append(", ");

			for (String name : column_map.keySet())
				sb.append("SUM(").append(name).append(") as ").append(name).append(", ");
			sb = sb.deleteCharAt(sb.lastIndexOf(","));

			sb.append(" FROM ").append(tname);

			sb.append(" WHERE ").append(TableColumn.TIME).append(" = ").append(daytime);

			sb.append(" GROUP BY ");
			for (String name : keycolumn_map.keySet())
				sb.append(name).append(", ");
			sb = sb.deleteCharAt(sb.lastIndexOf(","));
		} else {
			sb.append(" SELECT ").append(TableColumn.TIME).append(", ");
			for (String name : keycolumn_map.keySet())
				sb.append(name).append(", ");

			for (String name : column_map.keySet())
				sb.append("SUM(").append(name).append(") as ").append(name).append(", ");
			sb = sb.deleteCharAt(sb.lastIndexOf(","));

			sb.append(" FROM ").append(tname);

			sb.append(" WHERE ").append(TableColumn.TIME).append(" >= ").append(daytime - MS_PER_DAY).append(" AND ").append(TableColumn.TIME).append(" < ")
					.append(daytime);

			sb.append(" GROUP BY ");
			sb.append(TableColumn.TIME).append(", ");
			for (String name : keycolumn_map.keySet())
				sb.append(name).append(", ");
			sb = sb.deleteCharAt(sb.lastIndexOf(","));
		}

		try {
			logger.info("storeDaySql sumzid: " + sb.toString());
			executeUpdate(sb.toString());
		} catch (Exception e) {
		}
	}

	void repairDb_DAY(long from) throws Exception {
		if (fetch_period_minute == 1440)
			return;

		logger.info("repairDb_DAY " + identification);

		createTable_day_sumzid_dropold(true, false, true);
		createTable_day_sumzid_dropold(true, true, true);

		List<Long> renames = getTableRenames();
		long tm = from / MS_PER_DAY * MS_PER_DAY;
		long now = System.currentTimeMillis();

		if (!renames.isEmpty()) {
			int idx = getRenameIndex(renames, tm);
			if (idx < renames.size()) {
				for (int i = idx; i < renames.size(); i++) {
					long limit = renames.get(i);
					String tab = getTableNameAt(tablename, limit);
					for (; tm <= limit; tm += MS_PER_DAY)
						storeDb_DAY_fromTable(tm, tab);
				}
			}
		}
		for (; tm <= now; tm += MS_PER_DAY)
			storeDb_DAY_fromTable(tm, tablename);
	}

	void unshardingDb() throws Exception {
		if (fetch_period_minute == 1440)
			return;

		logger.info("unShardingDb " + identification);

		List<Long> renames = getTableRenames();

		if (renames.isEmpty())
			return;

		String to = tablename;
		for (int i = 0; i < renames.size(); i++) {
			String fromtab = getTableNameAt(to, renames.get(i));
			executeUpdate(new StringBuilder().append("INSERT INTO ").append(to).append(" SELECT * FROM ").append(fromtab).toString());
			executeUpdate("DROP TABLE " + fromtab);
		}

		to = getTableName_day_sumzid(false, true);
		for (int i = 0; i < renames.size(); i++) {
			String fromtab = getTableNameAt(to, renames.get(i));
			executeUpdate(new StringBuilder().append("INSERT INTO ").append(to).append(" SELECT * FROM ").append(fromtab).toString());
			executeUpdate("DROP TABLE " + fromtab);
		}
	}

	
	TreeMap<Long, Long> queryValue(String value, String key, Map<String, MultiLine.Range> ranges) throws Exception {
		return queryValue(value, key, ranges, calculateIfDayline(ranges));
	}
	
	// Key -> Value
	TreeMap<Long, Long> queryValue(String value, String key, Map<String, MultiLine.Range> ranges, boolean dayLine) throws Exception {
		QueryResult res = queryDb(Arrays.asList(value), null, key, ranges, dayLine);

		long div = res.needAveraging ? res.timeCount : 1;
		TreeMap<Long, Long> map = new TreeMap<Long, Long>();
		for (List<Long> e : res.data)
			map.put(e.get(0), e.get(1) / div);
		return map;
	}

	// SepKey -> (Key -> Value)
	TreeMap<Long, Map<Long, Long>> queryValueWithSepKey(String value, String sepkey, String key, Map<String, MultiLine.Range> ranges, boolean dayLine)
			throws Exception {
		QueryResult res = queryDb(Arrays.asList(value), sepkey, key, ranges, dayLine);
		long div = res.needAveraging ? res.timeCount : 1;
		TreeMap<Long, Map<Long, Long>> map = new TreeMap<Long, Map<Long, Long>>();
		for (List<Long> e : res.data) {
			Long k = e.get(0);
			Long sk = e.get(1);
			Long v = e.get(2);

			Map<Long, Long> m = map.get(sk);
			if (null == m) {
				m = new HashMap<Long, Long>();
				map.put(sk, m);
			}
			m.put(k, v / div);
		}

		return map;
	}

	// Value -> (Key -> Value)
	TreeMap<String, Map<Long, Long>> queryValues(List<String> values, String key, Map<String, MultiLine.Range> ranges, boolean dayLine) throws Exception {

		TreeMap<String, Map<Long, Long>> map = new TreeMap<String, Map<Long, Long>>();

		QueryResult res = queryDb(values, null, key, ranges, dayLine);
		long div = res.needAveraging ? res.timeCount : 1;
		for (List<Long> e : res.data) {
			Long k = e.get(0);
			int i = 1;
			for (String vstr : values) {
				Long v = e.get(i);
				i++;

				Map<Long, Long> m = map.get(vstr);
				if (null == m) {
					m = new HashMap<Long, Long>();
					map.put(vstr, m);
				}
				m.put(k, v / div);
			}
		}

		return map;
	}

	// zid为-1表示SUMZID, timeLow, timeUp可为null,可为负数
	TableData queryRaw(int zid, long origTimeLow, long origTimeUp, boolean dayLine) throws Exception {

		long timeLow = origTimeLow;
		long timeUp = origTimeUp;
		long now = System.currentTimeMillis();
		if (origTimeLow < 0)
			timeLow = now + origTimeLow * MS_PER_DAY;
		if (origTimeUp <= 0)
			timeUp = now + origTimeUp * MS_PER_DAY;

		if (timeLow > timeUp)
			throw new Exception("time low > up");

		boolean sumZid = (zid == -1);
		String basetablename = getTableName_day_sumzid(dayLine, sumZid);

		TableData result = new TableData();
		result.head = new ArrayList<String>();

		List<ColumnType> cts = new ArrayList<ColumnType>();
		result.head.add(TableColumn.TIME);
		cts.add(getKeyType(TableColumn.TIME));
		if (!sumZid) {
			result.head.add(TableColumn.ZID);
			cts.add(getKeyType(TableColumn.ZID));
		}
		for (TableColumn c : keycolumn_map.values()) {
			result.head.add(c.getName());
			cts.add(c.getType());
		}
		for (TableColumn c : column_map.values()) {
			result.head.add(c.getName());
			cts.add(c.getType());
		}

		String where = genWhereByZidAndTime(zid, origTimeLow, origTimeUp);
		List<Long> renames = getTableRenames();

		// TimeLow无设置，则不在rename数据表里找，直接用当前的吧
		if (false == dayLine && fetch_period_minute < 1440 && 0 != timeLow && renames.size() > 0) {

			int idx = getRenameIndex(renames, timeLow);
			if (idx < renames.size()) {
				long low_table_time = renames.get(idx);

				String selectsql = genSql(getTableNameAt(basetablename, low_table_time), where);
				List<List<Long>> res = executeQuery(selectsql, cts);

				// 最多连接2张表
				if (timeUp > low_table_time) {
					String tab = (idx + 1 < renames.size()) ? getTableNameAt(basetablename, renames.get(idx + 1)) : basetablename;
					selectsql = genSql(tab, where);
					res.addAll(executeQuery(selectsql, cts));
				}

				result.data = res;
				return result;
			}
		}

		result.data = executeQuery(genSql(basetablename, where), cts);
		return result;
	}

	private int getRenameIndex(List<Long> renames, long time) {
		for (int i = renames.size() - 1; i >= 0; i--) {
			long t = renames.get(i);
			if (t < time) {
				return i + 1;
			}
		}
		return 0;
	}

	static class QueryResult {
		List<List<Long>> data;
		boolean needAveraging;
		long timeCount;
	}

	QueryResult queryDb(List<String> values, String sepkey, String key, Map<String, MultiLine.Range> ranges, boolean dayLine) throws Exception {
		Set<String> keyset = new HashSet<String>(getKeyList());
		if (!keyset.contains(key))
			throw new Exception(key + " not key in " + this);

		if (null != sepkey && !keyset.contains(sepkey))
			throw new Exception(sepkey + " not key in " + this);

		for (Map.Entry<String, MultiLine.Range> e : ranges.entrySet())
			if (!keyset.contains(e.getKey()))
				throw new Exception(e.getKey() + " not key in " + this);

		Set<String> valueset = new HashSet<String>(getValueList());
		for (String e : values)
			if (!valueset.contains(e))
				throw new Exception(e + " not value in " + this);

		for (Map.Entry<String, MultiLine.Range> e : ranges.entrySet()) {
			if (!e.getValue().verify())
				throw new Exception(e.getKey() + " low > up");
		}

		Long timelow = null, timeup = null;
		boolean isSumZid = !key.equals(TableColumn.ZID);
		if (isSumZid && null != sepkey && sepkey.equals(TableColumn.ZID))
			isSumZid = false;

		for (Map.Entry<String, MultiLine.Range> e : ranges.entrySet()) {
			if (isSumZid && e.getKey().equals(TableColumn.ZID))
				isSumZid = false;
			if (e.getKey().equals(TableColumn.TIME)) {
				timelow = e.getValue().getLow();
				timeup = e.getValue().getUp();
			}
		}

		long now = System.currentTimeMillis();
		if (null != timelow && timelow < 0)
			timelow = now + timelow * MS_PER_DAY;
		if (null != timeup && timeup < 0)
			timeup = now + timeup * MS_PER_DAY;

		if (null != timelow && null != timeup && timelow > timeup)
			throw new Exception("time low > up");

		String basetablename = getTableName_day_sumzid(dayLine, isSumZid);
		String selectsql = null;

		boolean needAveraging = peak && !TableColumn.TIME.equals(key) && !TableColumn.TIME.equals(sepkey); // peak为true且sepkey为time这种情况禁止。
		String timecountersql = null;

		String rangewhere = genWhereByRanges(ranges);
		List<Long> renames = getTableRenames();

		// TimeLow无设置，则不在rename数据表里找，直接用当前的吧
		if (false == dayLine && fetch_period_minute < 1440 && null != timelow && renames.size() > 0) {

			int idx = getRenameIndex(renames, timelow);
			if (idx < renames.size()) {
				long low_table_time = renames.get(idx);
				String sqltable1 = getTableNameAt(basetablename, low_table_time);
				selectsql = genSql(sqltable1, values, sepkey, key, rangewhere);

				// 最多连接2张表
				if (null == timeup || timeup > low_table_time) {
					String sqltable2 = (idx + 1 < renames.size()) ? getTableNameAt(basetablename, renames.get(idx + 1)) : basetablename;
					String sql2 = genSql(sqltable2, values, sepkey, key, rangewhere);

					String uniontab = String.format("((%s) UNION ALL (%s)) AS __tmp", selectsql, sql2);
					selectsql = genSql(uniontab, values, sepkey, key, null);

					if (needAveraging) {
						timecountersql = String.format("SELECT COUNT(*) FROM ((%s) UNION ALL (%s)) AS __tmp2", genSqlForTime(sqltable1, rangewhere),
								genSqlForTime(sqltable2, rangewhere));
					}
				} else {
					if (needAveraging) {
						timecountersql = String.format("SELECT COUNT(*) FROM (%s) AS __tmp2", genSqlForTime(sqltable1, rangewhere));
					}
				}
			}
		}

		if (null == selectsql) {
			selectsql = genSql(basetablename, values, sepkey, key, rangewhere);
			if (needAveraging) {
				timecountersql = String.format("SELECT COUNT(*) FROM (%s) AS __tmp2", genSqlForTime(basetablename, rangewhere));
			}
		}

		List<ColumnType> cts = new ArrayList<ColumnType>();
		cts.add(getKeyType(key));
		if (null != sepkey)
			cts.add(getKeyType(sepkey));
		for (String v : values)
			cts.add(column_map.get(v).getType());

		QueryResult qr = new QueryResult();
		qr.data = executeQuery(selectsql, cts);
		qr.needAveraging = needAveraging;

		if (needAveraging) {
			List<ColumnType> tc = new ArrayList<ColumnType>();
			tc.add(ColumnType.TLONG);
			List<List<Long>> r = executeQuery(timecountersql, tc);
			qr.timeCount = r.get(0).get(0);
		}

		return qr;
	}

	static private final Logger logger = Logger.getLogger(Table.class.getName());
	static private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	static private final String SUFFIX_DAY_SUMZID = "_DAY_SUMZID";
	static private final String SUFFIX_DAY = "_DAY";
	static private final String SUFFIX_SUMZID = "_SUMZID";
	static final int MAX_TABLENAME_LEN = 32; // 因为MYSQL限制表名,列名不能超过64,这里为后缀预留32
	static private final int QUERY_TIMEOUT_SEC = 10;
	static private final int QUERY_ROW_LIMIT = 1024 * 1024;
	static private final long MS_PER_DAY = 1000 * 60 * 60 * 24;

	private ColumnType getKeyType(String key) {
		if (key.equals(TableColumn.TIME))
			return ColumnType.TLONG;
		if (key.equals(TableColumn.ZID))
			return ColumnType.TINT;

		return keycolumn_map.get(key).getType();
	}

	private String getTableName_day_sumzid(boolean day, boolean sumzid) {
		if (fetch_period_minute == 1440)
			return tablename + (sumzid ? SUFFIX_SUMZID : "");
		else
			return tablename + (day ? (sumzid ? SUFFIX_DAY_SUMZID : SUFFIX_DAY) : (sumzid ? SUFFIX_SUMZID : ""));
	}

	private void createTable_day_sumzid_dropold(boolean day, boolean sumzid, boolean drop) throws Exception {

		if (day && fetch_period_minute == 1440)
			throw new AssertionError();

		String tname = getTableName_day_sumzid(day, sumzid);

		if (drop)
			executeUpdate("DROP TABLE IF EXISTS " + tname);

		StringBuilder sb = new StringBuilder();
		if (drop)
			sb.append("CREATE TABLE " + tname + " (\n");
		else
			sb.append("CREATE TABLE IF NOT EXISTS " + tname + " (\n");

		sb.append("\t").append(TableColumn.TIME).append(" BIGINT NOT NULL,\n");

		if (!sumzid)
			sb.append("\t").append(TableColumn.ZID).append(" INTEGER NOT NULL,\n");

		for (TableColumn i : keycolumn_map.values()) {
			sb.append("\t");
			sb.append(i.DDL());
			sb.append(",\n");
		}

		for (TableColumn i : column_map.values()) {
			sb.append("\t");
			sb.append(i.DDL());
			sb.append(",\n");
		}
		sb = sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(")\n");

		String createsql = sb.toString();
		executeUpdate(createsql);
	}

	private static String getTableNameAt(String tablename, long time) {
		return tablename + "_" + sdf.format(new Date(time));
	}

	private static String genWhereByTime(Long timeLow, Long timeUp) {
		StringBuilder sb = new StringBuilder();
		if (null != timeLow && 0 != timeLow) {
			sb.append(TableColumn.TIME);
			if (timeLow < 0)
				sb.append(" >= 1000*UNIX_TIMESTAMP( NOW() - INTERVAL ").append(-timeLow).append(" DAY ) AND ");
			else
				sb.append(" >= ").append(timeLow).append(" AND ");
		}

		if (null != timeUp && 0 != timeUp) {
			sb.append(TableColumn.TIME);
			if (timeUp < 0)
				sb.append(" <= 1000*UNIX_TIMESTAMP( NOW() - INTERVAL ").append(-timeUp).append(" DAY ) AND ");
			else
				sb.append(" <= ").append(timeUp).append(" AND ");
		}

		return sb.toString();
	}

	private static String genWhereByZidAndTime(int zid, Long timeLow, Long timeUp) {
		StringBuilder sb = new StringBuilder();
		if (zid != -1)
			sb.append(TableColumn.ZID).append(" = ").append(zid).append(" AND ");
		sb.append(genWhereByTime(timeLow, timeUp));

		if (sb.length() > 0)
			sb.delete(sb.lastIndexOf("AND"), sb.length());
		return sb.toString();
	}

	private static String genWhereByRanges(Map<String, MultiLine.Range> ranges) {
		if (null == ranges)
			return "";
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, MultiLine.Range> e : ranges.entrySet()) {
			String k = e.getKey();
			Long low = e.getValue().getLow();
			Long up = e.getValue().getUp();

			if (null != low && low.equals(up))
				sb.append(k).append(" = ").append(low).append(" AND ");
			else if (k.equals(TableColumn.TIME))
				sb.append(genWhereByTime(low, up));
			else {
				if (null != low)
					sb.append(k).append(" >= ").append(low).append(" AND ");
				if (null != up)
					sb.append(k).append(" <= ").append(up).append(" AND ");
			}
		}
		if (sb.length() > 0)
			sb.delete(sb.lastIndexOf("AND"), sb.length());
		return sb.toString();
	}

	private static String genSql(String table, String where) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * ");
		sb.append(" FROM ").append(table);
		if (where.length() > 0)
			sb.append(" WHERE ").append(where);
		return sb.toString();
	}

	private static String genSqlForTime(String table, String where) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ").append(TableColumn.TIME);
		sb.append(" FROM ").append(table);
		if (where.length() > 0)
			sb.append(" WHERE ").append(where);
		sb.append(" GROUP BY ").append(TableColumn.TIME);
		return sb.toString();
	}

	private static String genSql(String tablename, List<String> values, String sepkey, String key, String rangewhere) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ").append(key);
		if (null != sepkey)
			sb.append(", ").append(sepkey);

		for (String v : values)
			sb.append(", SUM(").append(v).append(") AS ").append(v);

		sb.append(" FROM ").append(tablename);
		if (null != rangewhere && rangewhere.length() > 0)
			sb.append(" WHERE ").append(rangewhere);

		sb.append(" GROUP BY ").append(key);
		if (null != sepkey)
			sb.append(", ").append(sepkey);

		sb.append(" LIMIT ").append(QUERY_ROW_LIMIT);
		return sb.toString();
	}

	static private void executeUpdate(String sql) throws Exception {
		Context ctx = ApplicationThreadFrameWork.getContext();
		Connection conn = ctx.getConnection();
		Statement st = null;
		boolean close_ok = true;

		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			close_ok = false;
			logger.warning("executeUpdate exception: " + sql);
			throw e;
		} finally {
			if (null != st) {
				try {
					st.close();
				} catch (SQLException e) {
					close_ok = false;
				}
			}
			if (close_ok)
				ctx.returnConnection(conn);
			else
				ctx.discardConnection(conn);
		}
	}

	static private List<List<Long>> executeQuery(String sql, List<ColumnType> types) throws Exception {

		logger.info("querysql: " + sql);
		List<List<Long>> result = new ArrayList<List<Long>>(2048);

		Context ctx = ApplicationThreadFrameWork.getContext();
		Connection conn = ctx.getConnection();
		Statement st = null;
		ResultSet rs = null;
		boolean close_ok = true;
		try {
			st = conn.createStatement();
			st.setQueryTimeout(QUERY_TIMEOUT_SEC);

			rs = st.executeQuery(sql);
			while (rs.next()) {

				List<Long> res = new ArrayList<Long>();
				int i = 1;
				long v = 0;
				for (ColumnType ct : types) {

					switch (ct) {
					case TBYTE:
						v = rs.getByte(i);
					case TSHORT:
						v = rs.getShort(i);
					case TINT:
						v = rs.getInt(i);
					case TLONG:
						v = rs.getLong(i);
					}
					i++;
					res.add(v);

				}

				result.add(res);
			}
		} catch (SQLException e) {
			close_ok = false;
			throw e;
		} finally {
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e1) {
				close_ok = false;
			} finally {
				try {
					if (null != st)
						st.close();
				} catch (SQLException e2) {
					close_ok = false;
				}
			}
			if (close_ok)
				ctx.returnConnection(conn);
			else
				ctx.discardConnection(conn);

		}
		return result;
	}

	static String makeIdentification(String domain, String type, String name) {
		StringBuilder sb = new StringBuilder();
		sb.append(domain).append("_").append(type);
		if (name.length() > 0)
			sb.append("_").append(name);
		return sb.toString().trim().replaceAll("\\W", "_");
	}

	static boolean calculateIfDayline(Map<String, MultiLine.Range> ranges) {
		for (Map.Entry<String, MultiLine.Range> entry : ranges.entrySet()) {
			String k = entry.getKey();
			if (k.equals(TableColumn.TIME)) {
				Long low = entry.getValue().getLow();
				Long up = entry.getValue().getUp();

				long now = System.currentTimeMillis();
				if (null == low)
					return true;

				if (null == up) {
					if (low < 0)
						return -low > 7;
					else
						return now - low > 7 * MS_PER_DAY;
				} else {
					long uptm = up < 0 ? now + up * MS_PER_DAY : up;
					long lowtm = low < 0 ? now + low * MS_PER_DAY : low;
					return uptm - lowtm > 7 * MS_PER_DAY;
				}
			}
		}
		return true;
	}

	
}
