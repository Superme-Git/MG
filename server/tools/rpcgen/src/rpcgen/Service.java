package rpcgen;

import org.w3c.dom.*;
import java.util.*;
import rpcgen.types.Bean;

public class Service extends Naming {
	private List<Manager> managers = new ArrayList<Manager>();
	private Project project;

	public Project getProject() {
		return project;
	}

	public Service(Project project, Element self) {
		super(self.getAttribute("name"));
		this.project = project;
		Namespace.root().child(getName()).add(this);

		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element e = (Element) node;
			String nodename = e.getNodeName();
			if (nodename.equals("manager"))
				managers.add(new Manager(this, super.getSpace(), e));
			else
				throw new RuntimeException("Unknown element " + Main.quote(nodename) + " in service=" + getName());
		}
	}

	// refs
	private Set<Bean> beans = new HashSet<Bean>();
	private Set<Protocol> protocols = new HashSet<Protocol>();

	public Set<Bean> getBeans() {
		return beans;
	}

	public Set<Protocol> getProtocols() {
		return protocols;
	}

	public void compile() {
	}

	public List<Manager> getManagers() {
		return managers;
	}

	public Set<State> getStates() {
		Set<State> states = new HashSet<State>();
		for (Manager manager : managers)
			states.addAll(manager.getStates());
		return states;
	}

	public Set<Provider> getProviders() {
		Set<Provider> providers = new HashSet<Provider>();
		for (Manager manager : managers)
			providers.addAll(manager.getProviders());
		return providers;
	}

	public void make() throws Exception {
		// 收集 state 引入的协议
		for (State state : getStates())
			protocols.addAll(state.getProtocols());

		// 收集所有协议依赖的 bean
		for (Protocol protocol : protocols)
			protocol.dependBeans(beans);

		// generate java ...
		if (Main.isJava) {
			new rpcgen.java.Rpcgen(this).make();
			return; // generate java done
		}

		// generate as ...
		if (Main.isAs) {
			new rpcgen.as.Rpcgen(this).make();
			return;
		}
		
		if (Main.isJs){
			new rpcgen.js.Rpcgen(this).make();
			return;
		}

		// 基于share，linux平台的版本，一般是服务器。
		new rpcgen.cxx.Rpcgen(this).make();
	}
}
