package fire.pb.hook;

import java.util.List;

import fire.pb.PropRole;
import mkdb.Procedure;

public class PGetRoleFightAIProc extends mkdb.Procedure{
	
	private final long  roleid;
	private PropRole pRole;
	
	public PGetRoleFightAIProc(long roleid){
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {	
		pRole = new PropRole(roleid, true);
		if(pRole == null){
			return false;
		}
		
		List<xbean.BattleAI> list = pRole.getProperties().getFighteai();
		
		SFlushRoleFightAI send = new SFlushRoleFightAI();
		for(xbean.BattleAI ai: list)
		{
			int fighteraiid =fire.pb.battle.ai.BattleAIManager.getInstance().getRoleFighteAIIDbyAIID(ai.getId()); 
			if(fighteraiid != 0)
				send.fightaiids.add(fighteraiid);
		}
		Procedure.psendWhileCommit(roleid, send);

		return true;
	}
}
