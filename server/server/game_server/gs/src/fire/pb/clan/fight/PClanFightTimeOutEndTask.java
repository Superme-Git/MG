package fire.pb.clan.fight;

import com.locojoy.base.Runnable;

/***
 * 公会战场结束定时器 by changhao
 */
public class PClanFightTimeOutEndTask extends Runnable
{
	long clanfightid;
	
	public PClanFightTimeOutEndTask(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}

	@Override
	public void run()
	{
		new PClanFightTimeOutEnd(clanfightid).submit();
	}

}
