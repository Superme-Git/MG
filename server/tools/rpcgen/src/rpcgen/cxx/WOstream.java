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

public class WOstream implements Visitor 
{
	private Variable _var;
	private String _text;
	private PrintStream _ps;
	private String _prefix;
	
	public String getText()
	{
		return _text;
	}
	
	public WOstream(Variable var, PrintStream ps, String prefix)
	{
		_var = var;
		_ps = ps;
		_prefix = prefix;
	}
	
	public static void make(Bean bean, PrintStream ps, String prefix, String prot) 
	{
		ps.println(prefix + 
				"friend std::wostream& operator<<(std::wostream& _s_, const " + bean.getName() + " & _obj_){");
		
		if (prot != null)
		{
			ps.println(prefix + 
					"		_s_ << L\"" + prot + ": {\";");
		}
		
		for (Variable var : bean.getVariables()) 
		{
			WOstream e = new WOstream(var, ps, prefix);
			var.getType().accept(e);			

		}
		
		if (prot != null)
		{
			ps.println(prefix + 
					"		_s_ << L\" }\";");
		}
		
		ps.println(prefix + "		return _s_;" );
		ps.println(prefix + "}");
		ps.println("");
		
		
	}

	@Override
	public void visit(Bean type) 
	{
		basicVarVisit(type);
	}

	private void basicVarVisit(rpcgen.types.Type type)
	{
		_text = 
			_prefix + "		_s_ << L\"" + _var.getName() + ": {\" << "+ skipOctets(type , "_obj_."+_var.getName()) + " << L\"}, \";";
		_ps.println(_text);
	}
	
	private void complexVarVisit(rpcgen.types.TypeCollection type)
	{
		String otype = skipOctets(type.getValuetype() , "*_i_");
		_ps.println(_prefix + "		_s_ << L\"" + _var.getName() + ": {\";");
		_text = 
			"		for (" + TypeName.getName(type) + "::const_iterator _i_ = _obj_." + _var.getName() + ".begin(); _i_ != _obj_." + _var.getName() + ".end(); ++_i_){";
		_ps.println(_prefix + _text);				
		_text ="			_s_ << L\"(\" << " + otype + "<< L\"), \";}";
		_ps.println(_prefix + _text);	
		_ps.println(_prefix + "		_s_ << L\"}\";");
	}
	
	@Override
	public void visit(TypeByte type) 
	{
		basicVarVisit(type);
	}

	@Override
	public void visit(TypeInt type) 
	{
		basicVarVisit(type);
	}

	@Override
	public void visit(TypeLong type) 
	{
		basicVarVisit(type);
	}

	@Override
	public void visit(TypeOctets type) 
	{
		basicVarVisit(type);
	}

	private String skipOctets(rpcgen.types.Type t, String out)
	{
		if (t instanceof TypeOctets || t instanceof TypeText)
		{
			return "L\"[Octets]\"";
		}else
		{
			return out;
		}
	}
	
	@Override
	public void visit(TypeText type) 
	{
		basicVarVisit(type);
	}

	
	@Override
	public void visit(TypeList type) 
	{
		complexVarVisit(type);
	}

	@Override
	public void visit(TypeVector type) 
	{
		complexVarVisit(type);
	}

	@Override
	public void visit(TypeSet type) 
	{
		complexVarVisit(type);
	}


	@Override
	public void visit(TypeMap type) 
	{
		String okey = skipOctets(type.getKeytype() , "_i_->first");
		String ovalue = skipOctets(type.getValuetype() , "_i_->second");
		
		
		_ps.println(_prefix + "		_s_ << L\"" + _var.getName() + ": {\";");
		_text = 
			"		for (" + TypeName.getName(type) + "::const_iterator _i_ = _obj_." + _var.getName() + ".begin(); _i_ != _obj_." + _var.getName() + ".end(); ++_i_){";
		_ps.println(_prefix + _text);				
		_text ="			_s_ << L\"(\" << " + okey + " << L\"=\" << " + ovalue +" <<L\"), \";}";
		_ps.println(_prefix + _text);	
		_ps.println(_prefix + "		_s_ << L\"}\";");
	}

	@Override
	public void visit(TypeFloat type) 
	{
		basicVarVisit(type);
	}

	@Override
	public void visit(TypeShort type) 
	{
		basicVarVisit(type);
	}

}
