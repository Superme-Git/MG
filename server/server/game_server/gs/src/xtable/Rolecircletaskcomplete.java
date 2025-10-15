package xtable;

// typed table access point
public class Rolecircletaskcomplete {
	Rolecircletaskcomplete() {
	}

	public static xbean.CircleTaskCompleteTimes get(Long key) {
		return _Tables_.getInstance().rolecircletaskcomplete.get(key);
	}

	public static xbean.CircleTaskCompleteTimes get(Long key, xbean.CircleTaskCompleteTimes value) {
		return _Tables_.getInstance().rolecircletaskcomplete.get(key, value);
	}

	public static void insert(Long key, xbean.CircleTaskCompleteTimes value) {
		_Tables_.getInstance().rolecircletaskcomplete.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolecircletaskcomplete.delete(key);
	}

	public static boolean add(Long key, xbean.CircleTaskCompleteTimes value) {
		return _Tables_.getInstance().rolecircletaskcomplete.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolecircletaskcomplete.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.CircleTaskCompleteTimes> getCache() {
		return _Tables_.getInstance().rolecircletaskcomplete.getCache();
	}

	public static mkdb.TTable<Long, xbean.CircleTaskCompleteTimes> getTable() {
		return _Tables_.getInstance().rolecircletaskcomplete;
	}

	public static xbean.CircleTaskCompleteTimes select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CircleTaskCompleteTimes, xbean.CircleTaskCompleteTimes>() {
			public xbean.CircleTaskCompleteTimes get(xbean.CircleTaskCompleteTimes v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Integer> selectCircletaskcounts(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CircleTaskCompleteTimes, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.CircleTaskCompleteTimes v) { return v.getCircletaskcountsAsData(); }
			});
	}

	public static Long selectLogtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CircleTaskCompleteTimes, Long>() {
				public Long get(xbean.CircleTaskCompleteTimes v) { return v.getLogtime(); }
			});
	}

}
