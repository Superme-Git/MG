package fire.pb;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 查询xdb的数据
 */
public class XdbSelect {
	private boolean success;
	
	/**
	 * 
	 * @param tableKeyAndKeytype 参数用法
	 * 	key[keytype]默认long型
	 * 	如果是int型传入:keyi 形如:123i
	 * 	所有类型:
	 * 	i - int
	 * 	s - String
	 * 	l - long
	 * 	
	 * @param vars 参数用法：
	 * 1.tablename.basetypename
	 * 
	 * 2.tablename.xbeantypename
	 * 		-tablename.xbeantypename.basetypename
	 * 		-tablename.xbeantypename.xbeantpename[递归]
	 * 		-tablename.xbeantypename.maptypename
	 * 			-tablename.xbeantypename.maptypename.mapkeys
	 * 			-tablename.xbeantypename.maptypename.$mapKey
	 * 				-tablename.xbeantypename.maptypename.$mapKey.(xbeantypename[递归]/basetypename)
	 *		-tablename.xbeantypename.listtypename
	 *			-tablename.xbeantypename.listtypename.size
	 *			-tablename.xbeantypename.listtypename.$listIndex
	 *				-tablename.xbeantypename.listtypename.$listIndex.(xbeantypename[递归]/basetypename)
	 * 以宠物表为例可以如下操作：
	 * 	pet.petmap.1.skills.0.skillid会查询出玩家key=1的宠物的第一个技能的ID
	 * 	以上可以查到任意一步如pet.petmap.1会查询到玩家key=1的宠物的所有信息
	 * 
	 * @return
	 */
	public String select(String tableKeyAndKeytype, String vars) {
		String keyAndType = tableKeyAndKeytype;
		String key = tableKeyAndKeytype;
		Class<?> keyType = Long.class;
		if(keyAndType.endsWith("i")) {
			keyType = Integer.class;
		} else if(keyAndType.endsWith("s")) {
			keyType = String.class;
		}
		if(!keyType.getName().equals(Long.class.getName())
				|| keyAndType.endsWith("l")) {
			key = keyAndType.substring(0, keyAndType.length() - 1);
		}
		
		String[] cols = vars.split("\\.");
		String tableName = firstCharToUpperCase(cols[0]);
		String tablePackage = "xtable." + tableName;
		Class<?> tableClass = null;
		try {
			tableClass = Class.forName(tablePackage);
		} catch (Exception e) {
			return error("不存在的表:" + tableName);
		} catch(Error e) {
			return error("不存在的表:" + tableName);
		}
		Method selectMethod = null;
		try {
			selectMethod = tableClass.getMethod("select", keyType);
		} catch (Exception e) {
			return error("不存在的方法:select(" + keyType.getName() + ")");
		} catch(Error e) {
			return error("不存在的方法:select(" + keyType.getName() + ")");
		}
		Object tableResult = null;
		try {
			if(keyType.getName().equals(Long.class.getName())) {
				tableResult = selectMethod.invoke(null, Long.valueOf(key));
			} else if(keyType.getName().equals(Integer.class.getName())) {
				tableResult = selectMethod.invoke(null, Integer.valueOf(key));
			} else {
				tableResult = selectMethod.invoke(null, key);
			}
		} catch (Exception e) {
			return error("请告知程序需要添加新的类型处理:" + keyType.getName());
		} catch(Error e) {
			return error("请告知程序需要添加新的类型处理:" + keyType.getName());
		}
		Class<?> tableValueType = tableResult.getClass();
		if(isBaseType(tableValueType)) {
			return success(tableName + " = " + tableResult.toString());
		}
		Class<?> varType = tableValueType;
		Object varResult = tableResult;
		String varName = tableName;
		for(int i = 1; i < cols.length; i ++) {
			if(isMap(varType)) {//是map
				if("mapkeys".equals(cols[i])) {
					return success("mapkeys = " + ((Map<?, ?>)varResult).keySet().toString());
				}
				int mapKey = 0;
				try {
					mapKey = Integer.valueOf(cols[i]);
				} catch(Exception e) {
					return error("需要int型的mapKey");
				}
				varResult = ((Map<?, ?>)varResult).get(mapKey);
				if(null == varResult) {
					return error("不存在的mapKey=" + mapKey);
				}
				varType = varResult.getClass();
				varName += ".get(" + mapKey + ")";
				continue;
			} else if(isList(varType)) {//是list
				if("size".equals(cols[i])) {
					return success("size = " + String.valueOf(((List<?>)varResult).size()));
				}
				int index = 0;
				try {
					index = Integer.valueOf(cols[i]);
				} catch(Exception e) {
					return error("需要int型的listIndex");
				}
				varResult = ((List<?>)varResult).get(index); 
				if(null == varResult) {
					return error("不存在的listIndex=" + index);
				}
				varType = varResult.getClass();
				varName += ".get(" + index + ")";
				continue;
			}
			varName = firstCharToUpperCase(cols[i]);
			String varGetMethodName = "get" + varName;
			Method varGetMethod =  null;
			try {
				varGetMethod = varType.getMethod(varGetMethodName);
			} catch (Exception e) {
				return error(varType.getName() + "中不存在的方法:" + varGetMethodName);
			} catch (Error e) {
				return error(varType.getName() + "中不存在的方法:" + varGetMethodName);
			}
			try {
				varResult = varGetMethod.invoke(varResult);
			} catch (Exception e) {
				return error(e.getMessage());
			}
			varType = varResult.getClass();
			if(isBaseType(varType)) {
				return success(varName + " = " + varResult.toString());
			}
		}
		return success(varName + " = " + varResult.toString());
	}
	
	private String success(String result) {
		success = true;
		return result;
	}
	
	private String error(String result) {
		success = false;
		return result;
	}
	
	private String firstCharToUpperCase(String s) {
		return s.replaceFirst(String.valueOf(s.charAt(0)), String.valueOf(s.charAt(0)).toUpperCase());
	}
	
	private boolean isBaseType(Class<?> clazz) {
		return !isXbean(clazz)
				&& !isMap(clazz)
				&& !isList(clazz);
	}
	
	private boolean isXbean(Class<?> clazz) {
		return clazz.getName().startsWith("xbean");
	}
	
	private boolean isMap(Class<?> clazz) {
		return clazz.getName().startsWith("java")
				&& clazz.getName().indexOf("Map") > 0;
	}
	
	private boolean isList(Class<?> clazz) {
		return clazz.getName().startsWith("java")
				&& clazz.getName().indexOf("List") > 0;
	}

	public boolean isSuccess() {
		return success;
	}
}
