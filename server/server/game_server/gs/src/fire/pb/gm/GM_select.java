package fire.pb.gm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import mkdb.TTable.IWalk;

public class GM_select extends GMCommand {

	@SuppressWarnings("rawtypes")
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
		}
		try {
			Class<?> tableClass = Class.forName(args[0]);
			Method[] methods = tableClass.getMethods();
			Method method = null;
			Class<?> keyType = null;
			for (Method method2 : methods) {
				if (method2.getName().equals("select")) {
					method = method2;
					keyType = method.getParameterTypes()[0];
					break;
				}
			}
			if (method == null || keyType == null) {
				sendToGM("鎵句笉鍒板搴旂殑琛�" + args[0]);
				return false;
			}
			if (args.length > 1) {
				Method valueOf = keyType.getMethod("valueOf", String.class);
				Object instanceXbean = method.invoke(null,
						valueOf.invoke(null, args[1]));
				Class<?> valueType = instanceXbean.getClass();
				Field[] xbeanFields = valueType.getDeclaredFields();
				StringBuilder sb = new StringBuilder();
				for (Field field : xbeanFields) {
					field.setAccessible(true);
					sb.append(field.getName());
					sb.append(":");
					sb.append(field.get(instanceXbean).toString());
					sb.append("\n");
				}
				sendToGM(sb.toString());
			} else {
				Method getTableMethod = tableClass.getMethod("getTable");
				Class<?> TTableClass = getTableMethod.getReturnType();
				Object tTableInstance = getTableMethod.invoke(null,
						(Object) null);
				Method browse = TTableClass.getMethod("browse", IWalk.class);
				IWalk walk = new XdbWalker();
				browse.invoke(tTableInstance, walk);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return true;
	}

	@Override
	String usage() {

		return "//select tablename key or //select tablename";
	}

	class XdbWalker<K, V> implements mkdb.TTable.IWalk<K, V> {

		@Override
		public boolean onRecord(K k, V v) {
			try {

				Field[] xbeanFields = v.getClass().getDeclaredFields();
				StringBuilder sb = new StringBuilder();
				sb.append(k.toString());
				for (Field field : xbeanFields) {
					field.setAccessible(true);
					sb.append(field.getName());
					sb.append(":");
					sb.append(field.get(v).toString());
					sb.append("\n");
				}
				sendToGM(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

	}
}
