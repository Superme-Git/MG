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

class Marshal implements Visitor {
	private PrintStream ps;
	private String prefix;
	private String varname;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "public OctetsStream marshal(OctetsStream _os_) {");
		if (rpcgen.Main.validateMarshal && bean.getInclude() == null) {	// getInclude()用来判断是否是协议或RPC
			ps.println(prefix + "	if (!_validator_()) {");
			ps.println(prefix + "		throw new VerifyError(\"validator failed\");");
			ps.println(prefix + "	}");
		}
		for (Variable var : bean.getVariables())
			var.getType().accept(new Marshal(var.getName(), ps, prefix + "	"));
		ps.println(prefix + "	return _os_;");
		ps.println(prefix + "}");
		ps.println("");
	}

	public Marshal(String varname, PrintStream ps, String prefix) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

	@Override
	public void visit(Bean bean) {
		ps.println(prefix + "_os_.marshal(" + varname + ");");
	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + "_os_.marshal(" + varname + ");");
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + "_os_.marshal(" + varname + ");");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + "_os_.marshal(" + varname + ");");
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.println(prefix + "_os_.marshal(" + varname + ");");
	}

	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + "_os_.marshal(" + varname + ");");
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_.marshal(" + varname + ");");
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + "_os_.marshal(" + varname + ", \"UTF-16LE\");");
	}

	@Override
	public void visit(TypeMap type) {
		Type keytype = type.getKeytype();
		Type valuetype = type.getValuetype();
		String key = BoxingName.getName(keytype);
		String value = BoxingName.getName(valuetype);
		String KV = "<" + key + ", " + value + ">";

		ps.println(prefix + "_os_.compact_uint32(" + varname + ".size());");
		ps.println(prefix + "for (java.util.Map.Entry" + KV + " _e_ : " + varname + ".entrySet()) {");
		type.getKeytype().accept(new Marshal("_e_.getKey()", ps, prefix + "	"));
		type.getValuetype().accept(new Marshal("_e_.getValue()", ps, prefix + "	"));
		ps.println(prefix + "}");
	}

	private void marshalCollection(TypeCollection type) {
		Type valuetype = type.getValuetype();
		ps.println(prefix + "_os_.compact_uint32(" + varname + ".size());");
		ps.println(prefix + "for (" + BoxingName.getName(valuetype) + " _v_ : " + varname + ") {");
		valuetype.accept(new Marshal("_v_", ps, prefix + "	"));
		ps.println(prefix + "}");
	}

	@Override
	public void visit(TypeList type) {
		this.marshalCollection(type);
	}

	@Override
	public void visit(TypeSet type) {
		this.marshalCollection(type);
	}

	@Override
	public void visit(TypeVector type) {
		this.marshalCollection(type);
	}
}
