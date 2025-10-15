
package fire.pb.mbean.beans;

import java.lang.reflect.Method;

import fire.pb.util.ParseString;

import com.locojoy.base.Marshal.OctetsStream;

/**
 * 
 */
public interface SearchMXBean {
	public byte[] search(String field, String key) throws Exception;

	public static class DBSearchMXBean implements SearchMXBean {
		@Override
		public byte[] search(String field, String key) throws Exception {
			String classNameString = getClassName(field);
			Class<?> class1 = null;
			try {
				class1 = Class.forName(classNameString);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

			Method[] methods = class1.getMethods();
			Method tmpMethod = null;
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().endsWith("select")) {
					tmpMethod = methods[i];
					break;
				}
			}

			if (tmpMethod == null) {
				throw new RuntimeException("has not select method");
			}

			Object object = null;
			Class<?> paramClass = tmpMethod.getParameterTypes()[0];
			// 字符串类型
			if (String.class.equals(paramClass) ) {
				object = tmpMethod.invoke(class1, key);
			}
			if (Integer.class.equals(paramClass)) {
				object = tmpMethod.invoke(class1, Integer.valueOf(key));
			}
			if (Long.class.equals(paramClass)) {
				object = tmpMethod.invoke(class1, Long.valueOf(key));
			}
			// 没有该记录
			if (object == null) {
				throw new RuntimeException("key " + key +" type "+ paramClass.getName()+ " " +"in table "+classNameString + "not exist");
			}

			OctetsStream stream = new OctetsStream();
			((mkdb.Bean) object).marshal(stream);
			return stream.getBytes();

		}

		private String getClassName(String tableName) {
			StringBuilder builder = new StringBuilder();
			builder.append("xtable.");
			builder.append(ParseString.toUperCaseFristOne(tableName));
			return builder.toString();
		}

	}
}
