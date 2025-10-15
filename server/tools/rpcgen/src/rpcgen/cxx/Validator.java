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

class Validator implements Visitor {
	private static final String PREFIX_CAPACITY = "capacity=";
	private static final String PREFIX_KEY 		= "key=";
	private static final String PREFIX_VALUE 	= "value=";
	private static final String LN				= System.getProperty("line.separator");

	private PrintStream ps;
	private String varname;
	private String validator;
	private String prefix;

	public static void make(Bean bean, PrintStream ps, String prefix)
	{
		ps.println(prefix + "bool _validator_() const {");
		for(Variable var : bean.getVariables())
			var.getType().accept(new Validator(ps, var, prefix + "	"));
		ps.println(prefix + "	return true;");
		ps.println(prefix + "}");
		ps.println("");
	}

	public Validator(PrintStream ps, Variable var, String prefix)
	{
		this.ps = ps;
		this.varname = var.getName();
		this.validator = var.getValidator();
		this.prefix = prefix;
	}

	private static boolean IsBaseType(String str)
	{
		return str.equals("byte") ||
			   str.equals("int") ||
			   str.equals("long") ||
			   str.equals("float");
	}

	private static boolean IsComplexType(String str)
	{
		return str.equals("octets") ||
			   str.equals("string");
	}

	private static boolean IsByte(String str)
	{
		try
		{
			Byte.parseByte(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}

	private static boolean IsInt(String str)
	{
		try
		{
			Integer.parseInt(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}

	private static boolean IsShort(String str)
	{
		try
		{
			Short.parseShort(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}

	private static boolean IsFloat(String str)
	{
		try
		{
			Float.parseFloat(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}

	private static boolean IsLong(String str)
	{
		try
		{
			Long.parseLong(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}

	@SuppressWarnings("unused")
	private static boolean IsType(String type, String str)
	{
		if(type.equals("byte"))
			return IsByte(str);
		else if(type.equals("int"))
			return IsInt(str);
		else if(type.equals("short"))
			return IsShort(str);
		else if(type.equals("long"))
			return IsLong(str);
		else if(type.equals("float"))
			return IsFloat(str);
		else
			throw new RuntimeException("unknown base type '" + type + "'");
	}

	@SuppressWarnings("unused")
	private static String GetTypeSuffix(String type)
	{
		if(type.equals("long"))
			return "LL";
		else if(type.equals("float"))
			return "F";
		else
			return "";
	}

	// "[x,y)" => "name < x || name >= y" (failing condition)
	static String ParseRange(String name, String type, String str)
	{
		if(str == null)
			return "false";
		String[] strs = str.split(",");
		if(str.length() < 3 || strs.length != 2 || strs[0].length() < 1 || strs[1].length() < 1)
			throw new RuntimeException("bad range in '" + str + "'");
		char lb = str.charAt(0);
		if(lb != '[' && lb != '(')
			throw new RuntimeException("not found left bracket in '" + str + "'");
		char rb = str.charAt(str.length() - 1);
		if(rb != ']' && rb != ')')
			throw new RuntimeException("not found right bracket in '" + str + "'");
/*
 		// 严格验证数值类型.由于限定比较复杂,用起来也不灵活,暂不使用
		String valmin = strs[0].substring(1);
		if(!valmin.isEmpty() && !IsType(type, valmin))
			throw new RuntimeException("bad min value (" + type + "," + valmin + ") in '" + str + "'");
		String valmax = strs[1].substring(0, strs[1].length() - 1);
		if(!valmax.isEmpty() && !IsType(type, valmax))
			throw new RuntimeException("bad max value (" + type + "," + valmax + ") in '" + str + "'");
		String suffix = GetTypeSuffix(type);
		if(valmin.isEmpty())
		{
			if(valmax.isEmpty())
				return "false";
			return name + (rb == ']' ? " > " : " >= ") + valmax + suffix;
		}
		if(valmax.isEmpty())
			return name + (lb == '[' ? " < " : " <= ") + valmin + suffix;
		return name + (lb == '[' ? " < " : " <= ") + valmin + suffix + " || " +
			   name + (rb == ']' ? " > " : " >= ") + valmax + suffix;
*/
		// 不严格验证数值类型.写值的时候,在需要的情况下,手动加后缀"L"或"F"
		String valmin = strs[0].substring(1);
		String valmax = strs[1].substring(0, strs[1].length() - 1);
		if(valmin.isEmpty())
		{
			if(valmax.isEmpty())
				return "false";
			return name + (rb == ']' ? " > " : " >= ") + valmax;
		}
		if(valmax.isEmpty())
			return name + (lb == '[' ? " < " : " <= ") + valmin;
		return name + (lb == '[' ? " < " : " <= ") + valmin + " || " +
			   name + (rb == ']' ? " > " : " >= ") + valmax;
	}

	//	byte     value=[min,max]
	//  int      value=[min,max]
	//  float    value=[min,max]
	//  long     value=[min,max]
	private String ParseBaseType(String type)
	{
		String result = "";
		for(String node : validator.split(";"))
		{
			if(node.startsWith(PREFIX_VALUE))
			{
				if(!result.isEmpty())
					throw new RuntimeException("duplicate '" + PREFIX_VALUE + "' in '" + validator + "'");
				result = prefix + "if (" + ParseRange(varname, type, node.substring(PREFIX_VALUE.length())) + ") return false;" + LN;
			}
			else if(!node.isEmpty())
				throw new RuntimeException("unknown prefix in '" + validator + "'");
		}
		return result;
	}

	//  octets   capacity=max
	//  string   capacity=max
	private String ParseString()
	{
		String result = "";
		for(String node : validator.split(";"))
		{
			if(node.startsWith(PREFIX_CAPACITY))
			{
				if(!result.isEmpty())
					throw new RuntimeException("duplicate '" + PREFIX_CAPACITY + "' in '" + validator + "'");
				String valuestr = node.substring(PREFIX_CAPACITY.length());
				if(!IsInt(valuestr))
					throw new RuntimeException("bad size '" + valuestr + "' in '" + node + "'");
				result = prefix + "if (" + varname + ".size() > " + valuestr + ") return false;" + LN;
			}
			else if(!node.isEmpty())
				throw new RuntimeException("unknown prefix in '" + validator + "'");
		}
		return result;
	}

	//	list     capacity=max *value=[min,max]
	//	vector   capacity=max *value=[min,max]
	//	set      capacity=max *value=[min,max]
	private String ParseArray(String type, String valuetype)
	{
		String str_capacity = null;
		String str_value = null;
		String result = "";
		for(String node : validator.split(";"))
		{
			if(node.startsWith(PREFIX_CAPACITY))
			{
				if(str_capacity != null)
					throw new RuntimeException("duplicate '" + PREFIX_CAPACITY + "' in '" + node + "'");
				String valuestr = node.substring(PREFIX_CAPACITY.length());
				if(!IsInt(valuestr))
					throw new RuntimeException("bad size '" + valuestr + "' in '" + node + "'");
				str_capacity = prefix + "if (" + varname + ".size() > " + valuestr + ") return false;" + LN;
				result += str_capacity;
			}
			else if(node.startsWith(PREFIX_VALUE))
			{
				if(str_value != null)
					throw new RuntimeException("duplicate '" + PREFIX_VALUE + "' in '" + node + "'");
				if(!IsBaseType(valuetype))
					throw new RuntimeException("not base type '" + valuetype + "' for value range in '" + node + "'");
				str_value = ParseRange("*_i_", valuetype, node.substring(PREFIX_VALUE.length()));
			}
			else if(!node.isEmpty())
				throw new RuntimeException("unknown prefix in '" + validator + "'");
		}
		if(str_value != null || (valuetype != null && !IsBaseType(valuetype) && !IsComplexType(valuetype)))
		{
			result += prefix + "for (" + type + "::const_iterator _i_ = " + varname + ".begin(); _i_ != " + varname + ".end(); ++_i_)" + LN;
			if(str_value != null)
				result += prefix + "	if (" + str_value + ") return false;" + LN;
			else
				result += prefix + "	if (!_i_->_validator_()) return false;" + LN;
		}
		return result;
	}

	//	map      capacity=max *key=[min,max] *value=[min,max]
	private String ParseMap(String type, String keytype, String valuetype)
	{
		String str_capacity = null;
		String str_key = null;
		String str_value = null;
		String result = "";
		for(String node : validator.split(";"))
		{
			if(node.startsWith(PREFIX_CAPACITY))
			{
				if(str_capacity != null)
					throw new RuntimeException("duplicate '" + PREFIX_CAPACITY + "' in '" + node + "'");
				String valuestr = node.substring(PREFIX_CAPACITY.length());
				if(!IsInt(valuestr))
					throw new RuntimeException("bad size '" + valuestr + "' in '" + node + "'");
				str_capacity = prefix + "if (" + varname + ".size() > " + valuestr + ") return false;" + LN;
				result += str_capacity;
			}
			else if(node.startsWith(PREFIX_KEY))
			{
				if(str_key != null)
					throw new RuntimeException("duplicate '" + PREFIX_KEY + "' in '" + node + "'");
				if(!IsBaseType(keytype))
					throw new RuntimeException("not base type '" + keytype + "' for value range in '" + node + "'");
				str_key = ParseRange("_i_->first", keytype, node.substring(PREFIX_KEY.length()));
			}
			else if(node.startsWith(PREFIX_VALUE))
			{
				if(str_value != null)
					throw new RuntimeException("duplicate '" + PREFIX_VALUE + "' in '" + node + "'");
				if(!IsBaseType(valuetype))
					throw new RuntimeException("not base type '" + valuetype + "' for value range in '" + node + "'");
				str_value = ParseRange("_i_->second", valuetype, node.substring(PREFIX_VALUE.length()));
			}
			else if(!node.isEmpty())
				throw new RuntimeException("unknown prefix in '" + validator + "'");
		}
		boolean keyisbean = (keytype != null && !IsBaseType(keytype) && !IsComplexType(keytype));
		boolean valueisbean = (valuetype != null && !IsBaseType(valuetype) && !IsComplexType(valuetype));
		if(str_key != null || str_value != null || keyisbean || valueisbean)
		{
			result += prefix + "for (" + type + "::const_iterator _i_ = " + varname + ".begin(); _i_ != " + varname + ".end(); ++_i_) {" + LN;
			if(str_key != null)
				result += prefix + "	if (" + str_key + ") return false;" + LN;
			else if(keyisbean)
				result += prefix + "	if (!_i_->first._validator_()) return false;" + LN;
			if(str_value != null)
				result += prefix + "	if (" + str_value + ") return false;" + LN;
			else if(valueisbean)
				result += prefix + "	if (!_i_->second._validator_()) return false;" + LN;
			result += prefix + "}" + LN;
		}
		return result;
	}

	@Override
	public void visit(Bean type)
	{
		ps.println(prefix + "if (!" + varname + "._validator_()) return false;");
	}

	@Override
	public void visit(TypeByte type)
	{
		ps.print(ParseBaseType(type.getName()));
	}

	@Override
	public void visit(TypeInt type)
	{
		ps.print(ParseBaseType(type.getName()));
	}

	@Override
	public void visit(TypeShort type)
	{
		ps.print(ParseBaseType(type.getName()));
	}

	@Override
	public void visit(TypeLong type)
	{
		ps.print(ParseBaseType(type.getName()));
	}

	@Override
	public void visit(TypeOctets type)
	{
		ps.print(ParseString());
	}

	@Override
	public void visit(TypeText type)
	{
		ps.print(ParseString());
	}

	@Override
	public void visit(TypeList type)
	{
		ps.print(ParseArray(TypeName.getName(type), type.getValuetype().getName()));
	}

	@Override
	public void visit(TypeVector type)
	{
		ps.print(ParseArray(TypeName.getName(type), type.getValuetype().getName()));
	}

	@Override
	public void visit(TypeSet type)
	{
		ps.print(ParseArray(TypeName.getName(type), type.getValuetype().getName()));
	}

	@Override
	public void visit(TypeMap type)
	{
		String keytype = type.getKeytype().getName();
		String valuetype = type.getValuetype().getName();
		ps.print(ParseMap(TypeName.getName(type), keytype, valuetype));
	}

	@Override
	public void visit(TypeFloat type)
	{
		ps.print(ParseBaseType(type.getName()));
	}
}
