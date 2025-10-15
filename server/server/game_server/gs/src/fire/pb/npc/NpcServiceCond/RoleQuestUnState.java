package fire.pb.npc.NpcServiceCond;

public class RoleQuestUnState implements Condition{

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		
		final xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
		if (null == quest_map)
			return false;
		final xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(args1);
		if (null == sqinfo)
			return false;
		if (sqinfo.getQueststate() != args2)
			return true;
		return false;
	}
}
