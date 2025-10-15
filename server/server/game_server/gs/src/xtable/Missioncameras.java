package xtable;

// typed table access point
public class Missioncameras {
	Missioncameras() {
	}

	public static xbean.CameraInfo get(Long key) {
		return _Tables_.getInstance().missioncameras.get(key);
	}

	public static xbean.CameraInfo get(Long key, xbean.CameraInfo value) {
		return _Tables_.getInstance().missioncameras.get(key, value);
	}

	public static void insert(Long key, xbean.CameraInfo value) {
		_Tables_.getInstance().missioncameras.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().missioncameras.delete(key);
	}

	public static boolean add(Long key, xbean.CameraInfo value) {
		return _Tables_.getInstance().missioncameras.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().missioncameras.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.CameraInfo> getCache() {
		return _Tables_.getInstance().missioncameras.getCache();
	}

	public static mkdb.TTable<Long, xbean.CameraInfo> getTable() {
		return _Tables_.getInstance().missioncameras;
	}

	public static xbean.CameraInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CameraInfo, xbean.CameraInfo>() {
			public xbean.CameraInfo get(xbean.CameraInfo v) { return v.toData(); }
		});
	}

	public static Long selectEndtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CameraInfo, Long>() {
				public Long get(xbean.CameraInfo v) { return v.getEndtime(); }
			});
	}

	public static Integer selectSizebeforezip(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CameraInfo, Integer>() {
				public Integer get(xbean.CameraInfo v) { return v.getSizebeforezip(); }
			});
	}

	public static Integer selectSizeafterzip(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CameraInfo, Integer>() {
				public Integer get(xbean.CameraInfo v) { return v.getSizeafterzip(); }
			});
	}

	public static String selectCamerafileurl(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CameraInfo, String>() {
				public String get(xbean.CameraInfo v) { return v.getCamerafileurl(); }
			});
	}

}
