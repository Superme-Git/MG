package rpcgen.types;

import org.w3c.dom.*;
import java.util.regex.Pattern;

public class Variable {
	private String name;
	private String type;
	private String key;
	private String value;
	private String initial = "";
	private String comment = "";
	private String validator;

	public static void verfiyReserveVariableName(String name) {
		if (name.equals("type"))
			throw new RuntimeException("name of 'type' is reserved");
	}

	public String getName() {
		return name;
	}

	public String getInitial() {
		return initial;
	}

	////////////////////////////////////
	// FOR dynamic create
	public Variable(String name, String type, String key, String value) {
		this.name = name;
		this.type = type;
		this.key = key;
		this.value = value;
		this.initial = "";
	}

	public Variable(Element self) {
		name = self.getAttribute("name").trim().toLowerCase();
		verfiyReserveVariableName(name);
		type = self.getAttribute("type").trim();
		key = self.getAttribute("key").trim();
		value = self.getAttribute("value").trim();
		initial = self.getAttribute("default").trim();
		validator = self.getAttribute("validator").trim();
		
		//by lg
		Pattern pattern = Pattern.compile("knight");
		name = pattern.matcher(name).replaceFirst("fire");
		value = pattern.matcher(value).replaceFirst("fire");
		type = pattern.matcher(type).replaceFirst("fire");
		

		Node c = self.getNextSibling();
		if (c != null && Node.TEXT_NODE == c.getNodeType()) {
			comment = c.getTextContent().trim().replaceAll("[\r\n]", "");
		}
		if (!comment.isEmpty())
			comment = " // " + comment;
	}

	private Type variableType;

	public void compile(rpcgen.Namespace space) {
		variableType = Type.compile(space, type, key, value);
	}

	public Type getType() {
		return variableType;
	}

	/**
	 * 定义变量。c++,java版本通用。
	 * @param typeName
	 * @return
	 */
	public String declare(String typeName) {
		return typeName + " " + name + ";" + comment;
	}

	public String getValidator() {
		return validator;
	}

	public String getComment() {
		return comment;
	}
}
