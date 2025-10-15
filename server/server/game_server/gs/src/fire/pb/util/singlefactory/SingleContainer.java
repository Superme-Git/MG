package fire.pb.util.singlefactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例
 */
public class SingleContainer {
	/**
	 * 
	 */
	private  static final ConcurrentHashMap<Class<? extends ICreateable>, ICreateable> instances = new ConcurrentHashMap<Class<? extends ICreateable>, ICreateable>();

	@SuppressWarnings("unchecked")
	public static <T extends ICreateable> T getInstance(Class<T> clazz, Map<?,?> map) {
		if (instances.containsKey(clazz)) {
			return (T) instances.get(clazz);
		}
		T a = null;
		try {
			a = clazz.newInstance();
			if (map!=null) {
				a.init(map);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		instances.putIfAbsent(clazz, a);
		a=(T) instances.get(clazz);
		return a;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<? extends ICreateable> clazz) {
		return (T) getInstance(clazz,null);
	}

}
