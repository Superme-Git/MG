

package fire.pb.compensation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fire.pb.npc.NpcServiceCond.Condition;
import fire.pb.npc.NpcServiceCond.HideServiceCond;
import fire.pb.npc.NpcServiceCond.RoleCreateTime;
import fire.pb.npc.NpcServiceCond.RoleHasApprentice;
import fire.pb.npc.NpcServiceCond.RoleHasCircleTask;
import fire.pb.npc.NpcServiceCond.RoleHasQuest;
import fire.pb.npc.NpcServiceCond.RoleHasRegMaster;
import fire.pb.npc.NpcServiceCond.RoleLevel;
import fire.pb.npc.NpcServiceCond.RolePassonQuest;
import fire.pb.npc.NpcServiceCond.RoleQuestState;
import fire.pb.npc.NpcServiceCond.RoleQuestUnState;
import fire.pb.npc.NpcServiceCond.RoleSchool;
import fire.pb.npc.NpcServiceCond.RoleSex;
import fire.pb.npc.NpcServiceCond.RoleShopsNum;
import fire.pb.npc.NpcServiceCond.RoleState;

public class CompensationConditionFactory {

	private static Map<Integer, Condition> cons = new ConcurrentHashMap<Integer, Condition>();
	public static Condition getCondition(int conditionid){
		Condition result = cons.get(conditionid);
		if (result == null)  {
			switch (conditionid) {
			case 1: result = new RoleLevel(); break;
			case 2: result = new RoleSchool(); break;
			case 3: result = new RoleShopsNum(); break;
			case 4: result = new RoleState(); break;
			case 5: result = new RoleSex(); break;
			case 6: result = new RoleQuestState(); break;
			case 7: result = new RoleHasQuest(); break;
			case 8: result = new RoleQuestUnState(); break;
			case 9: result = new HideServiceCond(); break;
			case 10: result = new RoleHasApprentice(); break;
			case 11: result = new RoleHasRegMaster(); break;
			case 12: result = new RolePassonQuest(); break;
			case 13: result = new RoleHasApprentice(); break;
			case 19: result = new RoleHasCircleTask(); break;
			case 50: result = new RoleCreateTime(); break;
			default: throw new RuntimeException("error conditionid!");
			}
			cons.put(conditionid, result);
		}
		return result;
	}
}

