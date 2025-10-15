package fire.pb.clan.fight;

import com.locojoy.base.Runnable;

/***
 * 销毁胜利宝箱定时器 by changhao
 */
public class PClanFightDestroyRefreshBoxTask extends Runnable
{
	long clanfightid;
	
	public PClanFightDestroyRefreshBoxTask(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}

	@Override
	public void run()
	{
		mkdb.Procedure p = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
				
				if (bf != null)
				{
					bf.destroyVictoryBox();
				}
				
				return true;
			}
		};
		
		p.submit();
	}

}
