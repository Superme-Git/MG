package com.pwrd.definition;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * 类<code>XTableDefinition</code>是XDB数据库中表定义的对象表示。
 * 
 * <p>
 * 类<code>XTableDefinition</code>提供了从XDB数据库表定义xml 元素实例化对象以及将对象表示成表定义xml元素的方法。
 * 
 */
@Deprecated
public final class XTableDefinition {

	/** XTable的元素名。 */
	public static final String XML_TAG_NAME = "table";
	/** 表名的属性名。 */
	private static final String NAME_XML_TAG_NAME = "name";
	/** 键类型的属性名 */
	private static final String KEY_XML_TAG_NAME = "key";
	/** 值类型的属性名 */
	private static final String VALUE_XML_TAG_NAME = "value";

	/** XDB数据库中的表名。 */
	private String _name;

	/** 表的键类型。 */
	private String _keyType;

	/** 表的值类型。 */
	private String _valueType;

	/**
	 * 表的所有其他属性集合，为了能够以一致地方式存储这些属性值， 所 有属性的值类型都被转换成<code>String</code>。
	 */
	private Map<String, String> _attributes = new HashMap<String, String>();

	/**
	 * 创建一个默认的<code>XTableDefinition</code>对象， 集合类型成员清空，其他类型成员
	 */
	public XTableDefinition() {
		_name = null;
		_keyType = null;
		_valueType = null;
	}

	/**
	 * 解析XTable的XML定义创建一个<code>XTableDefinition</code>对象。
	 * 
	 * @param elem
	 *            - XTable的XML表示
	 */
	public XTableDefinition(org.w3c.dom.Element elem) {
		this();
		fromXml(elem);
	}

	/**
	 * @return XDB数据库中的表名。
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @return 表的键类型
	 */
	public String getKeyType() {
		return _keyType;
	}

	/**
	 * @return 表的值类型
	 */
	public String getValueType() {
		return _valueType;
	}

	/**
	 * @return 表的所有其他属性集合
	 */
	public Map<String, String> getAttributes() {
		return _attributes;
	}

	/**
	 * 读取XML元素表示创建对象。
	 * 
	 * @param elem
	 *            - <code>XTableDefinition</code>对象的XML元素表示
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
	 * 得到<code>XTableDefinition</code>对象的XML元素表示。
	 * 
	 * @param doc
	 *            - XML元素所属的文档对象
	 * @return <code>XTableDefinition</cdoe>对象的XML元素表示
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
