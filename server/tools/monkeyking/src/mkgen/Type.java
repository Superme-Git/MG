package mkgen;

import java.util.*;
import java.io.*;

/**
 * 
 * @see mkdb.util.DatabaseMetaData.Type
 */
public abstract class Type {

	/*
	 * 生成上下文。由XBean设置。类型实现需要根据当前上下文，生成不同代码。
	 */
	private static XBean currentBean;
	static void setCurrentBean(XBean bean) { currentBean = bean; }
	static XBean getCurrentBean()          { return currentBean; }

	/**
	 * 类型名字，用户自定义，或者系统基本类型
	 */
	public abstract String getName();

	private Set<Table> owners = new HashSet<Table>();

	/**
	 * 设置类型的拥有者。
	 * 随着foreign,capacity的功能加入，cbean,xbean 不再是纯粹的数据结构，
	 * 无法随意的被不同的表使用。自定义结构最好仅用于一张表。
	 * 增加这个属性用来检测这种情况。
	 * @param table
	 */
	public void addOwnerTable(Table owner) {
		// Main.checkAddTypeOwnerTable(this, owners, owner);
		owners.add(owner);
	}

	/**
	 * @see addOwnerTable(Table)
	 * @return owners 
	 */
	public Set<Table> getOwnerTables() {
		return owners;
	}

	public abstract void verifyForeign(Mkdb mkdb, Foreign foreign);
	public abstract void verifyCapacity(Mkdb mkdb, Capacity capacity);
	public abstract void printTableSelectMethod(PrintStream ps, String K);

	/**
	 * 编译，
	 * 检查类型是否已经定义，初始化内部类型定义。对于结构和容器，检查包括内部包含的类型。
	 * @param key   指名key类型名字，map需要，可以为null。
	 * @param value 指名value类型名字，容器需要，可以为null。
	 * @return
	 */
	public abstract Type compile(String key, String value);

	/**
	 * 程序语言中的实际名字
	 */
	public abstract String getTypeName();

	// get 定义中的返回类型，简单类型一般就是类型名，容器类型则是接口类型
	public String getGetterName() {
		return getTypeName();
	}

	public boolean supportAutoIncrement() {
		return false;
	}

	/**
	 * 装箱类型名字
	 */
	public abstract String getBoxingName();

	/**
	 * 是否常变量
	 */
	public abstract boolean isConstant();

	public abstract void getter(XBean bean, Variable var, PrintStream ps, String prefix);
	public abstract void getterConst(XBean bean, Variable var, PrintStream ps, String prefix);
	public abstract void getterInterface(XBean bean, Variable var, PrintStream ps, String prefix);
	public abstract void getterData(XBean bean, Variable var, PrintStream ps, String prefix);

	public abstract void setter(XBean bean, Variable var, PrintStream ps, String prefix);
	public abstract void setterConst(XBean bean, Variable var, PrintStream ps, String prefix);
	public abstract void setterInterface(XBean bean, Variable var, PrintStream ps, String prefix);
	public abstract void setterData(XBean bean, Variable var, PrintStream ps, String prefix);

	public abstract void construct(XBean bean, Variable var, PrintStream ps, String prefix);
	public abstract void print4reset(XBean bean, Variable var, PrintStream ps, String prefix);

	public String compareto(String vn1, String vn2) {
		throw new UnsupportedOperationException("compareTo");
	}

	/**
	 * 生成编码方法的实现代码
	 * @param bean 上下文当前bean maybe null
	 * @param var  上下文当前变量  maybe null
	 * @param ps
	 * @param prefix
	 * @param localvarname 需要实现marshal的变量名字
	 */
	public abstract void marshal(XBean bean, Variable var, PrintStream ps, String prefix, String localvarname);

	/**
	 * 生成解码方法的实现代码
	 * @param bean
	 * @param var
	 * @param ps
	 * @param prefix
	 */
	public abstract void unmarshal(XBean bean, Variable var, PrintStream ps, String prefix, String localvarname);

	// 生成深度拷贝的实现代码
	public abstract void deepCopy(boolean isData, Variable var, PrintStream ps, String prefix);
	// 当容器实现 deepCopy 时，需要下面这个辅助方法。只产生部分代码，可以嵌入到其他语句中。
	// 当类型不能放到容器中时，不需要实现。
	public abstract String deepCopy(boolean isData, Variable var, String fullvarname);

	/**
	 *  生成Data的深度拷贝的实现函数。
	 *  当ps为null时，仅判断是否需要生成，并不真正进行代码生成。
	 * @param ps
	 * @param pf
	 * @return 1 生成了深度拷贝代码 0 不需要生成 -1 不需要生成，是TypeBinary
	 */
	public abstract int deepcopy4cache(PrintStream ps, String pf);

	/**
	 * 生成toString的实现代码
	 * @param ps
	 * @param prefix
	 * @param varname
	 */
	public void toString(PrintStream ps, String prefix, String varname) {
		ps.println(prefix + "_sb_.append(" + varname + ");");
	}

	/**
	 * 返回true，如果类型是Any（即Object）
	 * <p>容器类型是不是Any，由它的模板类型(valuetype)决定。
	 * <p>XBean是不是Any，由它依赖的类型决定。只要XBean依赖的类型有一个是Any，则为Any。
	 * @return
	 */
	public boolean isAny() {
		return false;
	}

	/**
	 * 类型的定义代码。
	 * 对于XBean，会传递this，设置parent的构造函数。
	 * 只能在XBean子类里面使用。用于构造内部关联树。
	 * @param bean
	 * @param var
	 * @param definename 变量名字。
	 * @return
	 */
	public abstract String defineSetParent(XBean bean, Variable var, String definename);

	/**
	 * 类型的定义代码。
	 * 使用默认的构造函数。
	 * @param varname 变量名字。
	 * @return
	 */
	public abstract String defineNoParent(String varname);

	/**
	 * 相等判断表达式，默认按Object方式。
	 * @param varname
	 * @return
	 */
	public abstract String notEquals(String varname);

	/**
	 * 类型hashCode表达式。
	 * @param varname
	 * @return
	 */
	public abstract String hashCode(String varname);

	public abstract String newListenable(XBean bean, Variable var);

	//////////////////////////////////////////////////////////////
	// 所有的类型，包括基本和自定义的。
	private static Map<String, Type> types = new HashMap<String, Type>();

	static {
		// constant
		new TypeBoolean(types);
		new TypeShort(types);
		new TypeInt(types);
		new TypeLong(types);
		new TypeString(types);
		new TypeFloat(types);

		// collection
		new TypeList(types);
		new TypeVector(types);
		new TypeSet(types);

		// map
		new TypeMap(types);
		new TypeTreeMap(types);

		new TypeBinary(types);
		//new TypeOctets(types);
	}

	// 类型在加入时,是未经编译的。
	public static void add(Type type) {
		if (types.put(type.getName(), type) != null)
			throw new RuntimeException("duplicate type " + type.getName()); 
		//System.out.println("newtype '" + type.getName() + "'");
	}

	private static boolean any = false;
	/**
	 * 当设置any为true时，不再检查类型是否已经定义，使用TypeAny动态构造一个类型。
	 * 
	 * @param a
	 * @return
	 */
	public static boolean setAny(boolean a) {
		boolean tmp = any;
		any = a;
		return tmp;
	}

	public static Type compile(String name, String key, String value) {
		Type type = types.get(name);
		if (null != type)
			return type.compile(key, value);

		if (any) // see setAny
			return new TypeAny(name);
		throw new RuntimeException("'" + name + "' TYPE NOT FOUND!");
	}

	public abstract void depends(Set<Type> types);

	private static Type dependFrom = null;

	/**
	 * 在依赖类型的结果加入自己，并检查是否循环依赖。
	 * 
	 * <p> 按理说，所有的类型在实现depends(Set<Type> )时都应该使用这个方法来实现依赖方法。
	 * 由于只有 xbean, cbean 两种自定义数据结构，只要这两个类型的依赖实现检测了循环依赖，基本类型可以不检查。
	 * @param result
	 * @param self
	 * @return
	 */
	public static boolean addDepend(Set<Type> result, Type self) {
		if (result.add(self))
			return true;
		if (dependFrom == self)
			throw new RuntimeException("unsupport self-depend. type=" + Main.quote(self.getName()));
		return false;
	}

	public Set<Type> depends() {
		Set<Type> types = new HashSet<Type>();
		dependFrom = this;
		try {
			depends(types);
		} finally {
			dependFrom = null;
		}
		return types;
	}

	//判断这个type是否存在
	public static boolean isExist( String name )
	{
		return types.containsKey(name);
	}
	
}
