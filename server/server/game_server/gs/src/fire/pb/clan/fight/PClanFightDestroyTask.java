package fire.pb.clan.fight;

import com.locojoy.base.Runnable;

/***
 * 删除公会战场 by changhao
 */
public class PClanFightDestroyTask extends Runnable
{
	long clanfightid;
	
	public PClanFightDestroyTask(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}


	@Override
	public void run()
	{
		new PClanFightDestroy(clanfightid).submit();
	}

}
