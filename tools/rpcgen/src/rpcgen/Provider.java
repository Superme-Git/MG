package rpcgen;

import java.util.*;
import org.w3c.dom.Element;
import rpcgen.types.Bean;

/**
 * Provider 分类定义协议。
 * 支持按分类引入到状态(state)中。
 * 支持按分类进行派发。
 */
public class Provider extends Naming implements NameHolder {
	public static Provider compile(Project project, String name) {
		Name n = Namespace.search(project.getSpace(), name);
		if (n == null || !(n instanceof Provider))
			throw new RuntimeException("unkown Provider: name=" + name);
		return (Provider)n;
	}

	public static Set<Provider> compile(Project project, Set<String> names) {
		Set<Provider> providers = new HashSet<Provider>();
		for (String name : names)
			providers.add(compile(project, name));
		return providers;
	}

	private short pvid;
	private List<Protocol> protocols = new ArrayList<Protocol>();

	public String getPvidName() {
		return fullName("_").toUpperCase() + "_PVID";
	}

	public short getPvid() {
		return pvid;
	}

	public Provider(Project project, Element self) {
		super(self.getAttribute("name"));
		project.getSpace().add(this); // 放在 project 中

		pvid = Short.parseShort(self.getAttribute("pvid"));
		project.checkAdd(pvid);

		super.parseChild(this, getSpace(), self);
	}

	@Override
	public void parse(Namespace space, Element e) {
		String nodename = e.getNodeName();
		if (nodename.equals("bean")) {
			new Bean(space, e);
		} else if (nodename.equals("protocol")) {
			protocols.add(new Protocol(space, e, pvid));
		} else if (nodename.equals("rpc")) {
			protocols.add(new Rpc(space, e, pvid));
		} else if (nodename.equals("namespace")) {
			super.parseChild(this, space, e);
		} else
			throw new RuntimeException("nodename=" + nodename);
	}

	public List<Protocol> getProtocols() {
		return protocols;
	}
}
