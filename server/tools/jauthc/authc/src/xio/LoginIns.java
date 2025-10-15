/**
 * 
 */
package xio;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import xio.Protocol;

/**
 *
 */
public class LoginIns implements ILoginIns 
{
	private Param _param;
	private ILoginUI _callback;
	private LoginManager _manager;
	private xio.Connector _connector;
	private xio.Xio _xio;
	private boolean _close;
	
	//
	public gnet.ServerAttr serverattr;
	public boolean saready;
	
	public LoginIns(Param param, ILoginUI callback, LoginManager loginManager) 
	{
		_param = new Param(param);
		_callback = callback;
		_manager = loginManager;
		_close = false;
		_xio = null;
	}
	
	public void setXio(xio.Xio io)
	{
		_xio = io;
	}
	
	public xio.Xio getXio()
	{
		return _xio;
	}
	
	public ILoginUI getLoginUI()
	{
		return _callback;
	}
	
	/* (non-Javadoc)
	 * @see src.ILoginIns#InstantAddCash(java.lang.String, java.lang.String)
	 */
	@Override
	public void instantAddCash(String cardnum, String cardpasswd)
	{

	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#IsCanEnterWorld()
	 */
	@Override
	public boolean isCanEnterWorld() 
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#close()
	 */
	@Override
	public void close() 
	{
		if (!_close)
		{
			_connector.close();
			_xio.close();
			_close = true;
		}
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#getAddiction(src.ILoginIns.Addiction)
	 */
	@Override
	public boolean getAddiction(Addiction adict) 
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#getHostAddress()
	 */
	@Override
	public String getHostAddress() 
	{
		return _param.host;
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#getSecureLevel()
	 */
	@Override
	public Algorithm getSecureLevel() 
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#getServerInfo(src.ILoginIns.ServerInfo)
	 */
	@Override
	public boolean getServerInfo(ServerInfo info) 
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#getServerPing()
	 */
	@Override
	public int getServerPing() 
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#getServerVersion()
	 */
	@Override
	public int getServerVersion() 
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#send(xio.Protocol)
	 */
	@Override
	public boolean send(Protocol p) 
	{
		//_manager.getStatisis().incSendProType(p.getType());
		return p.send(_xio);
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#sendHandSetResponse(int)
	 */
	@Override
	public void sendHandSetResponse(int num) 
	{
	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#sendMatrixCardResponse(int[])
	 */
	@Override
	public void sendMatrixCardResponse(int[] v) 
	{

	}

	/* (non-Javadoc)
	 * @see src.ILoginIns#setupKeepAlive(int)
	 */
	@Override
	public void setupKeepAlive(int period) 
	{

	}

	public void start(int inputBufferSize, int outputBufferSize, 
			int receiveBufferSize, int sendBufferSize)
	{
		LoginManager.InputBufferSize = Integer.toString(inputBufferSize);
		LoginManager.OutputBufferSize = Integer.toString(outputBufferSize);
		LoginManager.ReceiveBufferSize = Integer.toString(receiveBufferSize);
		LoginManager.SendBufferSize = Integer.toString(sendBufferSize);
		start();
	}
	
	/* (non-Javadoc)
	 * @see src.ILoginIns#start()
	 */
	@Override
	public void start() 
	{
		//偷懒一下，直接用Connector
		
		Document doc = null;
		try 
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e1) 
		{
			e1.printStackTrace();
			return;
		}
		
		org.w3c.dom.Element self = doc.createElement("creator");
				
		
		self.setAttribute("inputBufferSize", LoginManager.InputBufferSize);
		self.setAttribute("outputBufferSize", LoginManager.OutputBufferSize);		
		self.setAttribute("receiveBufferSize", LoginManager.ReceiveBufferSize);
		self.setAttribute("sendBufferSize", LoginManager.SendBufferSize);
		self.setAttribute("tcpNoDelay", LoginManager.TcpNoDelay);
		
		self.setAttribute("remotePort", _param.port);
		self.setAttribute("remoteIp",  _param.host);
		self.setAttribute("reconnect",  "false");
		
		try 
		{
			_connector = xio.Connector.create(_manager, self);
			_connector.open();
			mkdb.Trace.debug("Connect OPEN: " + this.getParam().username);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		_close = false;
	}

	public xio.Connector getConnector() 
	{
		return _connector;
	}
	
	public Param getParam()
	{
		return _param;
	}

	public boolean checkVersion(int version) 
	{
		//!param.isCheckVersion || version == param.version;
		return (!_param.isCheckVersion || version == _param.version);
	}

 }
