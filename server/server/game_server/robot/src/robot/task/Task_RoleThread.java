package robot.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import robot.LoginRole;
import robot.LoginRoleMgr;

/**
 * 执行RoleTask的线程
 */
public class Task_RoleThread extends mkdb.ThreadHelper {
	private static Task_RoleThread instance;

	public static synchronized Task_RoleThread getInstance() {
		if (null == instance) {
			instance = new Task_RoleThread();
		}
		return instance;
	}

	public Task_RoleThread() {
		super("RoleTaskThread");
	}

	@Override
	public void run() {
		while (isRunning()) {
			try {
				Set<Long> roleidsets = LoginRoleMgr.getInstance().getRoles()
						.keySet();
				if (roleidsets.isEmpty())
					continue;
				List<Long> roleids = new ArrayList<Long>(roleidsets.size());
				roleids.addAll(roleidsets);
				for (long roleId : roleids) {
					try {
						LoginRole role = LoginRoleMgr.getInstance()
								.getLoginRole(roleId);
						if (role == null)
							continue;
						if (role.getRoleTask() == null)
							continue;
						role.runRoleTask();
					} catch (Exception e) {
						mkdb.Trace.error("", e);
					}
				}
				// if(RandomZhengyouTask.isZhengyouRun &&
				// RandomZhengyouTask.getSendNum() >= roleids.size() * 0.8){
				// System.exit(0); //90%的机器人都发送了征友协议之后突然掉线，突然下线（为了测一个bug）
				// }
				sleepIdle(1000);
			} catch (Exception e) {
				mkdb.Trace.error("", e);
			}
		}
	}

}
