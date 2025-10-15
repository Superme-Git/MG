package robot.task;

import fire.pb.CReturnToLogin;
import fire.pb.SReturnLogin;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import robot.LoginRole;
import mkio.Protocol;

/**
 * 上下线测试
 * 
 * @author Bill_Ye
 */
public class OnlineOffline extends Task_RoleBase {
	private BlockingQueue<Protocol> protocols = new LinkedBlockingQueue<Protocol>();

	public OnlineOffline(LoginRole role) {
		super(role);
	}

	private long offlineTime = System.currentTimeMillis();
	private boolean isSend = false;

	@Override
	public void run() {
		// 发送心跳包
		Global.DoGlobalTask(this);

		dealProtocols();
		long curTime = System.currentTimeMillis();
		if (!isSend && (curTime - offlineTime > 5000)) {
			CReturnToLogin cReturnToLogin = new CReturnToLogin();
			role.sendProtocol(cReturnToLogin);
			isSend = true;
			offlineTime = System.currentTimeMillis();
		}
	}

	@Override
	public void start() {
		System.out.println("roleId=" + role.roleId + " 已经上线");
	}

	@Override
	public void stop() {
		isSend = false;
		role.loginui.reconnect();
		System.out.println("roleId=" + role.roleId + " 准备重新连接服务器");
	}

	@Override
	public void processProtocol(Protocol p) {
		// if(p.str().indexOf("SReturnLogin") != -1){
		// System.out.println("协议名称" + p.str());
		// }
		synchronized (protocols) {
			protocols.offer(p);
		}
	}

	private void dealProtocols() {
		synchronized (protocols) {
			for (mkio.Protocol p : protocols) {
				dealProtocol(p);
			}
			protocols.clear();
		}
	}

	protected void dealProtocol(mkio.Protocol p) {
		if (p instanceof SReturnLogin) {
			role.loginui.onDisconnected();
			offlineTime = System.currentTimeMillis();
		}
	}
}
