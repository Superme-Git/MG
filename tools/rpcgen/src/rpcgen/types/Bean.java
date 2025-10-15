package rpcgen.types;

import org.w3c.dom.*;
import rpcgen.Main;
import rpcgen.Namespace;
import java.io.PrintStream;
import java.util.*;

/**
 * 自定义数据类型。 实现 Marshal
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

				// 常量初始化引用到的Bean也加入depends中。 BeanName.ConstStaticVarName
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
						incls.add(type); // type.depends(incls); 肯定是 Bean，不需要递归包含。 
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
		space.add(this); // 加入到名字空间中，统一编译
		Type.add(space, this); // 可以被其他bean引用
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
	 * 是否是一个Bean。Protocol和Rpc会动态创建一个Bean实例来解析它们自身的定义。
	 * 这些特例Bean实例并不是一个真正的Bean，不需要单独为它们生成代码。
	 * Protocol和Rpc自行管理和使用它们。
	 * @return Protocol 如果他 如果真的是个Bean。
	 */
	public boolean isBean() {
		return true;
	}

	/**
	 * 打印多行注释。虽然适用于 c++,java,as，但是仅在java版本中使用。
	 * 这是因为只有java版本的注释能同步到生成代码中。
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
	 * @return 如果 bean 不想被外部include，返回 null
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
