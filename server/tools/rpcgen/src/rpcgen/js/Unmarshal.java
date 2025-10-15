package rpcgen.js;

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

	public static void make(Bean bean, PrintStream ps) {
		ps.println(bean.getName() + ".prototype.unmarshal = function(_os_) {");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Unmarshal(bean, "this." + var.getName(), ps, 	"	"));
		ps.println("	return _os_;");
		ps.println("};");
		ps.println("");
	}

	public Unmarshal(Bean bean, String varname, PrintStream ps, String prefix) {
		this.bean = bean;
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

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
	public void visit(TypeShort type){
		ps.println(prefix + varname + " = _os_.unmarshalInt16();");
	}

	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + "_os_.unmarshal(" + varname + ");");
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + varname + " = _os_.unmarshalOctets();");
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + varname + " = _os_.unmarshalString();");
	}

	private void unmarshalCollection(TypeCollection type) {
		Type valuetype = type.getValuetype();
		final String tempVarName = Rpcgen.tempVarName(bean.fullName());
		ps.println(prefix + "for (var " + tempVarName + " = _os_.uncompactUint32(); "
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

		ps.println(prefix + "for (var " + iVar + " = _os_.uncompactUint32(); "
				+ iVar + " > 0; -- " + iVar + ") {");
		keytype.accept(new Define(kVar, ps, prefix + "	"));
		keytype.accept(new Unmarshal(bean, kVar, ps, prefix + "	"));
		valuetype.accept(new Define(vVar, ps, prefix + "	"));
		valuetype.accept(new Unmarshal(bean, vVar, ps, prefix + "	"));
		ps.println(prefix + "	var " + kvVar + " = {};");
		ps.println(prefix + "	" + kvVar + "[\"key\"]" + " = " + kVar + ";");
		ps.println(prefix + "	" + kvVar + "[\"value\"]" + " = " + vVar + ";");
		ps.println(prefix + "	" + varname + ".push(" + kvVar + ");");
		ps.println(prefix + "}");
	}

	@Override
	public void visit(Bean bean) {
		ps.println(prefix + "_os_.unmarshal(" + varname + ");");
	}
}
