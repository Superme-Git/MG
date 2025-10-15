package fire.pb.clan.fight;

import java.util.TimerTask;

public class PClanFightLeaveClanFieldBattleFieldTask extends TimerTask {

	long clanfightid;
	long roleid;
	boolean tran;
	boolean dismiss;
	
	public PClanFightLeaveClanFieldBattleFieldTask(long clanfightid, long roleid, boolean tran, boolean dismiss)
	{
		this.clanfightid = clanfightid;
		this.roleid = roleid;
		this.tran = tran;
		this.dismiss = dismiss;
	}
	
	@Override
	public void run()
	{
		new PClanFightLeaveClanFieldBattleField(clanfightid, roleid, tran, dismiss).submit();
	}

}
