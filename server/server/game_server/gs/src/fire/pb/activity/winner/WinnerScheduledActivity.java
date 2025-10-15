package fire.pb.activity.winner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.pb.map.SceneManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.activelist.SActivityOpen;
import fire.pb.npc.SGeneralSummonCommand;
import fire.pb.npc.TransmitTypes;
import fire.pb.timer.AbstractScheduledActivity;
import fire.pb.util.MessageUtil;
import fire.pb.util.XdbUtil;


public class WinnerScheduledActivity extends AbstractScheduledActivity {

	public WinnerScheduledActivity(int baseid,int id, long startTime, long endTime) {
		super(baseid,id, startTime, endTime);
	}

	@Override
	protected boolean init() throws Exception {

		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				//发送系统公告
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
				String startTimeStr=simpleDateFormat.format(new Date(activityStartTime+ WinnerManager.WINNER_BROAD_PERIOD1));
				List<String> paras = MessageUtil.getMsgParaList(startTimeStr);
				
				long key1 = MessageUtil.sendRepeatableMsg(140663, activityStartTime, activityStartTime+ WinnerManager.WINNER_BROAD_PERIOD1, WinnerManager.WINNER_BROAD_DELAY1,paras);
				
				long startTime2 = activityStartTime + WinnerManager.WINNER_BROAD_PERIOD1;
				long key2 = MessageUtil.sendRepeatableMsg(140664, startTime2, startTime2 + WinnerManager.WINNER_BROAD_PERIOD2,WinnerManager.WINNER_BROAD_DELAY2,null);
				
				addScheduledTask(key1);
				addScheduledTask(key2);
				

				WinnerManager.getInstance().init(activityStartTime+ WinnerManager.WINNER_BROAD_PERIOD1,false);
				
				SActivityOpen sActivityOpen = new SActivityOpen();
				sActivityOpen.activityid = RoleLiveness.WINNER;
				SceneManager.sendAll(sActivityOpen);
				
				//活动开始后拉人
				ScheduledFuture<?> scheduledFuture = mkdb.Mkdb.executor().schedule(new CallPlayer(), 3000, TimeUnit.MILLISECONDS);
				long key = XdbUtil.addScheduleTask(scheduledFuture);
				addScheduledTask(key);
				
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	protected boolean endActivity() {

		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				WinnerManager.getInstance().stopwinner();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(activityStartTime);
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				mkdb.Mkdb.executor().schedule(new RemoveWinnerNpc(), calendar.getTimeInMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
				return true;
			}

		}.submit();
		return true;
	}

	@Override
	protected boolean stopActivity() {
		
		new mkdb.Procedure() {
			
			@Override
			protected boolean process() throws Exception {
				
				WinnerManager.getInstance().stopwinner();
				SceneNpcManager.removeNpc(WinnerManager.winnerActiveNpc);
				
				return true;
			}
			
		}.submit();
		return true;
	}
	
	@Override
	protected long resumeActivity(boolean gm, long lastStartTime, long lastEndTime, long costTime) throws Exception {
		WinnerManager.getInstance().init(activityStartTime+ WinnerManager.WINNER_BROAD_PERIOD1,true);
		return 2*60*60*1000;
	}
	
	class CallPlayer implements Runnable {
		@Override
		public void run() {
			SGeneralSummonCommand sGeneralSummonCommand=new SGeneralSummonCommand();
			sGeneralSummonCommand.summontype = TransmitTypes.winnercall;
			SceneManager.sendAllByCondition(sGeneralSummonCommand, -1, -1, WinnerManager.MIN_LEVEL, -1,-1);
		}

	}
	
	public class RemoveWinnerNpc implements Runnable {
		@Override
		public void run() {

			SceneNpcManager.removeNpc(WinnerManager.winnerActiveNpc);
		}

	}
	

}
