
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mkdb.Executor;
import mkio.ILoginIns;
import mkio.JAuthc;
import mkio.LoginManager;

/**
 * 
 */

/**
 * @author DevUser
 *
 */
public class TestAuthcOk 
{

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		try 
		{
			Runtime.getRuntime().exec("cmd /c del xdb\\mkdb.inuse").waitFor();
		} catch (Exception e2) 
		{
			e2.printStackTrace();
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		JAuthc.start();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		JAuthc.close();;
	}
	
			
	@Test
	public void testOnAuthcOK()
	{
	
		JAuthc.openTraceDebug();
		mkdb.Trace.set(mkdb.Trace.DEBUG);
		//mkdb.Xdb.getInstance().setConf(new XdbConf("./mkdb.xml"));
		Executor.start(0, 50, 50, 1, 2000);
		
		ILoginIns.Param param = new ILoginIns.Param(); 	
		param.host = "172.16.0.72";
		param.port = "30010";
		param.username = "nvwa2001";
		param.password = "123456";
		param.iskickuser = true;
		
		LoginUI ui = new LoginUI();
		
		ILoginIns ins = JAuthc.getLoginManager().newLogin(param, ui);
		ins.start();
		synchronized (ui.monitor) {
			if (!ui.isAuthOK())
			{
				try {
					ui.monitor.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Assert.assertTrue(ui.isAuthOK());
		
		LoginManager.getInstance().close(ins);
	}

}
