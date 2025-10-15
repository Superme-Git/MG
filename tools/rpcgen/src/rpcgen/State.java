package rpcgen;

import org.w3c.dom.Element;
import java.util.*;

public class State extends Naming {
	private Element self;
	private Project project;

	// state 单独创建自己的子空间。总是位于project下。
	private static Namespace myspace(Project project) {
		return project.getSpace().child("state");
	}

	public State(Project project, Element self) {
		super(self.getAttribute("name"));
		this.project = project;
		myspace(project).add(this);
		this.self = self;
	}

	private Set<Protocol> protocols = new HashSet<Protocol>();
	private Set<Provider> providers = new HashSet<Provider>();

	@Override
	public void compile() {
		Set<String> refs = refs(self, "protocol");
		refs.addAll(refs(self, "rpc")); // rpc is protocol
		protocols = Protocol.compile(project.getSpace(), refs);
		providers = Provider.compile(project, refs(self, "provider"));
		for (Provider p : providers)
			protocols.addAll(p.getProtocols());
	}

	public Set<Protocol> getProtocols() {
		return protocols;
	}

	public Set<Provider> getProviders() {
		return providers;
	}

	public static Set<State> compile(Project project, Set<String> ss) {
		Set<State> states = new HashSet<State>();
		for (String s : ss)
			states.add(compile(project, s));
		return states;
	}

	public static State compile(Project project, String name) {
		Name n = Namespace.search(myspace(project), name);
		if (null != n && n instanceof State)
			return (State)n;
		throw new RuntimeException("unknown State name=" + name);
	}

	/**
	 * 返回超时配置，如果没有配置则返回“-1”。
	 * @return
	 */
	public String getTimeout() {
		String tout = self.getAttribute("timeout");
		return tout.isEmpty() ? "-1" : tout;
	}
}
