

package fire.pb.activity.award;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.skill.liveskill.LiveSkillManager;



public class Module implements ModuleInterface {

	@Override
	public void exit() {

	}

	@Override
	public void init() throws Exception {

		RewardMgr.getInstance().init();
		LiveSkillManager.getInstance().Init();
		//清除上限的线程
		Calendar cal = Calendar.getInstance();
		final long now = cal.getTimeInMillis();
		cal.add(Calendar.HOUR_OF_DAY, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		final long mtill = cal.getTimeInMillis();
		long delay = mtill - now;
		mkdb.Executor.getInstance().scheduleAtFixedRate(new ClearAwardItemLimit(), delay, 3600000, TimeUnit.MILLISECONDS);
	}

	@Override
	public ReloadResult reload() throws Exception {
       try {
    	   RewardMgr.getInstance().init();
    	   return new ReloadResult(true);
	  } catch (Exception e) {
		RewardMgr.logger.error(e);
		return new ReloadResult(false, "reload award failed");
	  }
	
	}

}

