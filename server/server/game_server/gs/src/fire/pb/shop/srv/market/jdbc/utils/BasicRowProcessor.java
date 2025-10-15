package fire.pb.shop.srv.market.jdbc.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BasicRowProcessor implements RowProcessor {

	private static final BeanProcessor defaultConvert = new BeanProcessor();

	private final BeanProcessor convert;

	public BasicRowProcessor() {
		this(defaultConvert);
	}

	public BasicRowProcessor(BeanProcessor convert) {
		super();
		this.convert = convert;
	}

	@Override
	public List<Object[]> toArray(ResultSet rs) throws SQLException {
		rs.beforeFirst();// pos居然是0,应该是-1的,有刁民想害朕,朕强制将光标移动到此 ResultSet 对象的开头位于第一行之前。
		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();
		List<Object[]> result = new ArrayList<>();
		while (rs.next()) {
			Object[] row = new Object[cols];
			for (int c = 0; c < cols; c++) {
				row[c] = rs.getObject(c + 1);
			}
			result.add(row);
		}
		return result;
	}

	@Override
	public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
		return this.convert.toBean(rs, type);
	}

	@Override
	public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
		return this.convert.toBeanList(rs, type);
	}

	@Override
	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new CaseInsensitiveHashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		for (int i = 1; i <= cols; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(i);
			}
			result.put(columnName, rs.getObject(i));
		}

		return result;
	}

	private static class CaseInsensitiveHashMap extends LinkedHashMap<String, Object> {
		private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

		private static final long serialVersionUID = -2848100435296897392L;

		@Override
		public boolean containsKey(Object key) {
			Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
			return super.containsKey(realKey);
		}

		@Override
		public Object get(Object key) {
			Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
			return super.get(realKey);
		}

		@Override
		public Object put(String key, Object value) {
			Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
			Object oldValue = super.remove(oldKey);
			super.put(key, value);
			return oldValue;
		}

		@Override
		public void putAll(Map<? extends String, ?> m) {
			for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				this.put(key, value);
			}
		}

		@Override
		public Object remove(Object key) {
			Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
			return super.remove(realKey);
		}
	}

}
