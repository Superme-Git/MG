package xtable;

// typed table access point
public class Instanceaskinfos {
	Instanceaskinfos() {
	}

	public static xbean.InstanceAskInfo get(Long key) {
		return _Tables_.getInstance().instanceaskinfos.get(key);
	}

	public static xbean.InstanceAskInfo get(Long key, xbean.InstanceAskInfo value) {
		return _Tables_.getInstance().instanceaskinfos.get(key, value);
	}

	public static void insert(Long key, xbean.InstanceAskInfo value) {
		_Tables_.getInstance().instanceaskinfos.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().instanceaskinfos.delete(key);
	}

	public static boolean add(Long key, xbean.InstanceAskInfo value) {
		return _Tables_.getInstance().instanceaskinfos.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().instanceaskinfos.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.InstanceAskInfo> getCache() {
		return _Tables_.getInstance().instanceaskinfos.getCache();
	}

	public static mkdb.TTable<Long, xbean.InstanceAskInfo> getTable() {
		return _Tables_.getInstance().instanceaskinfos;
	}

	public static xbean.InstanceAskInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceAskInfo, xbean.InstanceAskInfo>() {
			public xbean.InstanceAskInfo get(xbean.InstanceAskInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, Integer> selectAnswerinfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceAskInfo, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.InstanceAskInfo v) { return v.getAnswerinfoAsData(); }
			});
	}

	public static Integer selectInstid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceAskInfo, Integer>() {
				public Integer get(xbean.InstanceAskInfo v) { return v.getInstid(); }
			});
	}

	public static Long selectAsktime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceAskInfo, Long>() {
				public Long get(xbean.InstanceAskInfo v) { return v.getAsktime(); }
			});
	}

}
