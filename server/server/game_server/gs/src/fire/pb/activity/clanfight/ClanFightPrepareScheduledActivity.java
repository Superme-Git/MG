package fire.pb.activity.clanfight;

import fire.pb.timer.AbstractScheduledActivity;

/**
 * 公会战活动准备by changhao
 */
public class ClanFightPrepareScheduledActivity extends AbstractScheduledActivity
{
	public ClanFightPrepareScheduledActivity(int baseid,int id, long startTime, long endTime)
	{
		super(baseid,id, startTime, endTime);
	}
	
	@Override
	protected boolean init() throws Exception
	{
		ActivityClanFightManager.getInstance().activityInit(); //建立公会战 对战表 by changhao
		return true;
	}
	
	@Override
	protected long resumeActivity(boolean gm, long lastStartTime, long lastEndTime, long costTime) throws Exception
	{
		long cur = java.util.Calendar.getInstance().getTimeInMillis();
		if(activityEndTime > cur)
		{
			//ActivityClanFightManager.getInstance().start();	
		}
		
		return activityEndTime - cur;
	}
	
	@Override
	protected boolean stopActivity()
	{
		//ActivityClanFightManager.getInstance().stop(id);
		return true;
	}
	
	@Override
	protected boolean endActivity()
	{
		//ActivityClanFightManager.getInstance().stop(id);
		return true;
	}
}
