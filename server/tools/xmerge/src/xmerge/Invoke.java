package xmerge;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.*;
import java.util.*;

@SuppressWarnings({ "rawtypes" })
public class Invoke {
	private static Reference methodCacheRef;
	private static Reference fieldCacheRef;

	private static class MethodSignature {
		private Class targetClass;
		private String methodName;

		private Class[] argClasses;
		private volatile int hashCode = 0;

		public MethodSignature(Class targetClass, String methodName, Class[] argClasses) {
			this.targetClass = targetClass;
			this.methodName = methodName;
			this.argClasses = argClasses;
		}

		public boolean equals(Object o2) {
			if (this == o2) {
				return true;
			}
			MethodSignature that = (MethodSignature) o2;
			if (!(targetClass == that.targetClass)) {
				return false;
			}
			if (!(methodName.equals(that.methodName))) {
				return false;
			}
			if (argClasses.length != that.argClasses.length) {
				return false;
			}
			for (int i = 0; i < argClasses.length; i++) {
				if (!(argClasses[i] == that.argClasses[i])) {
					return false;
				}
			}
			return true;
		}

		public int hashCode() {
			if (hashCode == 0) {
				int result = 17;
				result = 37 * result + targetClass.hashCode();
				result = 37 * result + methodName.hashCode();
				if (argClasses != null) {
					for (int i = 0; i < argClasses.length; i++) {
						result = 37 * result + ((argClasses[i] == null) ? 0 : argClasses[i].hashCode());
					}
				}
				hashCode = result;
			}
			return hashCode;
		}
	}
	
	private static class FieldSignature {		
		private Class targetClass;
		private String fieldName;
		private volatile int hashCode = 0;

		public FieldSignature(Class targetClass, String fieldName) {
			this.targetClass = targetClass;
			this.fieldName = fieldName;
		}

		public boolean equals(Object o2) {
			if (this == o2) {
				return true;
			}
			FieldSignature that = (FieldSignature) o2;
			if (!(targetClass == that.targetClass)) {
				return false;
			}
			if (!(fieldName.equals(that.fieldName))) {
				return false;
			}
			return true;
		}

		public int hashCode() {
			if (hashCode == 0) {
				int result = 17;
				result = 37 * result + targetClass.hashCode();
				result = 37 * result + fieldName.hashCode();
				hashCode = result;
			}
			return hashCode;
		}
	}

	// 获取属性
	public static Object getProperty(Object instance, String fieldName) throws Exception {
		Class thisClass = instance.getClass();
		Field field = findProperty(thisClass, fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

	@SuppressWarnings("unchecked")
	public static Field findProperty(Class targetClass, String fieldName) throws Exception {
		try {
			Object signature = new FieldSignature(targetClass, fieldName);
			Field field = null;
			Map fieldCache = null;
			if (fieldCacheRef != null && (fieldCache = (Map) fieldCacheRef.get()) != null) {
				field = (Field) fieldCache.get(signature);
				if (field != null) {
					return field;
				}
			}
			field = targetClass.getDeclaredField(fieldName);
			if (field != null) {
				if (fieldCache == null) {
					fieldCache = new HashMap();
					fieldCacheRef = new SoftReference(fieldCache);
				}
				fieldCache.put(signature, field);
				return field;
			}
		} catch (Exception e) {
		}
		for (Class type = targetClass; type != null; type = type.getSuperclass()) {
			try {
				Field field = type.getDeclaredField(fieldName);
				if (field != null) {
					return field;
				}
			} catch (Exception e) {
				continue;
			}
		}
		return null;
	}

	// 修改属性
	public static void setProperty(Object instance, String fieldName, Object arg) throws Exception {
		Class thisClass = instance.getClass();
		Field field = findProperty(thisClass, fieldName);
		field.setAccessible(true);
		field.set(instance, arg);
	}

	// 调用含单个参数的方法
	public static Object invokeMethod(Object instance, String methodName, Object arg) {
		Object[] args = new Object[1];
		args[0] = arg;
		try {
			return invokeMethod(instance, methodName, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 调用含多个参数的方法
	public static Object invokeMethod(Object instance, String methodName, Object[] args) throws Exception {
		Class[] classTypes = null;
		if (args != null) {
			classTypes = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				if (args[i] != null) {
					classTypes[i] = args[i].getClass();
				}
			}
		}
		return getMethod(instance, methodName, classTypes).invoke(instance, args);
	}

	public static Method getMethod(Object instance, String methodName, Class[] classTypes) throws Exception {
		Method accessMethod = getMethod(instance.getClass(), methodName, classTypes);
		// 禁用访问控制检查
		accessMethod.setAccessible(true);
		return accessMethod;
	}

	@SuppressWarnings("unchecked")
	public static Method getMethod(Class targetClass, String methodName, Class[] argClasses) {
		Object signature = new MethodSignature(targetClass, methodName, argClasses);
		Method method = null;
		Map methodCache = null;
		if (methodCacheRef != null && (methodCache = (Map) methodCacheRef.get()) != null) {
			method = (Method) methodCache.get(signature);
			if (method != null) {
				return method;
			}
		}

		method = findMethod(targetClass, methodName, argClasses);
		if (method != null) {
			if (methodCache == null) {
				methodCache = new HashMap();
				methodCacheRef = new SoftReference(methodCache);
			}
			methodCache.put(signature, method);
			return method;
		}
		for (Class type = targetClass; type != null; type = type.getSuperclass()) {
			method = findMethod(type, methodName, argClasses);
			if (method != null) {
				return method;
			}
			Class[] interfaces = type.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				method = findMethod(interfaces[i], methodName, argClasses);
				if (method != null) {
					return method;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Method findMethod(Class declaringClass, String methodName, Class[] argClasses) {
		try {
			return declaringClass.getDeclaredMethod(methodName, argClasses);
		} catch (Exception e) {
		}
		Method[] methods = declaringClass.getDeclaredMethods();
		List list = new ArrayList();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getName().equals(methodName)) {
				if (matchArguments(argClasses, method.getParameterTypes())) {
					list.add(method);
				}
			}
		}
		if (list.size() > 0) {
			if (list.size() == 1) {
				return (Method) list.get(0);
			} else {
				ListIterator iterator = list.listIterator();
				Method method;
				while (iterator.hasNext()) {
					method = (Method) iterator.next();
					if (matchExplicitArguments(argClasses, method.getParameterTypes())) {
						return method;
					}
				}
				return getMostSpecificMethod(list, argClasses);
			}
		}
		return null;
	}

	private static boolean matchArguments(Class[] argClasses, Class[] argTypes) {
		return matchArguments(argClasses, argTypes, false);
	}

	private static boolean matchExplicitArguments(Class[] argClasses, Class[] argTypes) {
		return matchArguments(argClasses, argTypes, true);
	}

	@SuppressWarnings("unchecked")
	private static boolean matchArguments(Class[] argClasses, Class[] argTypes, boolean explicit) {
		boolean match = (argClasses.length == argTypes.length);
		for (int j = 0; j < argClasses.length && match; j++) {
			Class argType = argTypes[j];
			if (argType.isPrimitive()) {
				argType = typeToClass(argType);
			}
			if (explicit) {
				// Test each element for equality
				if (argClasses[j] != argType) {
					match = false;
				}
			} else {
				// Consider null an instance of all classes.
				if (argClasses[j] != null
						&& !(argType.isAssignableFrom(argClasses[j]))) {
					match = false;
				}
			}
		}
		return match;
	}

	private static Method getMostSpecificMethod(List methods, Class[] args) {
		Method method = null;
		int matches = 0;
		int lastMatch = matches;

		ListIterator iterator = methods.listIterator();
		while (iterator.hasNext()) {
			Method m = (Method) iterator.next();
			Class[] mArgs = m.getParameterTypes();
			matches = 0;
			for (int i = 0; i < args.length; i++) {
				Class mArg = mArgs[i];
				if (mArg.isPrimitive()) {
					mArg = typeToClass(mArg);
				}
				if (args[i] == mArg) {
					matches++;
				}
			}
			if (matches == 0 && lastMatch == 0) {
				if (method == null) {
					method = m;
				} else {
					if (!matchArguments(method.getParameterTypes(), m.getParameterTypes())) {
						method = m;
					}
				}
			} else if (matches > lastMatch) {
				lastMatch = matches;
				method = m;
			} else if (matches == lastMatch) {
				// ambiguous method selection.
				method = null;
			}
		}
		return method;
	}
	
	public static Class typeToClass(Class type) {
		return type.isPrimitive() ? primitiveTypeToClass(type) : type;
	}
	
	public static Class primitiveTypeToClass(Class type) {
		if (type == Boolean.TYPE)
			return Boolean.class;
		if (type == Byte.TYPE)
			return Byte.class;
		if (type == Character.TYPE)
			return Character.class;
		if (type == Short.TYPE)
			return Short.class;
		if (type == Integer.TYPE)
			return Integer.class;
		if (type == Long.TYPE)
			return Long.class;
		if (type == Float.TYPE)
			return Float.class;
		if (type == Double.TYPE)
			return Double.class;
		if (type == Void.TYPE)
			return Void.class;
		return null;
	}
}