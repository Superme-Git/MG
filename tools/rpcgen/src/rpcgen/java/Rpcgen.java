package rpcgen.java;

import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import rpcgen.CachedFileOutputStream;
import rpcgen.Main;
import rpcgen.Manager;
import rpcgen.Protocol;
import rpcgen.types.Bean;
import rpcgen.Rpc;

/**
 * java ´úÂëÉú³É
 */
public class Rpcgen {
	private rpcgen.Service service;

	public Rpcgen(rpcgen.Service service) {
		this.service = service;
	}

	private ProtocolFormatter getFormatter(Protocol p) {
		return p instanceof Rpc ? new RpcFormatter((Rpc)p) : new ProtocolFormatter(p);
	}

	public void make() throws Exception {
		java.io.File serviceDir = new java.io.File(service.getName());
		java.io.File srcDir = new java.io.File(serviceDir, "src");

		// generate protocol and rpc
		for (Protocol p : service.getProtocols())
			this.getFormatter(p).make(srcDir);

		// always clean all
		java.io.File beansDir = new java.io.File(serviceDir, "beans");
//		Main.remove(beansDir);
		for (Bean bean : service.getBeans())
			if (bean.isBean())
				new BeanFormatter(bean).make(beansDir);
		CachedFileOutputStream.removeOtherFiles(beansDir);

		// generate xioconf
		java.io.File xioconf = new java.io.File(serviceDir, service.getName() + ".xio.xml");
		if (!xioconf.exists()) {
			PrintStream ps = new PrintStream(new FileOutputStream(xioconf), false, "utf8");
			ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			ps.println("<MkioConf name=" + Main.quote(service.getName()) + "/>");
			ps.close();
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xioconf);
		Element root = doc.getDocumentElement();
		for (Manager manager : service.getManagers())
			new ManagerFormatter(manager).make(root, 1);
		Main.finishAppendChild(root, 0);
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.transform(new DOMSource(doc), new StreamResult(xioconf));
	}
}
