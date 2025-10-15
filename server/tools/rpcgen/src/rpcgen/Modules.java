package rpcgen;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.util.*;
import java.io.*;

public class Modules {
	private static Modules instance = new Modules();
	private static File path;

	private Map<String, Module> modules = new HashMap<String, Module>();

	private void add(Module module) {
		if (null != modules.put(module.getName(), module))
			throw new RuntimeException("duplicate module name=" + Main.quote(module.getName()));
	}

	private Modules() {
	}

	public static void parse(String xmlfile) throws Exception {
		File file = new File(xmlfile);
		path = file.getParentFile(); // save modules.xml path . used by include
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		getInstance().parse(doc.getDocumentElement());
	}

	public static void include(String xmlfile) throws Exception {
		File file = (null == path) ? new File(xmlfile) : new File(path, xmlfile) ;
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		getInstance().parse(doc.getDocumentElement());
	}

	public void parse(Element self) throws Exception {
		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element e = (Element) node;
			String nodename = e.getNodeName();

			if (nodename.equals("module"))
				add(new Module(e));
			else if (nodename.equals("include"))
				include(e.getAttribute("name"));
			else
				Main.debug("unkown element in modules. " + Main.quote(nodename));
		}
	}

	public static Modules getInstance() {
		return instance;
	}

	public Module findProcessModule(Protocol p) {
		for (Module module : modules.values())
			if (module.contains(p))
				return module;
		return null;
	}

	public void verify() {
		int total = 0;
		Set<String> ps = new HashSet<String>();
		for (Module module : modules.values()) {
			ps.addAll(module.getProtocols());
			total += module.getProtocols().size();
		}
		if (total != ps.size())
			throw new RuntimeException("protocol can only process in one module");
	}
}
