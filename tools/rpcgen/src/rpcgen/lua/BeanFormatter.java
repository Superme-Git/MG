package rpcgen.lua;

import java.io.PrintStream;

import rpcgen.Namespace;
import rpcgen.types.Enum;
import rpcgen.types.Variable;

class BeanFormatter {
	private rpcgen.types.Bean bean;

	BeanFormatter(rpcgen.types.Bean bean) {
		this.bean = bean;
	}

	void make(java.io.File output) {
		Namespace space = bean.getSpace();
		String name = bean.getName().toLowerCase();
		PrintStream ps = space.fopen(output, name + ".lua", true);

		makeRequire(ps);
		//makeNamespace(ps);
		makeTableBegin(ps);
		makeTableNew(ps);
		makeMarshal(ps);
		makeUnmarshal(ps);		
		makeTableEnd(ps);
		
		ps.close();
	}

	void declareEnums(PrintStream ps) {
		if (!bean.getEnums().isEmpty()) {
			int n = 0;
			for (Enum e : bean.getEnums()) {
				n++;
				if (n == bean.getEnums().size())
					ps.println("	"+ e.getName() + " = " + e.getValue());
				else
					ps.println("	"+ e.getName() + " = " + e.getValue() + ",");
			}
		}
	}

	void declareVariables(PrintStream ps) {
		if (!bean.getVariables().isEmpty()) {
			for (Variable var : bean.getVariables())
			{
				if(var.getType().getName() == "byte" ||
					var.getType().getName() == "short" ||
					var.getType().getName() == "int" ||
					var.getType().getName() == "long" ||
					var.getType().getName() == "float")
				{
					ps.println("	self." + var.getName() + " = 0");
				}
				else if(var.getType().getName() == "string" )
				{
					ps.println("	self." + var.getName() + " = \"\" ");
				}
				else if(var.getType().getName() == "octets" )
				{
					ps.println("	self." + var.getName() + " = FireNet.Octets() ");
				}
				else if (var.getType().getName() == "map" ||
						var.getType().getName() == "list" ||
						var.getType().getName() == "vector" ||
						var.getType().getName() == "set")
				{
					ps.println("	self." + var.getName() + " = {}");
				}		
				else
				{
					ps.println("	self." + var.getName() + " = " 
							+  var.getType().getName() + ":new()");
				}
			}		
			ps.println("");
		}
	}
	
	private void makeRequire(PrintStream ps)
	{
		ps.println("require \"utils.tableutil\"");
		for (String inc : Include.includes(bean))
			ps.println("" + inc);
	}
	private void makeNamespace(PrintStream ps)
	{
		ps.println("luaproto = {}");
		final Namespace space = bean.getSpace();
		space.maketable(ps);
		ps.println("");
	}
	private void makeTableBegin(PrintStream ps)
	{
		final Namespace space = bean.getSpace();
		
		if (!bean.getEnums().isEmpty()) {
			ps.println(bean.getName() + " = {");
			declareEnums(ps);
			ps.println("}");
		} else {
			ps.println(bean.getName() + " = {}");
		}

		ps.println(bean.getName() + ".__index = " + bean.getName());
//		ps.println("luaproto." + space.join(".") + "." + bean.getName()
//				+ " = " + bean.getName());
		ps.println("");
		ps.println("");
	}
	private void  makeTableNew(PrintStream ps)
	{
		ps.println("function " + bean.getName() + ":new()");
		ps.println("	local self = {}");
		ps.println("	setmetatable(self, "+ bean.getName() + ")");
		
		declareVariables(ps);
		
		//Validator.make(bean, ps, "	");				
		ps.println("	return self");
		ps.println("end");
	}
	private void  makeMarshal(PrintStream ps)
	{
		Marshal.make(bean, ps, "	");
	}
	private void  makeUnmarshal(PrintStream ps)	
	{
		Unmarshal.make(bean, ps, "	");
	}
	private void  makeTableEnd(PrintStream ps)
	{
		ps.println("return " + bean.getName());
	}

}
