package fire.pb.activity.timernpc;

import fire.pb.battle.PNewBattle;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SNpcShare;
import mkdb.Procedure;

public class PFightProc extends Procedure {
	private final long roleId;
	private final int npcId;
	private final long npcKey;
	
	public PFightProc(final long roleid, final int npcId, final long npcKey) {
		this.roleId = roleid;
		this.npcId = npcId;
		this.npcKey = npcKey;
	}
	
	@Override
	protected boolean process() throws Exception {
		SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(npcId);
		if (npc == null)
			return false;
		//获取战斗id
		int battleId = npc.battleinfo;
		if (battleId == -1) {
			return false;
		}
		
		//开始战斗
		TimerNpcBattleEndHandler handler = new TimerNpcBattleEndHandler(0, npcId, npcKey, roleId);
		PNewBattle battle = new PNewBattle(roleId,  battleId,  handler);
		return  battle.call();
	}

}
