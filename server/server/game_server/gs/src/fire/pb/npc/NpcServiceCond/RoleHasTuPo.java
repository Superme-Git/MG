package fire.pb.npc.NpcServiceCond;

import java.util.Map;

public class RoleHasTuPo implements Condition{
	
	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		Map<Integer,Integer> tupotips = xtable.Properties.selectTupotips(roleid);
		Integer st = tupotips.get(args1);
		if(st != null && st.intValue() == 1){
			return true;
		} else
			return false;
	}
	
}
