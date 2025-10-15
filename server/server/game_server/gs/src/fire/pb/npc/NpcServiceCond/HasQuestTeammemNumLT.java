package fire.pb.npc.NpcServiceCond;

public class HasQuestTeammemNumLT implements Condition{

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		//Auto-generated method stub
		int num = 0;
		if (num <= args2)
			return true;
		else
			return false;
	}
}

