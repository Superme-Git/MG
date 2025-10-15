package rpcgen.as;

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
	private Bean bean = null; // 指向当前Bean。

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "public override function marshal(_os_ : Stream) : Stream {");
		if (rpcgen.Main.validateMarshal && bean.getInclude() == null) {	// getInclude()用来判断是否是协议或RPC
			ps.println(prefix + "	if (!_validator_()) {");
			ps.println(prefix + "		throw new VerifyError(\"validator failed\");");
			ps.println(prefix + "	}");
		}
		for (Variable var : bean.getVariables())
			var.getType().accept(new Marshal(bean, var.getName(), ps, prefix + "	"));
		ps.println(prefix + "	return _os_;");
		ps.println(prefix + "}");
		ps.println("");
	}

	public Marshal(Bean bean, String varname, PrintStream ps, String prefix) {
		this.bean = bean;
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

//	public Marshal(String varname, PrintStream ps, String prefix) {
//		this.varname = varname;
//		this.ps = ps;
//		this.prefix = prefix;
//	}

	@Override
	public void visit(Bean bean) {
		ps.println(prefix + "_os_.marshal(" + varname + ");");
	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + "_os_.marshalInt8(" + varname + ");");
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + "_os_.marshalFloat(" + varname + ");");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + "_os_.marshalInt32(" + varname + ");");
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.println(prefix + "_os_.marshalInt16(" + varname + ");");		
	}

	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + "_os_.marshalInt64(" + varname + ");");
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_.marshalOctets(" + varname + ");");
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + "_os_.marshalString(" + varname + ", \"UTF-16LE\");");
	}

	@Override
	public void visit(TypeMap type) {
		ps.println(prefix + "_os_.compactUInt32(" + varname + ".length);");
		final String iVar = Rpcgen.tempVarName(bean.fullName());
		final String valueVar = Rpcgen.tempVarName(bean.fullName());
		ps.println(prefix + "for (var " + iVar + " : int = 0; "
				+ iVar + " < " + varname + ".length; ++ "
				+ iVar + ") {");
		ps.println(prefix + "	var " + valueVar + " : Object = " + varname + "[" + iVar + "];");
		type.getKeytype().accept(new Marshal(bean, valueVar + ".key", ps, prefix + "	"));
		type.getValuetype().accept(new Marshal(bean, valueVar + ".value", ps, prefix + "	"));
		ps.println(prefix + "}");
	}

	private void marshalCollection(TypeCollection type) {
		Type valuetype = type.getValuetype();
		ps.println(prefix + "_os_.compactUInt32(" + varname + ".length);");
		final String tempVarName = Rpcgen.tempVarName(bean.fullName());
		ps.println(prefix + "for (var " + tempVarName + " : int = 0; "
				+ tempVarName + " < " + varname + ".length; ++ "
				+ tempVarName + ") {");
		valuetype.accept(new Marshal(bean, varname + "[" + tempVarName + "]", ps, prefix + "	"));
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
