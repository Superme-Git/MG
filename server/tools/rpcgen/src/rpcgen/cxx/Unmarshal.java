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

class Unmarshal implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Unmarshal(var.getName(), ps, prefix + "	"));
		if (rpcgen.Main.validateUnmarshal && bean.getInclude() == null) {	// getInclude()用来判断是否是协议或RPC
			ps.println(prefix + "	if (!_validator_()) {");
			ps.println(prefix + "		throw Marshal::Exception();");
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
	public void visit(Bean bean) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}


	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.println(prefix + "_os_ >> " + varname + ";");		
	}

	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	@Override
	public void visit(TypeList type) {
		ps.println(prefix + "_os_ >> GNET::MarshalContainer(" + varname + ");");
	}

	@Override
	public void visit(TypeVector type) {
		ps.println(prefix + "_os_ >> GNET::MarshalContainer(" + varname + ");");
	}

	@Override
	public void visit(TypeSet type) {
		ps.println(prefix + "_os_ >> GNET::MarshalContainer(" + varname + ");");
	}

	@Override
	public void visit(TypeMap type) {
		ps.println(prefix + "_os_ >> GNET::MarshalContainer(" + varname + ");");
	}
}
