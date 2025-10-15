package fire.pb.battle.specialevent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.BattleConfig;
import fire.pb.battle.BattleField;
import fire.pb.battle.Module;
import fire.pb.map.RoleManager;

public class RunawaySpyBattleHandler extends BasicEndHandler {

	private boolean isSpyRunaway = false;
	
	@Override
	public boolean handleDeath() {
		super.handleDeath();
		return true;
	}


	@Override
	protected boolean handleAward() {
		
		if(isSpyRunaway)
		{
			//	SMineArea areaInfo = ((Map<Integer,SMineArea>)fire.pb.main.ConfigManager.getInstance().getConf("fire.pb.map.SMineArea")).get(battle.getAreaconf());
				
			int fighterId = BattleField.getLeaderFighterId(battle, true);
			xbean.Fighter xfighter = battle.getFighters().get(fighterId);
			RoleManager.getInstance().getRoleByID(xfighter.getUniqueid());
			
		//	int level = areaInfo.getLevel();//读取areaInfo等级
//			int npcid = 10254;
			//npcid = npcid + (level - 1)/10 ;
			//SNpcShare npcshare =  
			//fire.pb.main.ConfigManager.getInstance().getConf( fire.pb.npc.SNpcShare.class).get(PFortyThievesBattle.FORTY_THIEVES_NPC_KEY);
		}
		else
		{//常规奖励
			BattleConfig conf = Module.getInstance().getBattleConfigs().get(battle.getConfigid());
			if(conf == null || conf.award <= 0)
				return true;
			Map<String, Object> paras = new HashMap<String, Object>();
			paras.put("MapLv", getMapLv());
			paras.put("MasterNum", battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_CHIEF_NUM).intValue());
			paras.put("MonsterNum",battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_NUM).intValue());
			paras.put("TeamNum", battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
			int levelsum = 0;
			int num = 0;
			for(xbean.Monster monster: battle.getMonsters().values())
			{
				levelsum += monster.getLevel();
				num++;
			}
			int averagelevel = 0;
			if(num != 0)
				averagelevel = levelsum / num;
			paras.put("MonsterLv",averagelevel);
			List<Long> rids = new ArrayList<Long>();
			rids.addAll(battle.getRoleids().keySet());
			distributeItemAward(rids, conf.award, paras);
			List<Long> roleids = new ArrayList<Long>();
			for (xbean.Fighter f : getWinRoles()) {
				roleids.add(f.getUniqueid());
			}
//			// 增加友好度
//			addFriendShip(roleids);
		}
		
		return true;
	}

	@Override
	public boolean handleOutBattle(int index,int petkey) throws Exception {
		if(index == BattleField.GUEST_FIRST_ID && BattleField.checkRunAway(battle, index))
		{//是密探，并且是逃跑成功
			isSpyRunaway = true;
		}
		
		super.handleOutBattle(index,petkey);
		return true;
	}
	
}
