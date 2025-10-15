package rpcgen.lua;

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
	private boolean haveself;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println("function " + bean.getName() + ":marshal(ostream)");
		ps.println(prefix + "local _os_ = ostream or FireNet.Marshal.OctetsStream:new()");
//		if (rpcgen.Main.validateMarsshal && bean.getInclude() == null) {	// getInclude()用来判断是否是协议或RPC
//			ps.println(prefix + "	if (!_validator_()) {");
//			ps.println(prefix + "		throw Marshal::Exception();");
//			ps.println(prefix + "	}");
//		}
		for (Variable var : bean.getVariables())
			var.getType().accept(new Marshal(ps, prefix, var));
		ps.println(prefix + "return _os_");
		ps.println("end");
		ps.println("");
	}

	public Marshal(PrintStream ps, String prefix, Variable var) {
		this.ps = ps;
		this.prefix = prefix;
		this.varname = var.getName();
		this.haveself = true;
	}
	
	public Marshal(PrintStream ps, String prefix, String varname) {
		this.ps = ps;
		this.prefix = prefix;
		this.varname = varname;
		this.haveself = false;
	}

	public void visit(TypeByte type) {
		ps.println(prefix + "_os_:marshal_char(" + PrintSelf() + varname + ")");
	}

	public void visit(TypeInt type) {
		ps.println(prefix + "_os_:marshal_int32(" + PrintSelf() + varname + ")");
	}

	public void visit(TypeShort type)
	{
		ps.println(prefix + "_os_:marshal_short(" + PrintSelf() + varname + ")");		
	}

	public void visit(TypeFloat type) {
		ps.println(prefix + "_os_:marshal_float(" + PrintSelf() + varname + ")");
	}

	public void visit(TypeLong type) {
		ps.println(prefix + "_os_:marshal_int64(" + PrintSelf() +varname + ")");
	}

	public void visit(TypeList type) {
		ps.println("");
		ps.println(prefix + "----------------marshal list");
		ps.println(prefix + "_os_:compact_uint32(TableUtil.tablelength("
				+ PrintSelf() + varname + "))");
		ps.println(prefix + "for k,v in ipairs("+ PrintSelf()+ varname +") do");
		type.getValuetype().accept(new Marshal(ps, prefix + "	", "v"));
		ps.println(prefix + "end");
		ps.println("");
	}

	public void visit(TypeMap type) {
		ps.println("");
		ps.println(prefix + "----------------marshal map");
		ps.println(prefix + "_os_:compact_uint32(TableUtil.tablelength("
				+ PrintSelf() + varname + "))");
		ps.println(prefix + "for k,v in pairs("+ PrintSelf() + varname +") do");
		type.getKeytype().accept(new Marshal(ps, prefix + "	", "k"));
		type.getValuetype().accept(new Marshal(ps, prefix + "	", "v"));
		ps.println(prefix + "end");
		ps.println("");
	}

	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_: marshal_octets(" + PrintSelf()+ varname + ")");
	}

	public void visit(TypeSet type) {
		ps.println("");
		ps.println(prefix + "----------------marshal set");
		ps.println(prefix + "_os_:compact_uint32(TableUtil.tablelength("
				+ PrintSelf() + varname + "))");
		ps.println(prefix + "for k,v in pairs("+ PrintSelf()+ varname +") do");
		type.getValuetype().accept(new Marshal(ps, prefix + "	", "k"));
		ps.println(prefix + "end");
		ps.println("");
	}

	public void visit(TypeText type) {			
		ps.println(prefix + "_os_:marshal_wstring(" + PrintSelf() + varname + ")");
	}

	public void visit(TypeVector type) {
		ps.println("");
		ps.println(prefix + "----------------marshal vector");
		ps.println(prefix + "_os_:compact_uint32(TableUtil.tablelength(" 
				+ PrintSelf()+ varname + "))");
		ps.println(prefix + "for k,v in ipairs("+ PrintSelf()+ varname +") do");
		type.getValuetype().accept(new Marshal(ps, prefix + "	", "v"));
		ps.println(prefix + "end");
		ps.println("");
	}

	public void visit(Bean bean) {
		ps.println(prefix + "----------------marshal bean");
		ps.println(prefix + PrintSelf()
				+ varname + ":marshal(_os_) ");
	}
	
	private String PrintSelf(){
		return haveself ? "self.":"";
	}
}
