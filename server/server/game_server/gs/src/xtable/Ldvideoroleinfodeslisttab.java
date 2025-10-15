package xtable;

// typed table access point
public class Ldvideoroleinfodeslisttab {
	Ldvideoroleinfodeslisttab() {
	}

	public static xbean.LDVideoRoleInfoDesList get(Integer key) {
		return _Tables_.getInstance().ldvideoroleinfodeslisttab.get(key);
	}

	public static xbean.LDVideoRoleInfoDesList get(Integer key, xbean.LDVideoRoleInfoDesList value) {
		return _Tables_.getInstance().ldvideoroleinfodeslisttab.get(key, value);
	}

	public static void insert(Integer key, xbean.LDVideoRoleInfoDesList value) {
		_Tables_.getInstance().ldvideoroleinfodeslisttab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().ldvideoroleinfodeslisttab.delete(key);
	}

	public static boolean add(Integer key, xbean.LDVideoRoleInfoDesList value) {
		return _Tables_.getInstance().ldvideoroleinfodeslisttab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().ldvideoroleinfodeslisttab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.LDVideoRoleInfoDesList> getCache() {
		return _Tables_.getInstance().ldvideoroleinfodeslisttab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.LDVideoRoleInfoDesList> getTable() {
		return _Tables_.getInstance().ldvideoroleinfodeslisttab;
	}

	public static xbean.LDVideoRoleInfoDesList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LDVideoRoleInfoDesList, xbean.LDVideoRoleInfoDesList>() {
			public xbean.LDVideoRoleInfoDesList get(xbean.LDVideoRoleInfoDesList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.LDVideoRoleInfoDes> selectLdvideoroleinfodeslistall(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LDVideoRoleInfoDesList, java.util.List<xbean.LDVideoRoleInfoDes>>() {
				public java.util.List<xbean.LDVideoRoleInfoDes> get(xbean.LDVideoRoleInfoDesList v) { return v.getLdvideoroleinfodeslistallAsData(); }
			});
	}

}
