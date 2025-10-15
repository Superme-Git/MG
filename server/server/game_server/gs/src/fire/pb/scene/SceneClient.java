package fire.pb.scene;

import fire.pb.map.GsMapThread;

/**
 * 场景模块用于向逻辑模块发送协议
 */
public class SceneClient {

	/**
	 * 向逻辑模块发送协议现在实际是同步调用了协议.
	 * 所以不要在协议里使用xtable.select,可能会堵塞MapThread,尽量包装成Procedure
	 * submit让xdb的线程去执行
	 */
	public static void pSend(mkio.Protocol p1) {
		if (sendToGsMapThread(p1))
			return;
		p1.run();
	}

	/**
	 * 向GsMapThread发送协议，现在GsMapThread处理的协议只有:MUpdateRolePos和MMoveCheckHideBattle
	 */
	private static boolean sendToGsMapThread(mkio.Protocol p) {
		return GsMapThread.getInstance().tryAdd(p);
	}
}
