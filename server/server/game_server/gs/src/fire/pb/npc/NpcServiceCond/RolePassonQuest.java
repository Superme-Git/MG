package fire.pb.npc.NpcServiceCond;

public class RolePassonQuest implements Condition {

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
//		fire.pb.mission.TaskMasterChuangong task = new fire.pb.mission.TaskMasterChuangong( roleid, true );
//		final int status;
//		if ( task.getState() == fire.pb.specialquest.SpecialQuestState.DONE )
//			status = 1; // 任务已经完成
//		else if (task.getState() == fire.pb.specialquest.SpecialQuestState.UNDONE) 
//			status = 0; //任务未完成
//		else
//			status = -1;
//		return args1 == status;
		return false;
	}

}
