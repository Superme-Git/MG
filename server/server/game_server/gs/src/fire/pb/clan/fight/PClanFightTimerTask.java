package fire.pb.clan.fight;

import java.util.TimerTask;

public class PClanFightTimerTask extends TimerTask {

	long clanfightid;
	
	public PClanFightTimerTask(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}
	
	@Override
	public void run()
	{
		new PClanFightTimer(clanfightid).submit();
	}

}
