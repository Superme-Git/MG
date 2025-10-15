package rpcgen.types;

import org.w3c.dom.*;
import rpcgen.Main;
import rpcgen.Namespace;
import java.io.PrintStream;
import java.util.*;

/**
 * �Զ����������͡� ʵ�� Marshal
 */

public class Bean extends Type {
	private List<Variable> variables = new ArrayList<Variable>();
	private List<Enum> enums = new ArrayList<Enum>();
	private String comment = "";
	private boolean comparable = false; // setup in compile

	public Collection<Variable> getVariables() {
		return variables;
	}

	@Override
	public boolean isComparable() {
		return comparable;
	}

	private boolean _isComparable() {
		for (Variable var : this.variables)
			if (!var.getType().isComparable())
				return false;
		return true;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Type compile(Namespace space, String key, String value) {
		if (key != null && key.length() > 0)
			throw new RuntimeException(getName() + " type does not need a key. " + key);

		if (value != null && value.length() > 0)
			throw new RuntimeException(getName() + " type does not need a value. " + value);

		return this;
	}

	@Override
	public void depends(Set<Type> incls) {
		if (incls.add(this))
			for (Variable var : variables) {
				var.getType().depends(incls);

				// ������ʼ�����õ���BeanҲ����depends�С� BeanName.ConstStaticVarName
				// 
				String [] initial = var.getInitial().split("\\.");
				String beanNameMabe = "";
				for (int i = 0; i < initial.length - 1; ++i) {
					if (i > 0)
						beanNameMabe += '.';
					beanNameMabe += initial[i];
				}
				if (beanNameMabe.isEmpty())
					continue;
				try {
					Type type = Type.compile(super.getSpace(), beanNameMabe);
					if (null != type)
						incls.add(type); // type.depends(incls); �϶��� Bean������Ҫ�ݹ������ 
				} catch (Exception ex) {
					// skip depends error
				}
			}
	}

	public void add(Variable var) {
		variables.add(var);
	}

	// ///////////////////////////////////////////
	public Bean(Namespace space, Element self) {
		super(self.getAttribute("name"));
		space.add(this); // ���뵽���ֿռ��У�ͳһ����
		Type.add(space, this); // ���Ա�����bean����
		parse(self);
	}

	public Bean(String name) {
		super(name);
	}

	public void parse(Element self) {
		// previous sibling comment
		for (Node c = self.getPreviousSibling(); null != c; c = c.getPreviousSibling()) {
			if (Node.ELEMENT_NODE == c.getNodeType())
				break;
			if (Node.COMMENT_NODE == c.getNodeType()) {
				comment = c.getTextContent().trim();
				break;
			}
		}

		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;
			Element e = (Element) node;
			String nodename = e.getNodeName();
			if (nodename.equals("variable"))
				variables.add(new Variable(e));
			else if (nodename.equals("enum"))
				enums.add(new Enum(e));
			else
				throw new RuntimeException("node=" + nodename);
		}
	}

	@Override
	public void compile() {
		for (Variable var : variables)
			var.compile(getSpace());
		this.comparable = _isComparable();
	}

	public List<Enum> getEnums() {
		return enums;
	}

	/**
	 * �Ƿ���һ��Bean��Protocol��Rpc�ᶯ̬����һ��Beanʵ����������������Ķ��塣
	 * ��Щ����Beanʵ��������һ��������Bean������Ҫ����Ϊ�������ɴ��롣
	 * Protocol��Rpc���й����ʹ�����ǡ�
	 * @return Protocol ����� �������Ǹ�Bean��
	 */
	public boolean isBean() {
		return true;
	}

	/**
	 * ��ӡ����ע�͡���Ȼ������ c++,java,as�����ǽ���java�汾��ʹ�á�
	 * ������Ϊֻ��java�汾��ע����ͬ�������ɴ����С�
	 * @param ps
	 */
	public void printComment(PrintStream ps) {
		if (!comment.isEmpty()) {
			ps.print("/** ");
			ps.println(comment);
			ps.println("*/");
		}
	}

	/**
	 * @return ��� bean ���뱻�ⲿinclude������ null
	 */
	public String getInclude() {
		return "#include \"" + getSpace().join("/") + "/" + getName() + ".hpp\"";
	}
	
	public String getpkg() {
		return "$pfile " + Main.quote("ProtoDef/rpcgen/" + getSpace().join("/") + "/" + getName() + ".pkg");
	}
	
	public String getLuaRequire(){
		return "require " + Main.quote("protodef.rpcgen." + getSpace().join(".").toLowerCase() + "." + getName().toLowerCase() );
	}
}
