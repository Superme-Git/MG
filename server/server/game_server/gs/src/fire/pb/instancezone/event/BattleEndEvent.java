package fire.pb.instancezone.event;

import java.util.LinkedList;
import java.util.List;

import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.instancezone.conf.EventConfig;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;

public class BattleEndEvent extends Event {

	public BattleEndEvent(xbean.BattleInfo battle, final long battleNpcKey) {
		getParams().put(ConfigParamName.BATTLE_ID, battle.getConfigid());
		getParams()
				.put(ConfigParamName.BATTLE_RESULT, battle.getBattleresult());
		getParams().put(ConfigParamName.NPC_KEY, battleNpcKey);

		List<Long> roleids = new LinkedList<Long>();
		roleids.addAll(battle.getRoleids().keySet());
		getParams().put(ConfigParamName.ROLE_IDS, roleids);

		long npckey = battle.getMonsterid();
		if (npckey > 0) {
			Npc npc = SceneNpcManager.getNpcByKey(npckey);
			if (npc != null)
				getParams().put(ConfigParamName.NPC_ID, npc.getNpcID());
		}
	}

	@Override
	public int getType() {
		return EventConfig.TYPE_BATTLE_END;
	}

	public int getBattleResult() {
		return (Integer) getParams().get(ConfigParamName.BATTLE_RESULT);
	}

	public int getBattleId() {
		return (Integer) getParams().get(ConfigParamName.BATTLE_ID);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getRoleIds() {
		return (List<Long>) getParams().get(ConfigParamName.ROLE_IDS);
	}

	public int getNpcId() {
		return (Integer) getParams().get(ConfigParamName.NPC_ID);
	}

	public long getNpcKey() {
		return (Long) getParams().get(ConfigParamName.NPC_KEY);
	}

}
