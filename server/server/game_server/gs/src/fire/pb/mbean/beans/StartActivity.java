package fire.pb.mbean.beans;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fire.pb.main.ConfigManager;
import fire.pb.mbean.AbstractRequestHandler;
import fire.pb.timer.AbstractScheduledActivity;
import fire.pb.timer.ActivityEndThread;
import fire.pb.timer.ActivityManager;
import fire.pb.timer.ActivityStartThread;

/**
 * 开启活动的gm指令
 * @author nobody
 */
public class StartActivity extends AbstractRequestHandler {

	public StartActivity(String name) {
		super(name);
	}

	@Override
	protected Map<Object, Object> handleRequest(Map<?, ?> paras) {

		try{
			Object id = paras.get("activityid");
			Object t = paras.get("time");
			if(id == null || t == null){
				return failedMsg("活动开启失败:参数不能为null");
			}
			int baseid = Integer.parseInt((String)id );
			int timeMinite = Integer.parseInt((String) t);
			int actId = baseid*1000+1;
			Properties prop = ConfigManager.getInstance().getPropConf("activity");
			if(prop == null){
				return failedMsg("活动开启失败:服务器中没有该活动的配置activity");
			}
			String className = prop.getProperty(String.valueOf(baseid));
			if(className == null){	
				return failedMsg("活动开启失败:服务器中没有该活动的配置");
			}
			if(timeMinite <= 0){
				return failedMsg("活动开启失败:时间为="+timeMinite);
			}
			long startTime = System.currentTimeMillis();
			long endTime = startTime + timeMinite * 60 * 1000;
			
			Set<Integer>  set = ActivityManager.getActivitymap().keySet();
			
			if(set != null && set.size() > 0){
				for(int tempId : ActivityManager.getActivitymap().keySet()){
					if(tempId/1000 == baseid){
						return failedMsg("活动开启失败:不能重复开启");
					}
				}
			}

			@SuppressWarnings("unchecked")
			Constructor<? extends AbstractScheduledActivity> con = (Constructor<? extends AbstractScheduledActivity>) Class.forName(className).getConstructor(
					int.class, int.class ,long.class, long.class);
			AbstractScheduledActivity activityInstance = con.newInstance(baseid ,actId, startTime, endTime);

			ActivityManager.getActivitymap().put(actId, activityInstance);
			mkdb.Mkdb.executor().schedule(new ActivityStartThread(actId,endTime - startTime), 1000, TimeUnit.MILLISECONDS);
			mkdb.Mkdb.executor().schedule(new ActivityEndThread(actId,true), endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
			return successMsg("活动开启成功");
			
		}catch (Exception e) {
			return failedMsg("活动开启失败:"+e.getMessage());
		}
	}

}
