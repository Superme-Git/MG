package mkgen;

import java.util.*;
import java.io.*;

/**
 * 
 * @see mkdb.util.DatabaseMetaData.Type
 */
public abstract class Type {

	/*
	 * ���������ġ���XBean���á�����ʵ����Ҫ���ݵ�ǰ�����ģ����ɲ�ͬ���롣
	 */
	private static XBean currentBean;
	static void setCurrentBean(XBean bean) { currentBean = bean; }
	static XBean getCurrentBean()          { return currentBean; }

	/**
	 * �������֣��û��Զ��壬����ϵͳ��������
	 */
	public abstract String getName();

	private Set<Table> owners = new HashSet<Table>();

	/**
	 * �������͵�ӵ���ߡ�
	 * ����foreign,capacity�Ĺ��ܼ��룬cbean,xbean �����Ǵ�������ݽṹ��
	 * �޷�����ı���ͬ�ı�ʹ�á��Զ���ṹ��ý�����һ�ű�
	 * ���������������������������
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
	 * ���룬
	 * ��������Ƿ��Ѿ����壬��ʼ���ڲ����Ͷ��塣���ڽṹ���������������ڲ����������͡�
	 * @param key   ָ��key�������֣�map��Ҫ������Ϊnull��
	 * @param value ָ��value�������֣�������Ҫ������Ϊnull��
	 * @return
	 */
	public abstract Type compile(String key, String value);

	/**
	 * ���������е�ʵ������
	 */
	public abstract String getTypeName();

	// get �����еķ������ͣ�������һ������������������������ǽӿ�����
	public String getGetterName() {
		return getTypeName();
	}

	public boolean supportAutoIncrement() {
		return false;
	}

	/**
	 * װ����������
	 */
	public abstract String getBoxingName();

	/**
	 * �Ƿ񳣱���
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
	 * ���ɱ��뷽����ʵ�ִ���
	 * @param bean �����ĵ�ǰbean maybe null
	 * @param var  �����ĵ�ǰ����  maybe null
	 * @param ps
	 * @param prefix
	 * @param localvarname ��Ҫʵ��marshal�ı�������
	 */
	public abstract void marshal(XBean bean, Variable var, PrintStream ps, String prefix, String localvarname);

	/**
	 * ���ɽ��뷽����ʵ�ִ���
	 * @param bean
	 * @param var
	 * @param ps
	 * @param prefix
	 */
	public abstract void unmarshal(XBean bean, Variable var, PrintStream ps, String prefix, String localvarname);

	// ������ȿ�����ʵ�ִ���
	public abstract void deepCopy(boolean isData, Variable var, PrintStream ps, String prefix);
	// ������ʵ�� deepCopy ʱ����Ҫ�����������������ֻ�������ִ��룬����Ƕ�뵽��������С�
	// �����Ͳ��ܷŵ�������ʱ������Ҫʵ�֡�
	public abstract String deepCopy(boolean isData, Variable var, String fullvarname);

	/**
	 *  ����Data����ȿ�����ʵ�ֺ�����
	 *  ��psΪnullʱ�����ж��Ƿ���Ҫ���ɣ������������д������ɡ�
	 * @param ps
	 * @param pf
	 * @return 1 ��������ȿ������� 0 ����Ҫ���� -1 ����Ҫ���ɣ���TypeBinary
	 */
	public abstract int deepcopy4cache(PrintStream ps, String pf);

	/**
	 * ����toString��ʵ�ִ���
	 * @param ps
	 * @param prefix
	 * @param varname
	 */
	public void toString(PrintStream ps, String prefix, String varname) {
		ps.println(prefix + "_sb_.append(" + varname + ");");
	}

	/**
	 * ����true�����������Any����Object��
	 * <p>���������ǲ���Any��������ģ������(valuetype)������
	 * <p>XBean�ǲ���Any���������������;�����ֻҪXBean������������һ����Any����ΪAny��
	 * @return
	 */
	public boolean isAny() {
		return false;
	}

	/**
	 * ���͵Ķ�����롣
	 * ����XBean���ᴫ��this������parent�Ĺ��캯����
	 * ֻ����XBean��������ʹ�á����ڹ����ڲ���������
	 * @param bean
	 * @param var
	 * @param definename �������֡�
	 * @return
	 */
	public abstract String defineSetParent(XBean bean, Variable var, String definename);

	/**
	 * ���͵Ķ�����롣
	 * ʹ��Ĭ�ϵĹ��캯����
	 * @param varname �������֡�
	 * @return
	 */
	public abstract String defineNoParent(String varname);

	/**
	 * ����жϱ��ʽ��Ĭ�ϰ�Object��ʽ��
	 * @param varname
	 * @return
	 */
	public abstract String notEquals(String varname);

	/**
	 * ����hashCode���ʽ��
	 * @param varname
	 * @return
	 */
	public abstract String hashCode(String varname);

	public abstract String newListenable(XBean bean, Variable var);

	//////////////////////////////////////////////////////////////
	// ���е����ͣ������������Զ���ġ�
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

	// �����ڼ���ʱ,��δ������ġ�
	public static void add(Type type) {
		if (types.put(type.getName(), type) != null)
			throw new RuntimeException("duplicate type " + type.getName()); 
		//System.out.println("newtype '" + type.getName() + "'");
	}

	private static boolean any = false;
	/**
	 * ������anyΪtrueʱ�����ټ�������Ƿ��Ѿ����壬ʹ��TypeAny��̬����һ�����͡�
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
	 * ���������͵Ľ�������Լ���������Ƿ�ѭ��������
	 * 
	 * <p> ����˵�����е�������ʵ��depends(Set<Type> )ʱ��Ӧ��ʹ�����������ʵ������������
	 * ����ֻ�� xbean, cbean �����Զ������ݽṹ��ֻҪ���������͵�����ʵ�ּ����ѭ���������������Ϳ��Բ���顣
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

	//�ж����type�Ƿ����
	public static boolean isExist( String name )
	{
		return types.containsKey(name);
	}
	
}
