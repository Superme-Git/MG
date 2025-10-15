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

class Unmarshal implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;
	private Bean bean = null; // 指向当前Bean。

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "public override function unmarshal(_os_ : Stream) : Stream {");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Unmarshal(bean, var.getName(), ps, prefix + "	"));
		if (rpcgen.Main.validateUnmarshal && bean.getInclude() == null) {	// getInclude()用来判断是否是协议或RPC
			ps.println(prefix + "	if (!_validator_()) {");
			ps.println(prefix + "		throw new VerifyError(\"validator failed\");");
			ps.println(prefix + "	}");
		}
		ps.println(prefix + "	return _os_;");
		ps.println(prefix + "}");
		ps.println("");
	}

	public Unmarshal(Bean bean, String varname, PrintStream ps, String prefix) {
		this.bean = bean;
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

//	public Unmarshal(String varname, PrintStream ps, String prefix) {
//		this.varname = varname;
//		this.ps = ps;
//		this.prefix = prefix;
//	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + varname + " = _os_.unmarshalInt8();");
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + varname + " = _os_.unmarshalFloat();");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + varname + " = _os_.unmarshalInt32();");
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.println(prefix + varname + " = _os_.unmarshalInt16();");
	}

	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + "_os_.unmarshalInt64(" + varname + ");");
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_.unmarshalOctets(" + varname + ");");
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + varname + " = _os_.unmarshalString(\"UTF-16LE\");");
	}

	private void unmarshalCollection(TypeCollection type) {
		Type valuetype = type.getValuetype();
		final String tempVarName = Rpcgen.tempVarName(bean.fullName());
		ps.println(prefix + "for (var " + tempVarName + " : int = _os_.uncompactUInt32(); "
				+ tempVarName + " > 0; -- " + tempVarName + ") {");
		final String tempInnerVarName = Rpcgen.tempVarName(bean.fullName());
		valuetype.accept(new Define(tempInnerVarName, ps, prefix + "	"));
		valuetype.accept(new Unmarshal(bean, tempInnerVarName, ps, prefix + "	"));
		ps.println(prefix + "	" + varname + ".push(" + tempInnerVarName + ");");
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

		final String iVar = Rpcgen.tempVarName(bean.fullName());
		final String kVar = Rpcgen.tempVarName(bean.fullName());
		final String vVar = Rpcgen.tempVarName(bean.fullName());

		final String kvVar = Rpcgen.tempVarName(bean.fullName());

		ps.println(prefix + "for (var " + iVar + " : int = _os_.uncompactUInt32(); "
				+ iVar + " > 0; -- " + iVar + ") {");
		keytype.accept(new Define(kVar, ps, prefix + "	"));
		keytype.accept(new Unmarshal(bean, kVar, ps, prefix + "	"));
		valuetype.accept(new Define(vVar, ps, prefix + "	"));
		valuetype.accept(new Unmarshal(bean, vVar, ps, prefix + "	"));
		ps.println(prefix + "	var " + kvVar + " : Object = new Object();");
		ps.println(prefix + "	" + kvVar + "[\"key\"]" + " = " + kVar + ";");
		ps.println(prefix + "	" + kvVar + "[\"value\"]" + " = " + vVar + ";");
		ps.println(prefix + "	" + varname + ".push(" + kvVar + ");");
		ps.println(prefix + "}");
	}

	@Override
	public void visit(Bean bean) {
		ps.println(prefix + varname + ".unmarshal(_os_);");
	}
}
