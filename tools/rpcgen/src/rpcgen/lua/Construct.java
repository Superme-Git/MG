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

class Construct implements Visitor {
	private PrintStream ps;
	private Variable variable;
	private String prefix;

	public static void make(Bean bean, PrintStream ps, String prefix) {
		ps.println(prefix + bean.getName() + "() {");
		for (Variable var : bean.getVariables())
			var.getType().accept(new Construct(ps, var, prefix + "	"));
		ps.println(prefix + "}");
		ps.println("");
	}

	public Construct(PrintStream ps, Variable variable, String prefix) {
		this.ps = ps;
		this.variable = variable;
		this.prefix = prefix;
	}

	private void initialInteger() {
		// �������ͳ�ʼ���������е�'.'�ᱻ�������ֿռ�ָ��
		String value = variable.getInitial().replaceAll("\\.", "::");
		String varname = variable.getName();
		ps.println(prefix + varname + " = " + (value.isEmpty() ? "0" : value) + ";");
	}

	public void visit(Bean type) {
	}

	public void visit(TypeByte type) {
		this.initialInteger();
	}

	public void visit(TypeInt type) {
		this.initialInteger();
	}

	public void visit(TypeShort type)
	{
		this.initialInteger();		
	}

	public void visit(TypeLong type) {
		this.initialInteger();
	}

	public void visit(TypeOctets type) {
	}

	public void visit(TypeText type) {
		try {
			String VN = variable.getName();
			byte [] V = variable.getInitial().getBytes("utf-16le");
			int iMax = V.length - 1;
			if (iMax >= 0)
			{
				StringBuilder b = new StringBuilder();
				for (int i = 0; ; i++) {
					b.append(V[i]);
					if (i == iMax)
						break;
					b.append(", ");
				}
				ps.println(prefix + "{ // '" + variable.getInitial() + "' UTF-16LE");
				ps.println(prefix + "char __[] = {" + b.toString() + "};");
				ps.println(prefix + VN + " = FireNet::Octets(__, sizeof(__));");
				ps.println(prefix + "}");
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void visit(TypeList type) {
	}

	public void visit(TypeVector type) {
	}

	public void visit(TypeSet type) {
	}

	public void visit(TypeMap type) {
	}

	public void visit(TypeFloat type) {
		String varname = variable.getName();
		String value = variable.getInitial();

		if (value.isEmpty()) {
			ps.println(prefix + varname + " = 0.0f;");
			return;
		}

		try {
			// ����Ƿ�Ϸ��ĸ��㳣��
			Float.valueOf(value);
		} catch (NumberFormatException ex) {
			// ��ʽ���󣬿��ɳ������壬�滻���ֿռ�ָ�����
			value = value.replaceAll("\\.", "::");
		}
		ps.println(prefix + varname + " = " + value + ";");
	}
}
