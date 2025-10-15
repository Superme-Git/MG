package com.pwrd.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

@Deprecated
public class XdbDefinition {
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(XdbDefinition.class);

	private static XdbDefinition _xdbDef = null;
	private static ReentrantLock _initLock = null;

	private final Map<String, XTableDefinition> _tableDefs;
	private final Map<String, XBeanDefinition> _xbeanDefs;

	static {
		_initLock = new ReentrantLock();
	}

	private XdbDefinition() {
		_tableDefs = new TreeMap<String, XTableDefinition>();
		_xbeanDefs = new TreeMap<String, XBeanDefinition>();
	}

	private synchronized void addXTableDefinition(XTableDefinition tableDef) {
		assert tableDef != null;
		_tableDefs.put(tableDef.getName(), tableDef);
	}

	private void addXBeanDefinition(XBeanDefinition beanDef) {
		_xbeanDefs.put(beanDef.getName(), beanDef);
	}

	public synchronized boolean containsXTable(String xtableName) {
		return _tableDefs.containsKey(xtableName);
	}

	public synchronized boolean containsXBean(String xbeanName) {
		return _xbeanDefs.containsKey(xbeanName);
	}

	public synchronized XTableDefinition getXtableDefinition(String xtableName) {
		return _tableDefs.get(xtableName);
	}

	public synchronized Map<String, XTableDefinition> getAllXTableDefinitions() {
		return Collections.unmodifiableMap(_tableDefs);
	}

	public synchronized XBeanDefinition getXBeanDefinition(String xbeanName) {
		return _xbeanDefs.get(xbeanName);
	}

	public synchronized Map<String, XBeanDefinition> getAllXBeanDefinitions() {
		return _xbeanDefs;
	}

	public static XdbDefinition getInstance() {
		/** 加锁防止<code>xdbDef</code>被初始化多次 */
		_initLock.lock();
		if (_xdbDef == null)
			_xdbDef = new XdbDefinition();
		_initLock.unlock();
		return _xdbDef;
	}

	public void parse(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException {
		File file = new File(xmlFilePath);
		parse(new FileInputStream(file));
	}

	public synchronized void parse(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		org.w3c.dom.Document doc = docBuilder.parse(is);
		org.w3c.dom.Element docElem = doc.getDocumentElement();
		org.w3c.dom.NodeList children = docElem.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			org.w3c.dom.Node child = children.item(i);
			if (child instanceof org.w3c.dom.Element) {
				org.w3c.dom.Element elem = (org.w3c.dom.Element) child;
				if (XTableDefinition.XML_TAG_NAME.equals(elem.getTagName())) {
					XTableDefinition tableDef = new XTableDefinition(elem);
					addXTableDefinition(tableDef);
				} else if (XBeanDefinition.XML_TAG_NAME.equals(elem.getTagName())) {
					XBeanDefinition xbeanDef = new XBeanDefinition(elem);
					addXBeanDefinition(xbeanDef);
				}
			}
		}
	}
}
