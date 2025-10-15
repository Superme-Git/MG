


import mkio.ILoginUI;
import mkio.Manager;
import mkio.Protocol;
import mkio.Mkio;

public class LoginUI implements ILoginUI 
{
	
	public Object monitor;
	private boolean ok;
	
	LoginUI()
	{
		monitor = new Object();
		ok = false;
	}
	
	@Override
	public void onAnnounceForbidInfo(char type, int time, int createtime,
			String reason) 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void onInstantAddCashRep(int retcode, int userid, int reserved) 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void dispatch(Manager manager, Mkio connection, Protocol p) 
	{
		mkdb.Xdb.executor().execute(p);
	}

	@Override
	public void onAuthError(Action a, int e, String detail) 
	{
		synchronized (monitor) {
		mkdb.Trace.error("OnAuthError: Action: " + a + " Detail:" + detail + " Errcode: " + e);
		ok = false;
		monitor.notifyAll();
		}
		
	}

	@Override
	public void onAuthHandSet(int num) 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void onAuthMatrixCard(int[] x, int[] y) 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void onAuthOk(int userid) 
	{
		synchronized (monitor) {
			mkdb.Trace.debug("OnAuthOK , UserID is " + userid);
			ok = true;
			monitor.notifyAll();
		}
	}
	
	public boolean isAuthOK()
	{
		return ok;
	}

}
