package robot.task;

import robot.LoginRole;

/**
 * 角色任务的基础类 角色任务是挂在每个机器人角色身上的独立执行的任务 默认每隔一秒由RoleTaskThread线程遍历所有角色执行其身上的任务
 */
public abstract class Task_RoleBase implements Runnable {

	public final LoginRole role;

	public boolean isLockScreen() {
		return isLockScreen;
	}

	public void setLockScreen(boolean isLockScreen) {
		this.isLockScreen = isLockScreen;
	}

	public long getCurTickNum() {
		return curTickNum;
	}

	public void setCurTickNum(long curTickNum) {
		this.curTickNum = curTickNum;
	}

	protected boolean isLockScreen = false;
	protected long curTickNum = 0;

	public Task_RoleBase(LoginRole role) {
		this.role = role;
	}

	/**
	 * 任务执行可能有的参数
	 */
	public String args;

	public void setArgs(String args) {
		this.args = args;
	}

	/**
	 * 开始执行任务时的预处理
	 */
	public abstract void start();

	/**
	 * 结束任务时的处理
	 */
	public abstract void stop();

	/**
	 * 如果有的协议需要该任务特殊处理，调用此接口 注意：该接口需要自行在任务的process中主动调用
	 */
	public abstract void processProtocol(mkio.Protocol p);
}
