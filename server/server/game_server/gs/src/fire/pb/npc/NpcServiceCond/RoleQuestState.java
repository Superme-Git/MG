package fire.pb.npc.NpcServiceCond;


public class RoleQuestState  implements Condition{
	
	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		final xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
		if (null == quest_map)
			return false;
		xbean.CircleTaskInfo quest_info = quest_map.getTaskmap().get(args1);
		if (null == quest_info)
			return false;
		if (quest_info.getQueststate() != args2)
			return false;
		return true;
	}
}
