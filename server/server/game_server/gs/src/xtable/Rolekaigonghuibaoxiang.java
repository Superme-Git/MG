package xtable;

// typed table access point
public class Rolekaigonghuibaoxiang {
	Rolekaigonghuibaoxiang() {
	}

	public static xbean.GongHuiBaoXiangInfo get(Long key) {
		return _Tables_.getInstance().rolekaigonghuibaoxiang.get(key);
	}

	public static xbean.GongHuiBaoXiangInfo get(Long key, xbean.GongHuiBaoXiangInfo value) {
		return _Tables_.getInstance().rolekaigonghuibaoxiang.get(key, value);
	}

	public static void insert(Long key, xbean.GongHuiBaoXiangInfo value) {
		_Tables_.getInstance().rolekaigonghuibaoxiang.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolekaigonghuibaoxiang.delete(key);
	}

	public static boolean add(Long key, xbean.GongHuiBaoXiangInfo value) {
		return _Tables_.getInstance().rolekaigonghuibaoxiang.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolekaigonghuibaoxiang.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.GongHuiBaoXiangInfo> getCache() {
		return _Tables_.getInstance().rolekaigonghuibaoxiang.getCache();
	}

	public static mkdb.TTable<Long, xbean.GongHuiBaoXiangInfo> getTable() {
		return _Tables_.getInstance().rolekaigonghuibaoxiang;
	}

	public static xbean.GongHuiBaoXiangInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.GongHuiBaoXiangInfo, xbean.GongHuiBaoXiangInfo>() {
			public xbean.GongHuiBaoXiangInfo get(xbean.GongHuiBaoXiangInfo v) { return v.toData(); }
		});
	}

	public static Integer selectOpentimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.GongHuiBaoXiangInfo, Integer>() {
				public Integer get(xbean.GongHuiBaoXiangInfo v) { return v.getOpentimes(); }
			});
	}

	public static Long selectLastopentime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.GongHuiBaoXiangInfo, Long>() {
				public Long get(xbean.GongHuiBaoXiangInfo v) { return v.getLastopentime(); }
			});
	}

}
