package xtable;

// typed table access point
public class Rolespecialquesttimer {
	Rolespecialquesttimer() {
	}

	public static xbean.SpecailquestFuture get(Long key) {
		return _Tables_.getInstance().rolespecialquesttimer.get(key);
	}

	public static xbean.SpecailquestFuture get(Long key, xbean.SpecailquestFuture value) {
		return _Tables_.getInstance().rolespecialquesttimer.get(key, value);
	}

	public static void insert(Long key, xbean.SpecailquestFuture value) {
		_Tables_.getInstance().rolespecialquesttimer.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolespecialquesttimer.delete(key);
	}

	public static boolean add(Long key, xbean.SpecailquestFuture value) {
		return _Tables_.getInstance().rolespecialquesttimer.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolespecialquesttimer.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.SpecailquestFuture> getCache() {
		return _Tables_.getInstance().rolespecialquesttimer.getCache();
	}

	public static mkdb.TTable<Long, xbean.SpecailquestFuture> getTable() {
		return _Tables_.getInstance().rolespecialquesttimer;
	}

}
