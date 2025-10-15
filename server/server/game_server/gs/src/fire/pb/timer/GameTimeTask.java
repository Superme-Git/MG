package fire.pb.timer;
import gnet.link.Onlines;
import fire.log.Logger;
import fire.pb.SGameTime;
import fire.pb.WorldTime;
import fire.pb.util.Parser;

import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GameTimeTask extends TimerTask {
	// 当前时辰 
	private static int curGameTime = -1; 
	private static long gameTimeStartTime = -1;//当前时辰的起始毫秒,用于宝石等的刷新
	static Logger logger = Logger.getLogger("SYSTEM");
	public GameTimeTask(){
		
	}
	public static int getCurGameTime(){
		return curGameTime;
	}
	private static int calcCurGameTime(){
		WorldTime calendar = WorldTime.getInstance();
		
		final int allseconds = calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
		return allseconds/150 % 12;
	}
	
	public static boolean isDay()
	{
		//int time = calcCurGameTime(); 不需要再计算一遍了,用curGameTime即可
		if(curGameTime >= 4 && curGameTime < 10)
			return true;
		else 
			return false;
			
	}
	public static void sendGameTimeToRole(long roleid){
		final SGameTime send = new SGameTime(WorldTime.getInstance().getTimeInMillis()/*System.currentTimeMillis()*/);
		Onlines.getInstance().send(roleid, send);
	}
	@Override
	public void run(){
		final int gameTime = calcCurGameTime();
		logger.info("gametime update.curGameTime:"+curGameTime+"gametime:"+gameTime);
		if (curGameTime != gameTime){
			try {
				curGameTime = gameTime;
				gameTimeStartTime = System.currentTimeMillis();
				final SGameTime send = new SGameTime(WorldTime.getInstance().getTimeInMillis()/*System.currentTimeMillis()*/);
				Onlines.getInstance().broadcast(send);
				if (curGameTime % 2 == 0)
					mkdb.Executor.getInstance().schedule(new TimerTask() {
						@Override
						public void run() {
//							FactionExecutor.getInstance().notifyFactionDailyQuestHighRewardLefttimes();
						}
					}, 1, TimeUnit.SECONDS);
			} catch (Exception e) {
				logger.info("gametime Exception"+Parser.convertStackTrace2String(e));
			}
		}
	}

	public static long getGameTimeStartTime() {

		return gameTimeStartTime;
	}
}
