package fire.pb.option;

import mkdb.Procedure;

public class PAddProtector extends Procedure {
	public PAddProtector(final long battleid, int f, int t){
		battleID = battleid;
		from = f;
		to = t;
	}
	
	public boolean process(){
		final xbean.BattleInfo battle = xtable.Battle.get(battleID);
		if(null == battle)
			return false;
		
		xbean.Protectors ps = battle.getProtectormap().get(to);
		if(null == ps){
			ps = xbean.Pod.newProtectors();
			battle.getProtectormap().put(to, ps);
		}
		
		ps.getProtectorlist().add(from);
		return true;
		
	}
	
	private final long battleID;
	private final int from;
	private final int to;
}
