package rpcgen;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.*;

public class Naming implements Name {
	private Namespace space;
	private String name;

	public Naming(String name) {
		this.name = name;

		if (null == name)
			return; // see rpcgen.types.Type

		// reserve name
		if (name.equals("rpcgen") 
			|| (name.startsWith("_") && name.endsWith("_"))
			)
			throw new RuntimeException("name reserved ! name=" + name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Namespace getSpace() {
		return space;
	}

	@Override
	public void setSpace(Namespace space) {
		this.space = space;
	}

	@Override
	public String fullName(String sep) {
		String path = space.join(sep);
		return path.isEmpty() ? name : path + sep + name;
	}

	@Override
	public String fullName() {
		return fullName(".");
	}

	/////////////////////////////////////////
	// helper
	public static Set<String> refs(Element self, String nodename, String refName) {
		Set<String> refs = new HashSet<String>();
		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;
			Element e = (Element) node;
			if (e.getNodeName().equals(nodename))
				refs.add(e.getAttribute(refName));
		}
		return refs;
	}

	public static Set<String> refs(Element self, String nodename) {
		return refs(self, nodename, "ref");
	}

	@Override
	public void compile() {
	}

	@Override
	public String toString() {
		return this.fullName();
	}

	protected void parseChild(NameHolder holder, Namespace currentSpace, Element self) {
		Namespace childSpace = currentSpace.child(self.getAttribute("name"));
		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;
			holder.parse(childSpace, (Element) node);
		}
	}

	public static <N extends Name> Collection<N> sort(Collection<N> names) {
		java.util.SortedMap<String, N> sorted = new java.util.TreeMap<String, N>(); 
		for (N name : names)
			sorted.put(name.fullName(), name);
		return sorted.values();
	}
}

