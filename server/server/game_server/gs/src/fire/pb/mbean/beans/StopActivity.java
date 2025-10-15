package fire.pb.mbean.beans;

import java.util.Map;
import java.util.Set;

import fire.pb.mbean.AbstractRequestHandler;
import fire.pb.timer.AbstractScheduledActivity;
import fire.pb.timer.ActivityManager;

/**
 * 关闭活动的Gm指令
 * @author nobody
 */
public class StopActivity extends AbstractRequestHandler {

	public StopActivity(String name) {
		super(name);
	}

	@Override
	protected Map<Object, Object> handleRequest(Map<?, ?> paras) {
		try{
			
			Object o = paras.get("activityid");
			if(o == null){
				return failedMsg("停止失败：参数不能为null");
			}
			
			int baseid = Integer.parseInt((String) o);
			Set<Integer>  set = ActivityManager.getActivitymap().keySet();
			if(set == null || set.size() == 0){
				return failedMsg("该活动尚未开启");
			}
			
			boolean hasActivity = false;
			boolean stopActivity = false;
			
			for(int actId :  set){
				int temp = actId;
				if(actId > 1000){
					temp = actId/1000;
				}
				if(baseid == temp){
					hasActivity = true;
					stopActivity = stopActivity(actId);
				}
			}
			
			if(hasActivity){
				if(stopActivity){
					return successMsg("活动停止成功:");
				}else{
					return failedMsg("活动停止失败:请联系服务器同学");
				}
			}else{
				return failedMsg("活动停止失败:该活动没有开启");
			}
		}catch (Exception e) {
			return failedMsg("活动停止失败:"+e.getMessage());
		}
	}
	
	private boolean stopActivity(int actId){
		boolean stop = true;
		AbstractScheduledActivity act= ActivityManager.getActivitymap().get(actId);
		if(act == null)
			return true;
		try {
			stop = act.end(true);
			ActivityManager.getActivitymap().remove(actId);
			act = null;
			return stop;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
