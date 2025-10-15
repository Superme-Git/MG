package fire.pb.clan.fight;

import java.util.TimerTask;

public class PClanFightAddScroeTask extends TimerTask {

	long clanfightid;
	
	public PClanFightAddScroeTask(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}
	
	@Override
	public void run()
	{
		new PClanFightAddScroe(clanfightid).submit();
	}

}
