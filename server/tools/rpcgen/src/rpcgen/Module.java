package rpcgen;

import java.util.Set;
import org.w3c.dom.Element;

public class Module {
	private String name;
	private Set<String> protocols;

	public Module(Element self) {
		this.name = self.getAttribute("name");
		this.protocols = Naming.refs(self, "protocol", "name");
	}

	public String getName() {
		return name;
	}

	public Set<String> getProtocols() {
		return protocols;
	}

	public boolean contains(Protocol p) {
		return protocols.contains(p.fullName("::"));
	}
}
