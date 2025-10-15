package robot;

import fire.pb.CRoleList;
import gnet.KeepAlive;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import mkio.ILoginIns;
import mkio.Protocol;

/**
 * 登录排队
 * 
 * @author yangtao
 * @dateTime 2016年5月25日 下午2:46:57
 * @version 1.0
 */
public class QueueUtils {

	// private static ThreadLocal<Integer> isPrint = new ThreadLocal<>();
	private static QueueUtils _instance = new QueueUtils();

	public static QueueUtils getInstance() {
		return _instance;
	}

	/**
	 * 玩家实例对应心跳定时器
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月25日 下午2:47:16
	 * @version 1.0
	 */
	public Map<ILoginIns, ScheduledFuture<?>> iLoginIns2ScheduledFutureMap = new ConcurrentHashMap<ILoginIns, ScheduledFuture<?>>();

	/**
	 * 玩家实例对应玩家排队倒计时定时器
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月25日 下午2:47:36
	 * @version 1.0
	 */
	public Map<ILoginIns, ScheduledFuture<?>> iLoginIns2ScheduledFuture1Map = new ConcurrentHashMap<ILoginIns, ScheduledFuture<?>>();

	/**
	 * 设置登录排队
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月24日 下午5:26:21
	 * @version 1.0
	 * @param p
	 * @param second
	 */
	public void setQueueInfo(Protocol p, int second, int order, int queuelength) {
		ILoginIns ins = (ILoginIns) p.getContext();
		// if (isPrint.get() == null) {
		// isPrint.set(1);
		// System.out.println("----------order:" + order + "-------queuelength" + queuelength + "--second:" + second);
		// }
		// 存在相同的就重置
		stopScheduledFuture(ins);
		// 为了避免没有给服务器发送心跳掉线
		ScheduledFuture<?> scheduledFuture = mkdb.Executor.getInstance().scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				KeepAlive ka = new KeepAlive();
				ka.code = (int) (System.currentTimeMillis() / 1000);
				ins.send(ka);
			}
		}, 2, 5, TimeUnit.SECONDS);
		// 倒计时结束调用
		ScheduledFuture<?> scheduledFuture1 = mkdb.Executor.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				ins.send(new CRoleList());
			}
		}, second, TimeUnit.SECONDS);
		iLoginIns2ScheduledFutureMap.put(ins, scheduledFuture);
		iLoginIns2ScheduledFuture1Map.put(ins, scheduledFuture1);
	}

	/**
	 * 设置登录排队
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月24日 下午5:26:21
	 * @version 1.0
	 * @param p
	 * @param second
	 */
	public void setQueueInfo(Protocol p) {
		ILoginIns ins = (ILoginIns) p.getContext();
		// if (isPrint.get() == null) {
		// isPrint.set(1);
		// System.out.println("----------order:" + order + "-------queuelength" + queuelength + "--second:" + second);
		// }
		// 存在相同的就重置
		stopScheduledFuture(ins);
		// 为了避免没有给服务器发送心跳掉线
		ScheduledFuture<?> scheduledFuture = mkdb.Executor.getInstance().scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				KeepAlive ka = new KeepAlive();
				ka.code = (int) (System.currentTimeMillis() / 1000);
				ins.send(ka);
			}
		}, 2, 5, TimeUnit.SECONDS);
		iLoginIns2ScheduledFutureMap.put(ins, scheduledFuture);
	}

	/**
	 * 停止定时器
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月24日 下午5:30:29
	 * @version 1.0
	 * @param ins
	 */
	public void stopScheduledFuture(ILoginIns ins) {
		if (ins == null)
			return;
		ScheduledFuture<?> scheduledFuture = iLoginIns2ScheduledFutureMap.remove(ins);
		if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
			scheduledFuture.cancel(true);
		}
		ScheduledFuture<?> scheduledFuture1 = iLoginIns2ScheduledFuture1Map.remove(ins);
		if (scheduledFuture1 != null && !scheduledFuture1.isCancelled()) {
			scheduledFuture1.cancel(true);
		}
	}
}
