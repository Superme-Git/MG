package fire.pb.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class ConfigMgr implements IModule {
	private static ConfigMgr instance = null;
	public static String CONFIG_PATH = "gamedata/xml/auto";
	public static String PROPERTY_PATH = "properties";

	public static final ArrayList<String> deleteFile = new ArrayList<String>();

	public synchronized static ConfigMgr getInstance() {
		return instance;
	}

	static void init(String xmldir, Unmarshaller unmarshaller, String propdir) {
		final ConfigMgr cm = new ConfigMgr();
		cm.beannames.clear();
		for (java.io.File f : new java.io.File(xmldir).listFiles()) {
			if (!f.getName().endsWith(".xml") || !Main.isInXmlData(f.getName()))
				continue;

			Object o;
			try {
				o = unmarshaller.unmarshal(new FileInputStream(f));
			} catch (final Exception ex) {
				System.out.println("载入" + f.getAbsolutePath() + "失败:" + ex.toString());
				deleteFile.add(f.getAbsolutePath());
				continue;
			}
			final String beanname = f.getName().substring(0, f.getName().length() - new String(".xml").length());
			cm.beannames.put(beanname, o);
			if (!(o instanceof java.util.Map<?, ?>))
				continue;
		}

		mytools.ConvMain.doCheck(cm.beannames);

		Properties prop = null;
		FileInputStream fis = null;
		for (final java.io.File f : new java.io.File(propdir).listFiles()) {
			if (f.getName().endsWith(".properties")) {
				prop = new Properties();
				try {
					fis = new FileInputStream(f);
					prop.load(fis);
					fis.close();
				} catch (FileNotFoundException e) {
					System.out.println(f.getAbsolutePath() + "not found" + e);
					continue;
				} catch (IOException e) {
					System.out.println(f.getAbsolutePath() + "load error" + e);
					continue;
				}
				final String propname = f.getName().substring(0, f.getName().indexOf(".properties"));
				cm.propnames.put(propname, prop);
			}
		}
		synchronized (ConfigMgr.class) {
			instance = cm;
		}
	}

	static void usage() {
		System.out.println("Usage: java -jar gsxdb.jar [options]");
		System.out.println("    -rmiport    rmi port");
		System.out.println("    -zoneid      zone id");
		System.exit(0);
	}

	private final java.util.Map<String, Object> beannames = new java.util.concurrent.ConcurrentHashMap<String, Object>();

	private final java.util.Map<String, Properties> propnames = new java.util.concurrent.ConcurrentHashMap<String, Properties>();

	@Override
	public void exit() {
	}

	public <T> java.util.TreeMap<Integer, T> getConf(Class<T> t) {
		Object o = beannames.get(t.getName());
		if (o == null)
			return null;
		try {
			@SuppressWarnings("unchecked")
			java.util.TreeMap<Integer, T> result = (java.util.TreeMap<Integer, T>) o;
			return result;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T> T getConfigById(Class<T> t, int configId) {
		Map<Integer, T> map = getConf(t);
		if (map == null || map.isEmpty()) {
			System.out.println(t.getCanonicalName() + " config is null");
			return null;
		}
		return map.get(configId);
	}

	public Properties getPropConf(String name) {
		return propnames.get(name);
	}

	@Override
	public void init() throws Exception {
		init(Main.getCurPath() + CONFIG_PATH, new XStreamUnmarshaller(), Main.getCurPath() + PROPERTY_PATH);
	}
}
