import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerConf {
	
	static private class ServerList {
		String zoneid;
		String delivery;
		
		String getGsIP() {
			String[] v = delivery.split( "\\.");
			return v[0] + "." + v[1] + "." + v[2] + ".11";
		}
		static public final Map<String, ServerList> load( final String xmlfile, int aid) throws SAXException, IOException, ParserConfigurationException {

			final String aidstring = Integer.toString( aid);
			final Map<String, ServerList> servers = new HashMap<String, ServerList>();
		
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( xmlfile);
			final NodeList nl = doc.getDocumentElement().getChildNodes();
			int count = nl.getLength();
			
			for( int i = 0; i < count; i ++) {
				Node node = nl.item( i);
				if( Node.ELEMENT_NODE != node.getNodeType())
					continue;
				if( 0 != node.getNodeName().compareToIgnoreCase( "zone"))
					continue;
				Element ele = (Element)node;
				final String eleaid = ele.getAttribute( "aid");
				if( 0 != aidstring.compareToIgnoreCase( eleaid))
					continue;
				loadZone( servers, ele);
			}
			
			return servers;
		}
		
		static private void loadZone( Map<String, ServerList> servers, Element ele) {
			final String name = ele.getAttribute( "name");
			final ServerList	server = new ServerList();
			server.zoneid = ele.getAttribute( "id");
			server.delivery = ele.getAttribute( "delivery");
			
			servers.put( name, server);
		}
		
	}
	
	static private class ClientServerData {
		String 	name;
		String 	ip;
		String 	port;
		String 	portnum;
		
		String getLinkEndPort() {
			int iport = Integer.parseInt( port);
			int inum = Integer.parseInt( portnum);
			return Integer.toString( iport + inum - 1);
		}

		static public Collection<ClientServerData> load( String xmlfile) throws SAXException, IOException, ParserConfigurationException {
			
			final Collection<ClientServerData> 	servers = new LinkedList<ClientServerData>();
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( xmlfile);
			final NodeList nl = doc.getDocumentElement().getChildNodes();
			final int count = nl.getLength();
			for( int i = 0; i < count; i ++) {
				Node node = nl.item( i);
				if( Node.ELEMENT_NODE != node.getNodeType())
					continue;
				if( 0 != node.getNodeName().compareToIgnoreCase( "area"))
					continue;
				Element ele = (Element)node;
				String name = ele.getAttribute( "name");
				loadConfPreArea( servers, name, ele);
			}
			return servers;
		}
		static private void loadConfPreArea( Collection<ClientServerData> servers, String areaname, Element ele) {
			NodeList nl = ele.getChildNodes();
			int count = nl.getLength();
			for( int i = 0; i < count; i ++) {
				Node node = nl.item( i);
				if( Node.ELEMENT_NODE != node.getNodeType())
					continue;
				if( 0 != node.getNodeName().compareToIgnoreCase( "server"))
					continue;
				Element eleserver = (Element)node;
				ClientServerData server = new ClientServerData();
				server.name = areaname + "-" + eleserver.getAttribute( "servername");
				server.ip = eleserver.getAttribute( "ip");
				server.port = eleserver.getAttribute( "port");
				server.portnum = eleserver.getAttribute( "portnum");
				
				servers.add( server);
			}
		}
	}

	static private class ServerData {
		ClientServerData 	client;
		ServerList 			serverlist;
		String 				jmxport1;
		String 				jmxport2;
	}
	
	private final Collection<ServerData>			datas = new LinkedList<ServerData>();
	private int 											currentport;

	private ServerConf( String clientxmlfile, String serverlistxml, int aid, int startport, String gsjmxport) throws SAXException, IOException, ParserConfigurationException {
		
		this.currentport = startport;
		
		final Collection<ClientServerData> 	clientdatas = ClientServerData.load( clientxmlfile);
		final Map<String, ServerList>  serverlist = ServerList.load( serverlistxml, aid);

		for( ClientServerData csd : clientdatas) {
			
			ServerList sl = serverlist.get( csd.name);
			if( null == sl) {
				sl = findBySunname( serverlist, csd.name);
				if( null == sl) {
					System.out.println( "lost server [" + csd.name + "]");
					continue;
				}
			}
		
			ServerData		data = new ServerData();
			data.client = csd;
			data.serverlist = sl;
			data.jmxport1 = getNextPortString();
			data.jmxport2 = makeJMXPort2( sl.zoneid, gsjmxport);
			datas.add( data);
		}
	}
	static private ServerList findBySunname( final Map<String, ServerList> serverlist, final String name) {
		for( final String key : serverlist.keySet()) {
			if( -1 != key.indexOf( name))
				return serverlist.get( key);
		}
		return null;
	}
	
	static private String makeJMXPort2( final String a, final String b) {
		int ia = Integer.parseInt( a);
		int ib = Integer.parseInt( b);
		return Integer.toString( ia + ib);
	}

	private final int getNextPort() {
		int port = currentport;
		currentport ++;
		return port;
	}
	
	private final String getNextPortString() {
		return Integer.toString( getNextPort());
	}

	private void saveAsConf( String filename, String gsjmxport) throws FileNotFoundException {

		PrintStream	ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		
		ps.println( "[main]");
		ps.println( "count=" + datas.size());
		ps.println();
		
		int index = 0;
		for( ServerData sd : datas) {
			ps.println( "[server" + ( index ++ ) + "]");
			ps.println( "name=" + sd.client.name);
			ps.println( "ip=" + sd.client.ip);
			ps.println( "port=" + sd.client.port);
			ps.println( "eport=" + sd.client.getLinkEndPort());
			ps.println( "listenjmxport1=" + sd.jmxport1);
			ps.println( "listenjmxport2=" + sd.jmxport2);
			
			ps.println( "gsip=" + sd.serverlist.getGsIP());
			ps.println( "gsjmxport1=" + gsjmxport);
			ps.println( "gsjmxport2=" + sd.jmxport2);
			ps.println();
			
		}
		ps.close();		
	}
	private void saveAsXML( String filename, String gsjmxport) throws Exception {
	
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element rootele = doc.createElement( "data"); 
		doc.appendChild( rootele);

		for( ServerData sd : datas) {
			rootele.appendChild( doc.createTextNode( "\n\t"));
			Element subele = doc.createElement( "server");
			subele.setAttribute( "name", sd.client.name);
			subele.setAttribute( "server", "127.0.0.1");
			subele.setAttribute( "port1", sd.jmxport1);
			subele.setAttribute( "port2", sd.jmxport2);

			subele.setAttribute( "zoneid", sd.serverlist.zoneid);
			subele.setAttribute( "link", sd.client.ip);
			subele.setAttribute( "linkport", sd.client.port + "-" + sd.client.getLinkEndPort());
			subele.setAttribute( "gsip", sd.serverlist.getGsIP());
			subele.setAttribute( "gsjmxport1", gsjmxport);
			subele.setAttribute( "gsjmxport2", sd.jmxport2);
			rootele.appendChild( subele);			
		}
		rootele.appendChild( doc.createTextNode( "\n"));

		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource( doc);
			StreamResult result =  new StreamResult( new BufferedOutputStream( new FileOutputStream( filename)));
			transformer.transform( source, result);
		}
	}
	
	public static void main( String args[]) throws Exception {
		
		if( args.length < 7) {
			System.out.println( "usage : inputclientserverxml inputserverlistxml outlauthcconf outjmxconf startjmxport gsjmxport aid");
			return;
		}
		
		final String gsjmxport = args[5];
		
		ServerConf sconf = new ServerConf( args[0], args[1], Integer.parseInt( args[6]), Integer.parseInt( args[4]), gsjmxport);
		sconf.saveAsConf( args[2], gsjmxport);
		sconf.saveAsXML( args[3], gsjmxport);
	}
}
