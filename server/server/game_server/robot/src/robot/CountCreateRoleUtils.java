package robot;

import java.util.concurrent.atomic.AtomicInteger;



/**
 * @author yt
 * 统计创建角色（创建一定数量的用户需要的时间）
 *
 */
public class CountCreateRoleUtils {
	
	/**
	 * 开始创建的初始数量
	 */
	public static AtomicInteger currentStartCreateNum = new AtomicInteger(0);

	/**
	 * 已经创建的数量
	 */
	public static AtomicInteger currentCreateNum = new AtomicInteger(1);

	/**
	 * 增加创建数量
	 */
	public static void addCreateNum() {
		currentCreateNum.addAndGet(1);
	}
	

	/**
	 * 增加创建数量
	 */
	public static void addStartCreateNum() {
		currentStartCreateNum.addAndGet(1);
	}

}
