package rpcgen.java;

import java.io.PrintStream;

import rpcgen.types.Bean;
import rpcgen.types.Type;
import rpcgen.types.TypeByte;
import rpcgen.types.TypeCollection;
import rpcgen.types.TypeFloat;
import rpcgen.types.TypeInt;
import rpcgen.types.TypeList;
import rpcgen.types.TypeLong;
import rpcgen.types.TypeMap;
import rpcgen.types.TypeOctets;
import rpcgen.types.TypeSet;
import rpcgen.types.TypeShort;
import rpcgen.types.TypeText;
import rpcgen.types.TypeVector;
import rpcgen.types.Variable;
import rpcgen.types.Visitor;

class Unmarshal implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Unmarshal(var.getName(), ps, prefix + "	"));
		if (rpcgen.Main.validateUnmarshal && bean.getInclude() == null) {	// getInclude()用来判断是否是协议或RPC
			ps.println(prefix + "	if (!_validator_()) {");
			ps.println(prefix + "		throw new VerifyError(\"validator failed\");");
			ps.println(prefix + "	}");
		}
		ps.println(prefix + "	return _os_;");
		ps.println(prefix + "}");
		ps.println("");
	}

	public Unmarshal(String varname, PrintStream ps, String prefix) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + varname + " = _os_.unmarshal_byte();");
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + varname + " = _os_.unmarshal_float();");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + varname + " = _os_.unmarshal_int();");
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.println(prefix + varname + " = _os_.unmarshal_short();");		
	}

	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + varname + " = _os_.unmarshal_long();");
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + varname + " = _os_.unmarshal_Octets();");
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + varname + " = _os_.unmarshal_String(\"UTF-16LE\");");
	}

	private void unmarshalCollection(TypeCollection type) {
		Type valuetype = type.getValuetype();
		ps.println(prefix + "for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {");
		valuetype.accept(new DefineJava("_v_", ps, prefix + "	"));
		valuetype.accept(new Unmarshal("_v_", ps, prefix + "	"));
		ps.println(prefix + "	" + varname + ".add(_v_);");
		ps.println(prefix + "}");
	}

	@Override
	public void visit(TypeList type) {
		this.unmarshalCollection(type);
	}

	@Override
	public void visit(TypeVector type) {
		this.unmarshalCollection(type);
	}

	@Override
	public void visit(TypeSet type) {
		this.unmarshalCollection(type);
	}

	@Override
	public void visit(TypeMap type) {
		Type keytype = type.getKeytype();
		Type valuetype = type.getValuetype();

		ps.println(prefix + "for (int size = _os_.uncompact_uint32(); size > 0; --size) {");
		keytype.accept(new DefineJava("_k_", ps, prefix + "	"));
		keytype.accept(new Unmarshal("_k_", ps, prefix + "	"));
		valuetype.accept(new DefineJava("_v_", ps, prefix + "	"));
		valuetype.accept(new Unmarshal("_v_", ps, prefix + "	"));
		ps.println(prefix + "	" + varname + ".put(_k_, _v_);");
		ps.println(prefix + "}");
	}

	@Override
	public void visit(Bean bean) {
		ps.println(prefix + varname + ".unmarshal(_os_);");
	}
}
