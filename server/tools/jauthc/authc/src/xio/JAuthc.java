package xio;
/**
 * 	配置信息读取的生成相应的xio.xml文件，生成的xio.xml中不需要authc相关的协议
 * 	相关问题：
 * 	1. Jauthc与xdb无关，只跟xio有关，这里，所有的跟xdb数据库，线程池相关的，都不管理
 * 	k
 */
public class JAuthc 
{
	public static final String DefaultXioXML = "../jauthc/test.xio.xml";
	public static final String DefaultLibPath = "../../snail/bin/";

	public static final String LibSecX86 = "sec_x86.dll";
	public static final String LibSecAmd64 = "libsec_amd64.so";
	
	public static void start()
	{
		start(DefaultXioXML, DefaultLibPath);
	}
	
	public static void start(String xioxml)
	{
		start(xioxml, DefaultLibPath);
	}
	
	public static void close()
	{
		LoginManager.getInstance().closeAll();
		xio.Engine.getInstance().close();
	}
	/**
	 * 
	 * @param xioxml: xio相关的协议配置
	 * @param libpath: sec编解码依赖库的路径
	 */
	public static void start(String xioxml, String libpath)
	{	
//		String seclibname = 
//			System.getProperty("os.name").contains("Win") ?  "sec_amd64.dll" : "libsec_amd64.so";
		//mkdb.util.Dbx.load(new File(libpath), null, seclibname);	
		xio.Engine.getInstance().open();		
		try 
		{
			xio.XioConf.loadAndRegister(xioxml);
		} catch (Exception e1) 
		{
			e1.printStackTrace();
		}	
	}
	
	/** Jauthc的TraceDebug暂时采用mkdb.Trace，所以在打开mkdb.trace的时候，也会打开相应的trace
	 * 
	 */
	public static void openTraceDebug()
	{
		mkdb.Trace.openIf();
		mkdb.Trace.set(mkdb.Trace.DEBUG);
	}
	
	public static void closeTraceDebug()
	{
		mkdb.Trace.close();
	}
	
	public static LoginManager getLoginManager()
	{
		return LoginManager.getInstance();
	}
}
