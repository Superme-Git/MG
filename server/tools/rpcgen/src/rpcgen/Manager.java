package rpcgen;

import org.w3c.dom.*;
import java.util.*;

public class Manager extends Naming {
	private String initstate;
	private String port;
	private String type;
	private String bind = "";
	private boolean singleton = true;
	private Service service;

	private State initStateRef;
	private Set<State> states;
	private Element self;

	public Manager(Service service, Namespace space, Element self) {
		super(self.getAttribute("name"));
		this.self = self;
		this.service = service;
		space.add(this);

		initstate = self.getAttribute("initstate");
		port = self.getAttribute("port");
		type = self.getAttribute("type").toLowerCase();
		singleton = !self.getAttribute("singleton").equalsIgnoreCase("false");
		if (isProvider()) {
			singleton = false;
			bind = self.getAttribute("bind");
		}
	}

	@Override
	public void compile() {
		states = State.compile(service.getProject(), refs(self, "state"));
		initStateRef = State.compile(service.getProject(), initstate);
		states.add(initStateRef);
	}

	public Set<State> getStates() {
		return states;
	}

	public Set<Provider> getProviders() {
		Set<Provider> providers = new HashSet<Provider>();
		for (State state : getStates())
			providers.addAll(state.getProviders());
		return providers;
	}

	private Provider getProviderRef(String name) {
		for (State state : getStates())
			for (Provider p : state.getProviders())
				if (p.getName().equals(name))
					return p;
		throw new RuntimeException("unkown providerref=" + Main.quote(name));
	}

	public Set<Provider> bindProviders() {
		Set<Provider> ids = new HashSet<Provider>();
		if (isProvider())
			for (String pName : bind.split(","))
				if (!pName.isEmpty())
					ids.add(getProviderRef(pName));
		return ids;
	}

	public String getBind() {
		return bind;
	}

	public State getInitStateRef() {
		return initStateRef;
	}

	public String getInclude() {
		return "#include " + Main.quote(super.getName() + ".hpp");
	}

	public boolean isServer() {
		return type.equalsIgnoreCase("server");
	}

	public boolean isProvider() {
		return type.equalsIgnoreCase("provider");
	}
	
	public boolean isRobot() {
		return type.equalsIgnoreCase("robot");
	}

	public boolean isSingleton() {
		return singleton;
	}

	public String getPort() {
		return port;
	}
}
