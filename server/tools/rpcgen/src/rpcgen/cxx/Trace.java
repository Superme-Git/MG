package rpcgen.cxx;

import java.io.PrintStream;

import rpcgen.types.Bean;
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

class Trace implements Visitor {
	private String varname;
	private PrintStream ps;
	private String prefix;

	public static void make(Bean bean, PrintStream ps, String prefix, String proto) {
		ps.println(prefix + "std::ostream & trace(std::ostream & _os_) const");
		ps.println(prefix + "{");
		
		if (proto != null){
			ps.println(prefix + "    _os_ << \"" + proto + ":{\";" );
		}
		
		for (Variable var : bean.getVariables())
			var.getType().accept(new Trace(var.getName(), ps, prefix + "	"));
		
		if (proto != null){
			ps.println(prefix + "    _os_ << \"}\";" );
		}
		
		ps.println(prefix + "	return _os_;");
		ps.println(prefix + "}");
		ps.println("");
	}

	public Trace(String varname, PrintStream ps, String prefix) {
		this.varname = varname;
		this.ps = ps;
		this.prefix = prefix;
	}

	@Override
	public void visit(Bean bean) {
		ps.println(prefix + "_os_ << " + varname + " << \",\";");
	}

	@Override
	public void visit(TypeByte type) {
		ps.println(prefix + "_os_ << (int)" + varname + " << \",\";");
	}

	@Override
	public void visit(TypeFloat type) {
		ps.println(prefix + "_os_ << " + varname + " << \",\";");
	}

	@Override
	public void visit(TypeInt type) {
		ps.println(prefix + "_os_ << " + varname + " << \",\";");
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.println(prefix + "_os_ << " + varname + " << \",\";");		
	}

	private void traceCollection(TypeCollection type) {
		ps.println(prefix + "_os_ << \"[\";");
		String typeName = TypeName.getName(type);
		ps.println(prefix + "for (" + typeName + "::const_iterator _i_ = "
			+ varname + ".begin(), _e_ = " + varname + ".end(); _i_ != _e_; ++_i_)");
		ps.println(prefix + "{");
		type.getValuetype().accept(new Trace("(*_i_)", ps, prefix + "	"));
		ps.println(prefix + "}");
		ps.println(prefix + "_os_ << \"]\";");
	}

	@Override
	public void visit(TypeList type) {
		this.traceCollection(type);
	}

	@Override
	public void visit(TypeLong type) {
		ps.println(prefix + "_os_ << " + varname + " << \",\";");
	}

	@Override
	public void visit(TypeMap type) {
		ps.println(prefix + "_os_ << \"{\";");
		String typeName = TypeName.getName(type);
		ps.println(prefix + "for (" + typeName + "::const_iterator _i_ = "
				+ varname + ".begin(), _e_ = " + varname + ".end(); _i_ != _e_; ++_i_)");
		ps.println(prefix + "{");
		ps.println(prefix + "	_os_ << \"(\";");
		type.getKeytype().accept(new Trace("_i_->first", ps, prefix + "	"));
		type.getValuetype().accept(new Trace("_i_->second", ps, prefix + "	"));
		ps.println(prefix + "	_os_ << \")\";");
		ps.println(prefix + "}");
		ps.println(prefix + "_os_ << \"}\";");
	}

	@Override
	public void visit(TypeOctets type) {
		ps.println(prefix + "_os_ << \"" + varname + ".size=\" << " + varname + ".size() << \",\";");
	}

	@Override
	public void visit(TypeSet type) {
		this.traceCollection(type);
	}

	@Override
	public void visit(TypeText type) {
		ps.println(prefix + "_os_ << \"" + varname + ".size=\" << " + varname + ".size() << \",\";");
	}

	@Override
	public void visit(TypeVector type) {
		this.traceCollection(type);
	}
}
