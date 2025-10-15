package robot;

import mkio.ILoginIns;
import mkio.Protocol;

/**
 * 把协议放到角色对应任务的协议队列中
 * 
 * @author yebin
 *
 */
public class ProtocolMgr {

	public static void protocol2Queue(Protocol p) {
		ILoginIns ins = (ILoginIns) p.getContext();
		LoginUI ui = (LoginUI) ins.getLoginUI();
		ui.getLoginRole().onRoleAddProtocol(p);
	}
}
