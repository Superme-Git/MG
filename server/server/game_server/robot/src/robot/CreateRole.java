package robot;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import fire.pb.CCreateRole;

public class CreateRole {

	private static class RoleCreateConfig {

		public int id = -1;

		public final ArrayList<Integer> schools = new ArrayList<Integer>();
	}

	static private CreateRole instance = null;

	static public CCreateRole generateCCreateRole(String rolename) {
		return getInstance().randCCreateRole(rolename);
	}

	private final static int getElementContentValue(Element ele) {
		NodeList nl = ele.getChildNodes();
		int count = nl.getLength();
		for (int i = 0; i < count; i++) {
			Node n = nl.item(i);
			if (Node.TEXT_NODE != n.getNodeType())
				continue;
			Text e = (Text) n;
			return Integer.valueOf(e.getWholeText());
		}
		throw new RuntimeException("Element '" + ele.getTagName() + "' can't parse as int");
	}

	static public CreateRole getInstance() {
		if (null == instance)
			instance = new CreateRole();
		return instance;
	}

	static public void main(String args[]) throws Exception {
		CreateRole cr = new CreateRole(args[0]);
		System.out.println(cr.roles);
	}

	private final ArrayList<RoleCreateConfig> roles = new ArrayList<RoleCreateConfig>();

	private CreateRole() {
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse("config/auto/fire.pb.role.SCreateRoleConfig.xml");
			loadFromServerConfig(document);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private CreateRole(String filename) throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
		loadFromServerConfig(document);
	}

	private final void loadFromServerConfig(Document document) throws Exception {
		Element rootelement = document.getDocumentElement();
		NodeList nl = rootelement.getChildNodes();
		int count = nl.getLength();
		for (int i = 0; i < count; i++) {
			Node n = nl.item(i);
			if (Node.ELEMENT_NODE != n.getNodeType())
				continue;
			Element e = (Element) n;
			if (0 == e.getTagName().compareToIgnoreCase("entry"))
				parseSubElementEntry(e);
		}
	}

	private final void parseSubElementCreateRoleConfig(Element subele, RoleCreateConfig role) {
		NodeList nl = subele.getChildNodes();
		int count = nl.getLength();
		for (int i = 0; i < count; i++) {
			Node n = nl.item(i);
			if (Node.ELEMENT_NODE != n.getNodeType())
				continue;
			Element e = (Element) n;
			if (0 == e.getTagName().compareToIgnoreCase("id"))
				role.id = getElementContentValue(e);
			else if (0 == e.getTagName().compareToIgnoreCase("schools"))
				parseSubElementSchools(e, role);
		}
	}

	private final void parseSubElementEntry(Element subele) {
		NodeList nl = subele.getChildNodes();
		int count = nl.getLength();
		for (int i = 0; i < count; i++) {
			Node n = nl.item(i);
			if (Node.ELEMENT_NODE != n.getNodeType())
				continue;
			Element e = (Element) n;
			if (0 == e.getTagName().compareToIgnoreCase("fire.pb.role.SCreateRoleConfig")) {
				RoleCreateConfig role = new RoleCreateConfig();
				parseSubElementCreateRoleConfig(e, role);
				if (-1 != role.id && !role.schools.isEmpty())
					roles.add(role);
			}
		}
	}

	private final void parseSubElementSchools(Element subele, RoleCreateConfig role) {
		NodeList nl = subele.getChildNodes();
		int count = nl.getLength();
		for (int i = 0; i < count; i++) {
			Node n = nl.item(i);
			if (Node.ELEMENT_NODE != n.getNodeType())
				continue;
			Element e = (Element) n;
			if (0 == e.getTagName().compareToIgnoreCase("int")) {
				int school = getElementContentValue(e);
				if (school == 0)
					continue;
				role.schools.add(school);
			}
		}
	}

	private final CCreateRole randCCreateRole(String rolename) {
		int index = RandValue.rand(roles.size());
		RoleCreateConfig config = roles.get(index);
		index = RandValue.rand(config.schools.size());
		index = config.schools.get(index);
		if (rolename == null)
			rolename = RandValue.randName(CCreateRole.NAMELEN_MAX);
		return new CCreateRole(rolename, index, config.id,"");
	}
}
