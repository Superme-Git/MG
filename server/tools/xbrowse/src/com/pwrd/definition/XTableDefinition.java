package com.pwrd.definition;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * ��<code>XTableDefinition</code>��XDB���ݿ��б���Ķ����ʾ��
 * 
 * <p>
 * ��<code>XTableDefinition</code>�ṩ�˴�XDB���ݿ����xml Ԫ��ʵ���������Լ��������ʾ�ɱ���xmlԪ�صķ�����
 * 
 */
@Deprecated
public final class XTableDefinition {

	/** XTable��Ԫ������ */
	public static final String XML_TAG_NAME = "table";
	/** �������������� */
	private static final String NAME_XML_TAG_NAME = "name";
	/** �����͵������� */
	private static final String KEY_XML_TAG_NAME = "key";
	/** ֵ���͵������� */
	private static final String VALUE_XML_TAG_NAME = "value";

	/** XDB���ݿ��еı����� */
	private String _name;

	/** ��ļ����͡� */
	private String _keyType;

	/** ���ֵ���͡� */
	private String _valueType;

	/**
	 * ��������������Լ��ϣ�Ϊ���ܹ���һ�µط�ʽ�洢��Щ����ֵ�� �� �����Ե�ֵ���Ͷ���ת����<code>String</code>��
	 */
	private Map<String, String> _attributes = new HashMap<String, String>();

	/**
	 * ����һ��Ĭ�ϵ�<code>XTableDefinition</code>���� �������ͳ�Ա��գ��������ͳ�Ա
	 */
	public XTableDefinition() {
		_name = null;
		_keyType = null;
		_valueType = null;
	}

	/**
	 * ����XTable��XML���崴��һ��<code>XTableDefinition</code>����
	 * 
	 * @param elem
	 *            - XTable��XML��ʾ
	 */
	public XTableDefinition(org.w3c.dom.Element elem) {
		this();
		fromXml(elem);
	}

	/**
	 * @return XDB���ݿ��еı�����
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @return ��ļ�����
	 */
	public String getKeyType() {
		return _keyType;
	}

	/**
	 * @return ���ֵ����
	 */
	public String getValueType() {
		return _valueType;
	}

	/**
	 * @return ��������������Լ���
	 */
	public Map<String, String> getAttributes() {
		return _attributes;
	}

	/**
	 * ��ȡXMLԪ�ر�ʾ��������
	 * 
	 * @param elem
	 *            - <code>XTableDefinition</code>�����XMLԪ�ر�ʾ
	 */
	public void fromXml(org.w3c.dom.Element elem) {
		assert elem != null;
		if (XML_TAG_NAME.equals(elem.getTagName())) {
			org.w3c.dom.NamedNodeMap nodeMap = elem.getAttributes();
			for (int i = 0; i < nodeMap.getLength(); ++i) {
				if (nodeMap.item(i) instanceof org.w3c.dom.Attr) {
					org.w3c.dom.Attr attr = (org.w3c.dom.Attr) nodeMap.item(i);
					if (NAME_XML_TAG_NAME.equals(attr.getName()))
						_name = attr.getValue();
					else if (KEY_XML_TAG_NAME.equals(attr.getName()))
						_keyType = attr.getValue();
					else if (VALUE_XML_TAG_NAME.equals(attr.getName()))
						_valueType = attr.getValue();
					else
						_attributes.put(attr.getName(), attr.getValue());
				}
			}
		}
	}

	/**
	 * �õ�<code>XTableDefinition</code>�����XMLԪ�ر�ʾ��
	 * 
	 * @param doc
	 *            - XMLԪ���������ĵ�����
	 * @return <code>XTableDefinition</cdoe>�����XMLԪ�ر�ʾ
	 */
	public org.w3c.dom.Element toXml(org.w3c.dom.Document doc) {
		assert doc != null;
		org.w3c.dom.Element tableElem = doc.createElement(XML_TAG_NAME);
		tableElem.setAttribute(NAME_XML_TAG_NAME, _name);
		tableElem.setAttribute(KEY_XML_TAG_NAME, _keyType);
		tableElem.setAttribute(VALUE_XML_TAG_NAME, _valueType);
		for (Iterator<Map.Entry<String, String>> it = _attributes.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, String> attr = it.next();
			tableElem.setAttribute(attr.getKey(), attr.getValue());
		}

		return tableElem;
	}
}
