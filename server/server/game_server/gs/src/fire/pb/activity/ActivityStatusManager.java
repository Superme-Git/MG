package fire.pb.activity;
/**
 * 服务器活动开关管理器
 * @author nobody
 *
 */
public class ActivityStatusManager {
	private static ActivityStatusManager instance = new ActivityStatusManager();
	private ActivityStatusManager(){}
	
	public static ActivityStatusManager getInstance(){
		return instance;
	}
}
