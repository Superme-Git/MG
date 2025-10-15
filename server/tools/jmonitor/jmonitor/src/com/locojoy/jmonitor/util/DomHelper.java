package com.locojoy.jmonitor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class DomHelper {
	

	public static void prettySave(Document document, String filename) throws Exception {
		DOMImplementation domImplementation = document.getImplementation();
		if (domImplementation.hasFeature("LS", "3.0") && domImplementation.hasFeature("Core", "2.0")) {
			DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation.getFeature("LS", "3.0");
			LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
			DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
			if (domConfiguration.canSetParameter("format-pretty-print", Boolean.TRUE)) {
				lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
				LSOutput lsOutput = domImplementationLS.createLSOutput();
				lsOutput.setEncoding("UTF-8");
				lsOutput.setByteStream(new FileOutputStream(new File(filename)));
				lsSerializer.write(document, lsOutput);
				return;
			}
		}

		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setAttribute("indent-number", new Integer(4));

		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		Result output = new StreamResult(new File(filename));
		Source input = new DOMSource(document);
		transformer.transform(input, output);
	}

	public static String documentToString(Document document) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setAttribute("indent-number", new Integer(4));
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter sw = new StringWriter(512);
		Result output = new StreamResult(sw);
		Source input = new DOMSource(document);
		transformer.transform(input, output);
		sw.flush();
		return sw.toString();
	}

	public static List<Element> getElementsByName(Element ele, String name) {
		List<Element> es = new ArrayList<Element>();
	
		NodeList nl = ele.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element e = (Element) node;
			if (e.getNodeName().compareToIgnoreCase(name) == 0)
				es.add(e);
		}
		return es;
	}

}
