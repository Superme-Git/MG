package fire.pb.battle;
import mkdb.Procedure;

/**战斗内所有玩家发送消息 */
public class PSendAllBattleMembers extends mkdb.Procedure{
	
	private long battleid;
	private mkio.Protocol protocol;
	public PSendAllBattleMembers(long battleid, mkio.Protocol protocol){
		this.battleid = battleid;
		this.protocol = protocol;
	}
	
	public boolean process(){
		xbean.BattleInfo battle = xtable.Battle.get(battleid);
		if (battle == null){
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("No such battle exsist  battleid =" + battleid);}
			return false;
		}
		if (protocol == null)
			return false;
		Procedure.psendWhileCommit(battle.getRoleids().keySet(), protocol);
		Procedure.psendWhileCommit(battle.getAudience().keySet(), protocol);
		return true;
	}
}
