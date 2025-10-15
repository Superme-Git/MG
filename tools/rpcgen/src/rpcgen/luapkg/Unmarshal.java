package rpcgen.luapkg;

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
import rpcgen.types.Visitor;

class Unmarshal implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + "const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) ;");
	}

	public Unmarshal(String varname, PrintStream ps, String prefix) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

	public void visit(Bean bean) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}


	public void visit(TypeByte type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	public void visit(TypeFloat type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	public void visit(TypeInt type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	public void visit(TypeShort type)
	{
		ps.println(prefix + "_os_ >> " + varname + ";");		
	}

	public void visit(TypeLong type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_ >> " + varname + ";");
	}

	public void visit(TypeText type) {
		ps.println(prefix + "{");
		ps.println(prefix + "FireNet::Octets oct;");
		ps.println(prefix + "_os_ >> oct ;");
		
		
		ps.println(prefix + "if(oct.size() % 2 != 0) {");
		ps.println(prefix + "throw Marshal::Exception();");
		ps.println(prefix + "}");
		ps.println(prefix + varname+ ".reserve(oct.size()/2);");	
		ps.println(prefix + "typedef unsigned short utf16;");
		ps.println(prefix + "// only for little endian");
		ps.println(prefix + "for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)");
		ps.println(prefix + "{");
		ps.println(prefix + varname + ".insert(" + varname + ".end(), (wchar_t)*ch);");
		ps.println(prefix + "}");
		
		ps.println(prefix + "}");
	}

	public void visit(TypeList type) {
		ps.println(prefix + "_os_ >> FireNet::MarshalContainer(" + varname + ");");
	}

	public void visit(TypeVector type) {
		ps.println(prefix + "_os_ >> FireNet::MarshalContainer(" + varname + ");");
	}

	public void visit(TypeSet type) {
		ps.println(prefix + "_os_ >> FireNet::MarshalContainer(" + varname + ");");
	}

	public void visit(TypeMap type) {
		ps.println(prefix + "_os_ >> FireNet::MarshalContainer(" + varname + ");");
	}
}
