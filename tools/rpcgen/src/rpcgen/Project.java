package rpcgen;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import rpcgen.types.Bean;
import java.util.*;

public class Project extends Naming {
	private List<Service> services = new ArrayList<Service>();
	private Ranges pvidsAllow;
	private Ranges pvids = new Ranges();
	private String shareHome;

	public String getShareHome() {
		return shareHome;
	}

	public void checkAdd(short pvid) {
		pvidsAllow.assertInclude(pvid);
		pvids.checkAdd(pvid);
	}

	public Project(Element self) {
		super(self.getAttribute("name"));
		super.setSpace(Namespace.root().child(getName()));

		shareHome = self.getAttribute("shareHome");
		pvidsAllow = new Ranges(self.getAttribute("pvids"));
		Main.globalProviderIds.checkAdd(pvidsAllow);

		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element e = (Element) node;
			String nodename = e.getNodeName();

			if (nodename.equals("bean"))
				new Bean(getSpace(), e);
			else if (nodename.equals("protocol"))
				new Protocol(getSpace(), e, Protocol.LEVEL0);
			else if (nodename.equals("rpc"))
				new Rpc(getSpace(), e, Protocol.LEVEL0);

			else if (nodename.equals("provider"))
				new Provider(this, e);
			else if (nodename.equals("state"))
				new State(this, e);
			else if (nodename.equals("service"))
				services.add(new Service(this, e));
			else if (nodename.equals("import"))
				Main.importProject(e.getAttribute("file"));
			else
				throw new RuntimeException("nodename=" + nodename);
		}
	}

	public void compile() {
		Namespace.root().compile();
	}

	public void make() throws Exception {
		for (Service s : services) s.make();
	}
}