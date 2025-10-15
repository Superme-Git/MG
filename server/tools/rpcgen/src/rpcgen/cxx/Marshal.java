package rpcgen.cxx;

import java.io.PrintStream;

import rpcgen.types.Bean;
import rpcgen.types.TypeByte;
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
		ps.println(prefix + "GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {");
		if (rpcgen.Main.validateMarshal && bean.getInclude() == null) {	// getInclude()用来判断是否是协议或RPC
			ps.println(prefix + "	if (!_validator_()) {");
			ps.println(prefix + "		throw Marshal::Exception();");
			ps.println(prefix + "	}");
		}
		for (Variable var : bean.getVariables())
			var.getType().accept(new Marshal(ps, prefix + "	", var));
		ps.println(prefix + "	return _os_;");
		ps.println(prefix + "}");
		ps.println("");
	}

	public Marshal(PrintStream ps, String prefix, Variable var) {
		this.ps = ps;
		this.prefix = prefix;
		this.varname = var.getName();
	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.println(prefix + "_os_ << " + varname + ";");		
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}

	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}

	@Override
	public void visit(TypeList type) {
		ps.println(prefix + "_os_ << GNET::MarshalContainer(" + varname + ");");
	}

	@Override
	public void visit(TypeMap type) {
		ps.println(prefix + "_os_ << GNET::MarshalContainer(" + varname + ");");
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}

	@Override
	public void visit(TypeSet type) {
		ps.println(prefix + "_os_ << GNET::MarshalContainer(" + varname + ");");
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}

	@Override
	public void visit(TypeVector type) {
		ps.println(prefix + "_os_ << GNET::MarshalContainer(" + varname + ");");
	}

	@Override
	public void visit(Bean bean) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}
}
