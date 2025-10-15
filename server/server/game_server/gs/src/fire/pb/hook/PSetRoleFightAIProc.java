package fire.pb.hook;

import java.util.List;

import fire.pb.PropRole;
import fire.pb.battle.Fighter;
import fire.pb.battle.SRoleFighteAI;


public class PSetRoleFightAIProc extends mkdb.Procedure{
	
	private final List<Integer> rolefightais;
	private final long  roleid;
	private PropRole prole;
	
	public PSetRoleFightAIProc (long roleid, List<Integer> RoleFightAIs){
		this.roleid = roleid;
		this.rolefightais = RoleFightAIs;
	}

	@Override
	protected boolean process() throws Exception {	
		Long battleId = xtable.Roleid2battleid.select(roleid);
		xbean.BattleInfo battle = null;
		
		if(battleId!=null){	
//			lock(mkdb.Lockeys.get(xtable.Locks.BATTLE, new Object[]{battleId}));
			battle = xtable.Battle.get(battleId);
		}
		
		prole = new PropRole(roleid, false);
		if(prole != null){
			prole.getProperties().getFighteai().clear();

			List<Integer> ais = fire.pb.battle.ai.BattleAIManager.getInstance().getRoleFighteAIIDs(prole.getSchool());
			for(int ai:rolefightais){
				if(!ais.contains(ai))
					return false;

				SRoleFighteAI roleai =  fire.pb.battle.ai.BattleAIManager.getInstance().getRoleFighteAI(ai);
				xbean.BattleAI newai = xbean.Pod.newBattleAI();
				newai.setId(roleai.aiid);
				prole.getProperties().getFighteai().add(newai);				
			}
			
			if (battle != null)
			{
				final Integer index = battle.getRoleids().get(roleid);
				Fighter fighter = battle.getFighterobjects().get(index);
				if(fighter.isRole())
					fighter.getAiagent().setAis(prole.getProperties().getFighteai());	
			}
		}		
		return true;
	}
}
