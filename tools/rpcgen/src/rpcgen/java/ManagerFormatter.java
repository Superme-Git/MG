package rpcgen.java;

import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rpcgen.Main;
import rpcgen.Manager;
import rpcgen.Naming;
import rpcgen.Protocol;
import rpcgen.Provider;
import rpcgen.State;

class ManagerFormatter {
	private Manager manager;

	ManagerFormatter(Manager manager) {
		this.manager = manager;
	}

	/**
	 * 生成 Manager的java版本配置。
	 * @param parent
	 * @param deep
	 */
	void make(Element parent, int deep) {
		Element self = getElementByAttrName(parent.getElementsByTagName("Manager"), manager.getName());
		if (null == self) {
			self = parent.getOwnerDocument().createElement("Manager");
			self.setAttribute("name", manager.getName());
			if (manager.isProvider())
				self.setAttribute("class", "gnet.link.Onlines");
			if (manager.isRobot())
				self.setAttribute("class", "robot.Manager");
			Main.appendChild(parent, self, deep);
		}

		if (!manager.getBind().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (Provider p : Naming.sort(manager.bindProviders())) {
				if (first) first = false;
				else sb.append(",");
				sb.append(p.getPvid());
			}
			self.setAttribute("bind", sb.toString());
		}
		makeXioConf(self, deep + 1);
		makeCoder(self, deep + 1);
		Main.finishAppendChild(self, deep);
	}

	private Element getElementByAttrName(NodeList list, String attr) {
		for (int i = 0; i < list.getLength(); ++i) {
			Element e = (Element)list.item(i);
			if (e.getAttribute("name").equals(attr))
				return e;
		}
		return null;
	}

	private Element getFirstElement(NodeList list) {
		if (list.getLength() > 0)
			return (Element)list.item(0);
		return null;
	}

	/**
	 * 构造和更新网络配置。
	 * @param self
	 * @param deep
	 */
	private void makeXioConf(Element self, int deep) {
		if (manager.isServer()) {
			Element acceptor = getFirstElement(self.getElementsByTagName("Acceptor"));
			if (null == acceptor) {
				acceptor = self.getOwnerDocument().createElement("Acceptor");
				acceptor.setAttribute("localIp", "0.0.0.0");
				acceptor.setAttribute("localPort", manager.getPort());
				acceptor.setAttribute("backlog", "32");
				acceptor.setAttribute("sendBufferSize", "16384");
				acceptor.setAttribute("receiveBufferSize", "16384");
				acceptor.setAttribute("outputBufferSize", "16384");
				acceptor.setAttribute("inputBufferSize", "16384");
				acceptor.setAttribute("tcpNoDelay", "false");
				Main.appendChild(self, acceptor, deep);
			}
		} else {
			Element connector = getFirstElement(self.getElementsByTagName("Connector"));
			if (null == connector) {
				connector = self.getOwnerDocument().createElement("Connector");
				connector.setAttribute("remoteIp", "127.0.0.1");
				connector.setAttribute("remotePort", manager.getPort());
				connector.setAttribute("sendBufferSize", "16384");
				connector.setAttribute("receiveBufferSize", "16384");
				connector.setAttribute("outputBufferSize", "16384");
				connector.setAttribute("inputBufferSize", "16384");
				connector.setAttribute("tcpNoDelay", "false");
				Main.appendChild(self, connector, deep);
			}
		}
	}

	/**
	 * 构造 Coder 的协议存根配置。
	 * @param self
	 * @param deep
	 */
	private void makeCoder(Element self, int deep) {
		Element coder = getFirstElement(self.getElementsByTagName("Coder"));
		if (null == coder) {
			coder = self.getOwnerDocument().createElement("Coder");
			Main.appendChild(self, coder, deep);
		}
		coder.setAttribute("comment", "DO NOT EDIT CODER!");
		for (Node child : childNotes(coder))
			coder.removeChild(child);
		SortedMap<String, Protocol> protocols = new TreeMap<String, Protocol>();
		for (State state: manager.getStates())
			for (Protocol p : state.getProtocols())
				protocols.put(p.fullName(), p);
		for (Protocol p : protocols.values())
			p.makeCoder(coder, deep + 1);
		Main.finishAppendChild(coder, deep);
	}

	private Node [] childNotes(Node parent) {
		NodeList childs = parent.getChildNodes();
		Node [] rs = new Node[childs.getLength()];
		for (int i = 0; i < childs.getLength(); ++i)
			rs[i] = childs.item(i);
		return rs;
	}

}
