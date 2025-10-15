package robot.task;

import gnet.KeepAlive;

/**
 * 全局调用的任务
 * 
 * @author Bill_Ye
 */
public class Global {
	public static void DoGlobalTask(Task_RoleBase task) {
		SendTickMsg(task); // 发心跳
		//SendLockScreenMsg(task); // 测锁屏
	}

	/**
	 * 发送锁屏消息
	 * 
	 * @param role
	 * @param curStatus
	 * @return
	 */
	public static void SendLockScreenMsg(Task_RoleBase task) {
		if (Math.random() < 0.95) {
			return;
		}

		task.role.sendCommand("//lockscreen "
				+ (task.isLockScreen() ? "0" : "1"));
		task.setLockScreen(!task.isLockScreen());
	}

	/**
	 * 发送心跳包
	 * 
	 * @param role
	 * @param ticknum
	 * @return
	 */
	public static void SendTickMsg(Task_RoleBase task) {
		if (task.getCurTickNum() % 8 == 0) {
			KeepAlive ka = new KeepAlive();
			ka.code = (int) (System.currentTimeMillis() / 1000);
			task.role.sendProtocol(ka);
		}
		task.setCurTickNum(task.getCurTickNum() + 1);
	}
}
