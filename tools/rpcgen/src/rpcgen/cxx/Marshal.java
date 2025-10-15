package rpcgen.cxx;

import java.io.PrintStream;

import rpcgen.Main;
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
		if (Main.isClient){
			ps.println(prefix + "FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {");
		}else{
			ps.println(prefix + "GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {");
		}
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
		if (Main.isClient){
			ps.println(prefix + "_os_ << FireNet::MarshalContainer(" + varname + ");");
		}else{
			ps.println(prefix + "_os_ << GNET::MarshalContainer(" + varname + ");");
		}
	}

	@Override
	public void visit(TypeMap type) {
		if (Main.isClient){
			ps.println(prefix + "_os_ << FireNet::MarshalContainer(" + varname + ");");
		}else{
			ps.println(prefix + "_os_ << GNET::MarshalContainer(" + varname + ");");
		}
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}

	@Override
	public void visit(TypeSet type) {
		if (Main.isClient){
			ps.println(prefix + "_os_ << FireNet::MarshalContainer(" + varname + ");");
		}else{
			ps.println(prefix + "_os_ << GNET::MarshalContainer(" + varname + ");");
		}
	}

	public void visit(TypeText type) {		
		ps.println(prefix + "{");
		ps.println(prefix + "// utf32 to utf16");
		ps.println(prefix + "typedef unsigned short utf16;");
		if (Main.isClient){
			ps.println(prefix + "FireNet::Octets oct(" + varname + ".length() * 2);");
		}else{
			ps.println(prefix + "GNET::Octets oct(" + varname + ".length() * 2);");
		}
		ps.println(prefix + "for(size_t pos =0; pos < "+ varname + ".length(); pos++) {");
		ps.println(prefix + "utf16 ch = (utf16)"+varname+"[pos];");
		ps.println(prefix + "oct.insert(oct.end(), &ch, 2);");
		ps.println(prefix + "}");
		ps.println(prefix + "_os_ << oct; //only for LE");	
		ps.println(prefix + "}");
	}

	@Override
	public void visit(TypeVector type) {
		if (Main.isClient){
			ps.println(prefix + "_os_ << FireNet::MarshalContainer(" + varname + ");");
		}else{
			ps.println(prefix + "_os_ << GNET::MarshalContainer(" + varname + ");");
		}
	}

	@Override
	public void visit(Bean bean) {
		ps.println(prefix + "_os_ << " + varname + ";");
	}
}
