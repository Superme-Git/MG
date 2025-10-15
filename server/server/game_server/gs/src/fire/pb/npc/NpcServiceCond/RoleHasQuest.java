package fire.pb.npc.NpcServiceCond;

import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.SpecialQuestType;
import fire.pb.mission.UtilHelper;

public class RoleHasQuest implements Condition{
	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		if(UtilHelper.isSpecialQuest(args1)){
			final xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
			if (null == quest_map) {
				if (args2 > 0) //要求该任务存在
					return false;
				else //要求该任务不存在
					return true;
			}
			xbean.CircleTaskInfo quest_info = quest_map.getTaskmap().get(args1);
			if (null == quest_info) {
				if (args2 > 0) //要求该任务存在
					return false;
				else //要求该任务不存在
					return true;
			} else {
				if (args2 > 0) {
					//要求该任务存在
					if (quest_info.getQuesttype() == 0)
						return false;
					else
						return true;
				}
				else {
					//要求该任务不存在
					if (quest_info.getQuesttype() == SpecialQuestType.CatchIt_Normal || quest_info.getQuesttype() == SpecialQuestType.CatchIt_Increase) {
						if (quest_info.getQueststate() == SpecialQuestState.UNDONE)
							return false;
						else
							return true;
					}
					else {
						if (quest_info.getQuesttype() == 0)
							return true;
						else
							return false;
					}
				}
			}
		} else {
			//FIXME:非循环任务即主线支线,先不管了
			return false;
		}
	}
}
