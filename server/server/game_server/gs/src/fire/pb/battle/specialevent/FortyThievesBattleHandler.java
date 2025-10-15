package fire.pb.battle.specialevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.BattleConfig;
import fire.pb.battle.Module;
import fire.pb.map.SceneNpcManager;

public class FortyThievesBattleHandler extends BasicEndHandler {

	final private long npckey;
	
	public FortyThievesBattleHandler(long npckey)
	{
		this.npckey = npckey;
	}

	@Override
	protected boolean handleAward() {
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
		//paras.put("TeamNum", battle.getRoleids().size());
		List<Long> rids = new ArrayList<Long>();
		rids.addAll(battle.getRoleids().keySet());
		distributeItemAward(rids, conf.award, paras);
		//删除npc
		SceneNpcManager.premoveNpcWhileCommit(npckey);
		List<Long> roleids = new ArrayList<Long>();
		for (xbean.Fighter f : getWinRoles()) {
			roleids.add(f.getUniqueid());
		}
//		// 增加友好度
//		addFriendShip(roleids);
		return true;
	}

	
}
