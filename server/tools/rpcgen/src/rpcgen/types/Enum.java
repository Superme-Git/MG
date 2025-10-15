package rpcgen.types;

import org.w3c.dom.*;

public class Enum {
	private String name;
	private String value;
	private String comment = "";

	public Enum(Element self) {
		name = self.getAttribute("name").trim();
		value = self.getAttribute("value").trim();
		comment = self.getAttribute("description").trim();
		if (comment.isEmpty()) {
			Node c = self.getNextSibling();
			if (c != null && Node.TEXT_NODE == c.getNodeType()) {
				comment = c.getTextContent().trim().replaceAll("[\r\n]", "");
			}
		}
		if (!comment.isEmpty())
			comment = " // " + comment;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getComment() {
		return comment;
	}

	/**
	 * 定义枚举。c++,java通用
	 * @param div
	 * @return
	 */
	public String declare(String div) {
		return name + " = " + value + div + comment;
	}
}
