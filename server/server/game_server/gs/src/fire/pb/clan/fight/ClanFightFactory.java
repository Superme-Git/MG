package fire.pb.clan.fight;

import fire.pb.instancezone.conf.InstanceZoneConfig;
import mkdb.Transaction;
import fire.pb.instancezone.*;
import fire.pb.main.ConfigManager;

/***
 * 公会战 创建工厂 by changhao
 */
public abstract class ClanFightFactory
{	
	/***
	 * 得到公会战场数据 by changhao
	 * @param clanfightid
	 * @param readonly
	 * @return
	 */
	public static ClanFightBattleField getClanFightBattleField(long clanfightid, boolean readonly)
	{
		if (clanfightid <= 0)
		{
			return null;
		}
		
		xbean.ClanFightBattleField battlefield = null;
		if (!readonly)
		{
			if (Transaction.current() == null)
			{
				return null;
			}
			
			battlefield = xtable.Clanfightbattlefield.get(clanfightid);
		}
		else
		{
			battlefield = xtable.Clanfightbattlefield.select(clanfightid);
		}
		
		if (battlefield == null)
		{
			return null;
		}

		fire.pb.clan.SClanFight config = ConfigManager.getInstance().getConf(fire.pb.clan.SClanFight.class).get(battlefield.getBattlefieldid());		
		
		if (config == null)
		{
			return null;
		}

		try
		{
			ClanFightBattleField bf = new ClanFightBattleField(clanfightid, battlefield, config, readonly);
			return bf;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static ClanFightBattleField createClanFightBattleField(final int battlefieldid, final long clanfightid, long clanid1, long clanid2, String clanname1, String clanname2, long datetime)
	{
		fire.pb.clan.SClanFight config = ConfigManager.getInstance().getConf(fire.pb.clan.SClanFight.class).get(battlefieldid);		

		if (config == null)
		{
			fire.pb.activity.clanfight.ActivityClanFightManager.logger.error("[" + battlefieldid + "] createClanFightBattleField配置错误");
			return null;
		}

		xbean.ClanFightBattleField battlefield = xbean.Pod.newClanFightBattleField();
		battlefield.setBattlefieldid(battlefieldid);
		battlefield.setClanfightid(clanfightid);
		battlefield.setState(xbean.ClanFightBattleField.STATE_CLAN_FIGHT_NORMAL);
		battlefield.setClanid1(clanid1);
		battlefield.setClanid2(clanid2);
		battlefield.setClanname1(clanname1);
		battlefield.setClanname2(clanname2);
		battlefield.setDatetime(datetime);
		battlefield.setWhichwin(-1);
		
		xtable.Clanfightbattlefield.insert(clanfightid, battlefield);

		try
		{			
			ClanFightBattleField bf = new ClanFightBattleField(clanfightid, battlefield, config, false);
			bf.init();
			
			fire.pb.activity.clanfight.ActivityClanFightManager.logger.info("[" + clanfightid + "] createClanFightBattleField ok");
			
			return bf;
		}
		catch (Exception e)
		{
			fire.pb.activity.clanfight.ActivityClanFightManager.logger.error("error : ", e);
			return null;
		}
	}
	
	/***
	 * 销毁公会战场 by changhao
	 * @param clanfightid
	 * lock clanfightid -> roleids by changhao
	 */
	public static void destroyClanFightBattleFieldById(long clanfightid)
	{		
		long clanid1 = 0;
		long clanid2 = 0;
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf != null)
		{
			clanid1 = bf.getClanfightBean().getClanid1();
			clanid2 = bf.getClanfightBean().getClanid2();
			
			bf.destroyBattleField();
			
			fire.pb.activity.clanfight.ActivityClanFightManager.logger.info("[" + clanfightid + "] destroyClanFightBattleFieldById ok");			
			xtable.Clanfightbattlefield.remove(clanfightid);
			
			mkdb.Procedure.pexecuteWhileCommit(new fire.pb.clan.fight.PClanFightDeleteClanFightID(clanid1, clanid2, clanfightid));
		}
	}
}
