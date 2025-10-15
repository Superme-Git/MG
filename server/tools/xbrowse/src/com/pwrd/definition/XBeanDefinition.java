package com.pwrd.definition;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NodeList;

@Deprecated
public final class XBeanDefinition {
	public static final String XML_TAG_NAME = "xbean";
	private static final String NAME_XML_TAG_NAME = "name";

	private String _name;
	private Map<String, VariableDefinition> _varDefs;

	public XBeanDefinition() {
		_varDefs = new HashMap<String, VariableDefinition>();
	}

	public XBeanDefinition(org.w3c.dom.Element xbeanElem) {
		this();
		fromXml(xbeanElem);
	}

	public String getName() {
		return _name;
	}

	public Map<String, VariableDefinition> getVariables() {
		return _varDefs;
	}

	/**
	 * 
	 * @param varName
	 * @return
	 */
	public boolean containsVariable(String varName) {
		return _varDefs.containsKey(varName);
	}

	public VariableDefinition getVariable(String varName) {
		return _varDefs.get(varName);
	}

	public void fromXml(org.w3c.dom.Element xbeanElem) {
		assert xbeanElem != null;
		if (XML_TAG_NAME.equals(xbeanElem.getTagName())) {
			if (xbeanElem.hasAttribute(NAME_XML_TAG_NAME)) {
				_name = xbeanElem.getAttribute(NAME_XML_TAG_NAME);
				NodeList varList = xbeanElem.getChildNodes();
				for (int i = 0; i < varList.getLength(); ++i) {
					org.w3c.dom.Node varNode = varList.item(i);
					if (varNode instanceof org.w3c.dom.Element) {
						org.w3c.dom.Element varElem = (org.w3c.dom.Element) varNode;
						if (VariableDefinition.XML_TAG_NAME.equals(varElem.getTagName())) {
							VariableDefinition varDef = new VariableDefinition(varElem);
							_varDefs.put(varDef.getName(), varDef);
						}
					}
				}
			}
			// 不存在匿名的XBean，如果匿名，直接不解析
		}
	}
}
