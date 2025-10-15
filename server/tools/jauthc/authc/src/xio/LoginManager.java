/**
 * 
 */
package xio;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xio.Connector;
import xio.ILoginUI.Action;
import xio.Manager;
import xio.Protocol;
import xio.Xio;
import xio.Protocol.Stub;

/**
 *
 */
public class LoginManager extends xio.Manager 
{
	private Map<xio.Xio, LoginIns> 
		_session = new ConcurrentHashMap<xio.Xio, LoginIns>();
	private List<LoginIns>
		_tmpNewIns = new LinkedList<LoginIns>();
	
	static LoginManager s_instance = null;
	
	static String InputBufferSize = "102468";
	static String OutputBufferSize = "102468";
	static String ReceiveBufferSize = "65535";
	static String SendBufferSize = "65535";
	static String TcpNoDelay = "false";
	
	
	static public LoginManager getInstance()
	{
		return s_instance;
	}
	
	public LoginManager()
	{
		if (s_instance == null) 
		{
			s_instance = this;
		}
	}
	
	public void execute(Protocol p)
	{
		
		LoginIns ins = this.getLoginBySession(p.getConnection()); 
		if( ins == null )
		{
			mkdb.Trace.error("Can't get the login Instance by Xio Session");
			return;
		}
		else
		{
			this.dispatch(ins, p);
		}
	}
	
	public ILoginIns newLogin(ILoginIns.Param param, ILoginUI callback)
	{
		LoginIns newLogin = new LoginIns(param , callback, this);
		synchronized (_tmpNewIns) 
		{
			_tmpNewIns.add(newLogin);	
		}
		return newLogin;
	}
	
	public void close(ILoginIns ins)
	{
		ins.getXio().close();
	}
	
	public LoginIns getLoginBySession(Integer id)
	{
		return _session.get(id);
	}
	
	public LoginIns getLoginBySession(xio.Xio xio)
	{
		return _session.get(xio);
	}
	
	protected
		void onAddSession(ILoginIns session)
	{
		mkdb.Trace.info("add new Session: [user]:" + session.getParam().username);
	}
	
	protected 
		void onDelSession(ILoginIns session, Throwable e)
	{
		session.getLoginUI().onAuthError(Action.eNet, 0, "Connection Failed!!");
		mkdb.Trace.error("del one Session: [user]:" + session.getParam().username + " [Error]: " + e.toString());
	}
	
	protected 
		void dispatch(LoginIns ins, xio.Protocol p)
	{
		ins.getLoginUI().dispatch(this, ins.getXio(), p);
	}

	@Override
	protected void addXio(Xio xio) 
	{
		Connector con = (Connector) (xio.getCreator());
		
		LoginIns tmp = null;
		
		synchronized (_tmpNewIns)//锁掉_tmpNewIns 
		{
			for (LoginIns ins : _tmpNewIns)
			{
				Connector ccon = ins.getConnector(); 
				if (ccon != null && ccon.equals(con))			
				{				
					ins.setXio(xio);				
					_tmpNewIns.remove(ins);
					tmp = ins;
					break;
				}
			}
		}
		
		if (tmp != null)
		{
			this.onAddSession(tmp);
			_session.put(xio, tmp);
		}
		else
		{
			mkdb.Trace.error("creat an unknow session???");
			xio.close();
		}
	}

	@Override
	public Xio get() 
	{
		return null;
	}

	@Override
	protected void removeXio(Xio xio, Throwable e) 
	{
		if (e == null)
		{
			e = new Throwable("Reset by peer without Exception");
		}
		
		LoginIns ins = _session.get(xio);
		
		if (ins == null)
		{
			mkdb.Trace.error("the session have been remove?");
			xio.close();
			return;
		}
		this.onDelSession(ins, e);
		ins.close();
		_session.remove(xio);
	}

	@Override
	public int size() 
	{
		return _session.size();
	}

	protected void doAuthcParse() throws Exception
	{
		final String CONF = "/authc/authc.xio.xml";

		//jar包获取
		InputStream is = this.getClass().getResourceAsStream(CONF);
		if (is == null)
		{
			//尝试文件获取
			is = new FileInputStream("." + CONF);		
		}
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		NodeList childnodes = doc.getDocumentElement().getElementsByTagName("Protocol");
		
		for (int i = 0; i < childnodes.getLength(); ++i) 
		{
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType()) 
			{
				continue;
			}
			Element e = (Element) node;
			((Protocol.Coder)(this.coder)).add(new Stub(e));
		}		

	}
	
	protected void parse(Element self) throws Exception 
	{
		this.name = self.getAttribute("name");
		if (this.name.isEmpty()) 
		{
			throw new RuntimeException("Manager need a name");
		}

		String tmp = self.getAttribute("maxSize");
		this.maxSize = 0;
		if (!tmp.isEmpty())
		{
			this.maxSize = Integer.parseInt(tmp);
		}

		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) 
		{
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType()) 
			{
				continue;
			}

			Element e = (Element) node;
			String n = e.getNodeName();
			
			if (n.equals("Connector"))
			{
				InputBufferSize = e.getAttribute("inputBufferSize");
				OutputBufferSize = e.getAttribute("outputBufferSize");
				SendBufferSize = e.getAttribute("sendBufferSize");
				ReceiveBufferSize = e.getAttribute("receiveBufferSize");
				TcpNoDelay = e.getAttribute("tcpNoDelay");
			}
			if (n.equals("Coder"))
			{
				add(Manager.Coder.create(this, e));
			}
		}
		//处理authc的协议 
		doAuthcParse();
	}

	public void setConf(String conf)
	{
		// 主要用来添加Coder
		Document doc;
		try 
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conf);				
			NodeList childnodes = doc.getDocumentElement().getElementsByTagName("Manager");
		
			for (int i = 0; i < childnodes.getLength(); ++i) 
			{
				Node node = childnodes.item(i);
				if (Node.ELEMENT_NODE != node.getNodeType()) 
				{
					continue;
				}
				Element e = (Element) node;
				String n = e.getAttribute("name");
			
				if (n.equals("LinkClient"))
				{
					this.parse(e);
				} else 
				{
					throw new RuntimeException("Unkown! node=" + n);
				}
			}		
		} catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		
	}

	public void closeAll() 
	{
		for ( LoginIns ins : _session.values() )
		{
			this.close(ins);
		}
		
		_session.clear();
		synchronized (_tmpNewIns)//锁掉_tmpNewIns 
		{
			_tmpNewIns.clear();
		}
		
	}

}
