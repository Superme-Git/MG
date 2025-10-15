/**
 * 
 */
package fire.pb.lock.constants;

/**
 * 安全锁相关的常量定义
 * @author nobody
 *
 */
public class LockConstants {

	/** 解锁有效时间 */
	public static final long UNLOCK_TIME = 3 * 60 * 60 * 1000;
	/** 强行解锁有效时间 */
	public static final long FORCE_UNLOCK_TIME = 7 * 24 * 60 * 60 * 1000;
	/** 连续输入密码错误不能再输入密码的有效时间 */
	public static final long ERROR_PASSWORD_TIME = 10 * 60 * 1000;
	/** 连续输入密码错误的次数，达到该次数一定时间内不能输入密码 */
	public static final int ERROR_PASSWORD_TIMES = 5;
	/** 未设置安全锁状态 */
	public static final int CANCEL_LOCK = 0;
	/** 未解锁状态 */
	public static final int NOT_UNLOCK = 1;
	/** 已解锁状态 */
	public static final int HAVE_UNLOCK = 2;
	/** 强行解锁需要的符石数 */
	public static final int FORCE_UNLOCK_NEED_GOLD = 200;
}
