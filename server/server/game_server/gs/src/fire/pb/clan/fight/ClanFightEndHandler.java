package fire.pb.clan.fight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;

/***
 * 帮会战 战后处理 by changhao
 */
public class ClanFightEndHandler extends BattleEndHandler {

	public long clanfightid; //公会战id by changhao
	public long attackerid; //攻击者 by changhao
	public long targetid; //被攻击者 by changhao
	public long attackerclanid; //攻击方公会id by changhao
	public long clandid;//被攻击方公会id by changhao
	
	public ClanFightEndHandler(long clanfightid, long attackerid, long targetid, long attackerclanid, long clanid)
	{
		this.clanfightid = clanfightid;
		this.attackerid = attackerid;
		this.targetid = targetid;
		this.attackerclanid = attackerclanid;
		this.clandid = clanid;
	}

	@Override
	protected boolean handleDeath()
	{	
		handlePetFighters();
		
		List<xbean.Fighter> list = new ArrayList<xbean.Fighter>();
		List<xbean.Fighter> fighters = super.getDeadRoles();
		List<xbean.Fighter> fighters1 = super.getWinRoles();
		list.addAll(fighters);
		list.addAll(fighters1);
      
		for (xbean.Fighter f : list)
		{
			//满血满魔原地复活 by changhao
			long rid = f.getUniqueid();
			// 回血回魔 by changhao
			BattleField.fullRoleHpMp(rid);
			
			xbean.Properties prop = xtable.Properties.get(rid);
			if (prop != null)
				prop.setSp(f.getInisp());
		}
     
		return true;
	}
	
	@Override
	protected boolean handleCamera()
	{
		return true;
	}

	@Override
	/***
	 * @return
	 */
	protected boolean handleAward()
	{
		processResult();
		return true;
	}
	
	protected void processResult()
	{
		java.util.List<Long> winners = new java.util.ArrayList<Long>();
		List<xbean.Fighter> w = super.getWinRoles();
		for (xbean.Fighter e : w)
		{
			winners.add(e.getUniqueid());
		}
		
		java.util.List<Long> losers = new java.util.ArrayList<Long>();
		
		List<xbean.Fighter> l = getLoseRoles();  
		for (xbean.Fighter e : l)
		{
			losers.add(e.getUniqueid());
		}
		
		mkdb.Procedure.pexecuteWhileCommit(new PClanFightBattleHandleAward(this.attackerid, this.clanfightid, winners, losers, attackerclanid, clandid));		
	}
	
	/*返回输边的玩家 by changhao*/
	public List<xbean.Fighter> getLoseRoles() {

		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		if (battle.getBattleresult() == BattleField.BATTLE_DRAW)
			return fighters;
		
		boolean hostWin = false;
		if (battle.getBattleresult() == BattleField.BATTLE_WIN)
		{
			hostWin = true;
		}
		
		Collection<xbean.Fighter> allFighters = battle.getFighters().values();
		
		for (xbean.Fighter fighter : allFighters)
		{
			if (fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				if (hostWin && !fighter.getIshost())
					fighters.add(fighter);
				else if (!hostWin && fighter.getIshost())
					fighters.add(fighter);
			}
		}
		
		return fighters;
	}

	@Override
	public boolean handleOutBattle(int index,int petkey) throws Exception
	{
		return super.handleOutBattle(index,petkey);
	}

	@Override
	protected boolean handleDrawBattle()
	{	
		processResult();
		return super.handleDrawBattle();
	}
	
	@Override
	protected boolean handleBattleFailed()
	{
		processResult();
		return super.handleBattleFailed();
	}
/*
 */
}
