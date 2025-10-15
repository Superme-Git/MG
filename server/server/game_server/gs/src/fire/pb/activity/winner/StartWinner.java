package fire.pb.activity.winner;



import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.pb.map.SceneManager;
import fire.pb.npc.SGeneralSummonCommand;
import fire.pb.npc.TransmitTypes;
import fire.pb.util.XdbUtil;


public class StartWinner extends Thread {
    private final long time;
	public StartWinner(long currentTimeMillis) {
      this.time = currentTimeMillis;
	}
	@Override
	public void run() {
	  new mkdb.Procedure(){
		@Override
		protected boolean process() throws Exception {
			WinnerManager.getInstance().init(time,true);
			StopWinner stopWinner = new StopWinner();
			ScheduledFuture<?> future = mkdb.Mkdb.executor().schedule(stopWinner, 7200000, TimeUnit.MILLISECONDS);
			long futureid = XdbUtil.addScheduleTask(future);
			stopWinner.setFutureKey(futureid);
			//冠军试炼拉人
			SGeneralSummonCommand sGeneralSummonCommand=new SGeneralSummonCommand();
			sGeneralSummonCommand.summontype = TransmitTypes.winnercall;
			SceneManager.psendAllByConditionWhileCommit(sGeneralSummonCommand, -1, -1, WinnerManager.MIN_LEVEL, -1);
			return true;
		}
	  }.submit();
	}

}

