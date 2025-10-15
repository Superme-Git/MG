package xtable;

// typed table access point
public class Singleinviting {
	Singleinviting() {
	}

	public static xbean.SingleInvitings get(Long key) {
		return _Tables_.getInstance().singleinviting.get(key);
	}

	public static xbean.SingleInvitings get(Long key, xbean.SingleInvitings value) {
		return _Tables_.getInstance().singleinviting.get(key, value);
	}

	public static void insert(Long key, xbean.SingleInvitings value) {
		_Tables_.getInstance().singleinviting.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().singleinviting.delete(key);
	}

	public static boolean add(Long key, xbean.SingleInvitings value) {
		return _Tables_.getInstance().singleinviting.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().singleinviting.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.SingleInvitings> getCache() {
		return _Tables_.getInstance().singleinviting.getCache();
	}

	public static mkdb.TTable<Long, xbean.SingleInvitings> getTable() {
		return _Tables_.getInstance().singleinviting;
	}

	public static xbean.SingleInvitings select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleInvitings, xbean.SingleInvitings>() {
			public xbean.SingleInvitings get(xbean.SingleInvitings v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, Long> selectInvitingids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleInvitings, java.util.Map<Long, Long>>() {
				public java.util.Map<Long, Long> get(xbean.SingleInvitings v) { return v.getInvitingidsAsData(); }
			});
	}

}
