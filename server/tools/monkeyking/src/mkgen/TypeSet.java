package mkgen;

import java.io.PrintStream;
import java.util.Map;

public class TypeSet extends TypeCollection {

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public void verifyForeign(Mkdb mkdb, Foreign foreign) {
		foreign.throwIf(null != foreign.getKey(), "[set] need value only.");
		if (null != foreign.getValue()) {
			Table table = mkdb.getTable(foreign.getValue());
			foreign.throwIf(null == table, "[set.value] table not exist.");
			foreign.throwIf(table.isMemory(), "[set.value] foreign table is memory");
			foreign.throwIf(table.getKeyType() != this.getValueType(), "[set.value] type not match");
		}
	}

	@Override
	public void verifyCapacity(Mkdb mkdb, Capacity capacity) {
		capacity.capacityNeed();
		capacity.keyNotNeed();
		this.getValueType().verifyCapacity(mkdb, capacity.extractValue());
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public String hashCode(String varname) {
		return varname + ".hashCode()";
	}

	@Override
	public String notEquals(String varname) {
		return "!" + varname + ".equals(_o_." + varname + ")";
	}

	@Override
	public String defineNoParent(String varname) {
		return getTypeName() + " " + varname + " = new " + getTypeName() + "();";
	}

	@Override
	public String defineSetParent(XBean bean, Variable var, String definename) {
		return defineNoParent(definename);
	}

	@Override
	public String getCollectionName() {
		return Type.getCurrentBean().isData() ? "java.util.HashSet" : "mkdb.util.SetX";
	}

	@Override
	public int deepcopy4cache(PrintStream ps, String pf) {
		if (ps == null)
			return 1; // 仅仅判断是否需要生成

		ps.println(pf + "public static " + this.getGetterName() + " deepcopy(" + this.getGetterName() + " _o_) {");
		ps.println(pf + "	" + this.getGetterName() + " _r_ = new java.util.HashSet" + this.V() + "();");
		Type valuetype = super.getValueType();
		if (valuetype.isConstant()) {
			ps.println(pf + "	_r_.addAll(_o_);");
		} else if (valuetype instanceof TypeBinary) {
			ps.println(pf + "	for (" + valuetype.getBoxingName() + " _v_ : _o_)");
			ps.println(pf + "		_r_.add(java.util.Arrays.copyOf(_v_, _v_.length));");
		} else if (valuetype instanceof XBean) {
			ps.println(pf + "	for (" + valuetype.getBoxingName() + " _v_ : _o_)");
			ps.println(pf + "		_r_.add(_v_.toDataIf());");
		} else {
			// 增加类型是，可能需要增加这里的类型判断。弄个异常检查需要编写特殊代码的新类型。
			throw new RuntimeException("unknown type for deepropy4Cache!");
		}
		ps.println(pf + "	return _r_;");
		ps.println(pf + "}");
		return 1;
	}
	
	@Override
	public String getGetterName() {
		return "java.util.Set" + V();
	}

	@Override
	public void construct(XBean bean, Variable var, PrintStream ps, String prefix) {
		ps.println(prefix + var.getname() + " = new " + getTypeName() + "();");
	}

	@Override
	public void print4reset(XBean bean, Variable var, PrintStream ps, String prefix) {
		ps.println(prefix + var.getname() + ".clear();");
	}

	@Override
	public Type compile(String key, String value) {
		return new TypeSet(key, value);
	}

	private TypeSet(String key, String value) {
		_compile(key, value);
	}

	/**
	 * 创建类型实例，注册到types中
	 * @param types 类型名到类型的关联容器
	 */
	TypeSet(Map<String, Type> types) {
		types.put(getName(), this);
	}

	@Override
	public String newListenable(XBean bean, Variable var) {
		return "new mkdb.logs.ListenableSet().setVarName(" + Main.quote(var.getname()) + ")";
	}
}
