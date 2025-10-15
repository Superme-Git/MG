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

class Unmarshal implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;
	private boolean haveself;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println("function " + bean.getName() + ":unmarshal(_os_)");
		
		for (Variable var : bean.getVariables())
			var.getType().accept(new Unmarshal(var.getName(), ps, prefix ,true));
		
//		if (rpcgen.Main.validateUnmarshal && bean.getInclude() == null) {	// getInclude()用来判断是否是协议或RPC
//			ps.println(prefix + "	if (!_validator_()) {");
//			ps.println(prefix + "		throw Marshal::Exception();");
//			ps.println(prefix + "	}");
//		}
		ps.println(prefix + "return _os_");
		ps.println("end");
		ps.println("");
	}

	public Unmarshal(String varname, PrintStream ps, String prefix, boolean haveselfornot) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
		this.haveself = haveselfornot;
	}

	public void visit(Bean bean) {
		ps.println(prefix + "----------------unmarshal bean");
		if(!haveself)
		{
			ps.println(prefix + varname +"=" + 
					( bean.getName()) + ":new()" );
		}
		ps.println("");
		ps.println(prefix + PrintSelf() +varname + ":unmarshal(_os_)");
		ps.println("");
	}


	public void visit(TypeByte type) {
		ps.println(prefix + PrintSelf() + varname + " = _os_:unmarshal_char()");
	}

	public void visit(TypeFloat type) {
		ps.println(prefix + PrintSelf()+ varname + " = _os_:unmarshal_float()");
	}

	public void visit(TypeInt type) {
		ps.println(prefix +PrintSelf() + varname + " = _os_:unmarshal_int32()");
	}

	public void visit(TypeShort type)
	{
		ps.println(prefix + PrintSelf() + varname + " = _os_:unmarshal_short()");		
	}

	public void visit(TypeLong type) {
		ps.println(prefix + PrintSelf() + varname + " = _os_:unmarshal_int64()");
	}

	public void visit(TypeOctets type) {
		if(!haveself)
		{
			ps.println(prefix + varname+" = FireNet.Octets()" );
		}
		ps.println(prefix +
				"_os_:unmarshal_octets("+PrintSelf() + varname + ")");
	}

	public void visit(TypeText type) {
		ps.println(prefix + PrintSelf() + varname + 
				" = _os_:unmarshal_wstring("+PrintSelf() + varname + ")");
	}

	public void visit(TypeList type) {
		ps.println(prefix + "----------------unmarshal list");
		ps.println(prefix + "local sizeof_"+ varname + "=0 ,_os_null_"+varname );
		ps.println(prefix + "_os_null_" + varname + ", sizeof_" + varname +
				" = _os_: uncompact_uint32(sizeof_" + varname + ")");
		ps.println(prefix + "for k = 1,sizeof_"+varname+" do");
		type.getValuetype().accept(new Unmarshal(PrintSelf()+varname+"[k]", 
				ps, prefix + "	", false));
		ps.println(prefix + "end");		
	}

	public void visit(TypeVector type) {
		ps.println(prefix + "----------------unmarshal vector");
		ps.println(prefix + "local sizeof_"+ varname + "=0,_os_null_"+varname );
		ps.println(prefix + "_os_null_" + varname + ", sizeof_" + varname +
				" = _os_: uncompact_uint32(sizeof_" + varname + ")");
		ps.println(prefix + "for k = 1,sizeof_"+varname+" do");
		type.getValuetype().accept(new Unmarshal(PrintSelf()+varname+"[k]", 
				ps, prefix + "	", false));
		ps.println(prefix + "end");	
	}

	public void visit(TypeSet type) {
		ps.println(prefix + "----------------unmarshal set");
		ps.println(prefix + "local sizeof_"+ varname + "=0,_os_null_"+varname );
		ps.println(prefix + "_os_null_" + varname + ", sizeof_" + varname +
				" = _os_: uncompact_uint32(sizeof_" + varname + ")");
		ps.println(prefix + "for k = 1,sizeof_"+varname+" do");
		ps.println(prefix + "	local newkey");
		type.getValuetype().accept(new Unmarshal("newkey", 
				ps, prefix + "	", false));
		ps.println(prefix + "	"+PrintSelf()+varname+"[newkey] = true");
		ps.println(prefix + "end");
	}

	public void visit(TypeMap type) {
		ps.println(prefix + "----------------unmarshal map");
		ps.println(prefix + "local sizeof_"+ varname + "=0,_os_null_"+varname );
		ps.println(prefix + "_os_null_" + varname + ", sizeof_" + varname +
				" = _os_: uncompact_uint32(sizeof_" + varname + ")");
		ps.println(prefix + "for k = 1,sizeof_"+varname+" do");
		ps.println(prefix +"	local newkey, newvalue");
		type.getKeytype().accept(new Unmarshal("newkey", 
				ps, prefix + "	",false));
		type.getValuetype().accept(new Unmarshal("newvalue", 
				ps, prefix + "	",false));
		ps.println(prefix + "	"+PrintSelf()+varname+"[newkey] = newvalue");
		ps.println(prefix + "end");
	}
	
	private String PrintSelf(){
		return haveself ? "self.":"";
	}
}
