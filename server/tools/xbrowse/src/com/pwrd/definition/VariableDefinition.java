package com.pwrd.definition;

@Deprecated
public class VariableDefinition {
	public static final String XML_TAG_NAME = "variable";
	private static final String NAME_XML_TAG_NAME = "name";

	private String _name;
	private XdbDataType _type;

	public VariableDefinition() {
		_name = null;
		_type = null;
	}

	public VariableDefinition(org.w3c.dom.Element varElem) {
		this();
		fromXml(varElem);
	}

	public String getName() {
		return _name;
	}

	public XdbDataType getType() {
		return _type;
	}

	public void fromXml(org.w3c.dom.Element varElem) {
		assert varElem != null;
		if (XML_TAG_NAME.equals(varElem.getTagName())) {
			if (varElem.hasAttribute(NAME_XML_TAG_NAME))
				_name = varElem.getAttribute(NAME_XML_TAG_NAME);
			_type = XdbDataType.getXdbDataType(varElem);
		}
	}

	public org.w3c.dom.Element toXml(org.w3c.dom.Document doc) {
		return null;
	}

	public String toString() {
		return getName() + " : " + _type.toString();
	}
}