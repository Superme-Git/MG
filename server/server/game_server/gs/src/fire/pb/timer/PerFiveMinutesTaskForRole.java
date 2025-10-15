package fire.pb.timer;
import java.util.TimerTask;
/**
 * 人物的体力活力的刷新
 * 每5分钟刷新一次
 * @author nobody
 *
 */
public class PerFiveMinutesTaskForRole extends TimerTask{
	@SuppressWarnings("unused")
	private long roleid;
	
	public PerFiveMinutesTaskForRole(final long roleid){
		this.roleid = roleid;
	}

	@Override
	public void run() {
		//人物的体力活力的刷新
		//new fire.pb.PRefreshRoleTimigData(roleid).submit();	
	}

}
