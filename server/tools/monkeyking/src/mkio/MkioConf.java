package mkio;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * mkio װ�����á�
 * 
 */
public class MkioConf {
	private String name;
	private Map<String, Manager> managers = new HashMap<String, Manager>();

	public String getName() {
		return name;
	}

	public Manager getManager(String name) {
		return managers.get(name);
	}

	void open() {
		for (Manager manager : managers.values()) {
			manager.open();
			// �� mkio ֹͣʱ�����Զ� unregister ���е� MBean��
			Engine.mbeans().register(manager, "mkio:type=" + manager.getMBeanName());
		}
	}

	void close() {
		for (Manager manager : managers.values()) {
			manager.close();
		}
	}

	private void add(Manager manager) {
		if (null != managers.put(manager.getName(), manager))
			throw new RuntimeException("Manager duplicate : " + manager);
	}

	public MkioConf(Element self) throws Exception {
		if (false == self.getNodeName().equals("MkioConf"))
			throw new IllegalArgumentException(self.getNodeName() + " is not a MkioConf.");

		name = self.getAttribute("name");

		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element e = (Element) node;
			String n = e.getNodeName();
			if (n.equals("Manager"))
				add(Manager.create(this, e));
			else
				throw new RuntimeException("Unkown! node=" + n + " parent=" + self.getNodeName());
		}
	}

	/**
	 * ��ָ���ڵ���ӽڵ��в��Ҳ�װ�� mkio ���ã�Ȼ�󷵻ء�û���ҵ����׳��쳣��
	 * 
	 * @param parent
	 *            ָ���ĸ��ڵ�
	 * @return mkio���á�
	 * @throws Exception
	 */
	public static MkioConf loadInChildNodes(Element parent) throws Exception {
		NodeList childnodes = parent.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element e = (Element) node;
			String nodename = e.getNodeName();
			if (nodename.equals("MkioConf")) {
				return new MkioConf(e);
			}
		}
		throw new java.lang.IllegalStateException();
	}

	/**
	 * ���ļ���װ�غ�ע�� mkio ���á�
	 * 
	 * @param conf
	 *            �����ļ�����
	 * @throws Exception
	 */
	public static void loadAndRegister(String conf) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conf);
		Engine.getInstance().register(new MkioConf(doc.getDocumentElement()));
	}

	/**
	 * @see loadAndRegisterInChildNodes
	 * @param self
	 * @throws Exception
	 */
	public static void loadAndRegister(Element self) throws Exception {
		Engine.getInstance().register(new MkioConf(self));
	}

	/**
	 * �� MkioConf �ڵ㴴����ע�����á� ֧�ִӽڵ㴴�����ã���Ϊ�˷������ÿ��԰���������� xml �����С�
	 * <p>
	 * ��װ��ʱ��ֱ��ע�ᵽ�����У���ȥ����ʱ��ע����á������ڲ�����ͬ���� MkioConf ���ڣ�
	 * �ظ�ע���ʧ�ܡ�����������ַ�ʽװ�ز�ע�����ã������һ�����⣬���а������õ��ļ����޷�װ�ض�Ρ� ���õķ����ǵ����ð���������������ʱ��ʹ��
	 * loadInChildNodes �õ�����ʵ�����Լ�����ע��ʱ�� ��������mkio.Engine.start֮ǰ����
	 * 
	 * @param parent
	 * @throws Exception
	 */
	public static void loadAndRegisterInChildNodes(Element parent) throws Exception {
		Engine.getInstance().register(loadInChildNodes(parent));
	}
}
